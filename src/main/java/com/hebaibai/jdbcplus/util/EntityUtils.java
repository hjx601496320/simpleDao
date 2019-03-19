package com.hebaibai.jdbcplus.util;

import com.hebaibai.jdbcplus.FK;
import lombok.experimental.UtilityClass;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.hebaibai.jdbcplus.Column;
import com.hebaibai.jdbcplus.Id;
import com.hebaibai.jdbcplus.Table;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 实体类的工具类
 *
 * @author hjx
 */
@UtilityClass
public class EntityUtils {

    static final String IS_NOT_TABLE = "Class 不是一个Table";
    static final String IS_NOT_COLUMN = "field 不是一个Column";
    static final String IS_NOT_JOINCOLUMN = "field 不是一个JoinColumn";
    static final String JOINCOLUMN_NAME_NOT_BLANK = "FK.name() 不能为空";
    static final String CLASS_NOT_NULL = "class 不能为 null";
    static final String FIELD_NOT_NULL = "field 不能为 null";
    static final String ANNOTATIONCLASS_NOT_NULL = "annotationClass 不能为 null";

    /**
     * 找到clz中包含ID注解的属性
     *
     * @param clz
     * @return
     */
    public static Field idField(Class<?> clz) {
        Assert.isTrue(isTable(clz), IS_NOT_TABLE);
        for (Field field : clz.getDeclaredFields()) {
            //同时是Id 和 Column
            if (isColumn(field) && isId(field)) {
                return field;
            }
        }
        return null;
    }

    /**
     * 找到id的数据库字段名称
     *
     * @param clz
     * @return
     */
    public static String idColumnName(Class<?> clz) {
        Assert.isTrue(isTable(clz), IS_NOT_TABLE);
        Field idField = idField(clz);
        //entity中不存在@ID注解时，忽略。
        if (idField == null) {
            return null;
        }
        return columnName(idField);
    }

    /**
     * 获取Table的列名与Entity属性的映射Map
     *
     * @param clz
     * @param <T>
     * @return
     */
    public static <T> Map<String, Field> columnFieldMap(Class<T> clz) {
        Field[] declaredFields = clz.getDeclaredFields();
        Map<String, Field> map = new HashMap<>(declaredFields.length);
        for (Field field : declaredFields) {
            if (isColumn(field)) {
                map.put(columnName(field), field);
            }
        }
        return map;
    }

    /**
     * 获取添加了JoinColumn注解的属性的相关联的对象的属性
     *
     * @param field
     * @return
     */
    public static Field getEntityFkTargetField(Field field) {
        FK FK = getAnnotation(field, FK.class);
        Assert.notNull(FK, IS_NOT_JOINCOLUMN);
        String targetColumnName = FK.value();
        Assert.isTrue(!StringUtils.isEmpty(targetColumnName), JOINCOLUMN_NAME_NOT_BLANK);
        //关联的Entity
        Class<?> fieldType = field.getType();
        Assert.isTrue(isTable(fieldType), IS_NOT_TABLE);
        Field[] fields = fieldType.getDeclaredFields();
        for (Field f : fields) {
            if (isColumn(field) && columnName(f).equals(targetColumnName)) {
                return f;
            }
        }
        return null;
    }

    /**
     * 获取类上的注解
     *
     * @param clz
     * @param annotationClass
     * @param <T>
     * @return
     */
    public static <T extends Annotation> T getAnnotation(Class<?> clz, Class<T> annotationClass) {
        Assert.notNull(clz, CLASS_NOT_NULL);
        Assert.notNull(annotationClass, ANNOTATIONCLASS_NOT_NULL);
        return clz.getAnnotation(annotationClass);
    }

    /**
     * 获取属性上的注解
     *
     * @param field
     * @param annotationClass
     * @param <T>
     * @return
     */
    public static <T extends Annotation> T getAnnotation(Field field, Class<T> annotationClass) {
        Assert.notNull(field, FIELD_NOT_NULL);
        Assert.notNull(annotationClass, ANNOTATIONCLASS_NOT_NULL);
        return field.getAnnotation(annotationClass);
    }

    /**
     * 找到clz上Table注解中的值
     *
     * @param clz
     * @return
     */
    public static String tableName(Class<?> clz) {
        Assert.isTrue(isTable(clz), IS_NOT_TABLE);
        return getAnnotation(clz, Table.class).value();
    }

    /**
     * 获取列的名称
     *
     * @param field
     * @return
     */
    static String columnName(Field field) {
        Assert.isTrue(isColumn(field), IS_NOT_COLUMN);
        return getAnnotation(field, Column.class).value();
    }

    /**
     * 是否是一个Table
     *
     * @param aClass
     * @return
     */
    public static boolean isTable(Class aClass) {
        if (hasAnnotation(aClass, Table.class)) {
            return true;
        }
        return false;
    }

    /**
     * 是否是一个列
     *
     * @param field
     * @return
     */
    public static boolean isColumn(Field field) {
        if (hasAnnotation(field, Column.class)) {
            return true;
        }
        return false;
    }

    /**
     * 是否是一个外键
     *
     * @param field
     * @return
     */
    public static boolean isJoinColumn(Field field) {
        if (hasAnnotation(field, FK.class)) {
            return true;
        }
        return false;
    }

    /**
     * 是否是一个id
     *
     * @param field
     * @return
     */
    static boolean isId(Field field) {
        if (hasAnnotation(field, Id.class)) {
            return true;
        }
        return false;
    }

    static boolean hasAnnotation(Class<?> clz, Class<? extends Annotation> annotationClass) {
        Assert.notNull(clz, CLASS_NOT_NULL);
        Assert.notNull(annotationClass, ANNOTATIONCLASS_NOT_NULL);
        return clz.isAnnotationPresent(annotationClass);
    }

    static boolean hasAnnotation(Field field, Class<? extends Annotation> annotationClass) {
        Assert.notNull(field, FIELD_NOT_NULL);
        Assert.notNull(annotationClass, ANNOTATIONCLASS_NOT_NULL);
        return field.isAnnotationPresent(annotationClass);
    }

}
