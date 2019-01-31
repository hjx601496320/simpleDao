package com.hebaibai.jdbcplus.jdbc;

import com.hebaibai.jdbcplus.util.ClassUtils;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

/**
 * 数据库与实体类的映射关系
 *
 * @param <T>
 * @author hjx
 */
public class EntityTableRowMapper<T> implements RowMapper<T> {

    /**
     * id的字段名称
     */
    private String idName = null;

    /**
     * table对应的class
     */
    private Class<T> tableClass = null;

    /**
     * 对应的数据库名称
     */
    private String tableName = null;

    /**
     * 表中所有的字段
     */
    private Set<String> columnNames = null;

    /**
     * 表中所有的字段对应的属性名称
     */
    private Set<String> fieldNames = null;

    /**
     * 属性名称和数据库字段名的映射
     * K: 属性名
     * V：表字段名称
     */
    private Map<String, String> fieldNameColumnMapper = null;

    /**
     * 数据库字段名和class属性的映射
     * K：表字段名称
     * V：class属性
     */
    private Map<String, Field> columnFieldMapper = null;

    /**
     * sql 结果转换
     */
    private ColumnMapRowMapper columnMapRowMapper = new PlusColumnMapRowMapper();

    /**
     * 把数据库查询的结果与对象进行转换
     *
     * @param resultSet
     * @param rowNum
     * @return
     * @throws SQLException
     */
    @Override
    public T mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Map<String, Object> resultMap = columnMapRowMapper.mapRow(resultSet, rowNum);
        Object instance = ClassUtils.getInstance(tableClass);
        for (Map.Entry<String, Object> entry : resultMap.entrySet()) {
            //数据库字段名
            String key = entry.getKey();
            if (!columnFieldMapper.containsKey(key)) {
                continue;
            }
            Field declaredField = columnFieldMapper.get(key);
            if (declaredField == null) {
                continue;
            }
            //数据库字段值
            Object value = entry.getValue();
            ClassUtils.setValue(instance, declaredField, value);
        }
        return (T) instance;
    }


    public Map<String, Field> getColumnFieldMapper() {
        return columnFieldMapper;
    }

    public void setColumnFieldMapper(Map<String, Field> columnFieldMapper) {
        this.columnFieldMapper = columnFieldMapper;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getIdName() {
        Assert.notNull(idName, "@Id 不存在,无法找到id！");
        return idName;
    }

    public void setIdName(String idName) {
        this.idName = idName;
    }

    public Class<T> getTableClass() {
        return tableClass;
    }

    public void setTableClass(Class tableClass) {
        this.tableClass = tableClass;
    }

    public Set<String> getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(Set<String> columnNames) {
        this.columnNames = columnNames;
    }

    public Set<String> getFieldNames() {
        return fieldNames;
    }

    public void setFieldNames(Set<String> fieldNames) {
        this.fieldNames = fieldNames;
    }

    public Map<String, String> getFieldNameColumnMapper() {
        return fieldNameColumnMapper;
    }

    public void setFieldNameColumnMapper(Map<String, String> fieldNameColumnMapper) {
        this.fieldNameColumnMapper = fieldNameColumnMapper;
    }
}
