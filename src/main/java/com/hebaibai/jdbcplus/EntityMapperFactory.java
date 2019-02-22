package com.hebaibai.jdbcplus;

import com.hebaibai.jdbcplus.util.EntityUtils;
import lombok.experimental.UtilityClass;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * mapper 的 工厂类
 *
 * @author hjx
 */
@UtilityClass
class EntityMapperFactory {

    /**
     * mapper 的 缓存
     */
    private static Map<Class, EntityTableRowMapper> entityTableRowMapperMap = new HashMap<>();

    /**
     * 数据库的操作类
     */
    private static JdbcPlus jdbcPlus;

    /**
     * 获取mapper
     *
     * @param clz
     * @return
     */
    public static EntityTableRowMapper getMapper(Class clz) {
        Assert.notNull(clz);
        EntityTableRowMapper<?> mapper = null;
        //即使出现了并发的问题，无非就是多创建几次对象，不会出现数据错乱。
        if (entityTableRowMapperMap.get(clz) == null) {
            mapper = new EntityTableRowMapper();
            mapper.setJdbcPlus(jdbcPlus);
            Map<String, Field> columnFieldMap = EntityUtils.columnFieldMap(clz);
            int size = columnFieldMap.size();
            Map<String, String> fieldNameColumnMapper = new HashMap<>(size);
            Set<String> columnNames = new HashSet<>(size);
            Set<String> fieldNames = new HashSet<>(size);
            mapper.setTableClass(clz);
            mapper.setTableName(EntityUtils.tableName(clz));
            mapper.setIdName(EntityUtils.idColumnName(clz));
            mapper.setColumnFieldMapper(columnFieldMap);
            for (Map.Entry<String, Field> entry : columnFieldMap.entrySet()) {
                String columnName = entry.getKey();
                Field field = entry.getValue();
                String fieldName = field.getName();
                fieldNameColumnMapper.put(fieldName, columnName);
                fieldNames.add(fieldName);
                columnNames.add(columnName);
            }
            mapper.setColumnNames(columnNames);
            mapper.setFieldNameColumnMapper(fieldNameColumnMapper);
            mapper.setFieldNames(fieldNames);
            entityTableRowMapperMap.put(clz, mapper);
        }
        return entityTableRowMapperMap.get(clz);
    }

    public static void setJdbcPlus(final JdbcPlus jdbcPlus) {
        if (EntityMapperFactory.jdbcPlus == null) {
            EntityMapperFactory.jdbcPlus = jdbcPlus;
        }
    }
}
