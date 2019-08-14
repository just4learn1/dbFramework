package com.mzc.leetcode;

import com.mzc.leetcode.inst.ListNode;

import java.util.*;

public class NumSum {

    /**
     * 给定一个数组与一个目标数，返回数组中两个相加等于target的数的索引位置
     * 可以假定只有一个解
     * 例：nums={1,3,5,9,2}  target=6   return {0,2}
     *
     * @param nums
     * @param target
     * @return
     */
    public static int[] towSum(int[] nums, int target) {
        HashMap<Integer, Integer> map = new HashMap<>();
        int[] result = new int[]{-1, -1};
        for (int i = 0, len = nums.length; i < len; i++) {
            if (map.containsKey((target - nums[i]))) {
                result[0] = map.get(target - nums[i]);
                result[1] = i;
                break;
            }
            map.put(nums[i], i);
        }
        return result;
    }

    /*static class ListNode {
        public int val;
        public ListNode next;

        public ListNode(int val) {
            this.val = val;
        }
    }*/

    /**
     * 两个ListNode中的数相加，需要考虑进位问题
     * 例： {2, 4, 3} + {5, 6} = {7, 0, 4}
     *
     * @param l1
     * @param l2
     * @return
     */
    public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode head = new ListNode(0);
        ListNode cur = head;
        int val = 0;
        int carry = 0;
        do {
            val = carry;
            if (l1 != null) {
                val += l1.val;
                l1 = l1.next;
            }
            if (l2 != null) {
                val += l2.val;
                l2 = l2.next;
            }
            carry = val / 10;
            val -= carry * 10;
            cur.next = new ListNode(val);
            cur = cur.next;
        } while ((l1 != null) || (l2 != null) || carry > 0);
        return head.next;
    }

    /**
     * 判断输入的数字 x是不是回文数字
     * <p>
     * 例如： 121  return true   -121 return false
     *
     * @param x
     * @return
     */
    public static boolean isPalindrome(int x) {
        if (x == 0) {
            return true;
        }
        if (x < 0 || x % 10 == 0) {
            return false;
        }
        byte[] bytes = new byte[11];        // cause Integer.MAX_VALUE
        int cnt = 0;
        while (x != 0) {
            byte pop = (byte) (x % 10);
            x /= 10;
            bytes[cnt++] = pop;
        }
        for (int i = 0; i < (cnt) / 2; i++) {
            if (bytes[i] != bytes[cnt - 1 - i]) {
                return false;
            }
        }
        return true;
    }


    /**
     * 给定字符串，尽量多的匹配数字转换为int输出   (这题跳过，比较麻烦的点是需要考虑很多边界问题，比如  "-000000000000000121" "2000000000000000000000000" 等等 )
     * <p>
     * 例：  str = "   -123499 workds"   输出  -123499
     * str = "   jf 12345" 输出 0
     *
     * @param str
     * @return
     */
    public static int myAtoi(String str) {
        return 0;
    }

    /**
     * 给定一个非负数组，求最大容量，
     * 例如： 给定 [1,8,6,2,5,4,8,3,7]  输出 49  （因为index=1，值为8， length-1，值为7   两个index的距离为7，最低位为7，因此最大容量输出为49）
     * <p>
     * 最简单的算法： 时间复杂度O（n^2）   循环两次height数组，每次去判断max(oldContainer, newContainer)，直到获得最大值
     * 改进：因为每次索引加一都代表容器的宽度多1，只要后面的数字大于等于前一个数字，容量肯定只增不减   不过时间复杂度还是没变
     * 可以反着来：初始假定 (0, len-1)两个点组成的是最大容量，之后比较左右的值，左边大右侧-1，右侧大左侧+1，这样可以做到时间复杂度为O(n)
     *
     * @param height
     * @return
     */
    public static int maxArea(int[] height) {
        int l = 0, r = height.length - 1, maxArea = 0;
        while (l < r) {
            maxArea = Math.max(maxArea, Math.min(height[l], height[r]) * (r - l));
            if (height[l] > height[r]) {
                r--;
            } else {
                l++;
            }
        }
        return maxArea;
    }

    /**
     * 给定一个int数组，找出数组中任意3个相加等于0的元素
     * 例：[-1, 0, 1, 2, -1, -4]   输出  {[-1,0,1], [-1,2,-1]}
     *
     * @param nums
     * @return
     */
    public static List<List<Integer>> threeSum(int[] nums) {
        Arrays.sort(nums);
        List<List<Integer>> list = new LinkedList<>();

        for (int i = 0; i < nums.length; i++) {
            if (i == 0 || (i > 0 && nums[i] != nums[i - 1])) {
                int sum = 0 - nums[i];
                int l = i + 1;
                int h = nums.length - 1;

                while (l < h) {
                    if (nums[l] + nums[h] == sum) {
                        list.add(Arrays.asList(nums[i], nums[l], nums[h]));
                        while ((l < h) && (nums[l] == nums[l + 1])) l++;
                        while ((l < h) && (nums[l] == nums[h - 1])) h--;
                        l++;
                        h--;
                    } else if (nums[l] + nums[h] < sum) {
                        l++;
                    } else {
                        h--;
                    }
                }
            }
        }
        return list;
    }

    /**
     * 给定一个数组与一个目标值，找到数组中三个数字之和最接近target的值
     * <p>
     * Given array nums = [-1, 2, 1, -4], and target = 1.
     * The sum that is closest to the target is 2. (-1 + 2 + 1 = 2).
     *
     * @param nums
     * @param target
     * @return
     */
    public static int threeSumClosest(int[] nums, int target) {
        Arrays.sort(nums);
        int closetSum = nums[0] + nums[1] + nums[2];
        for (int i = 0, len = nums.length; i < len; i++) {
            if (i == 0 || (i > 0 && nums[i] != nums[i - 1])) {
//                int sum = target - nums[i];
                int l = i + 1;
                int r = len - 1;
                while (l < r) {
                    int tmpSum = nums[l] + nums[r] + nums[i];
                    if (tmpSum == target) {
                        return target;
                    }
                    int oldAbs = Math.abs(closetSum - target);
                    int newAbs = Math.abs(tmpSum - target);
                    if (tmpSum < target) {
                        l++;
                    } else {
                        r--;
                    }
                    closetSum = oldAbs > newAbs ? tmpSum : closetSum;
                }
            }
        }
        return closetSum;
    }

    /**
     * 给定一个int数组，找到所有的唯一的4个int数，其和为target
     * 例：Given array nums = [1, 0, -1, 0, -2, 2], and target = 0.
     * <p>
     * A solution set is:
     * [
     * [-1,  0, 0, 1],
     * [-2, -1, 1, 2],
     * [-2,  0, 0, 2]
     * ]
     * <p>
     * <p>
     * int[] arr = new int[]{-1,0,1,2,-1,-4};  target = 0  测试用例没过
     *
     * @param nums
     * @param target
     * @return
     */
    public static List<List<Integer>> fourSum(int[] nums, int target) {
        Arrays.sort(nums);
        System.out.println(Arrays.toString(nums));
        List<List<Integer>> result = new ArrayList<>();
        for (int i = 0; i < nums.length; i++) {
            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }
            for (int j = i + 1; j < nums.length; j++) {
                if (j > (i + 1) && nums[j] == nums[j - 1]) {
                    continue;
                }
                int sum = target - nums[i] - nums[j];
                int l = j + 1;
                int r = nums.length - 1;
                while (l < r && j < r) {
                    System.out.printf("i:%d, %d   j:%d, %d   l:%d, %d  r: %d, %d  sum:%d\n", i, nums[i], j, nums[j], l, nums[l], r, nums[r], sum);
                    if (l == j) {
                        l++;
                    } else if (sum == nums[l] + nums[r]) {
                        //这地方由于又可能会出现元素相同的情况，需要做去重处理
                        result.add(Arrays.asList(nums[i], nums[j], nums[l], nums[r]));
                        do {
                            l++;
                        } while (l < r && nums[l] == nums[l - 1]);
//                        r--;
                    } else if (sum < nums[l] + nums[r]) {
                        r--;
                    } else {
                        l++;
                    }
                }
            }
        }
        return result;
    }
//    private static boolean listContains(List<List<Integer>> list, int ...objs) {
//        for (List<Integer> l : list){
//
//        }
//    }

    /**
     * 给定一个数组，如果第一个元素后有比其大的元素就将最大的元素换到第二位，其他元素顺延，按照升序对数组排序，
     * <p>
     * 例：
     * 1,2,3 → 1,3,2
     * 3,2,1 → 1,2,3
     * 1,1,5 → 1,5,1
     *
     * @param nums
     */
    public static void nextPermutation(int[] nums) {
        int i = nums.length - 2;
        while (i >= 0 && nums[i + 1] <= nums[i]) {          //找出第i位到第length-1位最大的数
            i--;
        }
        if (i >= 0) {
            int j = nums.length - 1;
            while (j >= 0 && nums[j] <= nums[i]) {
                j--;
            }
            System.out.println(Arrays.toString(nums));
            System.out.printf("i:%d -- %d, j:%d --- %d\n", i, nums[i], j, nums[j]);
            swap(nums, i, j);
            System.out.println(Arrays.toString(nums));
        }
        reverse(nums, i + 1);
        System.out.println(Arrays.toString(nums));
    }

    /**
     * 反转有序数组（升序变为降序 or 降序变为升序）
     *
     * @param nums
     * @param start
     */
    public static void reverse(int[] nums, int start) {
        System.out.println(Arrays.toString(nums));
        int i = start, j = nums.length - 1;
        while (i < j) {
            swap(nums, i, j);
            i++;
            j--;
        }
        System.out.println(Arrays.toString(nums));
    }

    private static void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }

    /**
     * 给定一组候选数字及一个目标数，找到所有使用候选数字组合相加等于target的数字组合
     * note：候选数字可以多次使用 ; 所有候选数字全都是正数
     * <p>
     * Input: candidates = [2,3,6,7], target = 7,
     * A solution set is:
     * [
     * [7],
     * [2,2,3]
     * ]
     * <p>
     * Input: candidates = [2,3,5], target = 8,
     * A solution set is:
     * [
     * [2,2,2,2],
     * [2,3,3],
     * [3,5]
     * ]
     *
     * @param candidates
     * @param target
     * @return
     */
    public static List<List<Integer>> combinationSum(int[] candidates, int target) {
        Arrays.sort(candidates);
        List<List<Integer>> list = new ArrayList<>();
        List<Integer> tmp = new ArrayList<>();
        find(list, tmp, candidates, 0, target);
        return list;
    }

    public static void find(List<List<Integer>> list, List<Integer> tmp, int[] nums, int start, int target) {
        if (target < 0) {
            return ;
        }
        if (target == 0) {
            list.add(new ArrayList<>(tmp));
        } else {
            for (int i = start; i < nums.length; i++) {
                if (nums[i] > target) {         //因为nums有序，且其中数字全部都是正数，所以只要nums[i]>target就可以省去之后的循环递归了
                    break;
                }
                tmp.add(nums[i]);
                find(list, tmp, nums, i, target - nums[i]);
                tmp.remove(tmp.size() - 1);
            }
        }
    }

    /**
     * 给定一组候选数字及target，找到所有使用候选数字组合和等于target的数字组合
     * note：每一个候选数字只能使用一次; 所有候选数字全都是正数
     * Input: candidates = [10,1,2,7,6,1,5], target = 8,
     * A solution set is:
     * [
     * [1, 7],
     * [1, 2, 5],
     * [2, 6],
     * [1, 1, 6]
     * ]
     *
     * @param candidates
     * @param target
     * @return
     */
    public static List<List<Integer>> combinationSum2(int[] candidates, int target) {
        List<List<Integer>> list = new ArrayList<>();
        List<Integer> tmp = new ArrayList<>();
        Arrays.sort(candidates);
        find2(list, tmp, candidates, 0, target);
        return list;
    }

    public static void find2(List<List<Integer>> list, List<Integer> tmp, int[] nums, int start, int target) {
        if (target < 0) {
            return ;
        }
        if (target == 0) {
            list.add(new ArrayList<>(tmp));
        } else {
            for (int i = start; i < nums.length; i++) {
                if (nums[i] > target) {
                    break;
                }
                tmp.add(nums[i]);
                find2(list, tmp, nums, (i + 1), target - nums[i]);
                tmp.remove(tmp.size() - 1);
                while (i + 1 < nums.length && nums[i] == nums[i + 1]) {
                    i++;
                }
            }
        }
    }


    /**
     * 给定一个数组（非有序），找到最小的缺失正整数
     * Input: [1,2,0]
     * Output: 3
     *
     * Input: [3,4,-1,1]
     * Output: 2
     *
     * Input: [7,8,9,11,12]
     * Output: 1
     * @param nums
     * @return
     */
    public static int firstMissingPositive(int[] nums) {
        System.out.println(Arrays.toString(nums));
        Arrays.sort(nums);              //此解法为先对数组进行排序，之后比较获得确实的正整数，实现简单
        int result = 1;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] > result) {
                break;
            } else if (nums[i] == result) {
                result++;
            }
        }
        return result;
        /*if (nums == null)
            return 1;

        int min = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] > 0)
                min = (min == 0) ? nums[i] : Math.min(min, nums[i]);
        }
        if (min != 1) return 1;

        for (int i = 0; i < nums.length; i++) {
            int val = nums[i] - min;
            if (val == nums[i]) continue;           // 没看懂这句有啥用，可是加了确实会减少执行时间
            nums[i] = -1;
            while (val >= 0 && val < nums.length && (val+min != nums[val])) {
                int tmp = nums[val] - min;
                nums[val] = val + min;
                val = tmp;
            }
        }

        for (int i = 0; i < nums.length; i++)
            if (nums[i] < 0)
                return i + min;

        return nums.length + min;*/
    }

    /**
     * 给定一个整型数组nums，找到数组相邻数最大和并返回
     *
     * Input: [-2,1,-3,4,-1,2,1,-5,4],
     * Output: 6
     * Explanation: [4,-1,2,1] has the largest sum = 6.
     * @param nums
     * @return
     */
    public static int maxSubArray(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        int maxSum = 0;
        int tmpMax = 0;
        int max = nums[0];
        for (int i = 0; i < nums.length; i++) {
            if (maxSum == 0 && nums[i] <= 0) {
                if (max < nums[i]) {
                    max = nums[i];
                }
                continue;
            }
            tmpMax = Math.max(0, tmpMax+nums[i]);
            maxSum = Math.max(tmpMax, maxSum);
        }
        if (maxSum == 0) {
            maxSum = max;
        }
        return maxSum;
    }

    public static void main(String[] args) throws InterruptedException {
//        int[] arr = new int[]{0,0,0,0,0};
//        int[] arr = new int[]{-1,0,1,2,-1,-4};
//        int[] arr = new int[]{-3,-2,-1,0,0,1,2,3};
//        System.out.println(fourSum(arr, 0));
//        combinationSum2(new int[]{1, 1, 2, 2, 5, 8, 10}, 7).forEach(System.out::println);
        System.out.println(firstMissingPositive(new int[]{3,5,6,8,10,9,1,-5}));
    }
}
