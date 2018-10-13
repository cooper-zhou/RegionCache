package com.aervon.app.regioncache.provider;

/**
 * 缓存实现的提供者，默认为文件缓存提供者 {@link FileCacheProvider}, RegionCache提供自定义缓存实现方式
 * <p>
 *
 * @author : alvin.zhou
 * @datetime : 2018/10/9
 */

public interface ICacheProvider {

    /**
     * 缓存对象数据
     *
     * @param regionObj  指定缓存对象所在的区域
     * @param cacheTag 指定缓存对象的tag，针对同个类型对象不同情况的缓存
     * @param cacheField 指定缓存对象的属性名
     * @param cacheObj   指定需要缓存的对象，即ownerObj中标记的属性
     * @throws Exception
     */
    void cacheRegion(Object regionObj, String cacheTag, String cacheField, Object cacheObj) throws Exception;

    /**
     * 读取缓存对象数据
     *
     * @param regionObj  指定缓存对象所在的区域
     * @param cacheTag 指定缓存对象的tag
     * @param cacheField 指定缓存对象的属性名，通过属性名去读取其相应的缓存数据
     * @throws Exception
     * @return Object
     */
    Object readRegion(Object regionObj, String cacheTag, String cacheField) throws Exception;
}
