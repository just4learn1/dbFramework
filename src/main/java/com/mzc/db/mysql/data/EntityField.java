package com.mzc.db.mysql.data;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EntityField {

    private Field idField;        //主键id field

    private HashMap<String, Field> otherFieldMap;

    public EntityField(Field idField, HashMap<String, Field> otherFieldMap) {
        this.idField = idField;
        this.otherFieldMap = otherFieldMap;
    }

    /**
     * 是否包含字段
     *
     * @param fieldName
     * @return
     */
    public boolean containsField(String fieldName) {
        if (idField.getName().equalsIgnoreCase(fieldName)) {
            return true;
        }
        return otherFieldMap.containsKey(fieldName);
    }

    /**
     * 获取需要添加的字段 (这儿不会检测主键id的添加，因为创建库的时候要求必须有主键id)
     * @param existFieldNames 当前已存在的字段名数组
     * @return
     */
    public List<Field> needAddFields(List<String> existFieldNames) {
        final List<Field> resultList = new ArrayList<>();
        this.otherFieldMap.forEach((k, v) -> {
            if (!existFieldNames.contains(k)) {
                resultList.add(v);
            }
        });
        return resultList;
    }

    public Field getIdField() {
        return idField;
    }

    public HashMap<String, Field> getOtherFieldMap() {
        return otherFieldMap;
    }

}
