package org.shaman.dao.vo;

/**
 * Created by fenglei on 2016/3/7.
 */
public abstract class BaseVo<T> {

    private Class tableClazz;

    public Class getTableClazz() {
        return tableClazz;
    }

    /**
     * setTableClazz setTableClazz
     *
     * @param tableClazz
     * @return
     */
    public void setTableClazz(Class tableClazz) {
        this.tableClazz = tableClazz;
    }
}
