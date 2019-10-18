package com.zb.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author zhangbo
 * @date 2019-10-16
 */
@Getter
@Setter
@Builder
public class ResultDTO implements Serializable {

    public static final String OK = "OK";
    public static final String FAIL = "FAIL";

    private int code;

    private long timestamp;

    private String message;

    private Object body;

    public static ResultDTO success() {
        return build(200, OK, null);
    }

    public static ResultDTO success(Object body) {
        return build(200, OK, body);
    }

    public static ResultDTO fail() {
        return fail(500, FAIL);
    }

    public static ResultDTO fail(String message) {
        return fail(500, message);
    }

    public static ResultDTO fail(int code, String message) {
        return build(code, message, null);
    }

    private static ResultDTO build(int code, String message, Object body) {
        return ResultDTO.builder().code(code).message(message).body(body).timestamp(System.currentTimeMillis()).build();
    }
}
