package com.mzc.utils;

import java.util.Objects;

public class CommonUtil {

    /**
     * 替换str中第replaceIndex个字符为dest
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
        System.arraycopy(chars, replaceIndex+1, newChars, tmpLen, (totalLen-tmpLen));
        return new String(newChars);
    }
}
