package com.hebaibai.jdbcplus.util;

import lombok.experimental.UtilityClass;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * class工具类
 */
@UtilityClass
public class ClassUtils {
    /**
     * 实例化泛型对象
     *
     * @param aClass
     * @return
     */
    public static <T> Object getInstance(Class<T> aClass) {
        Assert.notNull(aClass);
        try {
            T t = aClass.newInstance();
            return t;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("实例化对象失败");
    }

    /**
     * 根据className加载class
     *
     * @param className
     * @return
     */
    public static Class forName(String className) {
        try {
            Class<?> aClass = Class.forName(className);
            return aClass;
        } catch (ClassNotFoundException e) {
        }
        return null;
    }

    /**
     * 取值
     *
     * @param target 要从哪一个对象中取值
     * @param field  要取这个对象的那个属性的值
     * @return
     */
    public static Object getValue(Object target, Field field) {
        Assert.notNull(target);
        Assert.notNull(field);
        boolean accessible = field.isAccessible();
        if (!accessible) {
            field.setAccessible(true);
        }
        try {
            return field.get(target);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            field.setAccessible(accessible);
        }
        return null;
    }

    /**
     * 设置值
     *
     * @param target 要从哪一个对象中取值
     * @param field  要取这个对象的那个属性的值
     * @param value  要设置的值
     * @return
     */
    public static boolean setValue(Object target, Field field, Object value) {
        Assert.notNull(target);
        Assert.notNull(field);
        boolean accessible = field.isAccessible();
        if (!accessible) {
            field.setAccessible(true);
        }
        try {
            field.set(target, value);
            return true;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            //数据库中没有值得情况下报错，可以忽略
        } finally {
            field.setAccessible(accessible);
        }
        return false;
    }

    /**
     * 获取该属性的get方法
     *
     * @param field
     * @return
     */
    public static Method getGetMethod(Field field) {
        Assert.notNull(field);
        String fieldName = field.getName();
        char[] ch = fieldName.toCharArray();
        if (ch[0] >= 'a' && ch[0] <= 'z') {
            ch[0] = (char) (ch[0] - 32);
        }
        Class<?> declaringClass = field.getDeclaringClass();
        String methodName = "get" + new String(ch);
        try {
            Method getMethod = declaringClass.getDeclaredMethod(methodName, null);
            return getMethod;
        } catch (NoSuchMethodException e) {
        }
        return null;
    }
}
