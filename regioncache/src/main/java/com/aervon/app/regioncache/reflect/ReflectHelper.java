package com.aervon.app.regioncache.reflect;

/**
 * {@link Reflect} 的帮助类，添加异常的捕获
 *
 * @author : alvin.zhou
 * @datetime : 2018/10/9
 */

public class ReflectHelper {

    private Object object;

    public ReflectHelper(Object object) {
        this.object = object;
    }

    public static ReflectHelper on(Object object) {
        return new ReflectHelper(object);
    }

    public Object call(String method) {
        try {
            return Reflect.on(object).call(method).get();
        } catch (Exception e) {
            return null;
        }
    }

    public Object call(String method, Object... params) {
        try {
            return Reflect.on(object).call(method, params).get();
        } catch (Exception e) {
            return null;
        }
    }
}
