package com.shanzhen.common.exception;

import lombok.Getter;

/**
 * Description: 错误码异常响应类
 * Created by nijunyang on 2020/5/9 17:33
 */
@Getter
public class ErrorCodeResponse {

    /**
     * 错误码
     */
    private int errorCode;
    /**
     * 错误信息
     */
    private String errorMsg;

    public ErrorCodeResponse(int errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public ErrorCodeResponse(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
