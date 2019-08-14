package com.mzc.leetcode;

import java.net.Socket;
import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

public class NumOpt {

    /**
     * 跳跃游戏，给定一个非负整型数组，其中每个数字代表最大可以跳跃的长度，从起始位置，求出最少跳跃几次可以到达队尾
     *
     * Input: [2,3,1,1,4]
     * Output: 2
     * Explanation: The minimum number of jumps to reach the last index is 2.
     *     Jump 1 step from index 0 to 1, then 3 steps to the last index.
     * @param nums
     * @return
     */
    public static int jump(int[] nums) {
        if (nums.length < 2) {
            return 0;
        }
        int point = 0;
        int maxRange = 0;
        int jump = 0;
        int maxPoint = 0;

        while (true) {
            for (int i = point+1; i < nums[point] + point + 1; i++) {       //从第point位置，最多可以移动到point+1+nums[point]位
                if (i == nums.length - 1) {
                    return ++jump;
                }
                if (maxRange < nums[i] + i) {
                    maxRange = nums[i] + i;
                    maxPoint = i ;
                }
//                System.out.printf("maxRange:%d, i:%d\n", maxPoint, i);
            }
//            System.out.printf("maxpoint:%d  maxRange:%d\n", maxPoint, maxRange);
            jump++;
            point = maxPoint;
            if (maxRange > nums.length-1) {
                return ++jump;
            }
        }
    }

    /**
     * 给定一个正整数数组（包含0），每个数字代表最大可跳跃的距离，从0开始跳跃，判断给定数组是否可以跳跃到数组最后一位
     * Input: [2,3,1,1,4]
     * Output: true
     * Explanation: Jump 1 step from index 0 to 1, then 3 steps to the last index.
     *
     * Input: [3,2,1,0,4]
     * Output: false
     * Explanation: You will always arrive at index 3 no matter what. Its maximum
     *              jump length is 0, which makes it impossible to reach the last index.
     *
     *
     * solution：逆向思维，从后往前标记可以达到最后一位的索引位置，最终如果索引位置等于0则代表此数组可以跳转到最后一位
     * @param nums
     * @return
     */
    public static boolean canJump(int[] nums) {
//        1591
        int lastPos = nums.length - 1;
        for (int i = nums.length - 1; i >= 0; i--) {
            if (i + nums[i] >= lastPos) {
                lastPos = i;
            }
        }
        return lastPos == 0;
    }



    public static void main(String[] args) {
        System.out.println(new Timestamp(1562140349_000L));
    }

}
