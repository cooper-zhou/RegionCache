package com.aervon.app.regioncache.core;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 区域注解，在某个区域里带有此注解的属性都会在 {@link CacheRequest#cacheRegion(Object)} 被保存
 * <p>
 * 当注解的属性带有field字段时，默认是保存注解属性对象里带有setter和getter方法的属性，最多生命3个field
 * </p>
 * @author : alvin.zhou
 * @datetime : 2018/10/9
 */
@Target(value = ElementType.FIELD)
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public @interface RegionCache {

    String field1() default "";

    String field2() default "";

    String field3() default "";

    int from() default -1;

    int to() default -1;
}
