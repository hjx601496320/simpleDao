package com.hebaibai.jdbcplus.entity;

import com.hebaibai.jdbcplus.Column;
import com.hebaibai.jdbcplus.Id;
import com.hebaibai.jdbcplus.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户表
 *
 * @author hejiaxuan
 */
@Getter
@Setter
@Table("user")
public class User {

    /**
     * big
     */
    @Column("big")
    private BigDecimal big;

    /**
     * 用户名
     */
    @Column("name")
    private String name;

    /**
     * 用户id
     */
    @Id
    @Column("id")
    private int id;

    /**
     * 年龄
     */
    @Column("age")
    private int age;

    /**
     * mark
     */
    @Column("mark")
    private String mark;

    /**
     * create_date
     */
    @Column("create_date")
    private Date createDate;

    /**
     * status
     */
    @Column("status")
    private int status;

    @Override
    public String toString() {
        return "User{" +
                "big=" + big +
                ", name='" + name + '\'' +
                ", id=" + id +
                ", age=" + age +
                ", mark='" + mark + '\'' +
                ", createDate=" + createDate +
                ", status=" + status +
                '}';
    }
}
