package org.shaman.dao;

import org.shaman.dao.setter.InsertPreparedStatementCreator;
import org.shaman.exception.ShamanArgsException;
import org.shaman.util.HumpUtils;
import org.springframework.dao.DataAccessException;
import org.shaman.dao.vo.*;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by fenglei on 2016/3/3.
 */
public class ShamanDao {

    private ShamanTemplate shamanTemplate;

    /**
     * execute execute
     *
     * @param sql
     */
    public void execute(String sql) {
        shamanTemplate.execute(sql);
    }

    /**
     * query query
     *
     * @param sql
     * @param args
     * @param clazz
     * @param <T>
     * @return
     * @throws DataAccessException
     */
    public <T> List<T> query(String sql, Object[] args, Class clazz) throws DataAccessException {
        return shamanTemplate.query(sql, args, clazz);
    }

    /**
     * queryForCountInt queryForCountInt
     *
     * @param queryVo
     * @return
     */
    public int queryForCountInt(QueryVo queryVo) {
        SQLSelectVo sqlSelectVo = SQLBuilder.buildSelectTableSQL(queryVo);
        return shamanTemplate.queryForInt(sqlSelectVo);
    }

    /**
     * queryForCountInt queryForCountInt
     *
     * @param sql
     * @param args
     * @return
     */
    public int queryForCountInt(String sql, Object[] args) {
        return shamanTemplate.queryForObject(sql, args, Integer.class);
    }

    /**
     * queryForCountLong queryForCountLong
     *
     * @param queryVo
     * @return
     */
    public long queryForCountLong(QueryVo queryVo) {
        SQLSelectVo sqlSelectVo = SQLBuilder.buildSelectTableSQL(queryVo);
        return shamanTemplate.queryForLong(sqlSelectVo);
    }

    /**
     * queryForCountLong queryForCountLong
     *
     * @param sql
     * @param args
     * @return
     */
    public long queryForCountLong(String sql, Object[] args) {
        return shamanTemplate.queryForObject(sql, args, Long.class);
    }

    /**
     * queryForTable queryForTable
     *
     * @param sql
     * @param args
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> List<T> queryList(String sql, Object[] args, Class clazz) {
        return shamanTemplate.query(sql, args, clazz);
    }

    /**
     * queryForTable queryForTable
     *
     * @param queryVo
     * @param <T>
     * @return
     */
    public <T> List<T> queryList(QueryVo<T> queryVo) {
//        Class tableClazz = ShamanReflectionUtils.getGenericClass(queryVo);
//        queryVo.setTableClazz(tableClazz);
        SQLSelectVo<T> sqlSelectVo = SQLBuilder.buildSelectTableSQL(queryVo);
        return shamanTemplate.queryListForTable(sqlSelectVo);
    }

    /**
     * @param queryVo
     * @param <T>
     * @return
     */
    public <T> T queryObject(QueryVo<T> queryVo) {
        SQLSelectVo<T> sqlSelectVo = SQLBuilder.buildSelectTableSQL(queryVo);
        return shamanTemplate.queryObjectForTable(sqlSelectVo);
    }

    /**
     * updateObject2Table updateObject2Table
     *
     * @param obj
     * @param <T>
     */
    public <T> void updateObjectForTable(T obj) {
        this.updateObjectForTable(obj, null, null);
    }

    /**
     * updateObject2Table updateObject2Table
     *
     * @param obj
     * @param sqlWhereCusSet
     * @param <T>
     */
    public <T> void updateObjectForTable(T obj, Set<String> sqlWhereCusSet) {
        this.updateObjectForTable(obj, sqlWhereCusSet, null);
    }

    /**
     * updateObject2Table updateObject2Table
     *
     * @param obj
     * @param sqlWhereCusSet
     * @param tableName
     * @param <T>
     */
    public <T> void updateObjectForTable(T obj, Set<String> sqlWhereCusSet, String tableName) {
        SQLUpdateVo sqlUpdateVo = SQLBuilder.buildUpdateBatchTableSQL(obj, sqlWhereCusSet, tableName);
        shamanTemplate.updateTable(sqlUpdateVo);
    }

    /**
     * addObject2Table addObject2Table
     *
     * @param obj
     * @param <T>
     */
    public <T> void insertObject(T obj) {
        this.insertObject(obj, null);
    }

    /**
     * addObject2Table addObject2Table
     *
     * @param obj
     * @param tableName
     * @param <T>
     */
    public <T> void insertObject(T obj, String tableName) {
        SQLInsertVo sqlInsertVo = SQLBuilder.buildInsertTableSQL(obj, tableName);
        shamanTemplate.insert(sqlInsertVo);
    }

    /**
     * insertObjectBatch insertObjectBatch
     *
     * @param objectList
     * @param <T>
     */
    public <T> void insertBatch(List<T> objectList) {
        this.insertBatch(objectList, null);
    }

    /**
     * insertObjectBatch insertObjectBatch
     *
     * @param objectList
     * @param tableName
     * @param <T>
     */
    public <T> void insertBatch(List<T> objectList, String tableName) {
        if (CollectionUtils.isEmpty(objectList)) {
            return;
        }
        // List is not empty,But each item is empty
        for (T item : objectList) {
            if (item == null) {
                throw new ShamanArgsException("insertBatch args objectList contains NULL item");
            }
        }
        SQLBatchVo sqlBatchVo = SQLBuilder.buildInsertBatchTableSQL(objectList, tableName);
        shamanTemplate.insertBatch(sqlBatchVo);
    }

    /**
     * insertObjectReturnGenKeyLong insertObjectReturnGenKeyLong
     *
     * @param obj
     * @param <T>
     */
    public <T> Long insertObjectReturnGenKeyLong(T obj) {
        SQLInsertVo sqlInsertVo = SQLBuilder.buildInsertTableSQL(obj, null);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        InsertPreparedStatementCreator insertPreparedStatementCreator =
                new InsertPreparedStatementCreator(sqlInsertVo);
        shamanTemplate.update(insertPreparedStatementCreator, keyHolder);
        return keyHolder.getKey().longValue();
    }

    /**
     * updateBatch updateBatch
     * update key default primary key
     *
     * @param objectList
     * @param <T>
     */
    public <T> void updateBatch(List<T> objectList) {
        this.updateBatch(objectList, null);
    }

    /**
     * updateBatch updateBatch
     * update key default primary key
     *
     * @param objectList
     * @param sqlWhereCusSet
     * @param <T>
     */
    public <T> void updateBatch(List<T> objectList, Set<String> sqlWhereCusSet) {
        this.updateBatch(objectList, sqlWhereCusSet, null);
    }

    /**
     * updateBatch updateBatch
     * Custmer Where Key/Value
     *
     * @param objectList
     * @param sqlWhereCusSet
     * @param tableName
     * @param <T>
     */
    public <T> void updateBatch(List<T> objectList, Set<String> sqlWhereCusSet, String tableName) {
        if (CollectionUtils.isEmpty(objectList)) {
            return;
        }
        // List is not empty,But each item is empty
        for (T item : objectList) {
            if (item == null) {
                throw new ShamanArgsException("updateBatch args objectList contains NULL item");
            }
        }
        SQLBatchVo sqlBatchVo = SQLBuilder.buildUpdateBatchTableSQL(objectList, sqlWhereCusSet, tableName);
        shamanTemplate.updateBatch(sqlBatchVo);
    }

    /**
     * replaceBatch replaceBatch
     *
     * @param objectList
     * @param <T>
     */
    public <T> void replaceBatch(List<T> objectList) {
        this.replaceBatch(objectList, null);
    }

    /**
     * replaceBatch replaceBatch
     *
     * @param objectList
     * @param tableName
     * @param <T>
     */
    public <T> void replaceBatch(List<T> objectList, String tableName) {
        if (CollectionUtils.isEmpty(objectList)) {
            return;
        }
        // List is not empty,But each item is empty
        for (T item : objectList) {
            if (item == null) {
                throw new ShamanArgsException("replaceBatch args objectList contains NULL item");
            }
        }
        SQLBatchVo sqlBatchVo = SQLBuilder.buildReplaceBatchTableSQL(objectList, tableName);
        shamanTemplate.insertBatch(sqlBatchVo);
    }

    /**
     * deleteRowForTable deleteRowForTable
     *
     * @param deleteVo
     */
    public void delete(DeleteVo deleteVo) {
        String deleteSQL = SQLBuilder.buildDeleteTableSQL(deleteVo);
        shamanTemplate.delete(deleteSQL);
    }

    /**
     * dropTable dropTable
     *
     * @param tableName
     */
    public void dropTable(String tableName) {
        String dropTableSQL = SQLBuilder.buildDropTableSQL(tableName);
        shamanTemplate.execute(dropTableSQL);
    }

    /**
     * truncateTable truncate 表
     *
     * @param tableName
     */
    public void truncateTable(String tableName) {
        String truncateTableSQL = SQLBuilder.buildTruncateTableSQL(tableName);
        shamanTemplate.execute(truncateTableSQL);
    }

    /**
     * truncateTable truncate 表
     *
     * @param className
     */
    public void truncateTable(Class className) {
        String tableName = HumpUtils.underscoreName(className.getName());
        String truncateTableSQL = SQLBuilder.buildTruncateTableSQL(tableName);
        shamanTemplate.execute(truncateTableSQL);
    }

    /**
     * renameTableName 重命名表
     *
     * @param sourceTableName
     * @param targetTableName
     */
    public void renameTableName(String sourceTableName, String targetTableName) {
        String renameTableNameSQL = SQLBuilder.buildRenameTableSQL(sourceTableName, targetTableName);
        shamanTemplate.execute(renameTableNameSQL);
    }

    /**
     * renameTableNameAtom 重命名表
     *
     * @param sourceTableName     原表表名
     * @param targetTableName     目标表表名
     * @param targetTableNameBack 目标表备份表名
     */
    public void renameTableNameAtom(String sourceTableName, String targetTableName, String targetTableNameBack) {
        String renameTableNameSQL = SQLBuilder.buildRenameTableSQLAtom(sourceTableName, targetTableName, targetTableNameBack);
        shamanTemplate.execute(renameTableNameSQL);
    }

    public void setShamanTemplate(ShamanTemplate shamanTemplate) {
        this.shamanTemplate = shamanTemplate;
    }


}
