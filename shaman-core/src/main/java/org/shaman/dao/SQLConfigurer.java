package org.shaman.dao;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by fenglei on 2016/3/9.
 */
public class SQLConfigurer {

    private static Logger logger = LoggerFactory.getLogger(SQLConfigurer.class);

    private static List<String> sqlFileList = Arrays.asList("/sql/newlog.sql");

    //    private static String sql="getStatUndoCount:SELECT COUNT(*) FROM log_urlkey_item_relation as relation WHERE relation.project_id=? AND (relation.server_state=1 OR " +
//            " relation.ios_state=1 OR relation.android_state=1);" +
//            " getStatDoneCount:SELECT COUNT(*) FROM log_urlkey_item_relation as relation WHERE relation.project_id=? AND (relation.server_state=2 OR " +
//            " relation.ios_state=2 OR relation.android_state=2);";
    private static String sql = "";

    private static Map<String, String> yamlConfigMap = Maps.newHashMap();
    private static StringBuilder yamlConfig = new StringBuilder(sql);

    public void setSqlFileList(List<String> sqlFileList) {
        this.sqlFileList = sqlFileList;
    }

//    @Override
//    public void afterPropertiesSet() throws Exception {

    static {
        Yaml yaml = new Yaml();
        for (String sqlFile : sqlFileList) {
            File file = null;
            try {
                file = new ClassPathResource(sqlFile).getFile();
                yamlConfig.append((String) yaml.load(new FileInputStream(file)));
            } catch (IOException e) {
                logger.error("sqlFile load Exceptino", e);
            }

//            yamlConfigMap.putAll((Map<String, Object>) yaml.load(new FileInputStream(file)));
        }
        String content = yamlConfig.toString();
        String[] contentArray = content.split(";");
        for (String contentItem : contentArray) {
            String[] contentItemArray = contentItem.split(":");
            yamlConfigMap.put(contentItemArray[0].trim(), contentItemArray[1].trim().replace(";", ""));
        }
    }

    /**
     * getYamlConfigMap getYamlConfigMap
     *
     * @param sqlId
     * @return
     */
    public static String getYamlConfigMap(String sqlId) {
        return yamlConfigMap.get(sqlId);
    }
}
