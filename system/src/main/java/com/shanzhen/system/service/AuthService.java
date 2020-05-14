package com.shanzhen.system.service;

import com.shanzhen.system.model.vo.auth.AuthResult;
import com.shanzhen.system.model.vo.auth.AuthUser;
import com.shanzhen.system.model.vo.auth.CaptchaInfo;

/**
 * Description:
 * Created by nijunyang on 2020/5/12 15:11
 */
public interface AuthService {

    AuthResult login(AuthUser authUser);

    CaptchaInfo generateCaptcha();
}
