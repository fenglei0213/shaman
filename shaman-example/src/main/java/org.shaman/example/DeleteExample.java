package org.shaman.example;

import org.shaman.dao.ShamanDao;
import org.shaman.dao.ShamanUtils;
import org.shaman.dao.vo.DeleteVo;
import org.shaman.example.vo.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * Created by fenglei on 2016/6/9.
 */
@Service
public class DeleteExample {

    private static final Logger logger = LoggerFactory.getLogger(DeleteExample.class);

    @Autowired
    private ShamanDao shamanDao;

    public void deleteForUserInfo(Long id) {
        DeleteVo deleteVo = ShamanUtils.newDeleteVo(UserInfo.class);
        deleteVo.setPrimaryKeyName("id");
        deleteVo.setIdList(Arrays.asList(id));
        shamanDao.deleteRow(deleteVo);
    }
}
