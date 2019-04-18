package com.mzc.db.mysql.data;

import java.lang.reflect.Field;
import java.util.HashMap;

public class EntityField {

    private Field idField;        //主键id field

    private HashMap<String, Field> otherFieldMap;

    public EntityField(Field idField, HashMap<String, Field> otherFieldMap) {
        this.idField = idField;
        this.otherFieldMap = otherFieldMap;
    }

    public Field getIdField() {
        return idField;
    }

    public HashMap<String, Field> getOtherFieldMap() {
        return otherFieldMap;
    }
}
