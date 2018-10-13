package com.aervon.app.regioncache.core;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * 缓存结果体
 *
 * @author : alvin.zhou
 * @datetime : 2018/10/11
 */

public class CacheResponse {

    public final static int SUCCESS = 200;
    public final static int EMPTY = 201;
    public final static int ERROR = 202;

    private int code;

    private String msg;

    private Map<Field, Object> cacheData;

    private CacheResponse(int code) {
        this(code, "");
    }

    private CacheResponse(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private CacheResponse(int code, Map<Field, Object> data) {
        this.code = code;
        this.cacheData = data;
    }

    public static CacheResponse createSuccessResponse() {
        return new CacheResponse(SUCCESS);
    }

    public static CacheResponse createSuccessResponse(Map<Field, Object> data) {
        return new CacheResponse(SUCCESS, data);
    }

    public static CacheResponse createEmptyResponse() {
        return new CacheResponse(EMPTY);
    }

    public static CacheResponse createErrorResponse(String msg) {
        return new CacheResponse(ERROR, msg);
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public Map<Field, Object> getCacheData() {
        return cacheData;
    }

    public void setCacheData(Map<Field, Object> cacheData) {
        this.cacheData = cacheData;
    }
}
