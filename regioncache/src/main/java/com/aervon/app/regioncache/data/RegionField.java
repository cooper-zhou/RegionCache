package com.aervon.app.regioncache.data;

import java.io.Serializable;
import java.util.Map;

/**
 * 保存注解属性里的缓存字段
 *
 * @author : alvin.zhou
 * @datetime : 2018/10/9
 */

public class RegionField implements Serializable {

    private Map<String, Object> cacheFields;

    public RegionField(Map<String, Object> cacheFields) {
        this.cacheFields = cacheFields;
    }

    public Map<String, Object> getCacheFields() {
        return cacheFields;
    }
}
