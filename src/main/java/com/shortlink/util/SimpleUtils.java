package com.shortlink.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SimpleUtils {
    /*
     * 密码本
     * */
    public static String keyOf62Char = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /*
     * 数字62进制编码
     * */
    public static String to62Code(long number) {
        StringBuilder sBuilder = new StringBuilder();
        while (true) {
            int remainder = (int) (number % 62);
            sBuilder.append(keyOf62Char.charAt(remainder));
            number = number / 62;
            if (number == 0) {
                break;
            }
        }

        // 上面的步骤是先加%运算对应的字符，所以要反转一下才能等到正确有序的结果
        sBuilder.reverse();
        return sBuilder.toString();
    }


    /*
     * 字符解码为数字
     * */
    public static Long toAutoIncrId(String value) {
        long result = 0;
        for (int i = 0; i < value.length(); i++) {
            int x = value.length() - i - 1;
            result += keyOf62Char.indexOf(value.charAt(i)) * Pow(62, x);
        }

        return result;
    }


    private static long Pow(long baseNo, long x) {
        long value = 1;
        while (x > 0) {
            value = value * baseNo;
            x--;
        }
        return value;
    }

    /*
     * 字符串md5加密
     * */
    public static String toMd5(String input) {
        try {
            // 得到一个信息摘要器
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] result = digest.digest(input.getBytes());
            StringBuffer buffer = new StringBuffer();
            // 把每一个byte 做一个与运算 0xff;
            for (byte b : result) {
                // 与运算
                int number = b & 0xff;// 加盐
                String str = Integer.toHexString(number);
                if (str.length() == 1) {
                    buffer.append("0");
                }
                buffer.append(str);
            }

            // 标准的md5加密后的结果
            return buffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }
}
