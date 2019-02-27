package com.hebaibai.jdbcplus.util;

import com.hebaibai.jdbcplus.entity.User;
import org.junit.Test;

import com.hebaibai.jdbcplus.Column;
import com.hebaibai.jdbcplus.Table;
import java.lang.reflect.Field;

public class EntityUtilsTest {

    @Test
    public void getClassAnnotation() {
        Table annotation = EntityUtils.getAnnotation(User.class, Table.class);
        System.out.println(annotation.value());
    }

    @Test
    public void getFieldAnnotation() throws NoSuchFieldException {
        Class userClass = User.class;
        Field field = userClass.getDeclaredField("createDate");
        Column annotation = EntityUtils.getAnnotation(field, Column.class);
        System.out.println(annotation.value());
    }
}