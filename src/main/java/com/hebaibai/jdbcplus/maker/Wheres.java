package com.hebaibai.jdbcplus.maker;

import com.hebaibai.jdbcplus.util.StringUtils;
import lombok.experimental.UtilityClass;

import java.util.Arrays;

/**
 * 查询条件
 *
 * @author hjx
 */
@UtilityClass
public class Wheres {

    public static Where equal(final String columnName, final Object value) {
        return new Where(columnName, Where.PLACEHOLDER + " = ? ", value);
    }

    public static Where notEqual(final String columnName, final Object value) {
        return new Where(columnName, Where.PLACEHOLDER + " != ? ", value);
    }

    public static Where not(final String columnName, final Object value) {
        return new Where(columnName, Where.PLACEHOLDER + " <> ? ", value);
    }

    public static Where isNotNull(final String columnName) {
        return new Where(columnName, Where.PLACEHOLDER + " IS NOT NULL ");
    }

    public static Where isNull(final String columnName) {
        return new Where(columnName, Where.PLACEHOLDER + " IS NULL ");
    }

    public static Where greater(final String columnName, final Object value, final boolean andEquals) {
        if (andEquals) {
            return new Where(columnName, Where.PLACEHOLDER + " >= ? ", value);
        }
        return new Where(columnName, Where.PLACEHOLDER + " > ? ", value);
    }

    public static Where less(final String columnName, final Object value, final boolean andEquals) {
        if (andEquals) {
            return new Where(columnName, Where.PLACEHOLDER + " <= ? ", value);
        }
        return new Where(columnName, Where.PLACEHOLDER + " < ? ", value);
    }

    public static Where like(final String columnName, final Object value) {
        return new Where(columnName, Where.PLACEHOLDER + " like ? ", value);
    }

    public static Where betweenAnd(final String columnName, final Object value1st, final Object value2nd) {
        return new Where(columnName, Where.PLACEHOLDER + " between ? and ? ", new Object[]{value1st, value2nd});
    }

    public static Where in(final String columnName, final Object[] values) {
        Object[] sqlVal = values;
        if (sqlVal.length == 0) {
            sqlVal = new Object[]{null};
        }
        StringBuffer inSql = new StringBuffer();
        inSql.append(Where.PLACEHOLDER);
        inSql.append(" IN ( ");
        String[] strings = StringUtils.repeat("?", sqlVal.length);
        inSql.append(StringUtils.join(Arrays.asList(strings), ", "));
        inSql.append(" ) ");
        return new Where(columnName, inSql.toString(), sqlVal);
    }

}
