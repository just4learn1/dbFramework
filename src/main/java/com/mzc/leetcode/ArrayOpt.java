package com.mzc.leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArrayOpt {

    /**
     * 给定一个 n*m 的二维数组，将其按照螺旋状转换成一个一维数组
     * <p>
     * Input:
     * [
     * [ 1, 2, 3 ],
     * [ 4, 5, 6 ],
     * [ 7, 8, 9 ]
     * ]
     * Output: [1,2,3,6,9,8,7,4,5]
     *
     * @param matrix
     * @return
     */
    public static List<Integer> spiralOrder(int[][] matrix) {
        List ans = new ArrayList();
        if (matrix.length == 0)
            return ans;
        int r1 = 0, r2 = matrix.length - 1;
        int c1 = 0, c2 = matrix[0].length - 1;
        while (r1 <= r2 && c1 <= c2) {
            //一下四个for循环等于将以 matirx[r1][c]作为起始位置，旋转一圈的数字加入到list中
            for (int c = c1; c <= c2; c++) ans.add(matrix[r1][c]);
            for (int r = r1 + 1; r <= r2; r++) ans.add(matrix[r][c2]);
            if (r1 < r2 && c1 < c2) {
                for (int c = c2 - 1; c > c1; c--) ans.add(matrix[r2][c]);
                for (int r = r2; r > r1; r--) ans.add(matrix[r][c1]);
            }
            r1++;
            r2--;
            c1++;
            c2--;
        }
        return ans;
    }

    /**
     * 给定过一个二维数组，每个数组代表一个数值区间，合并重合的区间数组，返回合并后的二维数组
     * Input: [[1,4],[4,5]]
     * Output: [[1,5]]
     * Explanation: Intervals [1,4] and [4,5] are considered overlapping.
     * Input: [[1,3],[2,6],[8,10],[15,18]]
     * Output: [[1,6],[8,10],[15,18]]
     * Explanation: Since intervals [1,3] and [2,6] overlaps, merge them into [1,6].
     *
     * @param intervals
     * @return
     */
    public static int[][] merge(int[][] intervals) {
        /*
        //
        if (intervals.length == 0) {
            return new int[0][];
        }
        LinkedList<int[]> list = new LinkedList<>();
        Arrays.sort(intervals, (a, b) -> a[0] - b[0]);
        int end=intervals[0][1];
        list.add(intervals[0]);
        for (int i = 1; i < intervals.length; i++) {
            if (end >= intervals[i][0]) {
                int max = Math.max(end, intervals[i][1]);
                if (max > end) {
                    list.getLast()[1] = max;
                    end = max;
                }
            } else {
                list.add(intervals[i]);
                end = intervals[i][1];
            }
        }
        return list.toArray(new int[0][]);*/
        if (intervals == null || intervals.length < 2) {
            return intervals;
        }
        Arrays.sort(intervals, (a, b) -> a[0 - b[0]]);
        int[] res = new int[intervals.length * 2];          //最坏情况为所有区间都独立，没法合并，此数组用来存储所有
        int idx = 0;
        int i = 0;
        while (i < intervals.length) {
            int start = intervals[i][0];
            int end = intervals[i][1];
            i++;
            while (i < intervals.length && intervals[i][0] <= end) {
                end = end > intervals[i][1] ? end : intervals[i][1];
                i++;
            }
            res[idx++] = start;
            res[idx++] = end;
        }
        int[][] result = new int[idx / 2][2];
        for (int j = 0; j < idx / 2; j++) {
            result[j][0] = res[j * 2];
            result[j][1] = res[j * 2 + 1];
        }
        return result;
    }


    /**
     * 给定一个二维数组和一个单独数组，所有数组中只包含两个数，代表区间，尽量将新加的数组与之前的区间合并，假设intervals数组是已经按照其中每个数组的第一个元素排序好的
     * <p>
     * Input: intervals = [[1,3],[6,9]], newInterval = [2,5]
     * Output: [[1,5],[6,9]]
     *
     * @param intervals
     * @param newInterval
     * @return
     */
    public static int[][] insert(int[][] intervals, int[] newInterval) {
        // TODO: 2019/6/3 未完成算法
        int i = 0;
        int start = newInterval[0];
        int end = newInterval[1];
        int tmpIndex = 0;
        while (i < intervals.length && intervals[i][1] < start) {           //找到右区间大于等于start的起始索引
            i++;
            tmpIndex = i;
        }
        return null;
    }

    /**
     * 给定一个正整数n，生成一个矩阵以螺旋形式填充从1-n^2的数字
     * Input: 3
     * Output:
     * [
     * [ 1, 2, 3 ],
     * [ 8, 9, 4 ],
     * [ 7, 6, 5 ]
     * ]
     *
     * @param n
     * @return
     */
    public static int[][] generateMatrix(int n) {
        int[][] result = new int[n][n];
        int r1 = 0, r2 = n - 1;
        int l1 = 0, l2 = n - 1;
        int value = 1;
        while (r1 <= r2 && l1 <= l2) {
            //行 从左到右
            for (int i = r1; i < r2; i++) {
                result[r1][i] = value++;
            }
            //列 从上到下
            for (int i = l1; i < l2; i++) {
                result[i][l2] = value++;
            }
            //行  从右到左
            for (int i = r2; i > r1; i--) {
                result[r2][i] = value++;
            }
            //左下
            for (int i = l2; i > l1; i--) {
                result[i][l1] = value++;
            }
            r1++;
            r2--;
            l1++;
            l2--;
        }
//        System.out.printf("l1:%d, l2:%d, r1:%d, r2:%d\n", l1, l2, r1, r2);
        if (n % 2 != 0) {
            result[(r1 + r2) / 2][(l1 + l2) / 2] = value;
        }
        return result;
    }

    /**
     * 给定一个正整数n，生成从1-n的数字以不同顺序排列出来的数字组合（升序排序），然后给定一个索引k，用来定位之前生成的数组的第k位数字组合，并输出第k位数字组合
     * <p>
     * Input: n = 3, k = 3
     * Output: "213"
     * explation：
     * By listing and labeling all of the permutations in order, we get the following sequence for n = 3:
     * <p>
     * 1 "123"
     * 2 "132"
     * 3 "213"
     * 4 "231"
     * 5 "312"
     * 6 "321"
     *
     * @param n
     * @param k
     * @return
     */
    public static String getPermutation(int n, int k) {
        if(n == 1){
            return String.valueOf(n);
        } else if (n ==0) {
            return new String();
        }
        int[] arr = new int[n];
        List<Integer> list = new ArrayList<>();
        for(int i = 1; i <= n; i++) {
            list.add(i);
        }
        k--;
        for(int i = 0; i < n-2; i++) {
            //没有看明白div的计算算法
            int div = k/ factorial(n-i-1);
            k = k % factorial(n-i-1);
            arr[i] = list.remove(div);
        }
        arr[n-2] = list.remove(k%2);
        arr[n-1] = list.remove(0);
        String ret ="";
        for(int i: arr) {
            ret += String.valueOf(i);
        }
        return ret;
    }

    private static int factorial(int n) {
        int result = 1;
        for (int i = n; i > 1; i--) {
            result *= i;
        }
        return result;
    }

    /**
     * 算法：交换a和b的值，而且不需要任何额外内存
     */
    public static void testSwap() {
        int a = 10;
        int b = 33;
        a ^= b;
        b ^= a;
        a ^= b;
    }

    public static void main(String[] args) {
    }
}
