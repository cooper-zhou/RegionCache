package com.aervon.app.regioncache.core;

import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import com.aervon.app.regioncache.Utils;
import com.aervon.app.regioncache.data.RegionField;
import com.aervon.app.regioncache.reflect.ReflectHelper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 区域对象帮助类，用于获取区域对象中标注的缓存对象，缓存域等
 * <p>
 * @author : alvin.zhou
 * @datetime : 2018/10/9
 */

public class RegionObject {

    private Object mRegionObject;

    private RegionObject(Object regionObj) {
        this.mRegionObject = regionObj;
    }

    public static RegionObject with(Object regionObj) {
        return new RegionObject(regionObj);
    }

    /**
     * 在进行区域缓存时，获取区域对象中的缓存对象
     *<p>
     *     @see CacheRequest#cacheRegion(Object)
     *</p>
     * @throws Exception
     */
    public Map<String, Object> getCacheObjects() throws Exception {
        Map<String, Object> fieldMap = new ArrayMap<>();
        Class clazz = mRegionObject.getClass();
        Field[] fields = clazz.getDeclaredFields();
        if (fields == null || fields.length == 0) {
            return null;
        }
        for (Field field : fields) {
            RegionCache annotation = field.getAnnotation(RegionCache.class);
            Object fieldValue = field.get(mRegionObject);
            if (annotation != null && fieldValue != null) {
                if (Utils.hasNotEmpty(annotation.field1(), annotation.field2(), annotation.field3())) {
                    // 有指定缓存属性
                    Map<String, Object> regionField = new HashMap<>();
                    if (!Utils.isEmpty(annotation.field1())) {
                        Object fieldObj = ReflectHelper.on(fieldValue).call(getObjectGetterMethod(annotation.field1()));
                        if (fieldObj != null) {
                            regionField.put(annotation.field1(), fieldObj);
                        }
                    }
                    if (!Utils.isEmpty(annotation.field2())) {
                        Object fieldObj = ReflectHelper.on(fieldValue).call(getObjectGetterMethod(annotation.field2()));
                        if (fieldObj != null) {
                            regionField.put(annotation.field2(), fieldObj);
                        }
                    }
                    if (!Utils.isEmpty(annotation.field3())) {
                        Object fieldObj = ReflectHelper.on(fieldValue).call(getObjectGetterMethod(annotation.field3()));
                        if (fieldObj != null) {
                            regionField.put(annotation.field3(), fieldObj);
                        }
                    }
                    if (regionField.size() != 0) {
                        fieldMap.put(field.getName(), new RegionField(regionField));
                    }
                } else {
                    // 无指定缓存属性
                    if (fieldValue instanceof List) {
                        // 有指定区间数据
                        if (annotation.from() != -1 && annotation.from() <= annotation.to()) {
                            List originList = (List) fieldValue;
                            List subList = new ArrayList();
                            for (int i = annotation.from(); i < originList.size() && i <= annotation.to(); i++) {
                                subList.add(originList.get(i));
                            }
                            fieldValue = subList;
                        } else {
                            continue;
                        }
                    }
                    fieldMap.put(field.getName(), fieldValue);
                }
            }
        }
        return fieldMap;
    }

    /**
     * 在读取区域缓存数据时，获取域中相应缓存的属性域名
     * <p>
     *     @see CacheRequest#readRegion(Object)
     * </p>
     * @throws Exception
     */
    public Map<String, Field> getCacheFields() throws Exception {
        Map<String, Field> fieldMap = new ArrayMap<>();
        Class clazz = mRegionObject.getClass();
        Field[] fields = clazz.getDeclaredFields();
        if (fields == null || fields.length == 0) {
            return null;
        }
        for (Field field : fields) {
            RegionCache annotation = field.getAnnotation(RegionCache.class);
            if (annotation != null) {
                fieldMap.put(field.getName(), field);
            }
        }
        return fieldMap;
    }

    /**
     * 在读取区域缓存数据后，自动设置缓存对象的缓存值
     * <p>
     *     @see CacheRequest#readRegion(Object)
     * </p>
     * @param field
     * @param cacheObj
     * @throws Exception
     */
    public void setCacheFields(Field field, Object cacheObj) throws Exception {
        if (cacheObj instanceof RegionField) {
            Object fieldObj = field.get(mRegionObject);
            if (fieldObj != null) {
                RegionField regionField = (RegionField) cacheObj;
                if (regionField.getCacheFields() != null && regionField.getCacheFields().size() > 0) {
                    for (Map.Entry<String, Object> fieldEntry : regionField.getCacheFields().entrySet()) {
                        String method = RegionObject.getObjectSetterMethod(fieldEntry.getKey());
                        ReflectHelper.on(fieldObj).call(method, fieldEntry.getValue());
                    }
                }
            }
        } else {
            field.set(mRegionObject, cacheObj);
        }
    }

    public static String getObjectGetterMethod(String fieldName) {
        if (TextUtils.isEmpty(fieldName)) {
            return "";
        }
        if (Character.isLowerCase(fieldName.charAt(0))) {
            return "get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1, fieldName.length());
        }
        return "get" + fieldName;
    }

    public static String getObjectSetterMethod(String fieldName) {
        if (TextUtils.isEmpty(fieldName)) {
            return "";
        }
        if (Character.isLowerCase(fieldName.charAt(0))) {
            return "set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1, fieldName.length());
        }
        return "set" + fieldName;
    }
}
