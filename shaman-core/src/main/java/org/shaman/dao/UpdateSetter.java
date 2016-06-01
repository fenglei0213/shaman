package org.shaman.dao;

import org.springframework.jdbc.core.PreparedStatementSetter;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by fenglei on 2016/3/4.
 */
public class UpdateSetter extends BaseSetter implements PreparedStatementSetter {

    private Map<Field, Object> sqlSetMap;
    private Map<Field, Object> sqlWhereMap;

    public UpdateSetter(Map<Field, Object> sqlSetMap, Map<Field, Object> sqlWhereMap) {
        this.setSqlSetMap(sqlSetMap);
        this.setSqlWhereMap(sqlWhereMap);
    }

    /**
     * setValues setValues
     *
     * @param pst
     * @throws SQLException
     */
    @Override
    public void setValues(PreparedStatement pst) throws SQLException {
        int i = 1;
        // set value
        i = this.setFieldValueLoop(pst, sqlSetMap, i);
        // set WHERE id=x
        this.setFieldValueLoop(pst, sqlWhereMap, i);
    }

    /**
     * setFieldValueLoop setFieldValueLoop
     *
     * @param pst
     * @param sqlMap
     * @param i
     * @return
     * @throws SQLException
     */
    private int setFieldValueLoop(PreparedStatement pst, Map<Field, Object> sqlMap, int i) throws SQLException {
        for (Map.Entry<Field, Object> mapEntry : sqlMap.entrySet()) {
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

    public Map<Field, Object> getSqlWhereMap() {
        return sqlWhereMap;
    }

    public void setSqlWhereMap(Map<Field, Object> sqlWhereMap) {
        this.sqlWhereMap = sqlWhereMap;
    }
}
