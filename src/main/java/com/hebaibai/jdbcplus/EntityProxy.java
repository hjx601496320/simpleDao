package com.hebaibai.jdbcplus;

import com.hebaibai.jdbcplus.util.ClassUtils;
import com.hebaibai.jdbcplus.util.EntityUtils;
import com.hebaibai.jdbcplus.util.StringUtils;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * entity的代理对象
 *
 * @author hjx
 */
@CommonsLog
public class EntityProxy implements MethodInterceptor {

    /**
     * 代理对象
     */
    @Getter
    private Object proxy;

    /**
     * 数据库操作工具
     */
    private JdbcPlus jdbcPlus;

    /**
     * 创建代理对象
     *
     * @param entity
     * @return
     */
    public static EntityProxy entityProxy(Object entity, JdbcPlus jdbcPlus) {
        log.debug("创建代理对象：" + entity.getClass().getName());
        EntityProxy entityProxy = new EntityProxy();
        Class targetClass = entity.getClass();
        Assert.isTrue(EntityUtils.isTable(targetClass), "代理对象不是一个@Table！");
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(targetClass);
        //设置方法回调
        enhancer.setCallback(entityProxy);
        //创建代理对象
        entityProxy.proxy = enhancer.create();
        entityProxy.jdbcPlus = jdbcPlus;
        return entityProxy;
    }


    /**
     * 代理对象方法拦截器，用于实现几联查询
     *
     * @param entity
     * @param method
     * @param values
     * @param methodProxy
     * @return
     */
    @Override
    @SneakyThrows(Throwable.class)
    public Object intercept(Object entity, Method method, Object[] values, MethodProxy methodProxy) {
        //执行原本的方法
        Object invokeResult = methodProxy.invokeSuper(proxy, values);
        Class fkEntityClass = method.getReturnType();
        String name = method.getName();
        //返回值位null，直接返回
        if (invokeResult == null) {
            return invokeResult;
        }
        //如果是get方法, 或者 boolean 类型的is 开头
        else if (name.startsWith("get") || name.startsWith("is")) {
            Class invokeResultClass = invokeResult.getClass();
            Class superclass = invokeResultClass.getSuperclass();
            //如果父类等于字段类型并且添加了@Table注解，
            // 说明是cglib生成的子类并且已经查询出来了结果，直接返回
            if (fkEntityClass == superclass || !EntityUtils.isTable(fkEntityClass)) {
                return invokeResult;
            }
            //通过方法名找到Entity的属性，之后找到该属性关联的Entity中的属性。
            Field fkField = getFieldBy(method);
            Field fkTargetField = EntityUtils.getEntityFkTargetField(fkField);
            if (fkTargetField == null) {
                return invokeResult;
            }
            Column column = EntityUtils.getAnnotation(fkTargetField, Column.class);
            Object value = ClassUtils.getValue(invokeResult, fkTargetField);
            //执行查询
            log.debug("对外键属性进行数据库查询。。。");
            Object fkEntityProxy = jdbcPlus.selectOneBy(fkEntityClass, column.value(), value);
            //将查询结果赋值给原对象
            ClassUtils.setValue(this.proxy, fkField, fkEntityProxy);
            return invokeResult;
        } else {
            return invokeResult;
        }
    }

    /**
     * 通过方法找到对应的属性
     *
     * @param method
     * @return
     */
    private Field getFieldBy(Method method) {
        String fieldName = method.getName();
        if (fieldName.startsWith("get")) {
            fieldName = fieldName.substring(3);
            fieldName = StringUtils.lowCase(fieldName, 0);
        } else if (fieldName.startsWith("is")) {
            fieldName = fieldName.substring(2);
            fieldName = StringUtils.lowCase(fieldName, 0);
        } else {
            //没有以get 或者 is 开头的方法，直接返回null
            return null;
        }
        //通过属性名找到class中对应的属性
        Class<?> declaringClass = method.getDeclaringClass();
        try {
            Field field = declaringClass.getDeclaredField(fieldName);
            //如果找到的属性字段类型与方法返回值不同，返回null
            if (field.getType() != method.getReturnType()) {
                return null;
            }
            return field;
        } catch (NoSuchFieldException e) {
            return null;
        }
    }


    /**
     * 私有化构造器
     */
    private EntityProxy() {
    }
}