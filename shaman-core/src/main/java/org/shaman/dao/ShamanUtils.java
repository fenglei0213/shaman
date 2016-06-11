package org.shaman.dao;


import org.shaman.dao.vo.BaseVo;
import org.shaman.dao.vo.DeleteVo;
import org.shaman.dao.vo.QueryCountVo;
import org.shaman.dao.vo.QueryVo;

/**
 * Created by fenglei on 2016/3/4.
 */
public class ShamanUtils {

    public static <T extends Object> QueryVo<T> newQueryVo(Class tableClazz){
          return new QueryVo(tableClazz);
    }

    public static QueryCountVo newQueryCountVo(Class tableClazz){
        return new QueryCountVo(tableClazz);
    }

    /**
     * newDeleteVo newDeleteVo
     *
     * @param tableClazz
     * @return
     */
    public static DeleteVo newDeleteVo(Class tableClazz){
          return new DeleteVo(tableClazz);
    }

}
