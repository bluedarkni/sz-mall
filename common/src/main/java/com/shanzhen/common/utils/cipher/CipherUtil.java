package com.shanzhen.common.utils.cipher;

import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;

/**
 * Description:
 * Created by nijunyang on 2020/5/9 19:05
 */
public class CipherUtil {
    // 密码字母表
    private static final String[] CHARACTERS = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "g", "k", "l", "m", "n", "o", "p",
            "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "G", "K", "L", "M",
            "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "~", "@", "#", "$", "%", "^", "&", "*", "(", ")",
            "_", "+", "|", "`", ".", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};

    private static final int ARRARY_LENGTH = 76;

    private static final int PASSWORD_LENGTH = 16;

    private static final String HMAC_SHA256 = "HmacSHA256";

    private static final String ENCODING = "UTF-8";

    // 使用AES对文件进行加密和解密
    private static final String TYPE = "AES";

    private static final String TYPE_DES = "DES";

    private static MessageDigest MESSAGE_DIGEST = null;

    static {
        try {
            MESSAGE_DIGEST = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取固定长度的随机密码
     */
    public static String generatePassword() {
        StringBuilder builder = new StringBuilder();
        Random random = new Random();
        int k;
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            k = random.nextInt();
            builder.append(String.valueOf(CHARACTERS[Math.abs(k % ARRARY_LENGTH)]));
        }

        return builder.toString();
    }

    /**
     * 把文件srcFile加密后存储为destFile
     *
     * @param srcFile    加密前的文件
     * @param destFile   加密后的文件
     * @param privateKey 密钥
     * @throws GeneralSecurityException
     * @throws IOException
     */
    public static void encrypt(String srcFile, String destFile, String privateKey) throws GeneralSecurityException, IOException {
        Key key = getKeySpec(privateKey);
        Cipher cipher = Cipher.getInstance(TYPE + "/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);

        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            fis = new FileInputStream(srcFile);
            fos = new FileOutputStream(mkdirFiles(destFile));

            crypt(fis, fos, cipher);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                fis.close();
            }
            if (fos != null) {
                fos.close();
            }
        }
    }

    /**
     * 把文件srcFile解密后存储为destFile
     *
     * @param srcFile    解密前的文件
     * @param destFile   解密后的文件
     * @param privateKey 密钥
     * @throws GeneralSecurityException
     * @throws IOException
     */
    public static void decrypt(String srcFile, String destFile, String privateKey) throws GeneralSecurityException, IOException {
        Key key = getKeySpec(privateKey);
        Cipher cipher = Cipher.getInstance(TYPE + "/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);

        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            fis = new FileInputStream(srcFile);
            fos = new FileOutputStream(mkdirFiles(destFile));

            crypt(fis, fos, cipher);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                fis.close();
            }
            if (fos != null) {
                fos.close();
            }
        }
    }

    /**
     * 根据filePath创建相应的目录
     *
     * @param filePath 要创建的文件路经
     * @return file 文件
     * @throws IOException
     */
    private static File mkdirFiles(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        file.createNewFile();

        return file;
    }

    /**
     * 加密解密流
     *
     * @param in     加密解密前的流
     * @param out    加密解密后的流
     * @param cipher 加密解密
     * @throws IOException
     * @throws GeneralSecurityException
     */
    private static void crypt(InputStream in, OutputStream out, Cipher cipher) throws IOException, GeneralSecurityException {
        int blockSize = cipher.getBlockSize() * 1000;
        int outputSize = cipher.getOutputSize(blockSize);

        byte[] inBytes = new byte[blockSize];
        byte[] outBytes = new byte[outputSize];

        int inLength = 0;
        boolean more = true;
        while (more) {
            inLength = in.read(inBytes);
            if (inLength == blockSize) {
                int outLength = cipher.update(inBytes, 0, blockSize, outBytes);
                out.write(outBytes, 0, outLength);
            } else {
                more = false;
            }
        }
        if (inLength > 0) {
            outBytes = cipher.doFinal(inBytes, 0, inLength);
        } else {
            outBytes = cipher.doFinal();
        }
        out.write(outBytes);
    }

    /**
     * 加密字符串（加密结果转为16进制）
     *
     * @param srcString  加密前的字符串
     * @param privateKey 密钥
     * @throws GeneralSecurityException
     */
    public static String encryptString(String srcString, String privateKey) {
        if (StringUtils.isBlank(srcString)) {
            return null;
        }

        try {
            Key key = getKeySpec(privateKey);
            Cipher cipher = Cipher.getInstance(TYPE + "/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] tmpByte = cipher.doFinal(srcString.getBytes(Charset.forName(ENCODING)));

            return BaseConvertUtil.parseByte2HexStr(tmpByte, true);
        } catch (GeneralSecurityException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param srcString
     * @param privateKey
     * @return
     * @throws
     * @Title: encryptStringAESECBToBase64
     * @Description: 加密并转成base64字符串  给php转换http://www.cnblogs.com/yipu/articles/3871576.html
     */
    public static String encryptStringAESECBToBase64(String srcString, String privateKey) {
        if (StringUtils.isBlank(srcString)) {
            return null;
        }
        try {
            Key key = getKeySpec(privateKey);
            Cipher cipher = Cipher.getInstance(TYPE + "/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] tmpByte = cipher.doFinal(srcString.getBytes(Charset.forName(ENCODING)));

            return new String(Base64.getEncoder().encode(tmpByte));
        } catch (GeneralSecurityException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param srcString
     * @param privateKey
     * @return
     * @throws
     * @Title: decryptStringAESECBFromBase64
     * @Description: 解密base64形式的字符串  给php转换http://www.cnblogs.com/yipu/articles/3871576.html
     */
    public static String decryptStringAESECBFromBase64(String srcString, String privateKey) {
        if (StringUtils.isBlank(srcString)) {
            return null;
        }

        try {
            Key key = getKeySpec(privateKey);
            Cipher cipher = Cipher.getInstance(TYPE + "/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] tmpByte = cipher.doFinal(Base64.getDecoder().decode(srcString));

            return new String(tmpByte, Charset.forName(ENCODING)).trim();
        } catch (GeneralSecurityException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 解密字符串（解密字符串需要是16进制的）
     *
     * @param srcString  解密前的字符串
     * @param privateKey 密钥
     * @throws GeneralSecurityException
     */
    public static String decryptString(String srcString, String privateKey) {
        if (StringUtils.isBlank(srcString)) {
            return null;
        }

        try {
            Key key = getKeySpec(privateKey);
            Cipher cipher = Cipher.getInstance(TYPE + "/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);

            byte[] tmpByte = BaseConvertUtil.parseHexStr2Byte(srcString);
            if (tmpByte != null) {
                tmpByte = cipher.doFinal(tmpByte);
            }

            return new String(tmpByte, Charset.forName(ENCODING));
        } catch (GeneralSecurityException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String digestString(String... srcString) {
        StringBuffer sb = new StringBuffer();
        byte[] digestResult = null;
        for (String tmp : srcString) {
            sb.append(tmp);
        }
        digestResult = MESSAGE_DIGEST.digest(sb.toString().getBytes(Charset.forName(ENCODING)));
        if (digestResult != null) {
            return BaseConvertUtil.byte2StringBase62(digestResult);
        } else {
            return null;
        }
    }

    /**
     * 使用指定key进行CBC加密（返回加密字段（第一个元素）和Iv（第2个元素））
     *
     * @param srcString
     * @param privateKey
     * @param cipherMode
     * @return
     * @throws Exception
     */
    public static String[] encryptStringCBC(String srcString, String privateKey) throws Exception {
        // Tuple2<String, String> result = new Tuple2<String, String>();
        String[] result = new String[2];
        Key key = getKeySpec(privateKey);
        // String ivStr = getRandomString(16);
        // / IvParameterSpec iv = new IvParameterSpec(ivStr.getBytes());
        Cipher cipher = Cipher.getInstance(TYPE + "/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);

        byte[] tmpByte = cipher.doFinal(srcString.getBytes("utf-8"));
        result[0] = BaseConvertUtil.parseByte2HexStr(tmpByte, true);
        result[1] = BaseConvertUtil.parseByte2HexStr(cipher.getIV(), true);
        // result.setItem1(BaseConvertUtil.parseByte2HexStr(tmpByte));
        // result.setItem2(ivStr);
        return result;

    }

    public static String decryptStringCBC(String srcString, String privateKey, String ivStr) throws Exception {
        Key key = getKeySpec(privateKey);
        IvParameterSpec iv = new IvParameterSpec(BaseConvertUtil.parseHexStr2Byte(ivStr));
        Cipher cipher = Cipher.getInstance(TYPE + "/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, iv);

        byte[] tmpByte = BaseConvertUtil.parseHexStr2Byte(srcString);
        if (tmpByte != null) {
            tmpByte = cipher.doFinal(tmpByte);
        }

        return new String(tmpByte, "utf-8");
    }

    public static String decryptStringCBC_liusha(String srcString, String privateKey, String ivStr) throws Exception {
        Key key = getKeySpec(privateKey);
        IvParameterSpec iv = new IvParameterSpec(ivStr.getBytes(ENCODING));
        // IvParameterSpec iv = new
        // IvParameterSpec(BaseConvertUtil.parseHexStr2Byte(ivStr));
        Cipher cipher = Cipher.getInstance(TYPE + "/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, iv);

        byte[] tmpByte = BaseConvertUtil.parseHexStr2Byte(srcString);
        if (tmpByte != null) {
            tmpByte = cipher.doFinal(tmpByte);
        }

        return new String(tmpByte, "utf-8");
    }

    private static SecretKeySpec getKeySpec(String secret) throws UnsupportedEncodingException {
        byte[] raw = secret.getBytes("UTF-8");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, TYPE);

        return skeySpec;
    }

    /**
     * 使用 HMAC-SHA1 签名方法对对encryptText进行签名
     *
     * @param encryptText 被签名的字符串
     * @param encryptKey  密钥
     * @return
     * @throws Exception
     */
    public static String HmacSHA256Encrypt(String encryptText, String encryptKey) throws Exception {
        byte[] key = encryptKey.getBytes(ENCODING);
        // 根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
        SecretKey secretKey = new SecretKeySpec(key, HMAC_SHA256);

        // 生成一个指定 Mac 算法 的 Mac 对象
        Mac mac = Mac.getInstance(HMAC_SHA256);
        // 用给定密钥初始化 Mac 对象
        mac.init(secretKey);

        byte[] data = encryptText.getBytes(ENCODING);
        // 完成 Mac 操作
        byte[] rawHmac = mac.doFinal(data);
        // byte[] hexB = new Hex().encode(rawHmac);
        return BaseConvertUtil.parseByte2HexStr(rawHmac, false);
        // return new String(rawHmac, ENCODING);
    }

    /**
     * DES 简单CBC加密 IV和密钥用同一个key
     *
     * @param message
     * @param key
     * @return
     * @throws Exception
     */
    public static String encryptDESCBC(String message, String key) throws Exception {
        Cipher cipher = Cipher.getInstance(TYPE_DES + "/CBC/PKCS5Padding");
        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
        byte[] ivByte = new byte[8];
        System.arraycopy(key.getBytes("UTF-8"), 0, ivByte, 0, 8);
        IvParameterSpec iv = new IvParameterSpec(ivByte);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
        return BaseConvertUtil.parseByte2HexStr(cipher.doFinal(message.getBytes("UTF-8")), false);
    }

    /**
     * DES 简单CBC解密 IV和密钥用同一个key
     *
     * @param message
     * @param key
     * @return
     * @throws Exception
     */
    public static String decryptDESCBC(String message, String key) throws Exception {
        byte[] bytesrc = BaseConvertUtil.parseHexStr2Byte(message);
        Cipher cipher = Cipher.getInstance(TYPE_DES + "/CBC/PKCS5Padding");
        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
        byte[] ivByte = new byte[8];
        System.arraycopy(key.getBytes("UTF-8"), 0, ivByte, 0, 8);
        IvParameterSpec iv = new IvParameterSpec(ivByte);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);

        byte[] retByte = cipher.doFinal(bytesrc);
        return new String(retByte);
    }
}
