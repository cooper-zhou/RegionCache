package com.aervon.app.regioncache;

import android.content.Context;

import com.aervon.app.regioncache.core.CacheRequest;
import com.aervon.app.regioncache.provider.FileCacheProvider;
import com.aervon.app.regioncache.provider.ICacheProvider;

/**
 * 区域缓存
 * <p>
 * @author : alvin.zhou
 * @datetime : 2018/10/9
 */
public class Region {

    private static ICacheProvider mCacheProvider;

    public static void build(ICacheProvider provider) {
        mCacheProvider = provider;
    }

    public static CacheRequest with(Context context) {
        if (mCacheProvider == null) {
            // 默认使用 FileCacheProvider
            String cacheDir = context.getCacheDir().getAbsolutePath();
            Utils.createDirIfNeed(cacheDir);
            mCacheProvider = new FileCacheProvider(cacheDir);
        }
        return new CacheRequest(mCacheProvider);
    }
}
