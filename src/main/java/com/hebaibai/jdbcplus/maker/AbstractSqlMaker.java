package com.hebaibai.jdbcplus.maker;

import com.hebaibai.jdbcplus.jdbc.EntityMapperFactory;
import com.hebaibai.jdbcplus.jdbc.EntityTableRowMapper;
import com.hebaibai.jdbcplus.util.StringUtils;
import org.springframework.util.Assert;

import java.util.*;

/**
 * 默认实现
 *
 * @author hjx
 */
public abstract class AbstractSqlMaker implements SqlMaker {

    public AbstractSqlMaker() {
    }

    /**
     * sql 是否创建完成
     */
    private boolean sqlComplete = false;

    /**
     * sqlValues 是否创建完成
     */
    private boolean sqlValueComplete = false;

    /**
     * sql中需要的值
     */
    protected Object[] sqlValues = {};

    /**
     * sql中的条件
     */
    protected List<Where> wheres = new ArrayList<>();

    /**
     * 构建完成的sql
     */
    protected String sql;

    /**
     * sql 中的基本字段
     */
    protected Set<String> sqlColumns;

    /**
     * 实体类与数据库的映射
     */
    protected EntityTableRowMapper entityTableRowMapper;

    /**
     * 设置目标
     *
     * @param entity
     * @return
     */
    @Override
    public SqlMaker target(Class entity) {
        Assert.notNull(entity);
        EntityTableRowMapper entityTableRowMapper = EntityMapperFactory.getMapper(entity);
        this.entityTableRowMapper = entityTableRowMapper;
        this.sqlColumns = entityTableRowMapper.getColumnNames();
        return this;
    }

    /**
     * 检测查询元素是是否存在于数据库表中
     *
     * @param columnName
     * @return
     */
    protected final void checkColumn(final String columnName) {
        Assert.notNull(entityTableRowMapper, "没有指定 entity。");
        Class tableClass = entityTableRowMapper.getTableClass();
        String name = getColumnName(columnName);
        if (name == null) {
            throw new UnsupportedOperationException(
                    "字段: >" + columnName + "< 不存在于 >" + tableClass.getSimpleName() + "< 表中。");
        }
    }

    /**
     * 获取sql
     * 只生成一次，之后取缓存起来的sql
     *
     * @return
     */
    @Override
    final public String toSql() {
        if (isSqlComplete()) {
            return sql;
        }
        this.sql = makeSql();
        this.sqlComplete = true;
        return sql;
    }

    /**
     * 获取sql中需要的value
     * 只生成一次，之后取缓存起来的sqlValues
     *
     * @return
     */
    @Override
    final public Object[] getSqlValues() {
        if (isSqlValueComplete()) {
            return sqlValues;
        }
        this.sqlValues = makeSqlValue().toArray();
        this.sqlValueComplete = true;
        return sqlValues;
    }

    @Override
    public boolean isSqlComplete() {
        return sqlComplete;
    }

    @Override
    public boolean isSqlValueComplete() {
        return sqlValueComplete;
    }

    @Override
    public Class<?> getEntity() {
        Assert.notNull(entityTableRowMapper, "没有指定 entity。");
        return entityTableRowMapper.getTableClass();
    }

    @Override
    public String getTableName() {
        Assert.notNull(entityTableRowMapper, "没有指定 entity。");
        return entityTableRowMapper.getTableName();
    }

    @Override
    public EntityTableRowMapper getEntityTableRowMapper() {
        return entityTableRowMapper;
    }

    @Override
    public SqlMaker where(Where... wheres) {
        for (Where where : wheres) {
            checkColumn(where.getColumn());
        }
        return where(Arrays.asList(wheres));
    }

    @Override
    final public SqlMaker where(List<Where> wheres) {
        List<Object> objects = new ArrayList<>(makeSqlValue());
        for (Where where : wheres) {
            this.wheres.add(where);
            //是否有值
            if (where.isHasValue()) {
                for (Object value : where.getValues()) {
                    objects.add(value);
                }
            }
        }
        this.sqlValues = objects.toArray();
        return this;
    }

    /**
     * 获取sql 中where 条件
     *
     * @return
     */
    final protected String sqlWhere() {
        StringBuilder sql = new StringBuilder();
        if (wheres.size() != 0) {
            sql.append("WHERE ");
            for (int i = 0; i < wheres.size(); i++) {
                Where where = wheres.get(i);
                if (i != 0) {
                    sql.append(where.getConnect());
                }
                String columnName = getColumnName(where.getColumn());
                String realSql = where.getSql().replace(Where.PLACEHOLDER, columnName);
                sql.append(realSql);
            }
        }
        return sql.toString();
    }

    /**
     * 获取数据库的字段名称.
     *
     * @param name
     * @return
     */
    final protected String getColumnName(final String name) {
        //columnNames 中包含name, 说明name是数据库中的column
        Set<String> columnNames = entityTableRowMapper.getColumnNames();
        if (columnNames.contains(name)) {
            return name;
        }
        Map<String, String> fieldNameColumnMapper = entityTableRowMapper.getFieldNameColumnMapper();
        //说明name是entity中的属性名称, 根据属性名称查找数据库中的column
        if (fieldNameColumnMapper.containsKey(name)) {
            return fieldNameColumnMapper.get(name);
        }
        return null;
    }

    /**
     * 需要子类实现的，创建sql的代码
     * 禁止直接调用
     *
     * @return
     */
    protected abstract String makeSql();

    /**
     * 需要子类实现的，创建sql中参数的代码
     * 禁止直接调用
     *
     * @return
     */
    protected abstract List<Object> makeSqlValue();

}
