package com.mzc.leetcode;

import java.sql.Timestamp;
import java.util.*;

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

    /**
     * 返回最长回文字符串
     * 例： s="ababa"  return aba or bab
     *
     * @param s
     * @return
     */
    public static String longestPalindrome(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }
        int len = s.length();
        int start = 0, end = 0;
        for (int i = 0; i < len; i++) {
            int length1 = expendAroundCenter(s, i, i);
            int length2 = expendAroundCenter(s, i, i + 1);
            int tmp = Math.max(length1, length2);
            if (tmp > (end - start)) {
                start = i - (tmp - 1) / 2;
                end = i + tmp / 2;
            }
        }
        return s.substring(start, end + 1);
    }

    private static int expendAroundCenter(String s, int left, int right) {
        int L = left, R = right;
        while (L >= 0 && R < s.length() && (s.charAt(L) == s.charAt(R))) {          //L可以为0， R最大为s.length()-1
            L--;
            R++;
        }
        return (R - L - 1);
    }

    /**
     * ZigZag Conversion    给定字符串和行数，将字符串转换为ZigZag形式 （倒Z字形）
     * 例如： 给定  ABCDEFGHIG 3
     * 转换
     * A       E       I
     * B   D   F   H   G
     * C       G
     * 输出： AEIBDFHGCG
     * <p>
     * <p>
     * ABCDEFGHIG 4
     * <p>
     * A           G
     * B       F   H
     * C   E       I
     * D           G
     * 输出  ACBFHCEIDG
     * <p>
     * <p>
     * 最简单的解法：创建min(numRows, s.length)个StringBuffer（也可以是StringBuilder），遍历s.toCharArray()，顺序将char放入StringBuffer，达到长度后再逆序加入StringBuffer中
     *
     * @param s
     * @param numRows
     * @return
     */
    public static String convert(String s, int numRows) {
        /**
         * 推导公式，需要根据规律来实现
         */
        if (numRows == 1) return s;

        StringBuilder ret = new StringBuilder();
        int n = s.length();
        int cycleLen = 2 * numRows - 2;

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j + i < n; j += cycleLen) {
                ret.append(s.charAt(j + i));
                if (i != 0 && i != numRows - 1 && j + cycleLen - i < n)
                    ret.append(s.charAt(j + cycleLen - i));
            }
        }
        return ret.toString();
    }

    /**
     * 反转数字
     * 给定 123  返回 321
     *
     * @param x
     * @return
     */
    public static int reverse(int x) {
        int rev = 0;
        while (x != 0) {
            int pop = x % 10;
            x /= 10;
            if (rev > Integer.MAX_VALUE / 10 || (rev == Integer.MAX_VALUE / 10 && pop > 7)) {
                return 0;
            }
            if (rev < Integer.MIN_VALUE / 10 || (rev == Integer.MIN_VALUE / 10 && pop < -8)) {
                return 0;
            }
            rev = rev * 10 + pop;
        }
        return rev;
    }

    /**
     * 实现正则表达式中的 . 和  *
     * * ：0或者多个前一个字符
     * . ：匹配任意单字符
     * <p>
     * solution： 简单版本：递归去判断字符是否满足，需要标记上一个字符是否匹配,每次递归去掉一个字符(subString(1))，根据此标记判断*是否生效,时间空间复杂度都高
     *
     * @param text
     * @param pattern
     * @return
     */
    public static boolean isMatch(String text, String pattern) {
        boolean[][] dp = new boolean[text.length() + 1][pattern.length() + 1];
        dp[text.length()][pattern.length()] = true;

        for (int i = text.length(); i >= 0; i--) {
            for (int j = pattern.length() - 1; j >= 0; j--) {
                boolean first_match = (i < text.length() &&
                        (pattern.charAt(j) == text.charAt(i) ||
                                pattern.charAt(j) == '.'));
                if (j + 1 < pattern.length() && pattern.charAt(j + 1) == '*') {
                    dp[i][j] = dp[i][j + 2] || first_match && dp[i + 1][j];
                } else {
                    dp[i][j] = first_match && dp[i + 1][j + 1];
                }
            }
        }
        return dp[0][0];
    }

    /**
     * 输出最长相同前缀
     * 例：{"flower", "flow", "flight"}   输出 "fl"
     *
     * @param strs
     * @return
     */
    public static String longestCommonPrefix(String[] strs) {
       /* if (strs.length == 0) {
            return "";
        }
        char[] chars = strs[0].toCharArray();
        int endIndex = chars.length;
        for (int i = 1, len=strs.length; i<len; i++) {
            if (endIndex == 0) {
                break;
            }
            for (int j = 0, strLen=strs[i].length(); j < endIndex; j++) {
                if (j>=strLen || chars[j] != strs[i].charAt(j)) {
                    endIndex = j;
                    break;
                }
            }
        }
        return strs[0].substring(0, endIndex);*/
        if (strs.length == 0) return "";
        String prefix = strs[0];
        for (int i = 1; i < strs.length; i++)
            while (strs[i].indexOf(prefix) != 0) {
                prefix = prefix.substring(0, prefix.length() - 1);
                if (prefix.isEmpty()) return "";
            }
        return prefix;
    }

    /**
     * Given a string containing just the characters '(', ')', '{', '}', '[' and ']', determine if the input string is valid.
     * <p>
     * An input string is valid if:
     * <p>
     * Open brackets must be closed by the same type of brackets.
     * Open brackets must be closed in the correct order.
     * <p>
     * 匹配括号，  例： {} true   {(}) false (){}[] true{[]}true
     * <p>
     * <p>
     * solution:  定义一个map，放入key=反括号( ')', ']', '}')， value为对应的正括号
     * 循环字符串的所有字符，如果是反括号就需要它的前一位字符是与之相匹配的正括号（pop出上一个放入的字符），如果是正括号就放入队列中
     * 最后判定队列为空代表所有的正括号都找到了对应的反括号，符合要求
     *
     * @param s
     * @return
     */
    public static boolean isValid(String s) {
        Stack<Character> stack = new Stack<Character>();

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (mappings.containsKey(c)) {

                char topElement = stack.empty() ? '#' : stack.pop();

                if (topElement != mappings.get(c)) {
                    return false;
                }
            } else {
                stack.push(c);
            }
        }

        return stack.isEmpty();
    }

    private static HashMap<Character, Character> mappings;

    static {
        mappings = new HashMap<Character, Character>();
        mappings.put(')', '(');
        mappings.put('}', '{');
        mappings.put(']', '[');
    }

    /**
     * 给定一个数字n，列出所有符合规范的括号组合
     * 例： n=3
     * 输出
     * [
     * "((()))",
     * "(()())",
     * "(())()",
     * "()(())",
     * "()()()"
     * ]
     *
     * @param n
     * @return
     */
    public static List<String> generateParenthesis(int n) {
        List<String> list = new ArrayList<>();
        generateParenthesis(list, "", 0, 0, n);
        return list;
    }

    private static void generateParenthesis(List<String> list, String s, int left, int right, int max) {
        if (s.length() == max * 2) {
            //需要添加合法性判定
            list.add(s);
            return;
        }
        if (left < max) {
            generateParenthesis(list, s + "(", left + 1, right, max);
        }
        if (right < left) {
            generateParenthesis(list, s + ")", left, right + 1, max);
        }

    }

    /**
     * 给定一个字符串s和一些长度相同的单词words，在s中找到刚好串联words中所有单词的字串的起始位置
     * Input:
     * s = "barfoothefoobarman",
     * words = ["foo","bar"]
     * Output: [0,9]
     * Explanation: Substrings starting at index 0 and 9 are "barfoor" and "foobar" respectively.
     * The output order does not matter, returning [9,0] is fine too.
     * <p>
     * Input:
     * s = "wordgoodgoodgoodbestword",
     * words = ["word","good","best","word"]
     * Output: []
     *
     * @param s
     * @param words
     * @return
     */
    public static List<Integer> findSubstring(String s, String[] words) {
        List<Integer> list = new ArrayList<>();
//        combileStrArray(words, list, s, 0, words.length);
        //需要递归所有排列情况
        return list;
    }

    /**
     * 给定字符串（只包含")" 和 "("），找到最长的有效括号
     * <p>
     * Input: "(()"
     * Output: 2
     * <p>
     * <p>longestValidParentheses
     * Input: ")()())"
     * Output: 4
     * Explanation: The longest valid parentheses substring is "()()"
     *
     * @param s
     * @return
     */
    public static int longestValidParentheses(String s) {
        int maxans = 0;
        Stack<Integer> stack = new Stack<>();
        stack.push(-1);
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(') {
                stack.push(i);
            } else {
                stack.pop();            //等于从队列中拿出上一个"("，用来与当前的")"配对
                if (stack.isEmpty()) {  //如果pop后队列为空代表前面没有push任何"("进去，所以就以当前位置作为起始
                    stack.push(i);
                } else {                //pop后队列中还有数据，此数据就是上一次不匹配的起始位置，而且此时属于合法的结束位置，所以比较设置最大的合法长度
                    maxans = Math.max(maxans, i-stack.peek());
                }
            }
        }
        return maxans;
    }

    /**
     * 给定一组字符串，将其归纳总结成多个list，每个list中存储相同字符但是组成顺序不同的字符串
     * Input: ["eat", "tea", "tan", "ate", "nat", "bat"],
     * Output:
     * [-
     *   ["ate","eat","tea"],
     *   ["nat","tan"],
     *   ["bat"]
     * ]
     * @param strs
     * @return
     */
    public static List<List<String>> groupAnagrams(String[] strs) {
        int[] prime = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103};//最多10609个z

        List<List<String>> res = new ArrayList<>();
        HashMap<Integer, Integer> map = new HashMap<>();
        for (String s : strs) {
            int key = 1;
            for (char c : s.toCharArray()) {
                key *= prime[c - 'a'];          //通过质数相乘，获得唯一的key  （）
            }
            List<String> t;
            if (map.containsKey(key)) {
                t = res.get(map.get(key));
            } else {
                t = new ArrayList<>();
                res.add(t);
                map.put(key, res.size() - 1);
            }
            t.add(s);
        }
        return res;
    }

    public static void main(String[] args) {
        long weekStart = Timestamp.valueOf("2019-06-17 00:00:00").getTime();
//        long day = 86400_000L;
//        long hour = 3600_000L;
//        long min = 60_000L;
//        long openTime = weekStart + day * 6 + hour * 20 + 0 * min;
//        System.out.println(new Timestamp(openTime));
//        long warTime = openTime + 586800_000L + 3600_000L + 30_000L;
//        System.out.println(new Timestamp(warTime));
    }

}
