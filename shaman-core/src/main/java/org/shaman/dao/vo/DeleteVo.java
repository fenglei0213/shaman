package org.shaman.dao.vo;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by fenglei on 2016/3/4.
 */
public class DeleteVo extends BaseVo {

    private DeleteVo() {

    }

    public DeleteVo(Class clazz) {
        this.setTableClazz(clazz);
    }

    private String primaryKeyName;

    private List idList = Lists.newArrayList();

    public List<Long> getIdList() {
        return idList;
    }

    public String getPrimaryKeyName() {
        return primaryKeyName;
    }

    public DeleteVo setPrimaryKeyName(String primaryKeyName) {
        this.primaryKeyName = primaryKeyName;
        return this;
    }

    public DeleteVo setIdList(List<Long> idList) {
        this.idList = idList;
        return this;
    }
}
