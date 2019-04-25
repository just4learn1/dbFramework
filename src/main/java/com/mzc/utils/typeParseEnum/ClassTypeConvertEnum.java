package com.mzc.utils.typeParseEnum;

import static com.mzc.utils.typeParseEnum.DbTypeField.*;

/**
 * classType与dbFieldType对应枚举
 */
public enum ClassTypeConvertEnum {
    ENUM_BOOLEAN(Boolean.class, DB_BOOLEAN, DB_BOOLEAN+ " NOT NULL DEFAULT '0'"),
    ENUM_BYTE(Byte.class, DB_BYTE, DB_BYTE+" NOT NULL DEFAULT '0'"),
    ENUM_SHORT(Short.class, DB_SHORT, DB_SHORT+" NOT NULL DEFAULT '0'"),
    ENUM_INTEGER(Integer.class, DB_INTEGER, DB_INTEGER+" NOT NULL DEFAULT '0'"),
    ENUM_LONG(Long.class, DB_LONG, DB_LONG + " NOT NULL DEFAULT '0'"),
    ENUM_FLOAT(Float.class, DB_FLOAT, DB_FLOAT + " NOT NULL DEFAULT '0'"),
    ENUM_DOUBLE(Double.class, DB_DOUBLE, DB_DOUBLE + " NOT NULL DEFAULT '0'"),
    ENUM_boolean(boolean.class, DB_BOOLEAN, DB_BOOLEAN+ " NOT NULL DEFAULT '0'"),
    ENUM_byte(byte.class, DB_BYTE, DB_BYTE+" NOT NULL DEFAULT '0'"),
    ENUM_short(short.class, DB_SHORT, DB_SHORT+" NOT NULL DEFAULT '0'"),
    ENUM_integer(int.class, DB_INTEGER, DB_INTEGER+" NOT NULL DEFAULT '0'"),
    ENUM_long(long.class, DB_LONG, DB_LONG + " NOT NULL DEFAULT '0'"),
    ENUM_float(float.class, DB_FLOAT, DB_FLOAT + " NOT NULL DEFAULT '0'"),
    ENUM_double(double.class, DB_DOUBLE, DB_DOUBLE + " NOT NULL DEFAULT '0'"),
    ;

    public Class clazz;
    public String dbType;
    public String defaultCreateSql;

    ClassTypeConvertEnum(Class clazz, String dbType) {
        this.clazz = clazz;
        this.dbType = dbType;
        this.defaultCreateSql = dbType;
    }

    ClassTypeConvertEnum(Class clazz, String dbType, String defaultCreateSql) {
        this.clazz = clazz;
        this.dbType = dbType;
        this.defaultCreateSql = defaultCreateSql;
    }

    public static ClassTypeConvertEnum getByClass(Class clazz) {
        for (ClassTypeConvertEnum ce : ClassTypeConvertEnum.values()) {
            if (ce.clazz == clazz) {
                return ce;
            }
        }
        throw new RuntimeException("[不支持的字段类型] [" + clazz.getName() + "]");
    }
}
