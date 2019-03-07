package com.hebaibai.jdbcplus.maker.query;

import com.hebaibai.jdbcplus.AbstractSqlMaker;
import com.hebaibai.jdbcplus.EntityTableRowMapper;
import com.hebaibai.jdbcplus.util.StringUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * 默认的查询
 *
 * @author hjx
 */
public class DefaultQuery extends AbstractSqlMaker implements Query {

    private String sqlLimit = StringUtils.BLANK;
    private String sqlOrderBy = StringUtils.BLANK;

    /**
     * 要查询作为结果的字段
     */
    private List<String> selectColumns = null;

    /**
     * sql
     */
    private StringBuilder sql = new StringBuilder();

    /**
     * 手动设定要查询的字段
     *
     * @param selectColumns
     */
    @Override
    public void addQueryColumns(List<String> selectColumns) {
        //不为空，并且list中有值的时候
        if (selectColumns != null && selectColumns.size() != 0) {
            this.selectColumns = selectColumns;
        }
    }

    @Override
    public Query orderBy(String orderBy, String type) {
        String[] split = orderBy.split(",");
        for (int i = 0; i < split.length; i++) {
            split[i] = getColumnName(split[i].trim());
        }
        sqlOrderBy = StringUtils.append("ORDER BY ", StringUtils.join(Arrays.asList(split), StringUtils.COMMA),
                StringUtils.SPACE + type + StringUtils.SPACE);
        return this;
    }

    @Override
    public Query limit(int line, int num) {
        this.sqlLimit = StringUtils.append("LIMIT ", line, StringUtils.COMMA + num + StringUtils.SPACE);
        return this;
    }

    /**
     * @return
     */
    @Override
    protected String makeSql() {
        EntityTableRowMapper entityTableRowMapper = getEntityTableRowMapper();
        Set<String> columnNames = entityTableRowMapper.getColumnNames();
        if (selectColumns == null) {
            selectColumns = new ArrayList<>(columnNames.size());
            for (String columnName : columnNames) {
                selectColumns.add(StringUtils.sqlColumn(columnName));
            }
        }
        sql.append(MessageFormat.format("SELECT {0} FROM {1} ",
                StringUtils.join(selectColumns, StringUtils.COMMA), getTableName()));
        sql.append(sqlWhere());
        sql.append(sqlOrderBy);
        sql.append(sqlLimit);
        return sql.toString();
    }

    @Override
    protected List<Object> makeSqlValue() {
        return Arrays.asList(sqlValues);
    }

}
