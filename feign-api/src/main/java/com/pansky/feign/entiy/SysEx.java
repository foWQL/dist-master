package com.pansky.feign.entiy;

import lombok.Builder;
import lombok.Getter;

/**
 * @author Fo
 * @date 2022/10/19 14:03
 */
@Getter
@Builder
public class SysEx extends RuntimeException{
    private ResCode resCode;

    public SysEx(ResCode resCode) {
        super(resCode.getMsg());
        this.resCode = resCode;
    }
}