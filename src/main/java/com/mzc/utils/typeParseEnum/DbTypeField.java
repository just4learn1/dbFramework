package com.mzc.utils.typeParseEnum;


public interface DbTypeField {
    String DB_BOOLEAN = "tinyint(1)";
    String DB_BYTE = "tinyint(4)";
    String DB_SHORT = "DECIMAL(6, 0)";
    String DB_INTEGER = "DECIMAL(11, 0)";
    String DB_LONG = "DECIMAL(30, 0)";
    String DB_FLOAT = "DECIMAL(15, 6)";
    String DB_DOUBLE = "DECIMAL(30, 10)";
}
