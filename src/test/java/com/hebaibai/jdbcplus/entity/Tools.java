package com.hebaibai.jdbcplus.entity;

import com.hebaibai.jdbcplus.Column;
import com.hebaibai.jdbcplus.Id;
import com.hebaibai.jdbcplus.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * 工具
 *
 * @author hejiaxuan
 */
@Getter
@Setter
@Table("tools")
public class Tools {

    /**
     * 工具名称
     */
    @Column("name")
    private String name;

    /**
     * 工具id
     */
    @Id
    @Column("id")
    private int id;

    /**
     * 类型
     */
    @Column("type")
    private String type;

    @Override
    public String toString() {
        return "Tools{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", type='" + type + '\'' +
                '}';
    }
}
