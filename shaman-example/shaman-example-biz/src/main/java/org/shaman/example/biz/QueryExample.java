package org.shaman.example.biz;

import org.shaman.dao.ShamanDao;
import org.shaman.dao.ShamanVos;
import org.shaman.dao.vo.QueryVo;
import org.shaman.example.biz.vo.UserInfo;
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
        QueryVo<UserInfo> queryVo = ShamanVos.newQueryVo(UserInfo.class);
        queryVo.addColumn("*")
                .addCondition(UserInfo.USER_NAME, userName)
                .addCondition(UserInfo.PASSWD, passwd)
                .addLimit(5);
//        queryVo.setSelectColumnList("*")
//                .setWhereColumnINMap(UserInfo.USER_NAME, Arrays.asList("zhangsan"));
//        queryVo.addColumn("*")
//                .addInCondition(UserInfo.ID, Arrays.asList(1));
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

    /**
     * queryForUserCount queryForUserCount
     *
     * @return
     */
    public int queryForUserCount() {
        QueryVo<UserInfo> queryVo = ShamanVos.newQueryVo(UserInfo.class);
        queryVo.addLikeCondition(UserInfo.USER_NAME, "fenglei");
        int count = shamanDao.queryForCountInt(queryVo);
        return count;
    }
}
