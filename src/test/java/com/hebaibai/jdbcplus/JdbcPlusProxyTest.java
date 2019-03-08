package com.hebaibai.jdbcplus;

import com.alibaba.fastjson.JSONObject;
import com.hebaibai.jdbcplus.entity.User;
import com.hebaibai.jdbcplus.jdbc.JdbcTest;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class JdbcPlusProxyTest extends JdbcTest {

    @Test
    public void selectById() {
        User user = jdbcPlus.selectById(User.class, 4);
        System.out.println(user.getClass());
        System.out.println(user.getParentId());
    }

    @Test
    public void selectById2() throws IOException {
        User user = jdbcPlus.selectById(User.class, 2);
        System.out.println(JSONObject.toJSONString(user));
        FileOutputStream fileOut = new FileOutputStream("/home/hjx/user.ser");
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(user);
        out.close();
        fileOut.close();
        System.out.printf("Serialized data is saved in /home/hjx/user.ser");
    }
}