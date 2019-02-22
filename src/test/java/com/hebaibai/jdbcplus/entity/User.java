package com.hebaibai.jdbcplus.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户表
 * @author hejiaxuan
 */
@Getter
@Setter
@Table(name = "user")
public class User {

    /**big*/
    @Column(name = "big")
    private BigDecimal big;

    /**用户名*/
    @Column(name = "name")
    private String name;

    /**用户id*/
    @Id
    @Column(name = "id")
    private int id;

    /**年龄*/
    @Column(name = "age")
    private int age;

    /**mark*/
    @Column(name = "mark")
    private String mark;

    /**parent_id*/
    @Column(name = "parent_id")
    @JoinColumn(name = "id")
    private User parentId;

    /**create_date*/
    @Column(name = "create_date")
    private Date createDate;

    /**status*/
    @Column(name = "status")
    private int status;

    @Override
    public String toString() {
        return "User{" +
                "big=" + big +
                ", name='" + name + '\'' +
                ", id=" + id +
                ", age=" + age +
                ", mark='" + mark + '\'' +
                ", parentId=" + parentId +
                ", createDate=" + createDate +
                ", status=" + status +
                '}';
    }
}
