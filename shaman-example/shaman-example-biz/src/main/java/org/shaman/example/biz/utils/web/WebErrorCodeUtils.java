package org.shaman.example.biz.utils.web;

import java.util.Set;

/**
 * Created by fenglei on 2016/6/28.
 */
public class WebErrorCodeUtils {


    /**
     * processErrorCode processErrorCode
     *
     * @param errorCodeSet
     * @param webResponse
     * @return
     */
    public static WebResponse processErrorCode(Set<Integer> errorCodeSet,WebResponse webResponse){
        webResponse.setStatus(ResponseCode.WEB_STATUS_FAILED);
        webResponse.setErrorCode(errorCodeSet);
        return webResponse;
    }
}
