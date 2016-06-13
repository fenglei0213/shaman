package org.shaman.dao;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.shaman.dao.annotation.FieldMeta;
import org.shaman.dao.vo.*;
import org.shaman.util.HumpUtil;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Created by fenglei on 2016/3/3.
 */
public class SQLBuilder {

    /**
     * buildInsertTableSQL buildInsertTableSQL
     *
     * @param obj
     * @return
     */
    public static <T> SQLInsertVo buildInsertTableSQL(T obj) {
        Class clazz = obj.getClass();
        SQLInsertVo sqlInsertVo = new SQLInsertVo();
        Map<Field, Object> sqlSetMap = sqlInsertVo.getSqlSetMap();
        //
        String tableName = SQLBuilder.getTableName(clazz);
        Field[] fields = clazz.getDeclaredFields();
        // build sql
        StringBuilder sqlBuilder = new StringBuilder();
        StringBuilder sqlColumnWholeBuilder = new StringBuilder(" ");
        StringBuilder sqlColumnBuilder = new StringBuilder();
        StringBuilder sqlQuestionWholeBuilder = new StringBuilder();
        StringBuilder sqlQuestionBuilder = new StringBuilder();
        boolean hasAnnotation = false;
        for (Field field : fields) {
            try {
                Assert.isTrue(field.isAnnotationPresent(FieldMeta.class), "Member Variable has not Annotation");
                FieldMeta fieldMeta = field.getAnnotation(FieldMeta.class);
                Assert.notNull(fieldMeta, "Member Variable has not FieldMeta Annotation");
                hasAnnotation = true;
                String fieldName = field.getName();
                //
                String getFieldName = "get" + SQLBuilder.captureName(fieldName);
                Method getMethod = ReflectionUtils.findMethod(clazz, getFieldName);
                Object getMethodValue = getMethod.invoke(obj);
                Assert.notNull(getMethodValue, "Member Variable is null,loop will continue");
                // if have value,set column in SQL
                String tableFieldName = HumpUtil.underscoreName(fieldName);
                sqlColumnBuilder.append(tableFieldName).append(",");
                sqlQuestionBuilder.append("?,");
                sqlSetMap.put(field, getMethodValue);
            } catch (Exception e) {
                continue;
            }
        }
        Assert.isTrue(hasAnnotation, "POJO has not FieldMeta Annotation");
        // build column sql
        String sqlColumnString = sqlColumnBuilder.toString();
        sqlColumnString = sqlColumnString.substring(0, sqlColumnString.lastIndexOf(","));
        // build question sql
        String sqlQuestionString = sqlQuestionBuilder.toString();
        sqlQuestionString = sqlQuestionString.substring(0, sqlQuestionString.lastIndexOf(","));
        // build parentheses sql
        sqlQuestionWholeBuilder.append("(").append(sqlQuestionString).append(")");
        sqlColumnWholeBuilder.append("(").append(sqlColumnString).append(")");
        // build whole sql
        sqlBuilder.append("INSERT INTO ").append(tableName)
                .append(sqlColumnWholeBuilder).append(" VALUES ").append(sqlQuestionWholeBuilder);
        sqlInsertVo.setSql(sqlBuilder.toString());
        sqlInsertVo.setSqlSetMap(sqlSetMap);
        return sqlInsertVo;
    }

    /**
     * buildInsertTableSQL buildInsertTableSQL
     *
     * @param objectList
     * @return
     */
    public static <T> SQLInsertBatchVo buildInsertBatchTableSQL(List<T> objectList) {
        SQLInsertBatchVo sqlInsertBatchVo = new SQLInsertBatchVo();
        List<Map<Field, Object>> sqlSetList = sqlInsertBatchVo.getSqlSetList();
        // Performance Optimization Here
        for (T object : objectList) {
            SQLInsertVo sqlInsertVo = SQLBuilder.buildInsertTableSQL(object);
            sqlSetList.add(sqlInsertVo.getSqlSetMap());
            sqlInsertBatchVo.setSql(sqlInsertVo.getSql());
        }
        return sqlInsertBatchVo;
    }

    /**
     * buildUpdateTableSql buildUpdateTableSql
     * <p>
     * support composite keys
     * should set mutiple key with @FieldMeta(id = true) annotation in POJO
     *
     * @param obj
     * @return
     */
    public static <T> SQLUpdateVo buildUpdateTableSQL(T obj) {
        Class clazz = obj.getClass();
        SQLUpdateVo sqlUpdateVo = new SQLUpdateVo();
        Map<Field, Object> sqlSetMap = sqlUpdateVo.getSqlSetMap();
        Map<Field, Object> sqlWhereMap = sqlUpdateVo.getSqlWhereMap();
        //
        String tableName = SQLBuilder.getTableName(clazz);
        Field[] fields = clazz.getDeclaredFields();
        // build sql
        StringBuilder sqlBuilder = new StringBuilder();
        StringBuilder sqlSetBuilder = new StringBuilder();
        StringBuilder sqlWhereBuilder = new StringBuilder(" WHERE ");
        boolean hasAnnotation = false;
        for (Field field : fields) {
            try {
                Assert.isTrue(field.isAnnotationPresent(FieldMeta.class), "Member Variable has not Annotation");
                FieldMeta fieldMeta = field.getAnnotation(FieldMeta.class);
                Assert.notNull(fieldMeta, "Member Variable has not FieldMeta Annotation");
                hasAnnotation = true;
                String fieldName = field.getName();
                String tableFieldName = HumpUtil.underscoreName(fieldName);
                String getFieldName = "get" + SQLBuilder.captureName(fieldName);
                Method getMethod = ReflectionUtils.findMethod(clazz, getFieldName);
                Object getMethodValue = getMethod.invoke(obj);
                Assert.notNull(getMethodValue, "Member Variable is null,loop will continue");
                if (fieldMeta.id()) {
                    // support composite keys
                    sqlWhereBuilder.append(tableFieldName).append("=? AND ");
                    sqlWhereMap.put(field, getMethodValue);
                } else {
                    sqlSetBuilder.append(tableFieldName).append("=?,");
                    sqlSetMap.put(field, getMethodValue);
                }
            } catch (Exception e) {
                continue;
            }
        }
        Assert.isTrue(hasAnnotation, "POJO has not FieldMeta Annotation");
        //
        String sqlSetString = sqlSetBuilder.toString();
        sqlSetString = sqlSetString.substring(0, sqlSetString.lastIndexOf(","));
        //
        String sqlWhereString = sqlWhereBuilder.toString();
        sqlWhereString = sqlWhereString.substring(0, sqlWhereString.lastIndexOf("AND"));
        //
        sqlBuilder.append("UPDATE ").append(tableName).append(" SET ").append(sqlSetString).append(sqlWhereString);
        sqlUpdateVo.setSql(sqlBuilder.toString());
        sqlUpdateVo.setSqlSetMap(sqlSetMap);
        sqlUpdateVo.setSqlWhereMap(sqlWhereMap);
        return sqlUpdateVo;
    }

    /**
     * buildSelectTableSQL buildSelectTableSQL
     *
     * @param queryVo
     * @return
     */
    public static SQLSelectVo buildSelectTableSQL(QueryVo queryVo) {
        // init SQLSelectVo
        SQLSelectVo sqlSelectVo = new SQLSelectVo();
        List<Object> argList = sqlSelectVo.getArgList();
        // get SQL meta data
        List<String> selectColumnList = queryVo.getSelectColumnList();
        Class tableClazz = queryVo.getTableClazz();
        String tableName = SQLBuilder.getTableName(tableClazz);
        Map<String, Object> whereColumnMap = queryVo.getWhereColumnMap();
        Map<String, List<Object>> whereColumnInMap = queryVo.getWhereColumnInMap();
        Map<String, Integer> distinctMap = queryVo.getDistinctColumnMap();
        // build SQL SELECT
        StringBuilder sqlBuilder = new StringBuilder("SELECT ");
        StringBuilder sqlColumnBuilder = new StringBuilder();
        for (String columnItem : selectColumnList) {
            if (distinctMap.containsKey(columnItem)) {
                sqlColumnBuilder.append(" DISTINCT ");
            }
            sqlColumnBuilder.append(columnItem).append(",");
        }
        String sqlColumnString = sqlColumnBuilder.toString();
        sqlColumnString = sqlColumnString.substring(0, sqlColumnString.lastIndexOf(","));
        // build SQL WHERE
        String sqlWhereString = SQLBuilder.buildSQLWhere(whereColumnMap, whereColumnInMap, argList);
        // join SQL SELECT
        sqlBuilder.append(sqlColumnString);
        // join SQL FROM
        sqlBuilder.append(" FROM ").append(tableName);
        // join SQL WHERE
        sqlBuilder.append(sqlWhereString);
        // SQLSelectVo set value
        sqlSelectVo.setSql(sqlBuilder.toString());
        sqlSelectVo.setTableClazz(tableClazz);
        sqlSelectVo.setArgList(argList);
        return sqlSelectVo;
    }

    /**
     * buildSelectCountTableSQL buildSelectCountTableSQL
     *
     * @param queryCountVo
     * @return
     */
    public static SQLSelectVo buildSelectCountTableSQL(QueryCountVo queryCountVo) {
        // init SQLSelectVo
        SQLSelectVo sqlSelectVo = new SQLSelectVo();
        List<Object> argList = sqlSelectVo.getArgList();
        // get SQL meta data
        Class tableClazz = queryCountVo.getTableClazz();
        String countColumName = queryCountVo.getCountColumnName();
        String tableName = SQLBuilder.getTableName(tableClazz);
        Map<String, Object> whereColumnMap = queryCountVo.getWhereColumnMap();
        Map<String, List<Object>> whereColumnInMap = Maps.newHashMap();
        // build SQL SELECT
        StringBuilder sqlBuilder = new StringBuilder("SELECT COUNT(")
                .append(countColumName).append(")");
        // build SQL WHERE
        String sqlWhereString = SQLBuilder.buildSQLWhere(whereColumnMap, whereColumnInMap, argList);
        // join SQL FROM
        sqlBuilder.append(" FROM ").append(tableName);
        // join SQL WHERE
        sqlBuilder.append(sqlWhereString);
        // SQLSelectVo set value
        sqlSelectVo.setSql(sqlBuilder.toString());
        sqlSelectVo.setTableClazz(tableClazz);
        sqlSelectVo.setArgList(argList);
        return sqlSelectVo;
    }

    /**
     * buildSQLWhere buildSQLWhere
     *
     * @param whereColumnMap
     * @param whereColumnInMap
     * @param argList
     * @return
     */
    public static String buildSQLWhere(Map<String, Object> whereColumnMap, Map<String, List<Object>> whereColumnInMap, List<Object> argList) {
        List<String> whereConditionAllList = Lists.newArrayList();
        StringBuffer sqlWherePrefixBuilder = new StringBuffer(" WHERE ");
        // Common WHERE Condition
        StringBuilder sqlWhereBuilder = new StringBuilder();
        List<String> sqlWhereList = Lists.newArrayList();
        for (Map.Entry<String, Object> mapItem : whereColumnMap.entrySet()) {
            String whereColumn = mapItem.getKey();
            StringBuilder sqlWhereItemBuilder = new StringBuilder(whereColumn).append("=?");
            sqlWhereList.add(sqlWhereItemBuilder.toString());
            // add argList
            Object whereValue = mapItem.getValue();
            argList.add(whereValue);
        }
        if (!CollectionUtils.isEmpty(sqlWhereList)) {
            String sqlWhereString =
                    sqlWhereBuilder.append(StringUtils.join(sqlWhereList, " AND ")).toString();
            whereConditionAllList.add(sqlWhereString);
        }
        // IN WHERE Condition
        // bug Only once
        StringBuilder sqlWhereInItemBuilder = new StringBuilder();
        for (Map.Entry<String, List<Object>> mapItem : whereColumnInMap.entrySet()) {
            String whereColumnIn = mapItem.getKey();
            List<Object> NumberList = mapItem.getValue();
            sqlWhereInItemBuilder.append(whereColumnIn)
                    .append(" IN ").append("(");
            List<String> sqlWhereInItemList = Lists.newArrayList();
            for (Object obj : NumberList) {
                StringBuffer objBuf = new StringBuffer();
                if (obj instanceof String) {
                    objBuf = objBuf.append("\"")
                            .append(obj.toString())
                            .append("\"");
                } else {
                    objBuf = objBuf.append(obj.toString());
                }
                StringBuilder sqlWhereInItemItemBuilder = new StringBuilder(objBuf);
                sqlWhereInItemList.add(sqlWhereInItemItemBuilder.toString());
            }
            sqlWhereInItemBuilder.append(StringUtils.join(sqlWhereInItemList, ","))
                    .append(")");
        }
        if (!StringUtils.isEmpty(sqlWhereInItemBuilder)) {
            whereConditionAllList.add(sqlWhereInItemBuilder.toString());
        }
        String whereSql = sqlWherePrefixBuilder
                .append(StringUtils.join(whereConditionAllList, " AND "))
                .toString();
        System.out.println("sql: " + whereSql);
        return whereSql;
    }


    /**
     * buildDeleteTableSQL buildDeleteTableSQL
     * delete 没有设计 SQLDeleteVo,因为IN语句无法使用占位符
     *
     * @param deleteVo
     * @return
     */
    public static String buildDeleteTableSQL(DeleteVo deleteVo) {
        // init SQLSelectVo
        // get SQL meta data
        Class tableClazz = deleteVo.getTableClazz();
        String tableName = SQLBuilder.getTableName(tableClazz);
        String primaryKeyName = deleteVo.getPrimaryKeyName();
        List<Long> idList = deleteVo.getIdList();
        // build SQL SELECT
        StringBuilder sqlBuilder = new StringBuilder("DELETE ");
        // build SQL WHERE
        StringBuilder sqlWhereBuilder = new StringBuilder(" WHERE ");
        sqlWhereBuilder.append(primaryKeyName).append(" IN (");
        // build SQL IN
        StringBuilder sqlInBuilder = new StringBuilder();
        for (Number number : idList) {
            sqlInBuilder.append(number).append(",");
        }
        String sqlInBuilderString = sqlInBuilder.toString();
        sqlInBuilderString = sqlInBuilderString.substring(0, sqlInBuilderString.lastIndexOf(","));
        sqlWhereBuilder.append(sqlInBuilderString).append(")");
        // join SQL FROM
        sqlBuilder.append(" FROM ").append(tableName);
        // join SQL WHERE
        sqlBuilder.append(sqlWhereBuilder);
        // SQLDeleteVo set value
        String deleteSQL = sqlBuilder.toString();
        return deleteSQL;
    }

    /**
     * getTableName getTableName
     *
     * @param clazz
     * @return
     */
    public static String getTableName(Class clazz) {
        String clazzName = clazz.getSimpleName();
        String tableName = HumpUtil.underscoreName(clazzName);
        return tableName;
    }

    /**
     * Initial Char Upcase
     *
     * @param name
     * @return
     */
    private static String captureName(String name) {
        char[] cs = name.toCharArray();
        cs[0] -= 32;
        return String.valueOf(cs);
    }
}
