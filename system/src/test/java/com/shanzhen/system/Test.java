package com.shanzhen.system;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;

/**
 * Description:
 * Created by nijunyang on 2020/5/12 16:33
 */
public class Test {

    public static void main(String[] args) {
        RSA rsa = new RSA();
        String publicKeyBase64 = rsa.getPublicKeyBase64();
        String privateKeyBase64 = rsa.getPrivateKeyBase64();
        System.out.println("公钥：" + publicKeyBase64);
        System.out.println("私钥：" + privateKeyBase64);
        rsa = new RSA(privateKeyBase64, publicKeyBase64);
        //公钥加密，私钥解密
        byte[] encrypt = rsa.encrypt(StrUtil.bytes("123456", CharsetUtil.CHARSET_UTF_8), KeyType.PublicKey);

        String result = Base64.encode(encrypt);
        System.out.println(result);

        byte[] decrypt = rsa.decrypt(Base64.decode(result), KeyType.PrivateKey);
        System.out.println(StrUtil.str(decrypt, CharsetUtil.CHARSET_UTF_8));

        //私钥加密，公钥解密
        byte[] encrypt2 = rsa.encrypt(StrUtil.bytes("654321", CharsetUtil.CHARSET_UTF_8), KeyType.PrivateKey);
        byte[] decrypt2 = rsa.decrypt(encrypt2, KeyType.PublicKey);
        System.out.println(StrUtil.str(decrypt2, CharsetUtil.CHARSET_UTF_8));


    }

}
