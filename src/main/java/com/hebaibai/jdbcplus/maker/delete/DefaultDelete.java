package com.hebaibai.jdbcplus.maker.delete;

import com.hebaibai.jdbcplus.util.StringUtils;
import com.hebaibai.jdbcplus.maker.AbstractSqlMaker;

import java.util.Arrays;
import java.util.List;

/**
 * 默认的删除
 *
 * @author hjx
 */
public class DefaultDelete extends AbstractSqlMaker implements Delete {

    @Override
    protected String makeSql() {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM ")
                .append(getTableName())
                .append(StringUtils.SPACE)
                .append(sqlWhere());
        return sql.toString();
    }

    @Override
    protected List<Object> makeSqlValue() {
        return Arrays.asList(sqlValues);
    }

}
