package com.hebaibai.jdbcplus.maker.update;

import com.hebaibai.jdbcplus.maker.Wheres;
import org.junit.Test;
import com.hebaibai.jdbcplus.entity.User;

import java.util.Arrays;
import java.util.Date;

public class DefaultUpdateTest {

    @Test
    public void makeSql() {
        User user = new User();
        user.setId(10);
        user.setCreateDate(new Date());
        user.setAge(20);
        user.setName("heiheihei");
        DefaultUpdate defaultUpdate = new DefaultUpdate();
        defaultUpdate.target(User.class);
        defaultUpdate.set(user, true);
        defaultUpdate.where(
                Wheres.equal("id", 12)
        );
        System.out.println(defaultUpdate.toSql());
        System.out.println(Arrays.toString(defaultUpdate.getSqlValues()));
    }

    @Test
    public void where() {
        User user = new User();
        user.setId(11);
        user.setCreateDate(new Date());
        user.setAge(21);
        user.setName("heiheihei2");
        DefaultUpdate defaultUpdate = new DefaultUpdate();
        defaultUpdate.target(User.class);
        defaultUpdate.set(user, true);
        defaultUpdate.where(Wheres.equal("age", 11));

        System.out.println(defaultUpdate.toSql());
        System.out.println(Arrays.toString(defaultUpdate.getSqlValues()));
    }
}