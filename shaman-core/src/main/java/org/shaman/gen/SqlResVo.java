package org.shaman.gen;

import java.util.List;

public class SqlResVo {

    private List<String> columnNameList;

    private List<String> columnTypeList;

    private String dbName = "";

    private String tableName = "";

    public List<String> getColumnNameList() {
        return columnNameList;
    }

    public void setColumnNameList(List<String> columnNameList) {
        this.columnNameList = columnNameList;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<String> getColumnTypeList() {
        return columnTypeList;
    }

    public void setColumnTypeList(List<String> columnTypeList) {
        this.columnTypeList = columnTypeList;
    }
}
