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

public class UpdateExampleTest extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(UpdateExampleTest.class);

    @Autowired
    private UpdateExample updateExample;

    @Test
    public void testQueryForUserInfo() {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(4L);
        userInfo.setNickName("rabbit");

        updateExample.updateUserInfo(userInfo);
    }
}
