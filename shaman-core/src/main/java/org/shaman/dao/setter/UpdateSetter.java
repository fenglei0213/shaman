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
        // set value
        int i = super.setFieldValueLoop(pst, sqlSetMap, 1);
        // set WHERE id=x
        super.setFieldValueLoop(pst, sqlWhereMap, i);
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
