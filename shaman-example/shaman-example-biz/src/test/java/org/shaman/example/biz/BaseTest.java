package org.shaman.example.biz;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.shaman.example.biz.db.H2DataBase;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by fenglei on 2016/6/10.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/init.xml"})
//@Transactional(transactionManager = "txManager")
public class BaseTest {

    @Before
    public void before() {
//        H2DataBase.start();

    }

    @After
    public void after() {

//        H2DataBase.stop();
    }
}
