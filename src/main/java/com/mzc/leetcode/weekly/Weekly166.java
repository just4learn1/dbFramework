package com.mzc.leetcode.weekly;

import java.util.ArrayList;
import java.util.List;

/**
 * create by zhencai.ma on 2019/12/10
 */
public class Weekly166 {
    /**
     * 给定一个数字n，求其逐项乘积与和的差值
     * Input: n = 234
     * Output: 15
     * Explanation:
     * Product of digits = 2 * 3 * 4 = 24
     * Sum of digits = 2 + 3 + 4 = 9
     * Result = 24 - 9 = 15
     */
    public static int subtractProductAndSum(int n) {
        List<Integer> list = new ArrayList<>();
        while (n != 0) {
            list.add(n % 10);
            n = n / 10;
        }
        int product = list.get(0);
        int sum = list.get(0);
        for (int i = 1; i < list.size(); i++) {
            product *= list.get(i);
            sum += list.get(i);
        }
        return product - sum;
    }

}
