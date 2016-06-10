package org.shaman.example;

import org.junit.After;
import org.junit.Before;
import org.shaman.example.db.H2DataBase;

/**
 * Created by fenglei on 2016/6/10.
 */
public class BaseTest {

    @Before
    public void before() {
        H2DataBase.start();

    }

    @After
    public void after() {
        H2DataBase.stop();
    }
}
