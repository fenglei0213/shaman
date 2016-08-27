package org.shaman.example.biz.web;

import org.shaman.example.biz.utils.web.WebResponse;
import org.shaman.example.biz.web.protocal.UserInfoVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.shaman.example.biz.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by fenglei on 2016/8/27.
 */
@Controller
public class UserController {

    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    /**
     * getDsData getDsData
     *
     * @return
     */
    @ResponseBody
    @RequestMapping
    public WebResponse test() {
        WebResponse webResponse = new WebResponse();
        logger.info("test is come in");
        return webResponse;
    }

    /**
     * getDsData getDsData
     *
     * @return
     */
    @ResponseBody
    @RequestMapping
    public WebResponse login(UserInfoVo userInfoVo) {
        WebResponse webResponse = new WebResponse();
        boolean flag = true;
        try {
            flag = userService.login(userInfoVo.getUserName(),
                    userInfoVo.getPasswd());
        } catch (Exception e) {

        }
        webResponse.setData(flag);
        return webResponse;
    }
}
