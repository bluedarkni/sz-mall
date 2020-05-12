package com.shanzhen.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Description: 敏感信息方法注解
 * Created by nijunyang on 2020/5/9 19:03
 */
@Documented
@Target({ ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SensitiveMethod {
    /**
     * 加密入参
     */
    boolean encrypt() default true;

    /**
     * 解密返回
     */
    boolean decrypt() default true;
}
