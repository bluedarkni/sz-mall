package com.shanzhen.system.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.shanzhen.common.component.RedisUtils;
import com.shanzhen.common.exception.ErrorCodeException;
import com.shanzhen.system.error.SystemErrorCode;
import com.shanzhen.system.model.vo.auth.AuthResult;
import com.shanzhen.system.model.vo.auth.AuthUser;
import com.shanzhen.system.model.vo.auth.CaptchaInfo;
import com.shanzhen.system.model.vo.auth.JwtUser;
import com.shanzhen.system.properties.JwtProperties;
import com.shanzhen.system.properties.RSAProperties;
import com.shanzhen.system.security.TokenProvider;
import com.shanzhen.system.service.AuthService;
import com.wf.captcha.ArithmeticCaptcha;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Description:
 * Created by nijunyang on 2020/5/12 15:11
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final RSAProperties rsaProperties;
    private final JwtProperties jwtProperties;
    private final RedisUtils redisUtils;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Override
    public AuthResult login(AuthUser authUser) {
        // 校验验证码
        if (StringUtils.isNotBlank(authUser.getCaptchaId())) {
            String code = (String) redisUtils.get(authUser.getCaptchaId());
            redisUtils.del(authUser.getCaptchaId());
            if (StringUtils.isBlank(code)) {
                throw new ErrorCodeException(SystemErrorCode.CAPTCHA_EXPIRE_1002);
            }
            if (StringUtils.isBlank(authUser.getCode()) || !authUser.getCode().equalsIgnoreCase(code)) {
                throw new ErrorCodeException(SystemErrorCode.CAPTCHA_FAILED_1001);
            }
        }

        RSA rsa = new RSA(rsaProperties.getPrivateKey(), rsaProperties.getPublicKey());
        String password = new String(rsa.decrypt(authUser.getPassword(), KeyType.PrivateKey));
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(authUser.getUsername(), password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        String token = tokenProvider.createToken(authentication);
        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
        AuthResult authInfo = new AuthResult();
        authInfo.setToken(jwtProperties.getTokenStartWith() + " " + token);
        authInfo.setUser(jwtUser);
        return authInfo;
    }

    @Override
    public CaptchaInfo generateCaptcha() {
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(111, 36);
        captcha.setLen(3);
        String result = captcha.text();
        String uuid = jwtProperties.getCodeKey() + IdUtil.simpleUUID();
        redisUtils.set(uuid, result, jwtProperties.getExpire(), TimeUnit.MINUTES);
        CaptchaInfo captchaInfo = new CaptchaInfo(uuid, captcha.toBase64());
        return captchaInfo;
    }


}
