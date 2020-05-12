package com.shanzhen.system.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * Description:
 * Created by nijunyang on 2020/5/12 16:07
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "sz.rsa")
@Component
public class RSAProperties {

    private String publicKey;

    private String privateKey;
}
