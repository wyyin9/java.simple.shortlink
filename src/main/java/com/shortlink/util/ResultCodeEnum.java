package com.shortlink.util;

import lombok.Getter;

@Getter
public enum ResultCodeEnum {
    SUCCESS(1, "成功"),
    PARAM_ERROR(2, "参数错误"),
    NOT_FOUND(404, "未找到信息"),
    SERVICE_ERROR(500, "服务异常");


    private ResultCodeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private Integer code;
    private String msg;


}
