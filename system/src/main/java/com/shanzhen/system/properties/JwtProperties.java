package com.shanzhen.system.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * Description:
 * Created by nijunyang on 2020/5/12 15:07
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "sz.jwt")
public class JwtProperties {

    private String header;

    private String tokenStartWith;

    private String base64Secret;

    private Long tokenValidityInSeconds;

    private String onlineKey;

    /**
     * 验证码key
     */
    private String codeKey;

    /**
     * 验证码失效时间
     */
    private long expire;
}
