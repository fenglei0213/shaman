package org.shaman.util;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created by fenglei on 2016/12/18.
 */
public class ShamanSQLUtils {

    /**
     * genInsertBatchSQL genInsertBatchSQL
     *
     * @param targetTableName
     * @param lineAllList
     * @param partitionSize
     * @return
     */
    public static List<String> genInsertBatchSQL(String targetTableName,
                                                 List<String> lineAllList,
                                                 int partitionSize) {
        List<String> sqlList = Lists.newArrayList();
        List<List<String>> lineListList = Lists.partition(lineAllList, partitionSize);
        for (List<String> lineList : lineListList) {
            StringBuilder sqlSb = new StringBuilder("INSERT"
                    + " INTO " + targetTableName + " VALUES ");
            List<String> columnValueAllList = Lists.newArrayList();
            for (String line : lineList) {
                String[] lineArray = line.split("\t");
                List<String> columnValueItemList = Lists.newArrayList();
                for (String item : lineArray) {
                    if (StringUtils.isEmpty(item)) {
                        columnValueItemList.add("null");
                        continue;
                    }
                    columnValueItemList.add("'" + item + "'");
                }
                String columnValueItemListString =
                        "(" + StringUtils.join(columnValueItemList,
                                ",") + ")";
                columnValueAllList.add(columnValueItemListString);
            }
            String columnValueAllListString = StringUtils.join(columnValueAllList,
                    ",");
            sqlSb.append(columnValueAllListString);
            String sql = sqlSb.toString();
            sqlList.add(sql);
        }
        return sqlList;
    }
}
