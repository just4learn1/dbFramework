package com.mzc.com.mzc.leetcode;

import java.util.HashSet;

public class StringOpt {

    /**
     * 找到传入字符串中最长的不重复字符串个数
     *
     * @param s
     * @return
     */
    public static int lengthOfLongestSubstring(String s) {
        int n = s.length(), ans = 0;
        int[] index = new int[128]; // current index of character
        // try to extend the range [i, j]
        for (int j = 0, i = 0; j < n; j++) {
            i = Math.max(index[s.charAt(j)], i);            //获得当前循环的字符串之前出现的位置索引（没有出现过会是0，由于使用max，因此会返回i）
            ans = Math.max(ans, j - i + 1);                 //这地方+1是为了解决传入的字符串全部唯一的时候得到的长度会少1
            index[s.charAt(j)] = j + 1;
        }
        return ans;
    }

    public static void main(String[] args) {
        String str = "qw";
        System.out.println(lengthOfLongestSubstring(str));
    }
}
