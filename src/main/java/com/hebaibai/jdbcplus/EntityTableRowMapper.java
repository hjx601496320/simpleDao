package com.hebaibai.jdbcplus;

import com.hebaibai.jdbcplus.mapper.PlusColumnMapRowMapper;
import com.hebaibai.jdbcplus.util.ClassUtils;
import com.hebaibai.jdbcplus.util.EntityUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.Assert;

import javax.persistence.JoinColumn;
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
@Getter
@Setter
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
     * 对象查询时使用
     */
    private JdbcPlus jdbcPlus;

    /**
     * 把数据库查询的结果与对象进行转换
     *
     * @param resultSet
     * @param rowNum
     * @return
     * @throws SQLException
     */
    @Override
    @SneakyThrows(SQLException.class)
    public T mapRow(ResultSet resultSet, int rowNum) {
        Map<String, Object> resultMap = columnMapRowMapper.mapRow(resultSet, rowNum);
        //创建cglib代理对象
        Object instance = ClassUtils.getInstance(tableClass);
        EntityProxy entityProxy = EntityProxy.entityProxy(instance, jdbcPlus);
        Object proxy = entityProxy.getProxy();
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
            Object value = entry.getValue();
            //日过添加@JoinColumn注解，将关联对象中新建一个空对象占位
            if (EntityUtils.isJoinColumn(declaredField)) {
                Object fkObject = getJoinFieldObject(declaredField, value);
                ClassUtils.setValue(proxy, declaredField, fkObject);
            } else {
                ClassUtils.setValue(proxy, declaredField, value);
            }
        }
        return (T) proxy;
    }


    /**
     * 用于填充查询对象，使其toString中外键值不显示null
     *
     * @param fkField 外键属性
     * @param value   sql中的结果
     * @return
     */
    Object getJoinFieldObject(Field fkField, Object value) {
        if (value == null) {
            return null;
        }
        Class fieldType = fkField.getType();
        JoinColumn joinColumn = EntityUtils.getAnnotation(fkField, JoinColumn.class);
        String fieldName = joinColumn.name();
        //实例化原始对象，与之后的代理对象做区分
        Object joinFieldValue = ClassUtils.getInstance(fieldType);
        Field field = columnFieldMapper.get(fieldName);
        ClassUtils.setValue(joinFieldValue, field, value);
        return joinFieldValue;
    }

    public String getIdName() {
        Assert.notNull(idName, "@Id 不存在,无法找到id！");
        return idName;
    }

}
