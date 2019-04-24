package com.mzc.utils;

import com.mzc.db.annotation.Content;
import com.mzc.utils.typeParseEnum.ClassTypeConvertEnum;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class SqlUtil {

    private static int DEFAULT_VARCHAR_LENGTH = 256;
    private static int MAX_VARCHAR_LENGTH = 8000;

    /**
     * 将Field转化为创建表的sql
     *
     * @param field
     * @return
     */
    public static String parseClassType2DbType(Field field) {
        StringBuffer sb = new StringBuffer();
        Class type = field.getType();
        Annotation content = field.getAnnotation(Content.class);
        sb.append(field.getName()).append(" ");
        if(type == String.class) {
            int len = content != null ? ((Content) content).length() : DEFAULT_VARCHAR_LENGTH;
            if(len > MAX_VARCHAR_LENGTH){
                throw new RuntimeException("[字段长度不允许超过"+MAX_VARCHAR_LENGTH+"]");
            }
            sb.append(" varchar(").append(len).append(")");
        } else{
            ClassTypeConvertEnum cte = ClassTypeConvertEnum.getByClass(type);
            sb.append(cte.defaultCreateSql);
        }
        return sb.toString();
    }
}
