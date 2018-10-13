package com.aervon.app.regioncache.core;

import android.support.annotation.NonNull;

import com.aervon.app.regioncache.callback.CacheCallback;
import com.aervon.app.regioncache.callback.ReadCallback;
import com.aervon.app.regioncache.provider.ICacheProvider;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 一个数据缓存的请求，可执行区域间的数据存储
 * <p>
 * 请求的构建需要传入一个缓存实现提供者 {@link ICacheProvider}
 * <p>
 * @author : alvin.zhou
 * @datetime : 2018/10/9
 */

public class CacheRequest {

    private ICacheProvider mCacheProvider;

    public CacheRequest(ICacheProvider provider) {
        this.mCacheProvider = provider;
    }

    /**
     * 执行缓存动作
     *
     * @param regionObj 缓存动作所在的域
     *
     * @see #cacheRegion(Object) 同步缓存
     * @see #cacheRegion(Object, String)
     * @see #cacheRegionAsync(Object, CacheCallback) 异步缓存
     * @see #cacheRegionAsync(Object, String, CacheCallback)
     */
    public boolean cacheRegion(Object regionObj) {
        return cacheRegion(regionObj, "");
    }

    public boolean cacheRegion(Object regionObj, String cacheTag) {
        try {
            doCacheRegion(regionObj, cacheTag);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void cacheRegionAsync(Object regionObj, CacheCallback callback) {
        cacheRegionAsync(regionObj, "", callback);
    }

    public void cacheRegionAsync(Object regionObj, final String cacheTag, final CacheCallback callback) {
        Observable.just(regionObj)
                .subscribeOn(Schedulers.io())
                .map(new Function<Object, CacheResponse>() {
                    @Override
                    public CacheResponse apply(@NonNull Object regionObj) throws Exception {
                        try {
                            doCacheRegion(regionObj, cacheTag);
                            return CacheResponse.createSuccessResponse();
                        } catch (Exception e) {
                            e.printStackTrace();
                            return CacheResponse.createErrorResponse(e.toString());
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CacheResponse>() {
                    @Override
                    public void accept(CacheResponse response) throws Exception {
                        if (callback != null) {
                            if (response.getCode() == CacheResponse.SUCCESS) {
                                callback.onCacheCompleted();
                            } else if (response.getCode() == CacheResponse.ERROR) {
                                callback.onCacheError(response.getMsg());
                            }
                        }
                    }
                });
    }

    private void doCacheRegion(Object regionObj, String cacheTag) throws Exception {
        Map<String, Object> cacheFields = RegionObject.with(regionObj).getCacheObjects();
        for (Map.Entry<String, Object> entry : cacheFields.entrySet()) {
            mCacheProvider.cacheRegion(regionObj, cacheTag, entry.getKey(), entry.getValue());
        }
    }

    /**
     * 执行读取动作
     *
     * @param regionObj
     * @see #readRegion(Object) 同步读取
     * @see #readRegion(Object, String)
     * @see #readRegionAsync(Object, ReadCallback) 异步读取
     * @see #readRegionAsync(Object, String, ReadCallback)
     */
    public boolean readRegion(Object regionObj) {
        return readRegion(regionObj, "");
    }

    public boolean readRegion(Object regionObj, String cacheTag) {
        try {
            Map<Field, Object> cacheData = doReadRegion(regionObj, cacheTag);
            setReadRegionData(regionObj, cacheData);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void readRegionAsync(final Object regionObj, ReadCallback callback) {
        readRegionAsync(regionObj, "", callback);
    }

    public void readRegionAsync(final Object regionObj, final String cacheTag, final ReadCallback callback) {
        Observable.just(regionObj)
                .subscribeOn(Schedulers.io())
                .map(new Function<Object, CacheResponse>() {
                    @Override
                    public CacheResponse apply(@NonNull Object regionObj) throws Exception {
                        try {
                            Map<Field, Object> cacheData = doReadRegion(regionObj, cacheTag);
                            if (cacheData.isEmpty()) {
                                return CacheResponse.createEmptyResponse();
                            }
                            return CacheResponse.createSuccessResponse(cacheData);
                        } catch (Exception e) {
                            e.printStackTrace();
                            return CacheResponse.createErrorResponse(e.toString());
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CacheResponse>() {
                    @Override
                    public void accept(CacheResponse response) throws Exception {
                        if (callback != null) {
                            if (response.getCode() == CacheResponse.SUCCESS) {
                                // 需要在主线程设置属性的值
                                setReadRegionData(regionObj, response.getCacheData());
                                callback.onReadCompleted();
                            } else if (response.getCode() == CacheResponse.EMPTY) {
                                callback.onReadEmpty();
                            } else if (response.getCode() == CacheResponse.ERROR) {
                                callback.onReadError(response.getMsg());
                            }
                    }   }
                });
    }

    private Map<Field, Object> doReadRegion(Object regionObj, String cacheTag) throws Exception {
        RegionObject region = RegionObject.with(regionObj);
        Map<String, Field> cacheFields = region.getCacheFields();
        Map<Field, Object> cacheData = new HashMap<>();
        for (Map.Entry<String, Field> entry : cacheFields.entrySet()) {
            Object cacheObj = mCacheProvider.readRegion(regionObj, cacheTag, entry.getKey());
            if (cacheObj != null) {
                Field field = entry.getValue();
                cacheData.put(field, cacheObj);
            }
        }
        return cacheData;
    }

    private void setReadRegionData(Object regionObj, Map<Field, Object> data) throws Exception {
        // 需要在主线程设置属性的值
        if (data != null && !data.isEmpty()) {
            for (Map.Entry<Field, Object> entry : data.entrySet()) {
                RegionObject.with(regionObj).setCacheFields(entry.getKey(), entry.getValue());
            }
        }
    }
}
