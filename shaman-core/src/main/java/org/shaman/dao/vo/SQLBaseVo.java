package org.shaman.dao.vo;

/**
 * Created by fenglei on 2016/3/7.
 */
public abstract class SQLBaseVo {

    private String sql = "";

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
