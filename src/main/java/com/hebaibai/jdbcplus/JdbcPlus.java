package com.hebaibai.jdbcplus;

import com.hebaibai.jdbcplus.maker.Where;
import com.hebaibai.jdbcplus.maker.Wheres;
import com.hebaibai.jdbcplus.maker.delete.DefaultDelete;
import com.hebaibai.jdbcplus.maker.delete.Delete;
import com.hebaibai.jdbcplus.maker.insert.DefaultInsert;
import com.hebaibai.jdbcplus.maker.insert.Insert;
import com.hebaibai.jdbcplus.maker.query.DefaultQuery;
import com.hebaibai.jdbcplus.maker.query.Query;
import com.hebaibai.jdbcplus.maker.update.DefaultUpdate;
import com.hebaibai.jdbcplus.maker.update.Update;
import com.hebaibai.jdbcplus.mapper.FieldColumnRowMapper;
import com.hebaibai.jdbcplus.util.ClassUtils;
import com.hebaibai.jdbcplus.util.EntityUtils;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 简化对象的数据操作
 *
 * @author hjx
 */
@CommonsLog
public class JdbcPlus {

    private JdbcTemplate jdbcTemplate;


    /**
     * 查询所有
     *
     * @param entityClass
     * @param <T>
     * @return
     */
    final public <T> List<T> select(final Class<T> entityClass) {
        Query query = new DefaultQuery();
        query.target(entityClass);
        return selectBy(query);
    }

    /**
     * 根据id查找
     *
     * @param entityClass
     * @param id
     * @param <T>
     * @return
     */
    final public <T> T selectById(final Class<T> entityClass, final Object id) {
        EntityTableRowMapper mapper = EntityMapperFactory.getMapper(entityClass);
        return selectOneBy(entityClass, mapper.getIdName(), id);
    }

    /**
     * 根据某一字段查找数据
     *
     * @param entityClass
     * @param columnName
     * @param columnValue
     * @param <T>
     * @return
     */
    final public <T> List<T> selectBy(
            final Class<T> entityClass,
            final String columnName, final Object columnValue
    ) {
        Query query = new DefaultQuery();
        query.target(entityClass);
        query.where(Wheres.equal(columnName, columnValue));
        return selectBy(query);
    }

    /**
     * 根据某字段查找数据
     *
     * @param entityClass
     * @param columnName1
     * @param columnValue1
     * @param columnName2
     * @param columnValue2
     * @param <T>
     * @return
     */
    final public <T> List<T> selectBy(
            final Class<T> entityClass,
            final String columnName1, final Object columnValue1,
            final String columnName2, final Object columnValue2
    ) {
        Query query = new DefaultQuery();
        query.target(entityClass);
        query.where(
                Wheres.equal(columnName1, columnValue1),
                Wheres.equal(columnName2, columnValue2)
        );
        return selectBy(query);
    }

    /**
     * 根据某一字段查找一条数据
     *
     * @param entityClass
     * @param columnName
     * @param columnValue
     * @param <T>
     * @return
     */
    final public <T> T selectOneBy(
            final Class<T> entityClass,
            final String columnName, final Object columnValue
    ) {
        Query query = new DefaultQuery();
        query.target(entityClass);
        query.where(Wheres.equal(columnName, columnValue));
        return selectOneBy(query);
    }

    /**
     * 根据字段查找一条数据
     *
     * @param entityClass
     * @param columnName1
     * @param columnValue1
     * @param columnName2
     * @param columnValue2
     * @param <T>
     * @return
     */
    final public <T> T selectOneBy(
            final Class<T> entityClass,
            final String columnName1, final Object columnValue1,
            final String columnName2, final Object columnValue2
    ) {
        Query query = new DefaultQuery();
        query.target(entityClass);
        query.where(
                Wheres.equal(columnName1, columnValue1),
                Wheres.equal(columnName2, columnValue2)
        );
        return selectOneBy(query);
    }

    /**
     * 根据条件查询
     * 查询结果只能是1条
     *
     * @param query
     * @param <T>
     * @return
     */
    final public <T> T selectOneBy(final Query query) {
        List<T> list = selectBy(query);
        return DataAccessUtils.requiredSingleResult(list);
    }

    /**
     * 执行一条sql
     *
     * @param sql
     * @param sqlValues
     * @param objClass
     * @param <T>
     * @return
     */
    final public <T> List<T> selectBySql(final String sql, final Object[] sqlValues, final Class<T> objClass) {
        return jdbcTemplate.query(sql, sqlValues, new FieldColumnRowMapper(objClass));
    }

    /**
     * 执行一条sql
     *
     * @param sql
     * @param objClass
     * @param <T>
     * @return
     */
    final public <T> List<T> selectBySql(final String sql, final Class<T> objClass) {
        return jdbcTemplate.query(sql, new FieldColumnRowMapper(objClass));
    }

    /**
     * 限定字段查询（多个结果）
     *
     * @param entityClass 添加了@Table的Class
     * @param wheres      查询条件
     * @param columnNames 数据库中的字段个名称
     * @return
     */
    final public List<Map<String, Object>> selectColumnForList(final Class entityClass, final List<Where> wheres, final String... columnNames) {
        Query query = new DefaultQuery();
        query.target(entityClass);
        query.addQueryColumns(Arrays.asList(columnNames));
        query.where(wheres);
        return jdbcTemplate.queryForList(query.toSql(), query.getSqlValues());
    }

    /**
     * 限定字段查询（多个结果）
     *
     * @param entityClass 添加了@Table的Class
     * @param wheres      查询条件
     * @param columnNames 数据库中的字段个名称
     * @return
     */
    final public Map<String, Object> selectColumnForMap(final Class entityClass, final List<Where> wheres, final String... columnNames) {
        return DataAccessUtils.requiredSingleResult(selectColumnForList(entityClass, wheres, columnNames));
    }

    /**
     * 根据id删除数据
     *
     * @param entityClass
     * @param id
     * @return
     */
    final public Integer deleteById(final Class entityClass, final Object id) {
        EntityTableRowMapper mapper = EntityMapperFactory.getMapper(entityClass);
        return deleteBy(entityClass, mapper.getIdName(), id);
    }

    /**
     * 根据一个字段删除数据
     *
     * @param entityClass
     * @param columnName
     * @param columnValue
     * @return
     */
    final public Integer deleteBy(final Class entityClass, final String columnName, final Object columnValue) {
        Delete delete = new DefaultDelete();
        delete.target(entityClass);
        delete.where(Wheres.equal(columnName, columnValue));
        return deleteBy(delete);
    }

    /**
     * 根据字段删除数据
     *
     * @param entityClass
     * @param columnName1
     * @param columnValue1
     * @param columnName2
     * @param columnValue2
     * @return
     */
    final public Integer deleteBy(
            final Class entityClass,
            final String columnName1, final Object columnValue1,
            final String columnName2, final Object columnValue2
    ) {
        Delete delete = new DefaultDelete();
        delete.target(entityClass);
        delete.where(
                Wheres.equal(columnName1, columnValue1),
                Wheres.equal(columnName2, columnValue2)
        );
        return deleteBy(delete);
    }

    /**
     * 添加一条数据
     *
     * @param entityClass
     * @param entity
     * @param <T>
     * @return
     */
    final public <T> Integer insert(final Class<T> entityClass, final T entity) {
        Insert insert = new DefaultInsert();
        insert.target(entityClass);
        insert.insert(entity);
        return insertBy(insert);
    }

    /**
     * 批量插入
     *
     * @param entityClass
     * @param entities
     * @return
     */
    final public <T> Integer insertBatch(Class<T> entityClass, final List<T> entities) {
        Insert insert = new DefaultInsert();
        insert.target(entityClass);
        for (int i = 0; i < entities.size(); i++) {
            Object entity = entities.get(i);
            insert.insert(entity);
        }
        return insertBy(insert);
    }

    /**
     * 根据id更新数据
     *
     * @param entityClass
     * @param entity
     * @param <T>
     * @return
     */
    final public <T> Integer updateById(final Class<T> entityClass, final T entity) {
        return updateById(entityClass, entity, false);
    }

    /**
     * 根据id 更新数据
     *
     * @param entityClass
     * @param entity
     * @param ignoreNull  是否忽略null
     * @param <T>
     * @return
     */
    final public <T> Integer updateById(final Class<T> entityClass, final T entity, final boolean ignoreNull) {
        EntityTableRowMapper mapper = EntityMapperFactory.getMapper(entityClass);
        Field field = EntityUtils.idField(entityClass);
        Object id = ClassUtils.getValue(entity, field);
        Update update = new DefaultUpdate();
        update.target(entityClass);
        update.set(entity, ignoreNull);
        update.where(Wheres.equal(mapper.getIdName(), id));
        return updateBy(update);
    }


    /**
     * 添加一条数据
     *
     * @param insert
     * @return
     */
    final public Integer insertBy(final Insert insert) {
        String sql = insert.toSql();
        Object[] sqlValues = insert.getSqlValues();
        if (log.isDebugEnabled()) {
            log.debug(sql);
            log.debug(Arrays.toString(sqlValues));
        }
        return jdbcTemplate.update(sql, sqlValues);
    }

    /**
     * 根据条件删除数据
     *
     * @param delete
     * @return
     */
    final public Integer deleteBy(final Delete delete) {
        String sql = delete.toSql();
        Object[] sqlValues = delete.getSqlValues();
        if (log.isDebugEnabled()) {
            log.debug(sql);
            log.debug(Arrays.toString(sqlValues));
        }
        return jdbcTemplate.update(sql, sqlValues);
    }

    /**
     * 根据条件更新
     *
     * @param update
     * @return
     */
    final public Integer updateBy(final Update update) {
        String sql = update.toSql();
        Object[] sqlValues = update.getSqlValues();
        if (log.isDebugEnabled()) {
            log.debug(sql);
            log.debug(Arrays.toString(sqlValues));
        }
        return jdbcTemplate.update(sql, sqlValues);
    }

    /**
     * 根据条件查询
     *
     * @param query
     * @return
     */
    final public List selectBy(final Query query) {
        EntityTableRowMapper mapper = EntityMapperFactory.getMapper(query.getEntity());
        mapper.setJdbcPlus(this);
        String sql = query.toSql();
        Object[] sqlValues = query.getSqlValues();
        if (log.isDebugEnabled()) {
            log.debug(sql);
            log.debug(Arrays.toString(sqlValues));
        }
        return jdbcTemplate.query(sql, sqlValues, mapper);
    }


    public JdbcPlus() {
        EntityMapperFactory.setJdbcPlus(this);
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
