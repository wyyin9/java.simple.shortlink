package com.shortlink.util;

/**
 * 短链相关redis key
 */
public class ShortlinkRedisKey {

    /**
     * 自增id key
     */
    public static String autoIncrId = "short-link:id";

    /**
     * 同步队列 listId
     */
    public static String syncListId = "short-link:sync-list";

    /**
     * 短码key （存原始连接）
     */
    public static String codeKey(String code) {
        return "short-link:code:" + code;
    }

    /**
     * url md5 key (存生成过的短码)
     */
    public static String urlMd5Key(String md5) {
        return "short-link:md5:" + md5;
    }
}
