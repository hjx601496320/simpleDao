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

import javax.persistence.Column;
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


    @Override
    @SneakyThrows(Throwable.class)
    public Object intercept(Object entity, Method method, Object[] values, MethodProxy methodProxy) {
        //执行原本的方法
        Object invokeResult = methodProxy.invokeSuper(proxy, values);
        Class fkEntityClass = method.getReturnType();
        //返回值的Class不是一个Table，说明不是一个外键，直接返回
        //方法定义的返回值与执行结果不属于一个Class，说明为代理对象，直接返回结果
        if (fkEntityClass != invokeResult.getClass() || !EntityUtils.isTable(fkEntityClass)) {
            return invokeResult;
        }
        Field fkField = getFieldBy(method);
        Field fkTargetField = EntityUtils.getEntityFkTargetField(fkField);
        if (fkTargetField == null) {
            return invokeResult;
        }
        Column column = EntityUtils.getAnnotation(fkTargetField, Column.class);
        Object value = ClassUtils.getValue(invokeResult, fkTargetField);
        //执行查询
        log.debug("对外键属性进行数据库查询。。。");
        Object fkEntityProxy = jdbcPlus.selectOneBy(fkEntityClass, column.name(), value);
        //将查询结果赋值给原对象
        ClassUtils.setValue(this.proxy, fkField, fkEntityProxy);
        return fkEntityProxy;
    }

    /**
     * 通过方法找到对应的属性
     *
     * @param method
     * @return
     */
    private Field getFieldBy(Method method) {
        String name = method.getName();
        if (name.startsWith("get")) {
            name = name.substring(3);
            name = StringUtils.lowCase(name, 0);
        } else if (name.startsWith("is")) {
            name = name.substring(2);
            name = StringUtils.lowCase(name, 0);
        } else {
            //没有以get 或者 is 开头的方法，直接返回null
            return null;
        }
        for (Field field : method.getDeclaringClass().getDeclaredFields()) {
            if (!field.getName().equals(name)) {
                continue;
            }
            if (field.getType() != method.getReturnType()) {
                continue;
            }
            return field;
        }
        return null;
    }


    /**
     * 私有化构造器
     */
    private EntityProxy() {
    }
}