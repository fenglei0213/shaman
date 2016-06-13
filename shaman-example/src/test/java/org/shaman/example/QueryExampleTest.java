package org.shaman.example;

import org.junit.Test;
import org.shaman.example.utils.MD5Util;
import org.shaman.example.vo.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by fenglei on 2016/6/10.
 */
public class QueryExampleTest extends BaseTest{

    private static final Logger logger = LoggerFactory.getLogger(QueryExampleTest.class);

    @Autowired
    private QueryExample queryExample;

    @Test
    public void testQueryForUserInfo() {
        String userName = "zhangsan";
        String passwdOrg = "123456";
        String passwd = MD5Util.MD5(passwdOrg);
//        System.out.println(passwd);
        UserInfo userInfo = queryExample.queryForUserInfo(userName, passwd);
        logger.info(userInfo.toString());
    }
}
