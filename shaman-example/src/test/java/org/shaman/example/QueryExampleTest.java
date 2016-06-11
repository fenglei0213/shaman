package org.shaman.example;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.shaman.example.utils.MD5Util;
import org.shaman.example.vo.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by fenglei on 2016/6/10.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/init.xml"})
@Transactional(transactionManager = "txManager")
public class QueryExampleTest {

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
