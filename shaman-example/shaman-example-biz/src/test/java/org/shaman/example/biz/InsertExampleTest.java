package org.shaman.example.biz;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.shaman.example.BaseTest;
import org.shaman.example.biz.utils.MD5Util;
import org.shaman.example.biz.vo.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fenglei on 2016/6/10.
 */

public class InsertExampleTest extends BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(InsertExampleTest.class);

    @Autowired
    private InsertExample insertExample;

    @Autowired
    private QueryExample queryExample;

    //    @Test
    public void testInsertForUserInfo() {
        String userName = "lisi";
        String passwdOrg = "123456";
        UserInfo userInfo = new UserInfo(userName, MD5Util.MD5(passwdOrg));
//        System.out.println(passwd);
        insertExample.insertForUserInfo(userInfo);
        UserInfo userInfoRs = queryExample.queryForUserInfo(userName, MD5Util.MD5(passwdOrg));
        logger.info(userInfoRs.toString());
    }

    public void testInsertBatchForUserInfo() {
        UserInfo userInfoOne = new UserInfo("wangwu", MD5Util.MD5("123456"));
        UserInfo userInfoTwo = new UserInfo("zhaoliu", MD5Util.MD5("123456"));
        List<UserInfo> userInfoList = Lists.newArrayList();
        insertExample.insertBatchForUserInfo(userInfoList);
    }

    /**
     * 最后一位丢掉了
     *
     * @param line
     * @return
     */
    private static List<String> splitByTab(String line) {
        Pattern p = Pattern.compile("\\t");
        Matcher m = p.matcher(line);
        //保存结果数组
        List<String> ret = Lists.newArrayList();
        //临时变量
        String temp = null;
        int index = 0;
        while (m.find()) {
            int start = m.start();
            temp = line.substring(index, start);
            ret.add(temp);
            index = m.end();
        }
        return ret;
    }
}
