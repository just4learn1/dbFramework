package com.mzc.db.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
public @interface Content {
    /**
     * 入库间隔
     * @return
     */
    int saveIntevel() default 180;

    /**
     * String类型对应数据库varchar长度，默认256
     * @return
     */
    int length() default 256;

}
