package com.hebaibai.jdbcplus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 添加在外键字属性上，
 * 属性类型是关联对象的Entity
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FK {

    /**
     * 被关联的字段名称
     *
     * @return
     */
    String value();

}
