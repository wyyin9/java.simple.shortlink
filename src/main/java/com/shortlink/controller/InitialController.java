package com.shortlink.controller;

import com.shortlink.util.Result;
import com.shortlink.service.MyBatisPlusDemoService;
import com.shortlink.service.ShortlinkService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("init")
public class InitialController {

    private final ShortlinkService shortlinkService;

    private final MyBatisPlusDemoService myBatisPlusDemoService;

    /**
     * 这里使用构造函数注入而非属性注入，官方推荐这么做，然而大部分项目还是使用属性注入
     *
     * @param shortlinkService 短链service
     */
    public InitialController(ShortlinkService shortlinkService, MyBatisPlusDemoService myBatisPlusDemoService) {
        this.shortlinkService = shortlinkService;
        this.myBatisPlusDemoService = myBatisPlusDemoService;
    }

    /**
     * 测试页
     *
     * @return 页面
     */
    @GetMapping("index")
    public String index() {
        return "/init/index";
    }

    /**
     * 重置数据，数据库表清空，自增id从1重新累加
     *
     * @return 结果
     */
    @RequestMapping("reset-all")
    @ResponseBody
    public String resetAll() {

        return shortlinkService.resetAll();
    }
}
