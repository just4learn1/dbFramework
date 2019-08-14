package com.mzc.leetcode;

public class PathFinding {

    /**
     * 一个机器人从左上角位置开始行走，求机器人走到右下角的不重复路径有多少条
     * 假定机器人只能向右或者向下移动
     * p： n和m最大为100
     *
     * 例如：
     *
     * Input: m = 3, n = 2
     * Output: 3
     * Explanation:
     * From the top-left corner, there are a total of 3 ways to reach the bottom-right corner:
     * 1. Right -> Right -> Down
     * 2. Right -> Down -> Right
     * 3. Down -> Right -> Right
     * @param m
     * @param n
     * @return
     */
    public static int uniquePaths(int m, int n) {
        //向下必须走  n-1 步
        //向右必须走 m-1 步
        return 0;
    }
}
