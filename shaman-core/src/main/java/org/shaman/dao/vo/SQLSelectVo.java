package org.shaman.dao.vo;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by fenglei on 2016/3/3.
 */
public class SQLSelectVo<T> extends SQLBaseVo {

    private Class tableClazz;
    private List<T> argList = Lists.newArrayList();

    public Class getTableClazz() {
        return tableClazz;
    }

    public void setTableClazz(Class tableClazz) {
        this.tableClazz = tableClazz;
    }

    public List<T> getArgList() {
        return argList;
    }

    public void setArgList(List<T> argList) {
        this.argList = argList;
    }
}
