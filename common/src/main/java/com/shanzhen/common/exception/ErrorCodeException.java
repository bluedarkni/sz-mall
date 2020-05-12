package com.shanzhen.common.exception;

import lombok.Getter;

/**
 * Description: 错误码异常
 * Created by nijunyang on 2020/5/9 17:31
 */
@Getter
public class ErrorCodeException extends RuntimeException {

    private ErrorCode errorCode;
    /**
     * 匹配提示信息中的占位符
     */
    private Object[] args;

    public ErrorCodeException(ErrorCode errorCode, Object... args) {
        this.errorCode = errorCode;
        this.args = args;
    }

}
