package com.mzc.leetcode;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Permutation {
    /**
     * 给定一个去重后的整型数组，列出所有数字组合
     * <p>
     * Input: [1,2,3]
     * Output:
     * [
     * [1,2,3],
     * [1,3,2],
     * [2,1,3],
     * [2,3,1],
     * [3,1,2],
     * [3,2,1]
     * ]
     *
     * @param nums
     * @return
     */
    public static List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        permute_rec(0, nums, result);

        return result;
    }

    public static void permute_rec(int currentIdx, int[] nums, List<List<Integer>> result) {
        if (currentIdx == nums.length - 1) {
            List<Integer> list = makeIntList(nums);
            result.add(list);
            return;
        }

        for (int i = currentIdx; i < nums.length; ++i) {
            swap(currentIdx, i, nums);
            permute_rec(currentIdx + 1, nums, result);
            swap(currentIdx, i, nums);
        }
    }


    private static void swap(int i, int j, int[] nums) {
        if (i == j) {
            return ;
        }
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }

    private static List<Integer> makeIntList(int[] nums) {
        List<Integer> list = new ArrayList<>();
        for (int num : nums) {
            list.add(num);
        }

        return list;
    }

    public static void main(String[] args) {
        System.out.println(new Timestamp(1560751365_000L));
        System.out.println(Timestamp.valueOf("2019-05-30 00:00:00").getTime());
    }
}
