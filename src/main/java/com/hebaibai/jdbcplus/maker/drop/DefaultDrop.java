package com.hebaibai.jdbcplus.maker.drop;

import com.hebaibai.jdbcplus.AbstractSqlMaker;
import com.hebaibai.jdbcplus.maker.SqlMaker;
import com.hebaibai.jdbcplus.maker.Where;

import java.util.ArrayList;
import java.util.List;

/**
 * 删除表格
 *
 * @author hjx
 */
public class DefaultDrop extends AbstractSqlMaker implements Drop {

    public static final String DROP_TABLE = "DROP TABLE %s;";

    @Override
    public String makeSql() {
        return String.format(DROP_TABLE, entityTableRowMapper.getTableName());
    }

    @Override
    public List<Object> makeSqlValue() {
        return new ArrayList<>();
    }

    @Override
    public SqlMaker where(Where... wheres) {
        throw new UnsupportedOperationException("不支持的操作");
    }
}
