package com.shanzhen.system.model.vo.auth;

import lombok.Data;

/**
 * Description: 验证码
 * Created by nijunyang on 2020/5/14 10:11
 */
@Data

public class CaptchaInfo {

    private String captchaId;

    private String image;

    public CaptchaInfo(String captchaId, String image) {
        this.image = image;
        this.captchaId = captchaId;
    }
}
