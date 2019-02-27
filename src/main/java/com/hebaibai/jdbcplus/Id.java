package com.hebaibai.jdbcplus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 添加在属性上，说明是一个Id
 * 每个Entity中只能有一个Id
 * 配合@Column使用
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Id {
}
