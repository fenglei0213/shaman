package org.shaman.example;

import org.shaman.dao.ShamanDao;
import org.shaman.dao.ShamanUtils;
import org.shaman.dao.vo.QueryVo;
import org.shaman.example.vo.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by fenglei on 2016/6/9.
 */

@Service
public class QueryExample {

    private static final Logger logger = LoggerFactory.getLogger(QueryExample.class);

    @Autowired
    private ShamanDao shamanDao;

    /**
     * queryForUserInfo queryForUserInfo
     *
     * @param userName
     * @param passwd
     */
    public UserInfo queryForUserInfo(final String userName, final String passwd) {
        QueryVo<UserInfo> queryVo = ShamanUtils.newQueryVo(UserInfo.class);
        queryVo.setSelectColumnList("*")
                .setWhereColumnMap(UserInfo.USER_NAME, userName)
                .setWhereColumnMap(UserInfo.PASSWD, passwd);
        // or
//        queryVo.setSelectColumnList(Arrays.asList("*"));
//        queryVo.setWhereColumnMap(new LinkedHashMap<String, Object>() {
//                                      {
//                                          put(UserInfo.USER_NAME, userName);
//                                          put(UserInfo.PASSWD, passwd);
//                                      }
//                                  }
//        );

        UserInfo userInfo = shamanDao.queryObject(queryVo);
        return userInfo;
    }
}
