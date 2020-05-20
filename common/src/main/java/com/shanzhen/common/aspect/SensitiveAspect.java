package com.shanzhen.common.aspect;

import com.shanzhen.common.annotation.SensitiveInfo;
import com.shanzhen.common.annotation.SensitiveMethod;
import com.shanzhen.common.utils.cipher.CipherUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Description:
 * Created by nijunyang on 2020/5/9 19:05
 */
@Aspect
@Component
public class SensitiveAspect {

    @Value("${sensitive.secretKey}")
    private String secretKey;

    @Pointcut("@annotation(com.shanzhen.common.annotation.SensitiveMethod)")
    public void annotationPointCut() {

    }

    @Around("annotationPointCut()")
//    @Around(value = "execution(* com.nijunyang.aspect.controller.*.testSecret2(..))")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object responseObj = null;
//        DigestUtils
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        // 获取方法上的SensitiveMethod注解
        Method method = methodSignature.getMethod();
        SensitiveMethod sensitiveMethod = method.getAnnotation(SensitiveMethod.class);
        // 参数加密
        if (sensitiveMethod.encrypt()) {
            Object[] args = joinPoint.getArgs();
            // 获取所有参数上的注解
            Annotation[][] parameterAnnotations = method.getParameterAnnotations();
            for (Annotation[] parameterAnnotation : parameterAnnotations) {
                int paramIndex = ArrayUtils.indexOf(parameterAnnotations, parameterAnnotation);
                for (Annotation annotation : parameterAnnotation) {
                    if (annotation instanceof SensitiveInfo) {
                        Object paramValue = args[paramIndex];
                        System.out.println(paramValue);
                        handleEncrypt(paramValue);
                    }
                }
            }
        }
        // 返回结果解密
        responseObj = joinPoint.proceed();
        if (sensitiveMethod.decrypt()) {
            handleDecrypt(responseObj);
        }

        return responseObj;
    }

    /**
     * 处理加密
     *
     * @param bean
     */
    private void handleEncrypt(Object bean) throws IllegalAccessException, NoSuchFieldException {
        if (Objects.isNull(bean)) {
            return;
        }
        Class<?> beanType = bean.getClass();
        if (bean instanceof String) {
            String plaintextValue = (String) bean;
            if (StringUtils.isNotBlank(plaintextValue)) {
                String encryptValue = CipherUtil.encryptString(plaintextValue, secretKey);
                Field field = bean.getClass().getDeclaredField("value");
                field.setAccessible(true);
                char[] ch = (char[]) field.get(bean);
                field.set(bean, encryptValue.toCharArray());
            }
        } else if (beanType.isArray()) {
            int len = Array.getLength(bean);
            for (int i = 0; i < len; i++) {
                Object arrayObject = Array.get(bean, i);
                handleEncrypt(arrayObject);
            }
        } else if (bean instanceof Collection<?>) {
            Collection<?> c = (Collection<?>) bean;
            Iterator<?> it = c.iterator();
            while (it.hasNext()) {
                Object collectionObj = it.next();
                handleEncrypt(collectionObj);
            }
        } else if (bean instanceof Map<?, ?>) {
            Map<?, ?> m = (Map<?, ?>) bean;
            Set<?> set = m.entrySet();
            for (Object o : set) {
                Map.Entry<?, ?> entry = (Map.Entry<?, ?>) o;
                Object mapVal = entry.getValue();
                handleEncrypt(mapVal);
            }
        } else {
            Field[] fields = FieldUtils.getFieldsWithAnnotation(bean.getClass(), SensitiveInfo.class);
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.getType().equals(String.class)) {
                    String plaintextValue = (String) field.get(bean);
                    if (StringUtils.isNotBlank(plaintextValue)) {
                        String encryptValue = CipherUtil.encryptString(plaintextValue, secretKey);
                        field.set(bean, encryptValue);
                    }
                } else { // 1.处理子属性，包括集合中的
                    Object value = field.get(bean);
                    if (value != null) {
                        Class<?> type = value.getClass();
                        if (type.isArray()) {
                            int len = Array.getLength(value);
                            for (int i = 0; i < len; i++) {
                                Object arrayObject = Array.get(value, i);
                                handleEncrypt(arrayObject);
                            }
                        } else if (value instanceof Collection<?>) {
                            Collection<?> c = (Collection<?>) value;
                            Iterator<?> it = c.iterator();
                            while (it.hasNext()) {
                                Object collectionObj = it.next();
                                handleEncrypt(collectionObj);
                            }
                        } else if (value instanceof Map<?, ?>) {
                            Map<?, ?> m = (Map<?, ?>) value;
                            Set<?> set = m.entrySet();
                            for (Object o : set) {
                                Map.Entry<?, ?> entry = (Map.Entry<?, ?>) o;
                                Object mapVal = entry.getValue();
                                handleEncrypt(mapVal);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 处理解密
     *
     * @param bean
     */
    private Object handleDecrypt(Object bean) throws IllegalAccessException {
        if (Objects.isNull(bean)) {
            return null;
        }
        Class<?> beanType = bean.getClass();
        if (bean instanceof String) {
            String plaintextValue = (String) bean;
            if (StringUtils.isNotBlank(plaintextValue)) {
                bean = CipherUtil.decryptString(plaintextValue, secretKey);
            }
        } else if (beanType.isArray()) {
            int len = Array.getLength(bean);
            for (int i = 0; i < len; i++) {
                Object arrayObject = Array.get(bean, i);
                handleDecrypt(arrayObject);
            }
        } else if (bean instanceof Collection<?>) {
            Collection<?> c = (Collection<?>) bean;
            Iterator<?> it = c.iterator();
            while (it.hasNext()) {
                Object collectionObj = it.next();
                handleDecrypt(collectionObj);
            }
        } else if (bean instanceof Map<?, ?>) {
            Map<?, ?> m = (Map<?, ?>) bean;
            Set<?> set = m.entrySet();
            for (Object o : set) {
                Map.Entry<?, ?> entry = (Map.Entry<?, ?>) o;
                Object mapVal = entry.getValue();
                handleDecrypt(mapVal);
            }
        } else if (bean instanceof Page<?>) {
            Page<?> p = (Page<?>) bean;
            if (p.hasContent()) {
                List<?> content = p.getContent();
                for (Object o : content) {
                    handleDecrypt(o);
                }
            }
        } else {
            Field[] fields = FieldUtils.getFieldsWithAnnotation(bean.getClass(), SensitiveInfo.class);
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.getType().equals(String.class)) {
                    String plaintextValue = (String) field.get(bean);
                    if (StringUtils.isNotBlank(plaintextValue)) {
                        String decryptValue = CipherUtil.decryptString(plaintextValue, secretKey);
                        field.set(bean, decryptValue);
                    }
                } else { // 1.处理子属性，包括集合中的
                    Object value = field.get(bean);
                    if (value != null) {
                        Class<?> type = value.getClass();
                        if (type.isArray()) {
                            int len = Array.getLength(value);
                            for (int i = 0; i < len; i++) {
                                Object arrayObject = Array.get(value, i);
                                handleDecrypt(arrayObject);
                            }
                        } else if (value instanceof Collection<?>) {
                            Collection<?> c = (Collection<?>) value;
                            Iterator<?> it = c.iterator();
                            while (it.hasNext()) {
                                Object collectionObj = it.next();
                                handleDecrypt(collectionObj);
                            }
                        } else if (value instanceof Map<?, ?>) {
                            Map<?, ?> m = (Map<?, ?>) value;
                            Set<?> set = m.entrySet();
                            for (Object o : set) {
                                Map.Entry<?, ?> entry = (Map.Entry<?, ?>) o;
                                Object mapVal = entry.getValue();
                                handleDecrypt(mapVal);
                            }
                        }
                    }
                }
            }
        }

        return bean;
    }
}
