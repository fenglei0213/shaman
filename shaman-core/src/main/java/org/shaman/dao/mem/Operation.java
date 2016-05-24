package org.shaman.dao.mem;

import java.util.Collection;

/**
 * Created by fenglei on 2016/3/10.
 */
public class Operation {

    /**
     * join join
     *
     * @param collection
     * @return
     */
    public <T> Operation join(Collection<T> collection) {
        Operation operation = new Operation();
        return operation;
    }

    public <T> Operation on(Collection<T> collection) {
        Operation operation = new Operation();
        return operation;
    }
}
