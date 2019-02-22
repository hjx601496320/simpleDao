package com.hebaibai.jdbcplus;

import com.hebaibai.jdbcplus.entity.Tools;
import com.hebaibai.jdbcplus.entity.User;
import com.hebaibai.jdbcplus.jdbc.JdbcTest;
import org.junit.Test;

public class JdbcPlusProxyTest extends JdbcTest {

    @Test
    public void selectById() {
        Tools tools = jdbcPlus.selectById(Tools.class, 1);
        Class aClass = tools.getClass();
        System.out.println(tools);
        User user = tools.getUserId();
        System.out.println(user);
    }

    @Test
    public void updateById() {
        Tools tools = jdbcPlus.selectById(Tools.class, 1);
        System.out.println(tools);
        User user = jdbcPlus.selectById(User.class, 2);
        System.out.println(user);
        tools.setUserId(user);
        jdbcPlus.updateById(Tools.class, tools);
    }
}