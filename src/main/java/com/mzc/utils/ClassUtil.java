package com.mzc.utils;

import com.mzc.db.annotation.EntitySerialzable;
import com.mzc.db.testEntity.Player;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static java.lang.reflect.Modifier.*;
import static com.mzc.utils.BitUtil.*;

public class ClassUtil {
    /**
     * 检测clazz是否有无参构造方法
     *
     * @param clazz
     * @return
     */
    public static boolean haveNoneParamConstructor(Class clazz) {
        try {
            Constructor c = clazz.getConstructor();
            return c != null;
        } catch (NoSuchMethodException e) {
        }
        return false;
    }

    public static Field[] getSerializableFields(Class clazz) {
        Field[] fields = clazz.getDeclaredFields();
        List<Field> resultList = new ArrayList<>();
        for (Field f : fields) {
            int modifiers = f.getModifiers();
            if (!typeOf(modifiers, STATIC) && !typeOf(modifiers, FINAL) && !typeOf(modifiers, TRANSIENT)) {
                resultList.add(f);
            }
        }
        return resultList.toArray(new Field[resultList.size()]);
    }

    public static boolean isEntitySerialzable(Class type) {
        return type.getAnnotation(EntitySerialzable.class) != null;
    }

    public static boolean isCollectionField(Class<?> clazz){
        if (Map.class.isAssignableFrom(clazz) || Collection.class.isAssignableFrom(clazz)) {
            return true;
        }
        return false;
    }

    public static boolean isBasicType(Class clazz){
        if (clazz.isPrimitive() || isEntitySerialzable(clazz) || clazz == String.class){
            return true;
        }
        return false;
    }

}
