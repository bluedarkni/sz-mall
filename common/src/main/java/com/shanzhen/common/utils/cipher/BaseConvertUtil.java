package com.shanzhen.common.utils.cipher;

import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Created by nijunyang on 2020/5/9 19:05
 */
public class BaseConvertUtil {
    private static final byte[] base34CharMap = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
            'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

    private static final BigInteger base34 = BigInteger.valueOf(base34CharMap.length);

    public static final byte[] base62CharMap = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
            'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a',
            'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
            'y', 'z'};

    private static final BigInteger base62 = BigInteger.valueOf(base62CharMap.length);

    // ASCII only
    public static final boolean[] yui64Index = new boolean[256];

    private static final byte[] yui64CharMap = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
            'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a',
            'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
            'y', 'z', '.', '_', '-'};

    private static final BigInteger yui64 = new BigInteger("65");

    // this is used for check-digit of seqnum
    private static final char[] base23CharMap = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M', 'N', 'P',
            'Q', 'R', 'S', 'T', 'V', 'W', 'X', 'Y', 'Z'};

    private static final int base23Hex = 23;

    private static final int[] digits_weight = new int[]{1, 3, 7, 9};

    private static final int digits_weight_size = 4;

    // ASCII only
    public static final boolean[] base42Index = new boolean[256];

    private static final byte[] base42CharMap = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
            'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '$',
            '*', '+', '-', '.', ':'};

    private static final BigInteger base42 = new BigInteger("42");

    static {
        for (int i = 0; i < yui64CharMap.length; i++) {
            yui64Index[yui64CharMap[i]] = true;
        }
        for (int i = 0; i < base42CharMap.length; i++) {
            base42Index[base42CharMap[i]] = true;
        }
    }

    public static String byte2String(byte[] input, byte[] baseMap) {
        if (input == null) {
            return "null";
        }
        int v = input[0];
        BigInteger bi;
        if (v < 0) {
            bi = new BigInteger(1, input);
        } else {
            bi = new BigInteger(input);
        }
        byte[] hexresult = new byte[input.length * 2];
        int hexcount = 0;
        while (true) {
            if (bi.bitLength() == 0) {
                break;
            }
            BigInteger[] results = bi.divideAndRemainder(BigInteger.valueOf(baseMap.length));
            bi = results[0];
            hexresult[hexcount++] = baseMap[results[1].byteValue()];
        }
        return new String(hexresult, 0, hexcount);
    }

    public static String byte2StringBase34(byte[] input) {
        return byte2String(input, base34CharMap);
    }

    public static byte[] String2ByteBase34(String input) {
        if (input == null) {
            return null;
        }
        input = input.trim();
        if ("".equals(input)) {
            return null;
        }
        byte[] hexarray = input.getBytes();
        int len = hexarray.length;
        BigInteger rbi = BigInteger.valueOf(getIndex34(hexarray[len - 1]));
        for (int i = len - 2; i >= 0; i--) {
            int r = getIndex34(hexarray[i]);
            rbi = rbi.multiply(base34).add(BigInteger.valueOf(r));
        }
        byte[] res = rbi.toByteArray();
        if (res[0] == 0) {
            byte[] res1 = new byte[res.length - 1];
            System.arraycopy(res, 1, res1, 0, res.length - 1);
            res = res1;
        }
        return res;
    }

    public static String byte2StringBase62(byte[] input) {
        return byte2String(input, base62CharMap);
    }

    public static byte[] String2ByteBase62(String input) {
        if (input == null) {
            return null;
        }
        input = input.trim();
        if ("".equals(input)) {
            return null;
        }
        byte[] hexarray = input.getBytes();
        int len = hexarray.length;
        BigInteger rbi = BigInteger.valueOf(getIndex62(hexarray[len - 1]));
        for (int i = len - 2; i >= 0; i--) {
            int r = getIndex62(hexarray[i]);
            rbi = rbi.multiply(base62).add(BigInteger.valueOf(r));
        }
        byte[] res = rbi.toByteArray();
        if (res[0] == 0) {
            byte[] res1 = new byte[res.length - 1];
            System.arraycopy(res, 1, res1, 0, res.length - 1);
            res = res1;
        }
        return res;
    }

    public static String byte2StringYuiBase64(byte[] input) {
        return byte2String(input, yui64CharMap);
    }

    public static byte[] String2ByteYuiBase64(String input) {
        if (input == null) {
            return null;
        }
        input = input.trim();
        if ("".equals(input)) {
            return null;
        }
        byte[] hexarray = input.getBytes();
        int len = hexarray.length;
        BigInteger rbi = BigInteger.valueOf(getIndexYui64(hexarray[len - 1]));
        for (int i = len - 2; i >= 0; i--) {
            int r = getIndexYui64(hexarray[i]);
            rbi = rbi.multiply(yui64).add(BigInteger.valueOf(r));
        }
        byte[] res = rbi.toByteArray();
        if (res[0] == 0) {
            byte[] res1 = new byte[res.length - 1];
            System.arraycopy(res, 1, res1, 0, res.length - 1);
            res = res1;
        }
        return res;
    }

    private static int getIndexYui64(byte inByte) {
        // Refer to http://www.asciitable.com/
        if (inByte >= 48 && inByte <= 57) {
            return inByte - 48;
        }
        if (inByte >= 65 && inByte <= 90) {
            return inByte - 55;
        }
        if (inByte >= 97 && inByte <= 122) {
            return inByte - 61;
        }
        if (inByte == 46) {
            return 62;
        }
        if (inByte == 95) {
            return 63;
        }
        if (inByte == 45) {
            return 64;
        }

        return 0;
    }

    private static int getIndex62(byte inByte) {
        // Refer to http://www.asciitable.com/
        if (inByte >= 48 && inByte <= 57) {
            return inByte - 48;
        }
        if (inByte >= 65 && inByte <= 90) {
            return inByte - 55;
        }
        if (inByte >= 97 && inByte <= 122) {
            return inByte - 61;
        }

        return 0;
    }

    private static int getIndex34(byte b) {
        if (b >= '0' && b <= '9') {
            return b - '0';
        }
        if (b >= 'A' && b <= 'H') {
            return b - 'A' + 10;
        }
        if (b >= 'J' && b <= 'N') {
            return b - 'A' + 9;
        }
        if (b >= 'P' && b <= 'Z') {
            return b - 'A' + 8;
        }

        return -1;
    }

    public static String toBase34(long value) {
        ByteBuffer bb = ByteBuffer.allocate(8).order(ByteOrder.BIG_ENDIAN);
        bb.putLong(value);
        byte[] bytes = bb.array();
        return byte2StringBase34(bytes);
    }

    public static long fromBase34(String s) {
        byte[] bytes = String2ByteBase34(s);
        ByteBuffer bb = ByteBuffer.allocate(8).order(ByteOrder.BIG_ENDIAN);
        int i = 0;
        for (; i < 8 - bytes.length; i++) {
            bb.put((byte) 0x0);
        }
        for (int j = 0; j < bytes.length; j++) {
            bb.put(bytes[j]);
        }
        return bb.getLong(0);
    }

    public static String toBase62(long value) {
        ByteBuffer bb = ByteBuffer.allocate(8);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.putLong(value);
        byte[] barray = shrink(bb.array());
        for (int i = 0; i < barray.length / 2; i++) {
            byte temp = barray[i];
            barray[i] = barray[barray.length - 1 - i];
            barray[barray.length - 1 - i] = temp;
        }
        return byte2StringBase62(barray);
    }

    public static long fromBase62(String s) {
        byte[] barray = String2ByteBase62(s);
        for (int i = 0; i < barray.length / 2; i++) {
            byte temp = barray[i];
            barray[i] = barray[barray.length - 1 - i];
            barray[barray.length - 1 - i] = temp;
        }
        return bytesToLong(barray);
    }

    protected static byte[] shrink(byte input[]) {
        int i = 0;
        for (i = input.length - 1; i >= 0; i--) {
            if ((input[i] & 0xFF) > 0) {
                break;
            }
        }
        byte[] data = new byte[i + 1];
        System.arraycopy(input, 0, data, 0, i + 1);
        return data;
    }

    public static byte[] padArrayOnLeft(byte[] input, int minSize) {
        if (input.length < minSize) {
            byte[] temp = new byte[minSize];
            System.arraycopy(input, 0, temp, minSize - input.length, input.length);
            input = temp;
        }
        return input;
    }

    public static byte[] longToBytes(long x) {
        ByteBuffer bb = ByteBuffer.allocate(8);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.putLong(x);
        return shrink(bb.array());
    }

    public static long bytesToLong(byte[] bytes) {
        if (bytes == null) {
            return 0;
        }
        return bytesToLong(bytes, 0, bytes.length);
    }

    protected static long bytesToLong(byte[] buffer, int offset, int len) {
        if (buffer == null) {
            return 0;
        }
        ByteBuffer bb = ByteBuffer.allocate(8);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        if (len > 8) {
            len = 8;
        }
        bb.put(buffer, offset, len);
        bb.rewind();
        return bb.getLong();
    }

    /**
     * Converts an integer to byte array in Big Endian order.
     *
     * @param intValue
     * @return
     */
    public static byte[] int2Byte(int intValue) {
        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            b[i] = (byte) (intValue >> 8 * (3 - i) & 0xFF);
        }
        return b;
    }

    /**
     * @param b
     * @return
     * @throws Exception
     */
    public static int byte2Int(byte[] b) {
        if (b.length > 4) {
            throw new IllegalArgumentException("Decrypted Tag type is invalid!");
        }
        int intValue = 0;
        for (int i = 0; i < b.length; i++) {
            intValue += (b[i] & 0xFF) << (8 * (3 - i));
        }
        return intValue;
    }

    /**
     * 将二进制转换成16进制
     *
     * @param buf
     * @param upperCase need UpperCase or not
     * @return
     */
    public static String parseByte2HexStr(byte buf[], boolean upperCase) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(upperCase ? hex.toUpperCase() : hex);

        }
        return sb.toString();
    }

    /**
     * 将16进制转换为二进制
     *
     * @param hexStr
     * @return
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (StringUtils.isEmpty(hexStr)) {
            return null;
        }
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    public static boolean isValidYui64String(String str) {
        if (StringUtils.isBlank(str)) {
            return false;
        }

        for (int i = 0; i < str.length(); i++) {
            char character = str.charAt(i);
            if (character >= 0 && character < yui64Index.length && !yui64Index[character]) {
                return false;
            }
        }

        return true;
    }

    /**
     * convert long value to bytes list save the long's bytes size into the
     * List,the first one byte is the size
     *
     * @param x
     * @return
     */
    public static byte[] longToBytesWithSize(long x) {
        byte[] value = longToBytes(x);
        int size = value.length;
        byte[] result = new byte[size + 1];
        result[0] = (byte) size;
        System.arraycopy(value, 0, result, 1, size);
        return result;
    }

    /**
     * convert bytes list to long value validate the size which was saved in the
     * byte List is equal to the byteList's length-1
     *
     * @param bytes
     * @return
     */
    public static long bytesToLongWithSize(byte[] bytes) {
        if (bytes == null) {
            return 0;
        }

        if (bytes[0] != bytes.length - 1) {
            throw new IllegalArgumentException("bytes length is not equal to the size saved in bytes");
        }
        return bytesToLong(bytes, 1, bytes[0]);
    }

    /**
     * convert bytes list to long list we have size at each long's bytes head
     *
     * @param bytes
     * @return
     */
    public static List<Long> bytesToLongList(byte[] bytes) {

        if (bytes == null || bytes.length == 0 || bytes.length == 1)
            return null;
        List<Long> result = new ArrayList<Long>();
        int index = 0;
        while (index < bytes.length) {
            int size = bytes[index];
            byte[] value = new byte[size + 1];

            System.arraycopy(bytes, index, value, 0, size + 1);
            result.add(bytesToLongWithSize(value));
            index += size + 1;
        }
        return result;
    }

    /**
     * create check-digit with weight for each char of seqnum
     *
     * @param seqnum
     * @return
     */
    public static char createCheckDigitWithWeight(String seqnum) {

        int result = 0;

        int size = seqnum.length();
        for (int i = 0; i < size; i++) {
            result += digits_weight[i % digits_weight_size] * (seqnum.charAt(i) - '0');

        }
        return base23CharMap[result % base23Hex];

    }

    public static String byte2Stringbase42(byte[] input) {
        return byte2String(input, base42CharMap);
    }

    public static byte[] String2Bytebase42(String input) {
        if (input == null) {
            return null;
        }
        input = input.trim();
        if ("".equals(input)) {
            return null;
        }
        byte[] hexarray = input.getBytes();
        int len = hexarray.length;
        BigInteger rbi = BigInteger.valueOf(getIndexbase42(hexarray[len - 1]));
        for (int i = len - 2; i >= 0; i--) {
            int r = getIndexbase42(hexarray[i]);
            rbi = rbi.multiply(base42).add(BigInteger.valueOf(r));
        }
        byte[] res = rbi.toByteArray();
        if (res[0] == 0) {
            byte[] res1 = new byte[res.length - 1];
            System.arraycopy(res, 1, res1, 0, res.length - 1);
            res = res1;
        }
        return res;
    }

    public static boolean isValidbase42String(String str) {
        if (StringUtils.isBlank(str)) {
            return false;
        }

        for (int i = 0; i < str.length(); i++) {
            char character = str.charAt(i);
            if (character >= 0 && character < base42Index.length && !base42Index[character]) {
                return false;
            }
        }

        return true;
    }

    private static int getIndexbase42(byte inByte) {
        // Refer to http://www.asciitable.com/
        if (inByte >= 48 && inByte <= 57) {
            return inByte - 48;
        }
        if (inByte >= 65 && inByte <= 90) {
            return inByte - 55;
        }
        if (inByte == 36) {
            return 36;
        }
        if (inByte == 42) {
            return 37;
        }
        if (inByte == 43) {
            return 38;
        }
        if (inByte == 45) {
            return 39;
        }
        if (inByte == 46) {
            return 40;
        }
        if (inByte == 58) {
            return 41;
        }

        return 0;
    }
}
