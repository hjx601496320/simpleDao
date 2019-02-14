package com.hebaibai.jdbcplus.util;

import org.springframework.util.Assert;

import java.lang.reflect.Field;

/**
 * class工具类
 */
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
     * 取值
     *
     * @param target 要从哪一个对象中取值
     * @param field  要取这个对象的那个属性的值
     * @return
     */
    public static Object getValue(Object target, Field field) {
        Assert.notNull(target);
        Assert.notNull(field);
        field.setAccessible(true);
        try {
            return field.get(target);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
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
        field.setAccessible(true);
        try {
            field.set(target, value);
            return true;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            //数据库中没有值得情况下报错，可以忽略
        }
        return false;
    }

}
