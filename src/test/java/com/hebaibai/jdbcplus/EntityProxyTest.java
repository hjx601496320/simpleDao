package com.hebaibai.jdbcplus;

import com.hebaibai.jdbcplus.entity.User;
import com.hebaibai.jdbcplus.jdbc.JdbcTest;
import com.hebaibai.jdbcplus.maker.insert.DefaultInsert;
import com.hebaibai.jdbcplus.maker.insert.Insert;
import com.hebaibai.jdbcplus.maker.update.DefaultUpdate;
import com.hebaibai.jdbcplus.maker.update.Update;
import org.junit.Test;

import java.util.Arrays;

public class EntityProxyTest extends JdbcTest {

    @Test
    public void insert() {
        User parent = new User();
        parent.setAge(20);
        parent.setId(11);
        User user = new User();
        user.setId(12);
        user.setParentId(parent);
        Insert insert = new DefaultInsert();
        insert.target(User.class);
        insert.insert(user);
        System.out.println(insert.toSql());
        System.out.println(Arrays.toString(insert.getSqlValues()));
    }

    @Test
    public void update() {
        User parent = new User();
        parent.setAge(20);
        parent.setId(13);
        User user = new User();
        user.setId(12);
        user.setParentId(parent);
        Update update = new DefaultUpdate();
        update.target(User.class);
        update.set(user,true);
        System.out.println(update.toSql());
        System.out.println(Arrays.toString(update.getSqlValues()));
    }
}