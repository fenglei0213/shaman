package org.shaman.dao;

import org.shaman.util.HumpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by fenglei on 2016/3/1.
 */
public class ObjectRowMapper implements RowMapper {

    private static Logger logger = LoggerFactory.getLogger(ObjectRowMapper.class);

    private Class className;

    public ObjectRowMapper(Class className) {
        this.className = className;
    }

    /*
     * 该方法自动将数据库字段对应到Object中相应字段
     * 要求：数据库与Object中字段名相同
     * 优点是:
     * 1 可以挑选字段,有注解的字段是否传递,取决于SELECT的返回值是否为空
     * 2 既适用于单张表的场景,也适用于多张表连接的情况,自由JOIN
     *
     */
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        Object nt = new Object();
        Field[] fields = className.getDeclaredFields();
        try {
            nt = className.newInstance();
            for (Field field : fields) {
                String underscoreName = "";
                //如果结果中没有改field项则跳过
                try {
                    if (!field.isAnnotationPresent(FieldMeta.class)) {
                        continue;
                    }
                    FieldMeta fieldMeta = field.getAnnotation(FieldMeta.class);
                    if (fieldMeta != null) {
                        String fieldName = field.getName();
                        underscoreName = HumpUtil.underscoreName(fieldName).toLowerCase();
                        rs.findColumn(underscoreName);
                    }
                } catch (Exception e) {
                    continue;
                }
                //修改相应filed的权限
                boolean accessFlag = field.isAccessible();
                field.setAccessible(true);
                String value = rs.getString(underscoreName);
                value = value == null ? "" : value;
                ObjectRowMapper.setFieldValue(nt, field, value);

                //恢复相应field的权限
                field.setAccessible(accessFlag);
            }
        } catch (Exception e) {
            logger.error("mapRow Exception", e);
        }
        return nt;
    }

    /*
     * 根据类型对具体对象属性赋值
     */

    private static void setFieldValue(Object form, Field field, String value) {

        String elemType = field.getType().toString();

        if (elemType.indexOf("boolean") != -1 || elemType.indexOf("Boolean") != -1) {
            try {
                field.set(form, Boolean.valueOf(value));
            } catch (IllegalAccessException e) {
                logger.error("setFieldValue Boolean Exception", e);
            }
        } else if (elemType.indexOf("byte") != -1 || elemType.indexOf("Byte") != -1) {
            try {
                field.set(form, Byte.valueOf(value));
            } catch (IllegalAccessException e) {
                logger.error("setFieldValue Byte Exception", e);
            }
        } else if (elemType.indexOf("char") != -1 || elemType.indexOf("Character") != -1) {
            try {
                field.set(form, Character.valueOf(value.charAt(0)));
            } catch (IllegalAccessException e) {
                logger.error("setFieldValue Character Exception", e);
            }
        } else if (elemType.indexOf("double") != -1 || elemType.indexOf("Double") != -1) {
            try {
                field.set(form, Double.valueOf(value));
            } catch (IllegalAccessException e) {
                logger.error("setFieldValue Double Exception", e);
            }
        } else if (elemType.indexOf("float") != -1 || elemType.indexOf("Float") != -1) {
            try {
                field.set(form, Float.valueOf(value));
            } catch (IllegalAccessException e) {
                logger.error("setFieldValue Float Exception", e);
            }
        } else if (elemType.indexOf("int") != -1 || elemType.indexOf("Integer") != -1) {
            try {
                field.set(form, Integer.valueOf(value));
            } catch (IllegalAccessException e) {
                logger.error("setFieldValue Integer Exception", e);
            }
        } else if (elemType.indexOf("long") != -1 || elemType.indexOf("Long") != -1) {
            try {
                field.set(form, Long.valueOf(value));
            } catch (IllegalAccessException e) {
                logger.error("setFieldValue Long Exception", e);
            }
        } else if (elemType.indexOf("short") != -1 || elemType.indexOf("Short") != -1) {
            try {
                field.set(form, Short.valueOf(value));
            } catch (IllegalAccessException e) {
                logger.error("setFieldValue Short Exception", e);
            }
        } else {
            try {
                field.set(form, (Object) value);
            } catch (IllegalAccessException e) {
                logger.error("setFieldValue field.set Exception", e);
            }
        }
    }
}
