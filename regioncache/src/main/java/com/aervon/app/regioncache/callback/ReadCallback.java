package com.aervon.app.regioncache.callback;

/**
 * 区域缓存读取的回调
 * <p>
 * @author : alvin.zhou
 * @datetime : 2018/10/9
 */

public interface ReadCallback {

    /**
     * 读取完成
     */
    void onReadCompleted();

    /**
     * 读取为空
     */
    void onReadEmpty();

    /**
     * 读取出错，一般Exception的捕捉
     *
     * @param msg 包括Exception的类型和错误信息
     */
    void onReadError(String msg);
}
