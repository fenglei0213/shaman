package org.shaman.dao;


import org.shaman.dao.vo.DeleteVo;
import org.shaman.dao.vo.QueryCountVo;
import org.shaman.dao.vo.QueryVo;

/**
 * Created by fenglei on 2016/3/4.
 */
public class ShamanUtils {

    public static QueryVo newQueryVo(){
          return new QueryVo();
    }

    public static QueryCountVo newQueryCountVo(){
        return new QueryCountVo();
    }

    /**
     * newDeleteVo newDeleteVo
     *
     * @param clazz
     * @return
     */
    public static DeleteVo newDeleteVo(Class clazz){
          return new DeleteVo(clazz);
    }

}
