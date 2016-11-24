package org.shaman.example;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.shaman.example.db.H2DataBase;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by fenglei on 2016/6/10.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/init.xml"})
// Transactional 打开的话,事务就会滚了
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
