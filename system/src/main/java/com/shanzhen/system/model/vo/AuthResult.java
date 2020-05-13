package com.shanzhen.system.model.vo;

import lombok.Data;

/**
 * Description:
 * Created by nijunyang on 2020/5/13 10:25
 */
@Data
public class AuthResult {

    private String token;
    private JwtUser user;
}
