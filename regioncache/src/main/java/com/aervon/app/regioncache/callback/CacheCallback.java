package com.aervon.app.regioncache.callback;

/**
 * 区域缓存动作的回调
 * <p>
 * @author : alvin.zhou
 * @datetime : 2018/10/9
 */

public interface CacheCallback {

    /**
     * 缓存完成
     */
    void onCacheCompleted();

    /**
     * 缓存出错，一般Exception的捕捉
     *
     * @param msg 包括Exception的类型和错误信息
     */
    void onCacheError(String msg);
}
