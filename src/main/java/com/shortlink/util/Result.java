package com.shortlink.util;

import lombok.Data;
import lombok.Getter;

/**
 * 构建结果
 */
@Data
public class Result<T> {
    /**
     * 状态 1为成功，其余失败
     */
    public ResultCodeEnum code;

    /**
     * 返回数据
     */
    public T data;

    /**
     * 错误消息
     */
    public String errMsg;

    /**
     * 返回失败
     */
    public static <T> Result<T> error(String msg) {
        Result res = new Result();
        res.code = ResultCodeEnum.SERVICE_ERROR;
        res.errMsg = msg;
        return res;
    }

    public static <T> Result<T> error(String msg, ResultCodeEnum code) {
        Result res = new Result();
        res.code = code;
        res.errMsg = msg;
        return res;
    }

    public static <T> Result<T> error(String msg, ResultCodeEnum code, T data) {
        Result res = new Result();
        res.code = code;
        res.errMsg = msg;
        return res;
    }

    public static <T> Result<T> errorPara(String msg) {
        Result res = new Result();
        res.code = ResultCodeEnum.PARAM_ERROR;
        res.errMsg = msg;
        return res;
    }

    /**
     * 返回成功
     */
    public static <T> Result<T> ok(T code) {
        Result res = new Result();
        res.code = ResultCodeEnum.SUCCESS;
        res.data = code;
        return res;
    }

    /**
     * 是否为成功状态
     */
    public boolean isSuccess() {
        return code == ResultCodeEnum.SUCCESS;
    }

}
