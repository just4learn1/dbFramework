package com.mzc.utils;

import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CommonUtil {

    /**
     * 替换str中第replaceIndex个字符为dest
     *
     * @param str
     * @param replaceIndex
     * @param dest
     * @return
     */
    public static String replaceStr(String str, int replaceIndex, String dest) {
        Objects.requireNonNull(str);
        Objects.requireNonNull(dest);
        if (str.length() <= replaceIndex) {
            return str;
        }
        char[] chars = str.toCharArray();
        char[] destChars = dest.toCharArray();
        int destLen = destChars.length;
        int totalLen = chars.length - 1 + destLen;
        int tmpLen = destLen + replaceIndex;
        char[] newChars = new char[totalLen];
        System.arraycopy(chars, 0, newChars, 0, replaceIndex);
        System.arraycopy(destChars, 0, newChars, replaceIndex, destLen);
        System.arraycopy(chars, replaceIndex + 1, newChars, tmpLen, (totalLen - tmpLen));
        return new String(newChars);
    }

    /**
     * 首字母变小写
     * @param str
     * @return
     */
    public static String firstChar2Lower(String str) {
        StringBuffer sb = new StringBuffer();
        sb.append(Character.toLowerCase(str.charAt(0))).append(str.substring(1));
        return sb.toString();
    }

    public static String getFieldname(String methodName) {
        if (methodName.startsWith("get")) {
            return firstChar2Lower(methodName.substring(3));
        } else if (methodName.startsWith("is")) {
            return firstChar2Lower(methodName.substring(2));
        }
        throw new RuntimeException(String.format("[方法需要以get或者is开头: %s]", methodName));
    }

    public static List<String> getFieldnames(Class clazz) {
        List<String> list = new ArrayList<>();
        for (Method method : clazz.getMethods()){
            list.add(getFieldname(method.getName()));
        }
        return list;
    }

    public static void main(String[] args) {
        System.out.println(new Timestamp(1556267111425L));
    }
}
