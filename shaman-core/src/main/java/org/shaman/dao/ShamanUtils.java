package org.shaman.dao;


import org.shaman.dao.vo.DeleteVo;
import org.shaman.dao.vo.QueryVo;

/**
 * Created by fenglei on 2016/3/4.
 */
public class ShamanUtils {

    /**
     * newQueryVo newQueryVo
     *
     * @param tableClazz
     * @param <T>
     * @return
     */
    public static <T> QueryVo<T> newQueryVo(Class tableClazz){
          return new QueryVo(tableClazz);
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
