package com.pansky.feign.entiy;

import lombok.Getter;

/**
 * @author Fo
 * @date 2022/10/19 14:04
 */
@Getter
public enum  ResCode {
    OK(0,"ok"),
    E_500(500,"500的异常"),
    E_1000(1000,"1000的异常"),
    E_2001(1001,"2001的异常"),
            ;
    private int code;

    private String msg;

    ResCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static ResCode ofCode(int code){
        ResCode[] values = ResCode.values();
        for (ResCode resCode : values){
            if (code == resCode.code){
                return resCode;
            }
        }
        return ResCode.E_500;
    }

}
