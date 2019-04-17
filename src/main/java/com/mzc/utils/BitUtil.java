package com.mzc.utils;

public class BitUtil {

    public static boolean typeOf(int type1, int type2){
        return (type1 & type2) != 0;
    }
}
