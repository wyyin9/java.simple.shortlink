package com.shortlink.controller;

import com.shortlink.model.entity.ShortlinkRecord;
import com.shortlink.service.MyBatisPlusDemoService;
import com.shortlink.service.ShortlinkService;
import com.shortlink.util.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
@ResponseBody
@RequestMapping("stat")
public class StatController {
    private final ShortlinkService shortlinkService;

    private final MyBatisPlusDemoService myBatisPlusDemoService;

    /**
     * 这里使用构造函数注入而非属性注入，官方推荐这么做，然而大部分项目还是使用属性注入
     *
     * @param shortlinkService 短链服务接口
     */
    public StatController(ShortlinkService shortlinkService, MyBatisPlusDemoService myBatisPlusDemoService) {
        this.shortlinkService = shortlinkService;
        this.myBatisPlusDemoService = myBatisPlusDemoService;
    }

    /**
     * 获取总数量
     *
     * @return 总数量
     */
    @GetMapping("total")
    public Result getTotal() {
        long currentRecordCount = myBatisPlusDemoService.selectCount(null);
        return Result.ok(currentRecordCount);
    }

    /**
     * 获取数据
     *
     * @return 数量&列表
     */
    @GetMapping("get-list")
    public Result getRecords() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("total", myBatisPlusDemoService.selectCount(null));
        data.put("list", myBatisPlusDemoService.selectList(null));
        return Result.ok(data);
    }

}
