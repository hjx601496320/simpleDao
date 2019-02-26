package com.hebaibai.jdbcplus.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

/**
 * 工具
 *
 * @author hejiaxuan
 */
@Getter
@Setter
@Table(name = "tools")
public class Tools {

    /**
     * 工具名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 工具id
     */
    @Id
    @Column(name = "id")
    private int id;

    /**
     * 类型
     */
    @Column(name = "type")
    private String type;

    /**
     * 所属用户
     */
    @Column(name = "user_id")
    @JoinColumn(name = "id")
    private User userId;

    @Override
    public String toString() {
        return "Tools{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", type='" + type + '\'' +
                ", userId=" + userId +
                '}';
    }
}
