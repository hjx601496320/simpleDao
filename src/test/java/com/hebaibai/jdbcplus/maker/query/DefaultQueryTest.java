package com.hebaibai.jdbcplus.maker.query;

import com.hebaibai.jdbcplus.maker.Wheres;
import org.junit.Test;
import com.hebaibai.jdbcplus.entity.User;

import java.util.Arrays;

public class DefaultQueryTest {

    @Test
    public void makeSql() {
        DefaultQuery query = new DefaultQuery();
        query.target(User.class);
        query.where(
                Wheres.equal("id", 12),
                Wheres.notEqual("name", 12),
                Wheres.greater("age", 12, true)
        );
        System.out.println(query.toSql());
        System.out.println(query.toSql());
        System.out.println(Arrays.toString(query.getSqlValues()));
        System.out.println(Arrays.toString(query.getSqlValues()));
    }


    @Test
    public void orderBy() {
        DefaultQuery query = new DefaultQuery();
        query.target(User.class);
        query.orderBy("name ,  age", "DESC");
        String sql = query.toSql();
        System.out.println(sql);
        System.out.println(Arrays.toString(query.getSqlValues()));
    }

    @Test
    public void limit() {
        DefaultQuery query = new DefaultQuery();
        query.target(User.class);
        query.limit(0, 10);
        query.orderBy("name,age", "DESC");
        System.out.println(query.toSql());
        System.out.println(Arrays.toString(query.getSqlValues()));

    }

    @Test
    public void where() {
        DefaultQuery query = new DefaultQuery();
        query.target(User.class);
        query.limit(0, 10);
        query.orderBy("user_name,age", "DESC");
        query.where(
                Wheres.equal("user_name", "hebaiabi"),
                Wheres.less("age", 13, true).or()
        );
        System.out.println(query.toSql());
        System.out.println(Arrays.toString(query.getSqlValues()));
    }
}