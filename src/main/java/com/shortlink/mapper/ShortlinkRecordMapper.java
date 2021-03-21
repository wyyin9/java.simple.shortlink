package com.shortlink.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.shortlink.model.entity.ShortlinkRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 短链记录Mapper
 */
public interface ShortlinkRecordMapper extends BaseMapper<ShortlinkRecord> {

    /**
     * 批量插入数据
     * @param records 短链记录
     * @return 插入行数
     */
    int batchInsert(@Param("list") List<ShortlinkRecord> records);

    /**
     * 清空表
     */
    int deleteAll();
}
