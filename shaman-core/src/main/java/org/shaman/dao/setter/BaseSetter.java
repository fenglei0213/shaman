package org.shaman.dao.setter;

import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;

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
        // canonicalName update
//        field.getClass().getCanonicalName();
        //clazz.getSimpleName();
        if (objValue == null) {
            if (objValue == null) {
                pst.setNull(i, Types.NULL);
                return;
            }
        }
        String elemType = field.getType().toString();
        if (elemType.indexOf("String") != -1) {
            pst.setString(i, (String) objValue);
        } else if (elemType.indexOf("Integer") != -1 || elemType.indexOf("int") != -1) {
            pst.setInt(i, (Integer) objValue);
        } else if (elemType.indexOf("Long") != -1) {
            pst.setLong(i, (Long) objValue);
        } else if (elemType.indexOf("Double") != -1) {
            pst.setDouble(i, (Double) objValue);
        } else if (elemType.indexOf("Clob") != -1 || elemType.indexOf("clob") != -1) {
            pst.setClob(i, (Clob) objValue);
        } else {
            System.out.println(elemType);
            pst.setClob(i, (Clob) objValue);
        }
    }

    /**
     * setFieldValueLoop setFieldValueLoop
     *
     * @param pst
     * @param sqlSetMap
     * @return
     * @throws SQLException
     */
    public int setFieldValueLoop(PreparedStatement pst, Map<Field, Object> sqlSetMap, int i) throws SQLException {
        for (Map.Entry<Field, Object> mapEntry : sqlSetMap.entrySet()) {
            Field field = mapEntry.getKey();
            Object objValue = mapEntry.getValue();
            this.setFieldValue(pst, field, objValue, i);
            i++;
        }
        return i;
    }

    /**
     * setFieldValueLoop setFieldValueLoop
     *
     * @param ps
     * @param i
     * @return
     * @throws SQLException
     */
    public void setFieldValueBatchLoop(PreparedStatement ps,
                                       List<Map<Field, Object>> sqlSetList,
                                       int i) throws SQLException {
        Map<Field, Object> sqlSetMap = sqlSetList.get(i);
        this.setFieldValueLoop(ps, sqlSetMap, 1);
    }
}
