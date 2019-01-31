package com.hebaibai.jdbcplus.jdbc;

import org.springframework.jdbc.core.ColumnMapRowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 捕获取值的错误
 *
 * @author hjx
 */
public class PlusColumnMapRowMapper extends ColumnMapRowMapper {

    /**
     * 数据库类型为时间时, 如果值为 0000-00-00 00:00:00
     * 会报错,所以重写此方法，返回null
     *
     * @param rs
     * @param index
     * @return
     * @throws SQLException
     */
    @Override
    protected Object getColumnValue(ResultSet rs, int index) throws SQLException {
        Object columnValue = null;
        try {
            columnValue = super.getColumnValue(rs, index);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return columnValue;
    }
}
