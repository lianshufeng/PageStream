package com.fast.pagestream.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @功能：MD5编码
 * @作者：练书锋
 * @创建日期 : 2013-9-5
 */
public class MD5Util {

    private static final MessageDigest messageDigest = createMessageDigest();

    private static final MessageDigest createMessageDigest() {
        try {
            return MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 保证线程安全
     *
     * @param bin
     * @return
     */
    public synchronized static final byte[] enCode(byte[] bin) {
        return messageDigest.digest(bin);
    }

    /**
     * 文本MD5到十六进制字符串
     *
     * @param text
     * @return
     */
    public static final String md5(String text) {
        return md5(text, "UTF-8");
    }

    /**
     * 数据摘要
     *
     * @param text
     * @return
     */
    public static final String md5(String text, String code) {
        try {
            return BytesUtil.binToHex(enCode(text.getBytes(code == null ? "UTF-8" : code)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
