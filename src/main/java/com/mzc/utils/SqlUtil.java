package com.mzc.utils;

import com.mzc.db.annotation.Content;
import com.mzc.utils.typeParseEnum.ClassTypeConvertEnum;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static com.mzc.utils.ClassUtil.isCollectionField;
import static com.mzc.utils.ClassUtil.isEntitySerialzable;

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
        if (type == String.class || type.isArray() || isEntitySerialzable(type) || isCollectionField(type)) {
            int len = content != null ? ((Content) content).length() : DEFAULT_VARCHAR_LENGTH;
            if (len > MAX_VARCHAR_LENGTH) {
                throw new RuntimeException("[字段长度不允许超过" + MAX_VARCHAR_LENGTH + "]");
            }
            sb.append(" varchar(").append(len).append(") NOT NULL DEFAULT ''");
        } else {
            ClassTypeConvertEnum cte = ClassTypeConvertEnum.getByClass(type);
            sb.append(cte.defaultCreateSql);
        }
        return sb.toString();
    }

    public static int fullFillParamater(int parameterIndex, Object o, PreparedStatement pstmt, LinkedList<Field> fields)
            throws Exception {
        for (Field f : fields) {
            Class<?> cl = f.getType();
            Object value = f.get(o);
            System.out.printf("name:%s, value:%s", f.getName(), f.get(o));
            if (cl == Boolean.TYPE || cl == Boolean.class) {
                // 我们以CHAR（1）标识boolean，T标识true，其他标识false
                Boolean s = (Boolean) value;
                if (s != null && s.booleanValue()) {
                    pstmt.setInt(parameterIndex, 1);
                } else {
                    pstmt.setInt(parameterIndex, 0);
                }
                parameterIndex++;
            } else if (cl == Byte.TYPE || cl == Byte.class) {
                Byte s = (Byte) value;
                if (s == null)
                    s = 0;
                pstmt.setByte(parameterIndex, s.byteValue());
                parameterIndex++;
            } else if (cl == Short.TYPE || cl == Short.class) {
                Short s = (Short) value;
                if (s == null)
                    s = 0;
                pstmt.setShort(parameterIndex, s.shortValue());
                parameterIndex++;
            } else if (cl == Integer.TYPE || cl == Integer.class) {
                Integer s = (Integer) value;
                if (s == null)
                    s = 0;
                pstmt.setInt(parameterIndex, s.intValue());
                parameterIndex++;
            } else if (cl == Character.TYPE || cl == Character.class) {
                String s = (String) value;
                pstmt.setString(parameterIndex, s);
                parameterIndex++;
            } else if (cl == Long.TYPE || cl == Long.class) {
                Long s = (Long) value;
                if (s == null)
                    s = 0L;
                pstmt.setLong(parameterIndex, s.longValue());
                parameterIndex++;
            } else if (cl == Float.TYPE || cl == Float.class) {
                Float s = (Float) value;
                if (s == null)
                    s = 0f;
                pstmt.setFloat(parameterIndex, s.floatValue());
                parameterIndex++;
            } else if (cl == Double.TYPE || cl == Double.class) {
                Double s = (Double) value;
                if (s == null)
                    s = 0.0;
                pstmt.setDouble(parameterIndex, s.doubleValue());
                parameterIndex++;
            } else if (cl == String.class) {
                String s = (String) value;
                pstmt.setString(parameterIndex, s);
                parameterIndex++;
            } else {
                String s = JsonUtil.jsonFromObject(value, f.getGenericType());
                pstmt.setString(parameterIndex, s);
                parameterIndex++;
            }
        }
        return parameterIndex;
    }

    /**
     * 填充数据
     * @param t
     * @param map
     * @param rs
     * @param fields
     * @param <T>
     * @throws Exception
     */
    public static <T> void constructObject(T t, HashMap<String, Integer> map, ResultSet rs, Collection<Field> fields)
            throws Exception {
        for (Field f : fields) {
            Class<?> cl = f.getType();
            int columnIndex = map.get(f.getName());
            Object value = null;
            if (cl == Boolean.TYPE || cl == Boolean.class) {
                value = rs.getBoolean(columnIndex);
            } else if (cl == Byte.TYPE || cl == Byte.class) {
                value = rs.getByte(columnIndex);
            } else if (cl == Short.TYPE || cl == Short.class) {
                value = rs.getShort(columnIndex);
            } else if (cl == Integer.TYPE || cl == Integer.class) {
                value = rs.getInt(columnIndex);
            } else if (cl == Character.TYPE || cl == Character.class) {
                value = rs.getString(columnIndex);
            } else if (cl == Long.TYPE || cl == Long.class) {
                value = rs.getLong(columnIndex);
            } else if (cl == Float.TYPE || cl == Float.class) {
                value = rs.getFloat(columnIndex);
            } else if (cl == Double.TYPE || cl == Double.class) {
                value = rs.getDouble(columnIndex);
            } else if (cl == String.class) {
                value = rs.getString(columnIndex);
            } else {
                String str = rs.getString(columnIndex);
                if (str != null) {
                    value = JsonUtil.objectFromJson(str, f.getGenericType());
                }

            }
            f.set(t, value);
        }
    }
}
