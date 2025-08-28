package com.omniperform.web.common;

import java.util.HashMap;

/**
 * 统一响应结果
 * 
 * @author omniperform
 */
public class Result<T> extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    /** 成功状态码 */
    public static final int SUCCESS = 200;
    
    /** 错误状态码 */
    public static final int ERROR = 500;

    public Result() {}

    public Result(int code, String message) {
        super.put("code", code);
        super.put("message", message);
    }

    public Result(int code, String message, T data) {
        super.put("code", code);
        super.put("message", message);
        if (data != null) {
            super.put("data", data);
        }
    }

    /**
     * 返回成功消息
     */
    public static <T> Result<T> success() {
        return new Result<>(SUCCESS, "操作成功");
    }

    /**
     * 返回成功数据
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(SUCCESS, "操作成功", data);
    }

    /**
     * 返回成功消息
     */
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(SUCCESS, message, data);
    }

    /**
     * 返回错误消息
     */
    public static <T> Result<T> error() {
        return new Result<>(ERROR, "操作失败");
    }

    /**
     * 返回错误消息
     */
    public static <T> Result<T> error(String message) {
        return new Result<>(ERROR, message);
    }

    /**
     * 返回错误消息
     */
    public static <T> Result<T> error(int code, String message) {
        return new Result<>(code, message);
    }
}
