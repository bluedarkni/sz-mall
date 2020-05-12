package com.shanzhen.system.model.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Description:
 * Created by nijunyang on 2020/5/12 15:13
 */
@Data
public class AuthUser {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    /**
     * 验证码
     */
    private String code;

    /**
     * 验证码key
     */
    private String codeKey = "";
}
