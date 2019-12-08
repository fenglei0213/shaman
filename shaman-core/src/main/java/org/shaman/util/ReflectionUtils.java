package org.shaman.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by fenglei on 2016/6/11.
 */
public class ReflectionUtils {

    /**
     * getGenericClass getGenericClass
     *
     * @param obj
     * @return
     */
    public static <T> Class<T> getGenericClass(Object obj) {
        Class clazz = obj.getClass();
        Type type = clazz.getGenericSuperclass();
        Type trueType = ((ParameterizedType) type).getActualTypeArguments()[0];
        Class<T> c = (Class<T>) trueType;
        return c;
    }
}
