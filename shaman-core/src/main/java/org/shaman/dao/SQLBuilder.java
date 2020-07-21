package org.shaman.dao;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.shaman.dao.annotation.FieldMeta;
import org.shaman.dao.vo.*;
import org.shaman.util.HumpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by fenglei on 2016/3/3.
 */
public class SQLBuilder {

    private static Logger logger = LoggerFactory.getLogger(SQLBuilder.class);

    /**
     * buildInsertTableSQL buildInsertTableSQL
     *
     * @param obj
     * @param tableNameCus
     * @return
     */
    public static <T> SQLInsertVo buildInsertTableSQL(T obj, String tableNameCus) {
        Class clazz = obj.getClass();
        SQLInsertVo sqlInsertVo = new SQLInsertVo();
        Map<Field, Object> sqlSetMap = sqlInsertVo.getSqlSetMap();
        //
        String tableName = SQLBuilder.getTableName(clazz);
        if (!StringUtils.isEmpty(tableNameCus)) {
            tableName = tableNameCus;
        }
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
                String tableFieldName = HumpUtils.underscoreName(fieldName);
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
     * @param tableName
     * @return
     */
    public static <T> SQLBatchVo buildInsertBatchTableSQL(List<T> objectList, String tableName) {
        SQLBatchVo sqlBatchVo = new SQLBatchVo();
        List<Map<Field, Object>> sqlSetList = sqlBatchVo.getSqlSetList();
        // Performance Optimization Here
        for (T object : objectList) {
            SQLInsertVo sqlInsertVo = SQLBuilder.buildInsertTableSQL(object, tableName);
            sqlSetList.add(sqlInsertVo.getSqlSetMap());
            sqlBatchVo.setSql(sqlInsertVo.getSql());
        }
        return sqlBatchVo;
    }

    /**
     * buildUpdateBatchTableSQL buildUpdateBatchTableSQL
     *
     * @param objectList
     * @param sqlWhereCusSet
     * @return
     */
    public static <T> SQLBatchVo buildUpdateBatchTableSQL(List<T> objectList,
                                                          Set<String> sqlWhereCusSet,
                                                          String tableName) {
        SQLBatchVo sqlBatchVo = new SQLBatchVo();
        List<Map<Field, Object>> sqlSetList = sqlBatchVo.getSqlSetList();
        // Performance Optimization Here
        for (T object : objectList) {
            SQLUpdateVo sqlUpdateVo = SQLBuilder.buildUpdateBatchTableSQL(object, sqlWhereCusSet, tableName);
            Map<Field, Object> sqlSetMap = sqlUpdateVo.getSqlSetMap();
            sqlSetList.add(sqlSetMap);
            // repeat
            sqlBatchVo.setSql(sqlUpdateVo.getSql());
        }
        return sqlBatchVo;
    }

    /**
     * buildUpdateBatchTableSQL buildUpdateBatchTableSQL
     * <p>
     * support composite keys
     * should set mutiple key with @FieldMeta(id = true) annotation in POJO
     *
     * @param obj
     * @param sqlWhereCusSet
     * @param tableNameCus
     * @return
     */
    public static <T> SQLUpdateVo buildUpdateBatchTableSQL(T obj, Set<String> sqlWhereCusSet, String tableNameCus) {
        Class clazz = obj.getClass();
        SQLUpdateVo sqlUpdateVo = new SQLUpdateVo();
        Map<Field, Object> sqlSetMap = sqlUpdateVo.getSqlSetMap();
        //
        String tableName = SQLBuilder.getTableName(clazz);
        if (!StringUtils.isEmpty(tableNameCus)) {
            tableName = tableNameCus;
        }
        Field[] fields = clazz.getDeclaredFields();
        // build sql
        StringBuilder sqlBuilder = new StringBuilder();
        StringBuilder sqlSetBuilder = new StringBuilder();
        StringBuilder sqlWhereBuilder = new StringBuilder(" WHERE ");
        List sqlWhereParamList = Lists.newArrayList();
        boolean hasAnnotation = false;
        Map<Field, Object> whereFieldValueMap = Maps.newLinkedHashMap();
        boolean idCondition = false;
        Pair<Field, Object> idValuePair = null;
        for (Field field : fields) {
            try {
                Assert.isTrue(field.isAnnotationPresent(FieldMeta.class), "Member Variable has not Annotation");
                FieldMeta fieldMeta = field.getAnnotation(FieldMeta.class);
                Assert.notNull(fieldMeta, "Member Variable has not FieldMeta Annotation");
                hasAnnotation = true;
                String fieldName = field.getName();
                String tableFieldName = HumpUtils.underscoreName(fieldName);
                String getFieldName = "get" + SQLBuilder.captureName(fieldName);
                Method getMethod = ReflectionUtils.findMethod(clazz, getFieldName);
                Object getMethodValue = getMethod.invoke(obj);
                Assert.notNull(getMethodValue, "Member Variable is null,loop will continue");
                if (!CollectionUtils.isEmpty(sqlWhereCusSet) && sqlWhereCusSet.contains(HumpUtils.underscoreName(fieldName))) {
                    // where not primary key
                    whereFieldValueMap.put(field, getMethodValue);

                }
                if (fieldMeta.id() && CollectionUtils.isEmpty(sqlWhereCusSet)) {
                    // where primary key
                    // first appear,but mast be set at last
                    sqlWhereBuilder.append(tableFieldName).append("=? ");
                    idCondition = true;
                    idValuePair = new ImmutablePair<Field, Object>(field, getMethodValue);
                } else if ((!CollectionUtils.isEmpty(sqlWhereCusSet) &&
                        !sqlWhereCusSet.contains(HumpUtils.underscoreName(fieldName)))
                        || CollectionUtils.isEmpty(sqlWhereCusSet)) {
                    sqlSetBuilder.append(tableFieldName).append("=?,");
                    sqlSetMap.put(field, getMethodValue);
                }
            } catch (Exception e) {
                continue;
            }
        }
        // gen Where SQL
        for (Map.Entry<Field, Object> item : whereFieldValueMap.entrySet()) {
            Field field = item.getKey();
            String fieldName = field.getName();
            Object getMethodValue = item.getValue();
            sqlSetMap.put(field, getMethodValue);
            sqlWhereParamList.add(HumpUtils.underscoreName(fieldName).concat("=?"));
        }
        // id condition valuePair,must be set at last
        if (idCondition) {
            sqlSetMap.put(idValuePair.getLeft(), idValuePair.getRight());
        }
        Assert.isTrue(hasAnnotation, "POJO has not FieldMeta Annotation");
        //
        String sqlSetString = sqlSetBuilder.toString();
//        if (!StringUtils.isEmpty(sqlSetString)) {
        sqlSetString = sqlSetString.substring(0, sqlSetString.lastIndexOf(","));
//        }
        //
        sqlWhereBuilder.append(StringUtils.join(sqlWhereParamList, " AND "));
        //
        sqlBuilder.append("UPDATE ").append(tableName).append(" SET ").append(sqlSetString).append(sqlWhereBuilder);
        sqlUpdateVo.setSql(sqlBuilder.toString());
        sqlUpdateVo.setSqlSetMap(sqlSetMap);
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
        Map<String, String> whereColumnLikeMap = queryVo.getWhereColumnLikeMap();
        Map<String, List<Object>> whereColumnUnEqualInMap = queryVo.getWhereColumnUnEqualInMap();
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
        if (!CollectionUtils.isEmpty(whereColumnMap) ||
                !CollectionUtils.isEmpty(whereColumnInMap) ||
                !CollectionUtils.isEmpty(whereColumnLikeMap) ||
                !CollectionUtils.isEmpty(whereColumnUnEqualInMap)) {
            logger.debug("buildSQLWhere Come in");
            // build SQL WHERE
            String sqlWhereString = SQLBuilder.buildSQLWhere(whereColumnMap,
                    whereColumnInMap,
                    whereColumnLikeMap,
                    whereColumnUnEqualInMap,
                    argList, limitPair);
            sqlBuilder.append(sqlWhereString);
        }
        // SQLSelectVo set value
        sqlSelectVo.setSql(sqlBuilder.toString());
        sqlSelectVo.setTableClazz(tableClazz);
        sqlSelectVo.setArgList(argList);
        logger.debug("SQL:{}", sqlSelectVo.getSql());
        return sqlSelectVo;
    }

    /**
     * buildSQLWhere buildSQLWhere
     *
     * @param whereColumnMap
     * @param whereColumnInMap
     * @param whereColumnLikeMap
     * @param whereColumnUnEqualInMap
     * @param argList
     * @param limitPair
     * @return
     */
    public static String buildSQLWhere(Map<String, Object> whereColumnMap,
                                       Map<String, List<Object>> whereColumnInMap,
                                       Map<String, String> whereColumnLikeMap,
                                       Map<String, List<Object>> whereColumnUnEqualInMap,
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
        // bug Only once IN Condition
        StringBuilder sqlWhereInItemBuilder = new StringBuilder();
        for (Map.Entry<String, List<Object>> mapItem : whereColumnInMap.entrySet()) {
            String whereColumnIn = mapItem.getKey();
            List<Object> numberList = mapItem.getValue();
            sqlWhereInItemBuilder.append(whereColumnIn)
                    .append(" IN ").append("(");
            List<String> sqlWhereInItemList = Lists.newArrayList();
            SQLBuilder.buildSelectWhereInValueList(numberList, sqlWhereInItemList);
            sqlWhereInItemBuilder.append(StringUtils.join(sqlWhereInItemList, ","))
                    .append(")");
        }
        if (!StringUtils.isEmpty(sqlWhereInItemBuilder)) {
            whereConditionAllList.add(sqlWhereInItemBuilder.toString());
        }
        // Like Condition
        StringBuilder sqlWhereLikeItemBuilder = new StringBuilder();
        for (Map.Entry<String, String> mapItem : whereColumnLikeMap.entrySet()) {
            String likeKey = mapItem.getKey();
            String likeValue = mapItem.getValue();
            sqlWhereLikeItemBuilder.append(likeKey)
                    .append(" LIKE ").append("'%").append(likeValue).append("%' AND ");
        }
        // 去掉最后的 AND
        sqlWhereLikeItemBuilder.setLength(sqlWhereLikeItemBuilder.length() - 5);
        if (!StringUtils.isEmpty(sqlWhereLikeItemBuilder)) {
            whereConditionAllList.add(sqlWhereLikeItemBuilder.toString());
        }
        // unEqual In Condition
        // IN WHERE Condition
        // bug Only once IN Condition
        StringBuilder sqlWhereUnEqualInItemBuilder = new StringBuilder();
        for (Map.Entry<String, List<Object>> mapItem : whereColumnUnEqualInMap.entrySet()) {
            String whereColumnUnEqualIn = mapItem.getKey();
            List<Object> numberList = mapItem.getValue();
            sqlWhereUnEqualInItemBuilder.append(whereColumnUnEqualIn)
                    .append(" NOT IN ").append("(");
            List<String> sqlWhereUnEqualInItemList = Lists.newArrayList();
            SQLBuilder.buildSelectWhereInValueList(numberList, sqlWhereUnEqualInItemList);
            sqlWhereUnEqualInItemBuilder.append(StringUtils.join(sqlWhereUnEqualInItemList, ","))
                    .append(")");
        }
        if (!StringUtils.isEmpty(sqlWhereUnEqualInItemBuilder)) {
            whereConditionAllList.add(sqlWhereUnEqualInItemBuilder.toString());
        }
        //
        StringBuilder whereSql = sqlWherePrefixBuilder
                .append(StringUtils.join(whereConditionAllList, " AND "));
        if (limitPair != null) {
            whereSql.append(" LIMIT ").append(limitPair.getLeft())
                    .append(",").append(limitPair.getRight());
        }
        return whereSql.toString();
    }

    /**
     * buildSelectWhereInValueList buildSelectWhereInValueList
     *
     * @param numberList
     * @param sqlWhereUnEqualInItemList
     * @return
     */
    public static void buildSelectWhereInValueList(List<Object> numberList, List<String> sqlWhereUnEqualInItemList) {
        for (Object obj : numberList) {
            StringBuffer objBuf = new StringBuffer();
            if (obj instanceof String) {
                objBuf = objBuf.append("\"")
                        .append(obj.toString())
                        .append("\"");
            } else {
                objBuf = objBuf.append(obj.toString());
            }
            StringBuilder sqlWhereUnEqualInItemItemBuilder = new StringBuilder(objBuf);
            sqlWhereUnEqualInItemList.add(sqlWhereUnEqualInItemItemBuilder.toString());
        }
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
        String tableName = HumpUtils.underscoreName(clazzName);
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

    /**
     * buildDropTableSQL buildDropTableSQL
     *
     * @param tableName
     * @return
     */
    public static String buildDropTableSQL(String tableName) {
        String dropTableSQL = "DROP TABLE ".concat(tableName);
        return dropTableSQL;
    }

    /**
     * buildTruncateTableSQL buildTruncateTableSQL
     *
     * @param tableName
     * @return
     */
    public static String buildTruncateTableSQL(String tableName) {
        String truncateTableSQL = "TRUNCATE TABLE ".concat(tableName);
        return truncateTableSQL;
    }

    /**
     * buildReplaceBatchTableSQL buildReplaceBatchTableSQL
     *
     * @param objectList
     * @param tableName
     * @param <T>
     * @return
     */
    public static <T> SQLBatchVo buildReplaceBatchTableSQL(List<T> objectList, String tableName) {
        SQLBatchVo sqlBatchVo = new SQLBatchVo();
        List<Map<Field, Object>> sqlSetList = sqlBatchVo.getSqlSetList();
        // Performance Optimization Here
        for (T object : objectList) {
            SQLInsertVo sqlInsertVo = SQLBuilder.buildReplaceBatchTableSQL(object, tableName);
            sqlSetList.add(sqlInsertVo.getSqlSetMap());
            sqlBatchVo.setSql(sqlInsertVo.getSql());
        }
        return sqlBatchVo;
    }

    /**
     * buildUpdateBatchTableSQL buildUpdateBatchTableSQL
     * <p>
     * support composite keys
     * should set mutiple key with @FieldMeta(id = true) annotation in POJO
     *
     * @param obj
     * @param tableNameCus
     * @return
     */
    public static <T> SQLInsertVo buildReplaceBatchTableSQL(T obj, String tableNameCus) {
        Class clazz = obj.getClass();
        SQLInsertVo sqlInsertVo = new SQLInsertVo();
        Map<Field, Object> sqlSetMap = sqlInsertVo.getSqlSetMap();
        //
        String tableName = SQLBuilder.getTableName(clazz);
        if (!StringUtils.isEmpty(tableNameCus)) {
            tableName = tableNameCus;
        }
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
                String tableFieldName = HumpUtils.underscoreName(fieldName);
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
        sqlBuilder.append("REPLACE INTO ").append(tableName)
                .append(sqlColumnWholeBuilder).append(" VALUES ").append(sqlQuestionWholeBuilder);
        sqlInsertVo.setSql(sqlBuilder.toString());
        sqlInsertVo.setSqlSetMap(sqlSetMap);
        return sqlInsertVo;
    }
}
