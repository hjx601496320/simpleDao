package com.hebaibai.jdbcplus;

import com.hebaibai.jdbcplus.entity.User;
import com.hebaibai.jdbcplus.jdbc.JdbcTest;
import org.junit.Test;

public class JdbcPlusProxyTest extends JdbcTest {

    @Test
    public void selectById() {
        User user = jdbcPlus.selectById(User.class, 4);
        System.out.println(user);
    }

    @Test
    public void selectById2() {
        User user = jdbcPlus.selectById(User.class, 2);
        System.out.println(user);
        System.out.println(user.getParentId());
        System.out.println(user.getParentId().hashCode());
        System.out.println(user.getParentId().hashCode());
    }
}