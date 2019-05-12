package com.qinfengsa.spring.orm.framework.entity;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.RowMapper;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * @author: qinfengsa
 * @date: 2019/5/12 09:44
 */
@Slf4j
public class EntityOperation<T extends Serializable> {
    /**
     * 泛型实体类Class
     */
    public Class<T> entityClass = null;

    /**
     * 字段类型对应
     */
    public final Map<String, PropertyMapping> mappings;

    /**
     * jdbcTemplete结果封装
     */
    public final RowMapper<T> rowMapper;

    /**
     * 表名
     */
    public final String tableName;

    /**
     * 字段名
     */
    public String allColumn = "*";

    /**
     * 注解
     */
    public Field pkField;

    public EntityOperation(Class<T> clazz) throws Exception{
        if(!clazz.isAnnotationPresent(Entity.class)){
            throw new Exception("在" + clazz.getName() + "中没有找到Entity注解，不能做ORM映射");
        }
        this.entityClass = clazz;
        Table table = entityClass.getAnnotation(Table.class);
        if (table != null) {
            this.tableName = table.name();
        } else {
            this.tableName =  entityClass.getSimpleName();
        }
        Map<String, Method> getters = ClassMappings.findPublicGetters(entityClass);
        Map<String, Method> setters = ClassMappings.findPublicSetters(entityClass);
        Field[] fields = ClassMappings.findFields(entityClass);

        this.mappings = getPropertyMappings(getters, setters, fields);
        this.allColumn = this.mappings.keySet().toString().replace("[", "").replace("]","").replaceAll(" ","");
        this.rowMapper = createRowMapper();

        getPkField(fields,clazz.getName());


    }

    /**
     * 获取主键
     * @param fields
     * @param className
     * @throws Exception
     */
    private void getPkField(Field[] fields,String className) throws Exception{
        for (Field field : fields) {
            if (field.isAnnotationPresent(Id.class)) {
                this.pkField =  field;
            }

        }
        if (Objects.isNull(this.pkField)) {
            throw new Exception("在" + className + "中没有找到Id注解，无法映射主键");
        }
        this.pkField.setAccessible(true);
    }

    Map<String, PropertyMapping> getPropertyMappings(Map<String, Method> getters, Map<String, Method> setters, Field[] fields) {
        Map<String, PropertyMapping> mappings = new HashMap<String, PropertyMapping>(16);
        String name;
        for (Field field : fields) {
            if (field.isAnnotationPresent(Transient.class)) {
                continue;
            }

            name = field.getName();
            if(name.startsWith("is")){
                name = name.substring(2);
            }
            name = Character.toLowerCase(name.charAt(0)) + name.substring(1);
            Method setter = setters.get(name);
            Method getter = getters.get(name);
            if (setter == null || getter == null){
                continue;
            }
            Column column = field.getAnnotation(Column.class);
            if (column == null) {
                mappings.put(field.getName(), new PropertyMapping(getter, setter, field));
            } else {
                mappings.put(column.name(), new PropertyMapping(getter, setter, field));
            }
        }
        return mappings;
    }

    RowMapper<T> createRowMapper() {
        return new RowMapper<T>() {
            @Override
            public T mapRow(ResultSet rs, int rowNum) throws SQLException {
                try {
                    T t = entityClass.newInstance();
                    ResultSetMetaData meta = rs.getMetaData();
                    int columns = meta.getColumnCount();
                    String columnName;
                    for (int i = 1; i <= columns; i++) {
                        Object value = rs.getObject(i);
                        columnName = meta.getColumnName(i);
                        fillBeanFieldValue(t,columnName,value);
                    }
                    return t;
                }catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    protected void fillBeanFieldValue(T t, String columnName, Object value) {
        if (value != null) {
            PropertyMapping pm = mappings.get(columnName);
            if (pm != null) {
                try {
                    pm.set(t, value);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Map<String, Object> parse(T t) {
        // 修改为有序Map(先入先出)
        Map<String, Object> columnMap = new LinkedHashMap<String, Object>();
        try {

            for (String columnName : mappings.keySet()) {
                Object value = mappings.get(columnName).getter.invoke(t);
                if (value == null) {
                    continue;
                }
                columnMap.put(columnName, value);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return columnMap;
    }

}

@Slf4j
class PropertyMapping {

    final boolean insertable;
    final boolean updatable;
    final String columnName;
    final boolean id;
    final Method getter;
    final Method setter;
    final Class enumClass;
    final String fieldName;

    public PropertyMapping(Method getter, Method setter, Field field) {
        this.getter = getter;
        this.setter = setter;
        this.enumClass = getter.getReturnType().isEnum() ? getter.getReturnType() : null;
        Column column = field.getAnnotation(Column.class);
        this.insertable = column == null || column.insertable();
        this.updatable = column == null || column.updatable();
        this.columnName = column == null ? ClassMappings.getGetterName(getter) : ("".equals(column.name()) ? ClassMappings.getGetterName(getter) : column.name());
        this.id = field.isAnnotationPresent(Id.class);
        this.fieldName = field.getName();
    }

    @SuppressWarnings("unchecked")
    Object get(Object target) throws Exception {
        Object r = getter.invoke(target);
        return enumClass == null ? r : Enum.valueOf(enumClass, (String) r);
    }

    @SuppressWarnings("unchecked")
    void set(Object target, Object value) throws Exception {
        if (enumClass != null && value != null) {
            value = Enum.valueOf(enumClass, (String) value);
        }
        try {
            if(value != null){
                setter.invoke(target, setter.getParameterTypes()[0].cast(value));
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 出错原因如果是boolean字段 mysql字段类型 设置tinyint(1)
            log.error(fieldName + "--" + value);
        }

    }
}
