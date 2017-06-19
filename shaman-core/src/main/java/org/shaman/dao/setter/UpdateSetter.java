package org.shaman.dao.setter;

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

    public UpdateSetter(Map<Field, Object> sqlSetMap) {
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
        // set value
        super.setFieldValueLoop(pst, sqlSetMap, 1);
    }

    public Map<Field, Object> getSqlSetMap() {
        return sqlSetMap;
    }

    public void setSqlSetMap(Map<Field, Object> sqlSetMap) {
        this.sqlSetMap = sqlSetMap;
    }

}
