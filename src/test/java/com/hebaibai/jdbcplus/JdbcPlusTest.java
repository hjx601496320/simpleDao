package com.hebaibai.jdbcplus;

import com.hebaibai.jdbcplus.entity.Foo;
import com.hebaibai.jdbcplus.entity.User;
import com.hebaibai.jdbcplus.jdbc.JdbcTest;
import com.hebaibai.jdbcplus.maker.Wheres;
import com.hebaibai.jdbcplus.maker.delete.DefaultDelete;
import com.hebaibai.jdbcplus.maker.delete.Delete;
import com.hebaibai.jdbcplus.maker.query.DefaultQuery;
import com.hebaibai.jdbcplus.maker.query.Query;
import org.junit.Test;

import java.util.*;

public class JdbcPlusTest extends JdbcTest {

    @Test
    public void insertBatch() {
        long l = System.currentTimeMillis();
        List list = new ArrayList();
        for (int i = 0; i < 50000; i++) {
            User user = new User();
            user.setName(UUID.randomUUID().toString());
            user.setMark("mark");
            user.setAge(new Random().nextInt(100));
            user.setCreateDate(new Date());
            list.add(user);
        }
        Integer integer = jdbcPlus.insertBatch(User.class, list);
        System.out.println(integer);
        System.out.println(System.currentTimeMillis() - l);
    }

    @Test
    public void insert() {
        long l = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            User user = new User();
            user.setName(UUID.randomUUID().toString());
            user.setMark("mark");
            user.setAge(new Random().nextInt(100));
            user.setCreateDate(new Date());
            jdbcPlus.insert(user);
        }
        System.out.println(System.currentTimeMillis() - l);
    }

    @Test
    public void select() {
        List<User> users = jdbcPlus.select(User.class);
        System.out.println(users);
    }

    @Test
    public void selectById() {
        User user = jdbcPlus.selectById(User.class, "4028b88159153cce0159165c345a002f");
        System.out.println(user);
    }


    @Test
    public void selectUserBy() {
        List<User> users = jdbcPlus.select(User.class);
        System.out.println(users);
    }

    @Test
    public void selectBy1() {
        List<User> users = jdbcPlus.selectBy(
                User.class,
                "name", "123",
                "age", "18"
        );
        System.out.println(users);
    }


    @Test
    public void selectOneBy() {
        User user = jdbcPlus.selectOneBy(User.class, "age", "18");
        System.out.println(user);
    }

    @Test
    public void selectOneBy1() {
        User user = jdbcPlus.selectOneBy(
                User.class,
                "user_name", "123",
                "age", "18"
        );
        System.out.println(user);
    }

    @Test
    public void selectOneBy2() {
        Query query = new DefaultQuery();
        query.target(User.class);
        query.where(
                Wheres.equal("age", "18")
        );
        User user = jdbcPlus.selectOneBy(query);
        System.out.println(user);
    }

    @Test
    public void updateById() {
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setMark("markUpdate");
            user.setId(new Random().nextInt(100));
            Integer integer = jdbcPlus.updateById(user);
            System.out.println(integer);
        }
    }

    @Test
    public void updateById1() {
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setMark("markUpdate");
            user.setId(new Random().nextInt(100));
            Integer integer = jdbcPlus.updateById(user, true);
            System.out.println(integer);
        }
    }

    @Test
    public void deleteById() {
        Integer deleteById = jdbcPlus.deleteById(User.class, "123");
        System.out.println(deleteById);
    }

    @Test
    public void deleteBy() {
        Delete delete = new DefaultDelete();
        delete.target(User.class);
        Integer deleteBy = jdbcPlus.deleteBy(delete);
        System.out.println(deleteBy);
    }

    @Test
    public void deleteBy1() {
        Integer deleteBy = jdbcPlus.deleteBy(
                User.class,
                "age", "19",
                "user_name", "hebaibai"
        );
        System.out.println(deleteBy);
    }

    @Test
    public void selectOne() {
        List<Foo> foos = jdbcPlus.selectBySql("select * from user limit 1, 2", Foo.class);
        for (Foo foo : foos) {
            System.out.println(foo);
        }

        List<Foo> foos2 = jdbcPlus.selectBySql("select * from user limit ?, ?", new Object[]{1, 10}, Foo.class);
        for (Foo foo : foos2) {
            System.out.println(foo);
        }
    }
}