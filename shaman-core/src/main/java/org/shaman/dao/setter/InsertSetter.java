package org.shaman.dao.setter;

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
        super.setFieldValueLoop(pst, sqlSetMap, 1);
    }

    /**
     * setSqlSetMap setSqlSetMap
     *
     * @param sqlSetMap
     */
    public void setSqlSetMap(Map<Field, Object> sqlSetMap) {
        this.sqlSetMap = sqlSetMap;
    }
}
