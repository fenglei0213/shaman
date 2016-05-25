package org.shaman.core.dao;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by fenglei on 2016/3/2.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldMeta {

    /**
     * 是否为序列号
     * @return
     */
    boolean id() default false;
    /**
     * 字段名称
     * @return
     */
    String name() default "";
    /**
     * 字段描述
     * @return
     */
    String description() default "";
}
