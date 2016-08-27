package org.shaman.example.biz;

import org.junit.Test;
import org.shaman.example.biz.utils.MD5Util;
import org.shaman.example.biz.vo.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by fenglei on 2016/6/10.
 */
public class DeleteExampleTest extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(DeleteExampleTest.class);

    @Autowired
    private DeleteExample deleteExample;

    @Test
    public void testDeleteForUserInfo() {
        Long id = 3L;
        deleteExample.deleteForUserInfo(id);
    }
}
