package org.shaman.example.biz;

import org.shaman.dao.ShamanDao;
import org.shaman.example.biz.vo.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by fenglei on 2016/6/9.
 */
@Service
public class InsertExample {

    private static final Logger logger = LoggerFactory.getLogger(InsertExample.class);

    @Autowired
    private ShamanDao shamanDao;

    /**
     * queryForUserInfo queryForUserInfo
     *
     * @param userInfo
     */
    public void insertForUserInfo(UserInfo userInfo) {
        shamanDao.insertObject(userInfo);
    }

    /**
     * insertBatchForUserInfo insertBatchForUserInfo
     *
     * @param userInfoList
     */
    public void insertBatchForUserInfo(List<UserInfo> userInfoList) {
        shamanDao.insertObjectBatch(userInfoList);
    }


}
