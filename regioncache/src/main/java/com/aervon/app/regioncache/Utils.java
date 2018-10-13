package com.aervon.app.regioncache;

import android.text.TextUtils;

import java.io.File;
import java.security.MessageDigest;

/**
 * 工具类，提供MD5等
 * <p>
 * @author : alvin.zhou
 * @datetime : 2018/10/9
 */

public class Utils {

    private Utils() {
    }

    public static String MD5(String sourceStr) {
        try {
            // 获得MD5摘要算法的 MessageDigest对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(sourceStr.getBytes());
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            StringBuffer buf = new StringBuffer();
            for (int i = 0; i < md.length; i++) {
                int tmp = md[i];
                if (tmp < 0) {
                    tmp += 256;
                }
                if (tmp < 16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(tmp));
            }
            return buf.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void createDirIfNeed(String dir) {
        File file = new File(dir);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static boolean hasNotEmpty(String... params) {
        for (String param : params) {
            if (!TextUtils.isEmpty(param)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isEmpty(String... params) {
        for (String param : params) {
            if (TextUtils.isEmpty(param)) {
                return true;
            }
        }
        return false;
    }
}
