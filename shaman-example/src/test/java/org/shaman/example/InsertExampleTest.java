package org.shaman.example;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.shaman.example.utils.MD5Util;
import org.shaman.example.vo.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by fenglei on 2016/6/10.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/init.xml"})
@Transactional(transactionManager = "txManager")
public class InsertExampleTest {
    private static final Logger logger = LoggerFactory.getLogger(InsertExampleTest.class);

    @Autowired
    private InsertExample insertExample;

    @Autowired
    private QueryExample queryExample;

    @Test
    public void testInsertForUserInfo() {
        String userName = "lisi";
        String passwdOrg="123456";
        UserInfo userInfo = new UserInfo(userName,MD5Util.MD5(passwdOrg));
//        System.out.println(passwd);
        insertExample.insertForUserInfo(userInfo);
        UserInfo userInfoRs = queryExample.queryForUserInfo(userName, MD5Util.MD5(passwdOrg));
        logger.info(userInfoRs.toString());
    }

    public void testInsertBatchForUserInfo(){
        UserInfo userInfoOne = new UserInfo("wangwu",MD5Util.MD5("123456"));
        UserInfo userInfoTwo = new UserInfo("zhaoliu",MD5Util.MD5("123456"));
        List<UserInfo> userInfoList = Lists.newArrayList();
        insertExample.insertBatchForUserInfo(userInfoList);
    }
}
