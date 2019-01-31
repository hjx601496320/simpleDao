package com.hebaibai.jdbcplus.jdbc;

import com.hebaibai.jdbcplus.util.ClassUtils;
import com.hebaibai.jdbcplus.util.EntityUtils;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.RowMapper;

import javax.persistence.Column;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;


/**
 * 属性和查询结果根据名称进行绑定的RowMapper
 *
 * @author hjx
 */
public class FieldColumnRowMapper implements RowMapper {

    /**
     * 目标class
     */
    private Class objectClass;

    public FieldColumnRowMapper(Class objectClass) {
        this.objectClass = objectClass;
    }

    /**
     * sql 结果转换
     */
    private ColumnMapRowMapper columnMapRowMapper = new PlusColumnMapRowMapper();

    /**
     * 处理每一行结果，并转换对象
     *
     * @param resultSet
     * @param rowNum
     * @return
     * @throws SQLException
     */
    @Override
    public Object mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Map<String, Object> resultMap = columnMapRowMapper.mapRow(resultSet, rowNum);
        Object instance = ClassUtils.getInstance(objectClass);
        Field[] fields = objectClass.getDeclaredFields();
        for (Field field : fields) {
            String name = field.getName();
            Object value = resultMap.get(name);
            //value为null时，查找字段上有没有Column注解
            if (value == null) {
                Column column = EntityUtils.getAnnotation(field, Column.class);
                //注解不为null时，通过字段名称从结果集中找结果
                if (column != null) {
                    String columnName = column.name();
                    value = resultMap.get(columnName);
                }
            }
            if (value == null) {
                continue;
            }
            ClassUtils.setValue(instance, field, value);
        }
        return instance;
    }
}
