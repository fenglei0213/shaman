package org.shaman.core.dao;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by fenglei on 2016/3/16.
 */
public abstract class BaseSetter {

    /**
     * setFieldValue setFieldValue
     * DataBase MetaData return Integer OR int
     *
     * @param pst
     * @param field
     * @param objValue
     */
    public void setFieldValue(PreparedStatement pst, Field field, Object objValue, int i) throws SQLException {
        String elemType = field.getType().toString();
        if (elemType.indexOf("String") != -1) {
            pst.setString(i, (String) objValue);
        } else if (elemType.indexOf("Integer") != -1 || elemType.indexOf("int") != -1) {
            pst.setInt(i, (Integer) objValue);
        } else if (elemType.indexOf("Long") != -1 || elemType.indexOf("long") != -1) {
            pst.setLong(i, (Long) objValue);
        }
    }
}
