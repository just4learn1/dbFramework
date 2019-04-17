package com.mzc.db.mysql.data;

import java.lang.reflect.Field;
import java.util.HashMap;

public class EntityData {
    private Field idField;

    private HashMap<String, Field> otherFieldMap;
    //表名(默认是class.getSimpleName()， 可以通过@TableAlias注解设置) 全部使用大写
    private String tableName;

}
