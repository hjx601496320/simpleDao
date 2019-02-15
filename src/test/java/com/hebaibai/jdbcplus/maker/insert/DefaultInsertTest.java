package com.hebaibai.jdbcplus.maker.insert;

import org.junit.Test;
import com.hebaibai.jdbcplus.entity.User;

import java.util.Arrays;
import java.util.Date;

public class DefaultInsertTest {

    @Test
    public void makeSql() {
        User user = new User();
        user.setId(10);
        user.setCreateDate(new Date());
        user.setAge(20);
        user.setMark("ceshi");
        user.setName("heiheihei");
        DefaultInsert insert = new DefaultInsert();
        insert.target(User.class);
        insert.insert(user);
        System.out.println(insert.toSql());
        System.out.println(Arrays.toString(insert.getSqlValues()));
    }

}