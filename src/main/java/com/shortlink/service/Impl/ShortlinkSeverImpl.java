package com.shortlink.service.Impl;

import com.shortlink.mapper.ShortlinkRecordMapper;
import com.shortlink.model.dto.BuildDto;
import com.shortlink.util.Result;
import com.shortlink.model.entity.ShortlinkRecord;
import com.shortlink.util.*;
import com.shortlink.service.ShortlinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service //加此注解，程序启动时才会自动扫描，否则无法注入使用
public class ShortlinkSeverImpl implements ShortlinkService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ShortlinkRecordMapper shortlinkRecordMapper;

    /**
     * 构建短码
     *
     * @param request 构建请求信息
     * @return 构建结果
     */
    @Override
    public Result buildShortCode(BuildDto request) {
        // 先做下简单的判断
        if (request == null) {
            return Result.errorPara("错误的请求");
        }

        if (request.url == null || request.url.length() == 0) {
            return Result.errorPara("url不能为null或空字符串");
        }

        if (request.source == null || request.source.length() == 0) {
            return Result.errorPara("必须标明来源");
        }
        try {
            // 将url转为md5
            String urlMd5 = SimpleUtils.toMd5(request.url);

            // 拼接redis key
            String urlKey = ShortlinkRedisKey.urlMd5Key(urlMd5);

            // opsForValue().get返回Object类型，此处需要转为String类型
            String codeCache = (String) redisTemplate.opsForValue().get(urlKey);
            if (codeCache != null && codeCache.length() > 0) {
                System.out.println("命中缓存！！！直接返回成功结果");
                return Result.ok(codeCache);
            }

            // 获取自增id
            long autoIncrId = this.getShortlinkId();

            // 将id转为编码
            String newCode = SimpleUtils.to62Code(autoIncrId);

            // 30天过期时间
            int expireDays = 30;
            // 将短码写入Redis
            redisTemplate.opsForValue().set(urlKey, newCode, expireDays, TimeUnit.DAYS);

            // 以编码拼一个redis key
            String codeKey = ShortlinkRedisKey.codeKey(newCode);
            // 将原链接写入redis 过期180天
            redisTemplate.opsForValue().set(codeKey, request.url, 180, TimeUnit.DAYS);

            // 这里用redis的list数据类型实现一个简单队列
            ShortlinkRecord listItem = new ShortlinkRecord();
            listItem.id = autoIncrId;
            listItem.shortCode = newCode;
            listItem.originalUrl = request.url;
            listItem.source = request.source;

            // 写入队列，
            redisTemplate.opsForList().rightPush(ShortlinkRedisKey.syncListId, listItem);

            return Result.ok(newCode);

        } catch (Exception e) {
            System.out.println(e.toString());
            return Result.error(e.getMessage());

        }
    }

    /**
     * 获取全局自增id
     *
     * @return long型id
     */
    private long getShortlinkId() {
        RedisAtomicLong idCounter = new RedisAtomicLong(ShortlinkRedisKey.autoIncrId, redisTemplate.getConnectionFactory());
        // 此方法为先加+1后返回
        long id = idCounter.incrementAndGet();
        return id;
    }


    /**
     * 通过短码获取原始连接信息
     *
     * @param code 短码
     * @return
     */
    @Override
    public Result getOriginalUrlByCode(String code) {
        String codeKey = ShortlinkRedisKey.codeKey(code);
        try {
            String url = (String) redisTemplate.opsForValue().get(codeKey);
            if (url != null && url.length() > 0) {
                return Result.ok(url);
            }

            long id = SimpleUtils.toAutoIncrId(code);
            // 去数据库查
            ShortlinkRecord record = shortlinkRecordMapper.selectById(id);
            if (record == null) {
                // 数据库里也找不到记录，直接返回
                return Result.error("找不到原始链接", ResultCodeEnum.NOT_FOUND);
            }

            // 再次缓存180天
            redisTemplate.opsForValue().set(codeKey, record.originalUrl, 180, TimeUnit.DAYS);
            return Result.ok(record.originalUrl);

        } catch (Exception ex) {
            System.out.println(ex.toString());
            return Result.error(ex.getMessage(), ResultCodeEnum.SERVICE_ERROR);
        }
    }

    /**
     * 同步数据
     */
    @Override
    public void syncBuildRecord() {
        try {
            long notSyncCount = redisTemplate.opsForList().size(ShortlinkRedisKey.syncListId);

            if (notSyncCount <= 0) {
                return;
            }

            long batchCount = 100;
            // 1次处理100条需要循环的次数
            long loopCount = notSyncCount / batchCount;

            for (int i = 0; i < loopCount + 1; i++) {
                batchSyncBuildRecord(batchCount);
            }

            ////也可以用while来循环，但是如果内部没有处理好很容易造成死循环，所以不要轻易使用
            //while (redisTemplate.opsForList().size(listId) > 0) {
            //// todo:入库处理
            //}
        } catch (Exception ex) {
            System.out.println("同步异常" + ex.toString());
        }
    }

    /**
     * 批量同步数据
     *
     * @param batchCount 同步条数
     * @return 入库条数
     */
    private int batchSyncBuildRecord(long batchCount) {
        int insertCount = 0;
        try {
            String listId = ShortlinkRedisKey.syncListId;
            // 获取0-batchCount 条数据
            List<ShortlinkRecord> list = redisTemplate.opsForList().range(listId, 0, batchCount);
            if (list.isEmpty()) {
                return 0;
            }

            insertCount = shortlinkRecordMapper.batchInsert(list);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
            System.out.println("成功同步" + insertCount + "条,当前时间" + sdf.format(new Date()));

            // 没有提供批量删除，只能循环删除
            for (ShortlinkRecord item : list) {
                redisTemplate.opsForList().remove(listId, 1, item);
            }

        } catch (Exception ex) {
            System.out.println("同步异常" + ex.toString());
        }

        return insertCount;
    }

    /**
     * 重置，清除所有缓存，数据
     *
     * @return
     */
    public String resetAll() {
        try {
            // 获取所有以short-link开头的key
            Set<String> keys = redisTemplate.keys("short-link*");
            // 批量删除
            redisTemplate.delete(keys);
            // 数据库删除所有数据

            // mybatis plus内置删除方法，wrapper为空即删除所有
            shortlinkRecordMapper.delete(null);
            // 也可以使用自己实现的删除所有
            //shortlinkRecordMapper.deleteAll();
            return "ok";
        } catch (Exception ex) {
            return ex.toString();
        }
    }
}
