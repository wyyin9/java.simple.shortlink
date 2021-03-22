package com.shortlink.controller;

import com.shortlink.model.dto.BuildDto;
import com.shortlink.util.Result;
import com.shortlink.service.ShortlinkService;
import com.shortlink.util.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "")
public class ShortlinkController {

    @Autowired
    private ShortlinkService shortlinkService;

    /**
     * 创建短码
     *
     * @param req 请求信息
     * @return 短码创建结果
     */
    @PostMapping("sl/build")
    @ResponseBody
    public Result buildShortLink(@RequestBody BuildDto req) {
        return shortlinkService.buildShortCode(req);
    }

    /**
     * 访问短链
     *
     * @param code 短码
     * @return
     */
    @GetMapping("{code}")
    public String redirect(@PathVariable("code") String code) {
        Result result = shortlinkService.getOriginalUrlByCode(code);
        if (result.isSuccess()) {
            return "redirect:" + result.data;
        }

        // 有错误消息为内部出现异常
        return result.getCode() == ResultCodeEnum.SERVICE_ERROR
                ? "/error/500"
                : "/error/404";
    }
}
