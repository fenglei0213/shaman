package org.shaman.dao;

import org.shaman.dao.BaseSetter;
import org.springframework.jdbc.core.PreparedStatementSetter;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by fenglei on 2016/3/4.
 */
public class InsertSetter extends BaseSetter implements PreparedStatementSetter {

    private Map<Field, Object> sqlSetMap;

    public InsertSetter(Map<Field, Object> sqlSetMap) {
        this.setSqlSetMap(sqlSetMap);
    }

    /**
     * setValues setValues
     *
     * @param pst
     * @throws SQLException
     */
    @Override
    public void setValues(PreparedStatement pst) throws SQLException {
        this.setFieldValueLoop(pst, sqlSetMap);
    }

    /**
     * setFieldValueLoop setFieldValueLoop
     *
     * @param pst
     * @param sqlSetMap
     * @return
     * @throws SQLException
     */
    private int setFieldValueLoop(PreparedStatement pst, Map<Field, Object> sqlSetMap) throws SQLException {
        int i = 1;
        for (Map.Entry<Field, Object> mapEntry : sqlSetMap.entrySet()) {
            Field field = mapEntry.getKey();
            Object objValue = mapEntry.getValue();
            if (objValue != null) {
                super.setFieldValue(pst, field, objValue, i);
                i++;
            }
        }
        return i;
    }

    public Map<Field, Object> getSqlSetMap() {
        return sqlSetMap;
    }

    public void setSqlSetMap(Map<Field, Object> sqlSetMap) {
        this.sqlSetMap = sqlSetMap;
    }
}
