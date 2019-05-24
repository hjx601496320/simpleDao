package com.hebaibai.jdbcplus.maker.insert;

import org.junit.Test;
import com.hebaibai.jdbcplus.entity.User;

import java.util.Arrays;
import java.util.Date;

public class DefaultInsertTest {

    @Test
    public void makeSql() {
        DefaultInsert insert = new DefaultInsert();
        insert.target(User.class);

        User user = new User();
        user.setId(10);
        user.setCreateDate(new Date());
        user.setAge(20);
        user.setMark("ceshi");
        user.setName("heiheihei");
        insert.insert(user);

        User user1 = new User();
        user1.setId(10);
        user1.setCreateDate(new Date());
        user1.setAge(20);
        user1.setMark(null);
        user1.setName(null);
        insert.insert(user1);
        System.out.println(insert.toSql());
        System.out.println(Arrays.toString(insert.getSqlValues()));
    }

}