package com.pansky.feign.exception;

import lombok.Data;

/**
 * @author Fo
 * @date 2022/10/19 12:14
 */
@Data
public class ApiException extends Exception {
    private Long timestamp;

    private Integer code;

    private String exception;

    private String message;

    private String path;

    private String error;

    public ApiException(String message, Integer code) {
    }
}
