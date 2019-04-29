package com.mzc.db.mysql.data;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class EntityField {

    private Class clazz;

    private HashMap<String, Class> subClasses = null;

    private Field idField;        //主键id field

    private HashMap<String, Field> otherFieldMap = null;

    public EntityField(Class clazz, HashMap<String, Class> subClasses, Field idField, HashMap<String, Field> otherFieldMap) {
        this.idField = idField;
        this.otherFieldMap = otherFieldMap;
        this.clazz = clazz;
        this.subClasses = subClasses != null ? subClasses : new HashMap<>();
    }

    public Field getFieldByFieldName(String fieldname) {
        if (fieldname.equalsIgnoreCase(idField.getName())) {
            return idField;
        }
        return otherFieldMap.get(fieldname);
    }

    /**
     * 获取需要添加的字段 (这儿不会检测主键id的添加，因为创建库的时候要求必须有主键id)
     *
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

    /**
     * 通过classname获取对应Class，省去使用Class.forname加载类
     *
     * @param classname
     * @return
     */
    public Class getClassByClassname(String classname) {
        if (classname.equalsIgnoreCase(clazz.getName())) {
            return clazz;
        }
        return subClasses.get(classname);
    }

    public void fullfillObject(Consumer consumer) {
        otherFieldMap.values().forEach(k -> {
            consumer.accept(k);
        });
    }
    public void fullfillObject(Consumer consumer, List<Field> fields) {
        fields.forEach(k -> {
            consumer.accept(k);
        });
    }

}
