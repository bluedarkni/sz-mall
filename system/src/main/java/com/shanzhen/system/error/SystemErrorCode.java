package com.shanzhen.system.error;

import com.shanzhen.common.exception.ErrorCode;

/**
 * Description:
 * Created by nijunyang on 2020/5/12 15:37
 */
public enum SystemErrorCode implements ErrorCode {

    /**
     * 验证码错误
     */
    CAPTCHA_FAILED_1001(1001),
    /**
     * 验证码过期
     */
    CAPTCHA_EXPIRE_1002(1002),

    /**
     * 用户不存在
     */
    USER_NOT_EXITS_1003(1003),


    ;

    private int code;

    SystemErrorCode(int code) {
        this.code = code;
    }

    @Override
    public String getKey() {
        return this.getClass().getSimpleName() + "_" + this.code;
    }

    @Override
    public int getCode() {
        return code;
    }
}
