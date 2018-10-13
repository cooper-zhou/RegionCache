package com.aervon.app.regioncache.provider;

import android.text.TextUtils;

import com.aervon.app.regioncache.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 文件缓存提供者，要求存储对象必须实现 {@link java.io.Serializable}
 * <p>
 * 文件名：CacheRequest.CACHE_DIR + "/" + MD5(区域类名) + "_" + MD5(缓存对象域名) + ".dat"
 * <p>
 * @author : alvin.zhou
 * @datetime : 2018/10/9
 */

public class FileCacheProvider implements ICacheProvider {

    private String mCacheDir;

    public FileCacheProvider(String cacheDir) {
        this.mCacheDir = cacheDir;
    }

    /**
     * 获取缓存文件完整路径
     *
     * @param regionObj   指定缓存对象所在的区域
     * @param cacheField 指定缓存对象的属性名
     * @return 缓存文件完整路径
     */
    public String getCacheFilePath(String cacheTag, Object regionObj, String cacheField) {
        StringBuilder sb = new StringBuilder();
        sb.append(mCacheDir)
                .append(File.separator)
                .append(Utils.MD5(regionObj.getClass().getSimpleName()));
        if (!TextUtils.isEmpty(cacheTag)) {
            sb.append(File.separator).append(cacheTag);
        }
        Utils.createDirIfNeed(sb.toString());
        return sb.append(File.separator)
                .append(Utils.MD5(cacheField))
                .append(".txt")
                .toString();
    }

    @Override
    public void cacheRegion(Object regionObj, String cacheTag, String cacheField, Object cacheObj) throws Exception {
        if (TextUtils.isEmpty(cacheField) || cacheObj == null) {
            // 对象为空不做缓存处理
            return;
        }
        String cachePath = getCacheFilePath(cacheTag, regionObj, cacheField);
        File cacheFile = new File(cachePath);
        if (!cacheFile.exists()) {
            cacheFile.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(cacheFile);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(cacheObj);
        oos.flush();
        oos.close();
    }

    @Override
    public Object readRegion(Object regionObj, String cacheTag, String cacheField) throws Exception {
        String cachePath = getCacheFilePath(cacheTag, regionObj, cacheField);
        File cacheFile = new File(cachePath);
        if (!cacheFile.exists()) {
            // 没有对应缓存
            return null;
        }
        FileInputStream fins = new FileInputStream(cacheFile);
        ObjectInputStream oins = new ObjectInputStream(fins);
        Object cacheObj = oins.readObject();
        oins.close();
        return cacheObj;
    }
}
