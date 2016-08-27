package org.shaman.example.biz;

import org.shaman.dao.ShamanDao;
import org.shaman.dao.ShamanUtils;
import org.shaman.dao.vo.QueryVo;
import org.shaman.example.biz.vo.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by fenglei on 2016/8/27.
 */
@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private ShamanDao shamanDao;

    @Transactional
    public boolean login(String userName, String passwd) {
        QueryVo<UserInfo> queryVo = ShamanUtils.newQueryVo(UserInfo.class);
        queryVo.addColumn("*")
                .addCondition(UserInfo.USER_NAME, userName)
                .addCondition(UserInfo.PASSWD, passwd);
        UserInfo userInfo = shamanDao.queryObject(queryVo);
        if (userInfo != null) {
            return true;
        }
        return false;
    }
}
