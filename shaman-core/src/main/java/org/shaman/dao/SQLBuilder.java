package org.shaman.dao;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.tuple.ImmutablePair;
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
    public static <T> SQLBatchVo buildInsertBatchTableSQL(List<T> objectList) {
        SQLBatchVo sqlBatchVo = new SQLBatchVo();
        List<Map<Field, Object>> sqlSetList = sqlBatchVo.getSqlSetList();
        // Performance Optimization Here
        for (T object : objectList) {
            SQLInsertVo sqlInsertVo = SQLBuilder.buildInsertTableSQL(object);
            sqlSetList.add(sqlInsertVo.getSqlSetMap());
            sqlBatchVo.setSql(sqlInsertVo.getSql());
        }
        return sqlBatchVo;
    }

    /**
     * buildUpdateBatchTableSQL buildUpdateBatchTableSQL
     *
     * @param objectList
     * @return
     */
    public static <T> SQLBatchVo buildUpdateBatchTableSQL(List<T> objectList) {
        SQLBatchVo sqlBatchVo = new SQLBatchVo();
        List<Map<Field, Object>> sqlSetList = sqlBatchVo.getSqlSetList();
        // Performance Optimization Here
        for (T object : objectList) {
            SQLUpdateVo sqlUpdateVo = SQLBuilder.buildUpdateTableSQL(object);
            sqlSetList.add(sqlUpdateVo.getSqlSetMap());
            sqlBatchVo.setSql(sqlUpdateVo.getSql());
        }
        return sqlBatchVo;
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
        String countColumName = queryVo.getCountColumnName();
        String countDistinctColumnName = queryVo.getCountDistinctColumnName();
        Class tableClazz = queryVo.getTableClazz();
        String tableName = SQLBuilder.getTableName(tableClazz);
        Map<String, Object> whereColumnMap = queryVo.getWhereColumnMap();
        Map<String, List<Object>> whereColumnInMap = queryVo.getWhereColumnInMap();
        Map<String, Integer> distinctMap = queryVo.getDistinctColumnMap();
        ImmutablePair<Integer, Integer> limitPair = queryVo.getLimitPair();
        // build SQL SELECT
        StringBuilder sqlBuilder = new StringBuilder("SELECT ");
        StringBuilder sqlColumnBuilder = new StringBuilder();
        for (String columnItem : selectColumnList) {
            if (distinctMap.containsKey(columnItem)) {
                sqlColumnBuilder.append(" DISTINCT ");
            }
            if (!StringUtils.isEmpty(countColumName)) {
                sqlColumnBuilder.append(" COUNT(")
                        .append(countColumName).append(") ");
            }
            if (!StringUtils.isEmpty(countDistinctColumnName)) {
                sqlColumnBuilder.append(" COUNT(DISTINCT ")
                        .append(countColumName).append(") ");
            }
            sqlColumnBuilder.append(columnItem).append(",");
        }
        String sqlColumnString = sqlColumnBuilder.toString();
        sqlColumnString = sqlColumnString.substring(0, sqlColumnString.lastIndexOf(","));

        // join SQL SELECT
        sqlBuilder.append(sqlColumnString);
        // join SQL FROM
        sqlBuilder.append(" FROM ").append(tableName);
        // join SQL WHERE
        if (!CollectionUtils.isEmpty(whereColumnMap)) {
            // build SQL WHERE
            String sqlWhereString = SQLBuilder.buildSQLWhere(whereColumnMap,
                    whereColumnInMap,
                    argList, limitPair);
            sqlBuilder.append(sqlWhereString);
        }
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
    public static String buildSQLWhere(Map<String, Object> whereColumnMap,
                                       Map<String, List<Object>> whereColumnInMap,
                                       List<Object> argList,
                                       ImmutablePair<Integer, Integer> limitPair) {
        List<String> whereConditionAllList = Lists.newArrayList();
        StringBuilder sqlWherePrefixBuilder = new StringBuilder(" WHERE ");
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
        StringBuilder whereSql = sqlWherePrefixBuilder
                .append(StringUtils.join(whereConditionAllList, " AND "));
        if (limitPair != null) {
            whereSql.append(" LIMIT ").append(limitPair.getLeft())
                    .append(",").append(limitPair.getRight());
        }
        return whereSql.toString();
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
        Map<String, Object> whereColumnMap = deleteVo.getWhereColumnMap();
        Map<String, List<Object>> whereColumnInMap = deleteVo.getWhereColumnInMap();
        // build SQL SELECT
        StringBuilder sqlBuilder = new StringBuilder("DELETE ");
        StringBuilder sqlWhereBuilder = new StringBuilder();
        // build SQL WHERE

        List<String> whereItemList = Lists.newArrayList();
        if (!StringUtils.isEmpty(primaryKeyName)) {
            StringBuilder sqlPrimaryKeyWhereBuilder = new StringBuilder();
            sqlPrimaryKeyWhereBuilder.append(primaryKeyName).append(" IN (");
            // build SQL primary id IN
            String primaryIdListString = StringUtils.join(idList, ",");
            sqlWhereBuilder.append(primaryIdListString).append(")");
            whereItemList.add(sqlPrimaryKeyWhereBuilder.toString());
        }
        if (!CollectionUtils.isEmpty(whereColumnMap)) {
            String sqlWhereColumnMapString = SQLBuilder.buildWhereSql(whereColumnMap);
            whereItemList.add(sqlWhereColumnMapString);
        }
        if (!CollectionUtils.isEmpty(whereColumnInMap)) {
            String sqlWhereColumnInMapString = SQLBuilder.buildWhereInSql(whereColumnInMap);
            whereItemList.add(sqlWhereColumnInMapString);
        }
        if (!StringUtils.isEmpty(primaryKeyName) || !CollectionUtils.isEmpty(whereColumnMap)) {
            sqlWhereBuilder.append(" WHERE ");
            String whereItemListString = StringUtils.join(whereItemList, " AND ");
            sqlWhereBuilder.append(whereItemListString);
        }
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

    /**
     * buildWhereSql buildWhereSql
     *
     * @param whereColumnMap
     */
    private static String buildWhereSql(Map<String, Object> whereColumnMap) {
        List<String> whereItemList = SQLBuilder.buildWhereItemList(whereColumnMap);
        String whereSql = StringUtils.join(whereItemList, " AND ");
        return whereSql;
    }

    /**
     * buildWhereInSql buildWhereInSql
     *
     * @param whereColumnInMap
     */
    private static String buildWhereInSql(Map<String, List<Object>> whereColumnInMap) {
        List<String> whereItemList = SQLBuilder.buildWhereInItemList(whereColumnInMap);
        String whereSql = StringUtils.join(whereItemList, " AND ");
        return whereSql;
    }

    /**
     * buildWhereInItemList buildWhereInItemList
     *
     * @param whereColumnInMap
     * @return
     */
    private static List<String> buildWhereInItemList(Map<String, List<Object>> whereColumnInMap) {
        List<String> whereItemList = Lists.newArrayList();
        for (Map.Entry<String, List<Object>> mapEntry : whereColumnInMap.entrySet()) {
            StringBuilder sqlItemBuilder = new StringBuilder();
            String key = mapEntry.getKey();
            List<Object> objectList = mapEntry.getValue();
            sqlItemBuilder.append(key).append(" IN (");

            List<String> objectStringList = Lists.newArrayList();
            for (Object obj : objectList) {
                StringBuilder objItemBuilder = new StringBuilder();
                if (obj instanceof String) {
                    objItemBuilder.append("\"")
                            .append(obj)
                            .append("\"");
                } else {
                    objItemBuilder.append(obj.toString());
                }
                objectStringList.add(objItemBuilder.toString());
            }
            String objectStringSQL = StringUtils.join(objectStringList, ",");
            sqlItemBuilder.append(objectStringSQL).append(")");
            whereItemList.add(sqlItemBuilder.toString());
        }
        return whereItemList;
    }

    /**
     * buildWhereItemList buildWhereItemList
     *
     * @param whereColumnMap
     * @return
     */
    private static List<String> buildWhereItemList(Map<String, Object> whereColumnMap) {
        List<String> whereItemList = Lists.newArrayList();
        for (Map.Entry<String, Object> mapEntry : whereColumnMap.entrySet()) {
            StringBuilder sqlItemBuilder = new StringBuilder();
            String key = mapEntry.getKey();
            Object value = mapEntry.getValue();
            sqlItemBuilder.append(key).append("=");
            if (value instanceof String) {
                sqlItemBuilder.append("\"")
                        .append(value)
                        .append("\"");
            } else {
                sqlItemBuilder.append(value.toString());
            }
            whereItemList.add(sqlItemBuilder.toString());
        }
        return whereItemList;
    }
}
