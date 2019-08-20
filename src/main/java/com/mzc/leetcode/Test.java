package com.mzc.leetcode;

import com.mzc.leetcode.inst.ListNode;

import java.sql.Timestamp;
import java.util.*;

public class Test {

    public static void main(String[] args) {
        insert(new int[][]{{1,2},{3,5},{6,7},{8,10},{12,16}}, new int[]{7,8});
    }

    /**
     * 给定一个二维数组intervals作为数值区间（以区间最小值排序），及newInterval，将newInterval插入到intervals，并且合并区间
     *
     * Input: intervals = [[1,3],[6,9]], newInterval = [2,5]
     * Output: [[1,5],[6,9]]
     *
     * Input: intervals = [[1,2],[3,5],[6,7],[8,10],[12,16]], newInterval = [4,8]
     * Output: [[1,2],[3,10],[12,16]]
     * @param intervals
     * @param newInterval
     * @return
     */
    public static int[][] insert(int[][] intervals, int[] newInterval) {
        /**
         * 通过二分法查找到newInterval[0]处于intervals[0]的位置，从此位置开始继续通过二分法找到newInterval[1]处于intervals[0]的位置，得到最终的结果
         */
        int startIndex = binarySearch(intervals, newInterval[0]);
        int endIndex = binarySearch(intervals, newInterval[1]);
        int tmpCnt = startIndex + intervals.length - endIndex;
        int[][] result = new int[startIndex+intervals.length-endIndex][2];
        for (int i = 0; i < startIndex; i++) {
            result[i] = intervals[i];
        }
        System.out.println(startIndex);
        System.out.println(endIndex);
        return null;
    }

    public static int binarySearch(int[][] intervals, int target) {
        int start = 0;
        int end = intervals.length-1;
        while (start < end) {
            int mid = (start + end) / 2 + 1;
            if (intervals[mid][0] == target) {
                return mid;
            } else if (intervals[mid][0] < target) {
                start = mid + 1;
            } else {
                end = mid - 1;
            }
        }
        return start-1<0?0:start-1;
    }

    /**
     * 给定一个二维数组，其中每组元素代表一组区间，合并可合并的区间并返回结果
     * Input: [[1,3],[2,6],[8,10],[15,18]]
     * Output: [[1,6],[8,10],[15,18]]
     * Input: [[1,4],[4,5]]
     * Output: [[1,5]]
     *
     * @param intervals
     * @return
     */
    public static int[][] merge(int[][] intervals) {
        /**
         * 题目没有假定区间为有序的，因此需要先以区间开始位置做排序
         * 创建一个一维数组，将所有的区间组合顺序放入一维数组中，在放入期间需要判定新的区间最小/最大值与一维数组中最后一位（前序区间的最大值）进行比较，大于后续区间最小值就将对应数组位置设置为max(olr,new)，否则加入newMin,newMax到数组中
         *
         */
        /*Arrays.sort(intervals, (a, b) -> a[0] - b[0]);
        int index = 0;
        int[] tmp = new int[intervals.length * 2];
        for (int i = 0; i < intervals.length; i++) {
            if (index == 0 || tmp[index - 1] < intervals[i][0]) {
                tmp[index++] = intervals[i][0];
                tmp[index++] = intervals[i][1];
            } else {
                tmp[index - 1] = Math.max(tmp[index-1], intervals[i][1]);       //具有优化空间，可以使用while循环找到下次的最大值，不需要多次赋值
            }
        }
        int[][] result = new int[index/2][2];
        for (int i = 0; i < result.length; i++) {
            result[i][0] = tmp[i*2];
            result[i][1] = tmp[i*2+1];
        }*/
        if (intervals == null || intervals.length < 2) {
            return intervals;
        }
        Arrays.sort(intervals, new Comparator<int[]>() {
            @Override
            public int compare(int[] i1, int[] i2) {
                return i1[0] - i2[0];
            }
        });
        int[] res = new int[intervals.length * 2];
        int idx = 0;
        int i = 0;
        while (i < intervals.length) {
            int start = intervals[i][0];
            int end = intervals[i][1];
            i++;
            while (i <= intervals.length - 1 && intervals[i][0] <= end) {
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
     * 检测是否可以跳转到最后，数组每个元素代表可移动的距离
     * Input: [2,3,1,1,4]
     * Output: true
     * Input: [3,2,1,0,4]
     * Output: false
     *
     * @param nums
     * @return
     */
    public boolean canJump(int[] nums) {
        /**
         * 逆序检测，设定lastPosition=nums.length-1， 逆序循环遍历nums，如果nums[i]+i>=lastPosition，则设置lastPosition=i，最终判定lastPosition==0
         */
        int lastPosition = nums.length - 1;
        for (int i = nums.length - 1; i > 0; i--) {
            if (nums[i] + i >= lastPosition) {
                lastPosition = i;
            }
        }
        return lastPosition == 0;
    }

    /**
     * 给定一个二维数组，按照蛇形循环组合成一个一维数组
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
    public List<Integer> spiralOrder(int[][] matrix) {
        /**
         * while(true)， 因为是二维数组，因此只有4面，所以循环体中每次执行一圈
         */
        return null;
    }


    /**
     * 给定一个整数数组nums，找出n个相邻元素，使其组成的和最大
     * <p>
     * Input: [-2,1,-3,4,-1,2,1,-5,4],
     * Output: 6
     * Explanation: [4,-1,2,1] has the largest sum = 6.
     *
     * @param nums
     * @return
     */
    public static int maxSubArray(int[] nums) {
        /**
         * 首先需要考虑全是负数的情况（从0开始遍历之后连续的负值，找到最大的存起来，后续如果maxSum==0则将此值复给maxSum）
         *
         * 从第一个正整数起循环至nums.length， tmpMax = Math.max(0, tmpMax+nums[i])  maxSum=Math.max(maxSum, tmpMax);
         */
        if (nums == null || nums.length == 0) {
            return 0;
        }
        int startIndex = 0;
        int max = nums[0];
        while (startIndex < nums.length && nums[startIndex] <= 0) {
            max = Math.max(max, nums[startIndex]);
            startIndex++;
        }
        int tmpMaxSum = 0;
        int maxSum = 0;
        for (int i = startIndex; i < nums.length; i++) {
            tmpMaxSum = Math.max(0, tmpMaxSum + nums[i]);
            maxSum = Math.max(maxSum, tmpMaxSum);
        }
        if (maxSum == 0) {
            maxSum = max;
        }
        return maxSum;
    }

    /**
     * 给定一组字符串，对其进行归类
     * Input: ["eat", "tea", "tan", "ate", "nat", "bat"],
     * Output:
     * [
     * ["ate","eat","tea"],
     * ["nat","tan"],
     * ["bat"]
     * ]
     *
     * @param strs
     * @return
     */
    public List<List<String>> groupAnagrams(String[] strs) {
        /**
         * 使用HashMap来归类，关键在于key的构建（此处使用的构建方式是创建一个int[] count=new int[26], 然后循环每个字符串，将count[c-'a']++, 之后把count中的所有元素利用连接符连接起来作为key）
         */
        int[] count = new int[26];
        HashMap<String, List<String>> map = new HashMap<>();
        for (int i = 0; i < strs.length; i++) {
            Arrays.fill(count, 0);
            for (int j = 0; j < strs[i].length(); j++) {
                count[strs[i].charAt(j) - 'a']++;
            }
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < count.length; j++) {
                sb.append(count[j]).append("#");
            }
            String key = sb.toString();
            if (!map.containsKey(key)) {
                map.put(key, new ArrayList<>());
            }
            map.get(key).add(strs[i]);
        }
        return new ArrayList<>(map.values());
    }

    /**
     * 旋转2d图片
     * 例：
     * Given input matrix =
     * [
     * [1,2,3],
     * [4,5,6],
     * [7,8,9]
     * ],
     * <p>
     * rotate the input matrix in-place such that it becomes:
     * [
     * [7,4,1],
     * [8,5,2],
     * [9,6,3]
     * ]
     *
     * @param matrix
     */
    public void rotate(int[][] matrix) {

    }

    /**
     * 给定一个可能有重复值的数组nums，返回所有的唯一数字组合
     *
     * @param nums
     * @return
     */
    public static List<List<Integer>> permuteUnique(int[] nums) {
        /**
         * 统计出所有的nums中的元素及每个元素出现的次数，递归遍历count数组，每次取出一个数字并且count[i]--，直到i==nums.length时取出结果a
         */
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            map.put(nums[i], map.getOrDefault(nums[i], 0) + 1);
        }
        int[] digits = new int[nums.length];
        int[] counts = new int[nums.length];
        int index = 0;
        for (Map.Entry<Integer, Integer> e : map.entrySet()) {
            digits[index] = e.getKey();
            counts[index++] = e.getValue();
        }
        List<List<Integer>> list = new ArrayList<>();
        genUnique(list, new int[nums.length], digits, counts, 0);
        list.forEach(System.out::println);
        return list;
    }

    public static void genUnique(List<List<Integer>> list, int[] tmpNums, int[] digits, int[] counts, int curIndex) {
        if (curIndex == digits.length) {
            list.add(asList(tmpNums));
            return;
        }
        for (int i = 0; i < counts.length; i++) {
            if (counts[i] > 0) {
                tmpNums[curIndex] = digits[i];
                counts[i]--;
                genUnique(list, tmpNums, digits, counts, curIndex + 1);
                counts[i]++;
            }
        }
    }


    /**
     * 给定一组不重复数字，输出所有的数字组合
     * 例：
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
        /**
         * 每次调整nums中元素的位置，然后生成对应的List，之后需要还原交换的位置
         */
        List<List<Integer>> list = new ArrayList<>();
        testGen(list, nums, 0);
        list.forEach(System.out::println);
        return list;
    }

    public static void testGen(List<List<Integer>> list, int[] nums, int index) {
        if (index == nums.length - 1) {
            list.add(asList(nums));
            return;
        }
        for (int i = index; i < nums.length; i++) {
            swapInt(nums, index, i);
            testGen(list, nums, index + 1);
            swapInt(nums, index, i);
        }
    }

    public static List<Integer> asList(int[] nums) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < nums.length; i++) {
            list.add(nums[i]);
        }
        return list;
    }

    /**
     * 给定一组非负整数，其中每个元素代表可以跳转的最大距离，求出最少的达到终点的步数
     * Input: [2,3,1,1,4]
     * Output: 2
     *
     * @param nums
     * @return
     */
    public static int jump(int[] nums) {
        if (nums.length < 2) {
            return 0;
        }
        int step = 0;
        int start = 0;
        int maxPoint = 0;
        int maxRange = 0;
        while (true) {
            for (int i = start + 1; i < start + nums[start] + 1; i++) {
                if (i == nums.length - 1) {
                    return ++step;
                }
                if (maxRange < (i + nums[i])) {
                    maxRange = i + nums[i];
                    maxPoint = i;
                }
            }
            step++;
            start = maxPoint;
            if (maxRange >= nums.length) {          //因为maxRange等于是计算了下一步走的距离，因此需要返回++step
                return ++step;
            }
        }
    }

    /**
     * 给定一个未排序的数组nums，找到不存在于数组中的最小的正整数
     * Input: [1,2,0]
     * Output: 3
     * Input: [7,8,9,11,12]
     * Output: 1
     * Input: [3,4,-1,1]
     * Output: 2
     *
     * @param nums
     * @return
     */
    public static int firstMissingPositive(int[] nums) {
        /**
         *  因为数组长度一定，需要查找的又是正整数，因此如果可以将所有的大于0且小于nums.length的数移动到对应的下标下，循环移动之后的nums，
         * 只要nums[i] != (i+1)，即代表(i+1)就是欠缺的正整数，如果所有都相同则nums.length+1为目标值
         */
        int i = 0;
        while (i < nums.length) {
            while (i < nums.length && (nums[i] <= 0 || nums[i] > nums.length)) {
                i++;
            }
            if (i < nums.length) {
                if (nums[i] - 1 != i) {
                    if (nums[i] != nums[nums[i] - 1]) {            //如果传入{1, 2, 2} 不加此判断会出现死循环的情况，换位置后如果执行i++可能导致漏数 比如{3, 2, 6, 1},第一次循环将1换到了第0位，不再换位就会导致1不在第二位
                        swapInt(nums, i, nums[i] - 1);
                    } else {
                        i++;
                    }
                } else {
                    i++;
                }
            }
        }
        System.out.println(Arrays.toString(nums));
        for (int j = 0; j < nums.length; j++) {
            if (nums[j] != j + 1) {
                return j + 1;
            }
        }
        return nums.length + 1;
    }

    public static void swapInt(int[] nums, int index1, int index2) {
        int tmp = nums[index1];
        nums[index1] = nums[index2];
        nums[index2] = tmp;
    }

    /**
     * 给定一组候选数字candidates及一个目标数字target，统计出所有候选数字组合，使其和等于target，每个候选数字只能使用一次（候选数字可能会有重复值）
     *
     * @param candidates
     * @param target
     * @return
     */
    public List<List<Integer>> combinationSum2(int[] candidates, int target) {
        Arrays.sort(candidates);
        List<List<Integer>> list = new ArrayList<>();
        testFind2(list, new ArrayList<>(), candidates, target, 0);
        return list;
    }

    public static void testFind2(List<List<Integer>> list, List<Integer> tmp, int[] candidates, int target, int start) {
        if (target < 0) {
            return;
        }
        if (target == 0) {
            list.add(new ArrayList<>(tmp));
        } else {
            for (int i = start; i < candidates.length; i++) {
                if (candidates[i] > target) {
                    return;
                }
                tmp.add(candidates[i]);
                testFind2(list, tmp, candidates, target - candidates[i], i + 1);
                tmp.remove(tmp.size() - 1);
                while (i + 1 < candidates.length && candidates[i] == candidates[i + 1]) {           //因为递归中已经使用了后一位的数字，如果相同在循环中再次使用可能会统计出相同的结果
                    i++;
                }
            }
        }
    }

    /**
     * 给定一组候选数字 candidates（没有重复数）及目标数target，找到所有组合使 其和为target
     * <p>
     * 例：Input: candidates = [2,3,6,7], target = 7,
     * A solution set is:
     * [
     * [7],
     * [2,2,3]
     * ]
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
        /**
         * 递归，需要记录之前使用的所有数字
         */
        List<List<Integer>> list = new ArrayList<>();
        testFind(list, new ArrayList<>(), candidates, target, 0);
        return list;
    }

    public static void testFind(List<List<Integer>> list, List<Integer> currentList, int[] candidates, int target, int index) {
        if (target < 0) {
            return;
        }
        if (target == 0) {
            list.add(new ArrayList<>(currentList));
        } else {
            for (int i = index; i < candidates.length; i++) {
                if (target < candidates[i]) {           //在之前先对candidates排序，因此如果大于target则后面不可能会出现目标数字了
                    return;
                }
                currentList.add(candidates[i]);
                testFind(list, currentList, candidates, target - candidates[i], i);
                currentList.remove(currentList.size() - 1);
            }
        }
    }

    /**
     * 给定一个有序数组nums及目标值target，如果在nums中存在和target一样的值则返回其索引，否则返回target应该插入的索引位置
     *
     * @param nums
     * @param target
     * @return
     */
    public int searchInsert(int[] nums, int target) {
        int start = 0;
        int end = nums.length - 1;
        while (start < end) {
            int mid = (start + end + 1) / 2;
            if (nums[mid] == target) {
                return mid;
            }
            if (nums[mid] > target) {
                end = mid - 1;
            } else {
                start = mid + 1;
            }
        }
        if (start >= nums.length) {
            return nums.length;
        }
        return nums[start] < target ? (start + 1) : start;  //如果传入 {1}, 1， 由于start和end初始都为0，所以直接执行到此，nums[start]会等于target，返回的值应该是start
    }

    /**
     * 给定一个有序数组nums，及目标值target，找到target在数组中首次和最后一次出现的位置索引，要求时间复杂度为O(logn)
     * Input: nums = [5,7,7,8,8,10], target = 8
     * Output: [3,4]
     * Input: nums = [5,7,7,8,8,10], target = 6
     * Output: [-1,-1]
     *
     * @param nums
     * @param target
     * @return
     */
    public int[] searchRange(int[] nums, int target) {
        /**
         * 如果使用二分法查找到目标值后向前向后循环比较，则时间复杂度最坏能达到O(n)，不符合条件
         * 因此需要始终使用二分法逻辑查找，找到目标值后需要继续使用二分法向前及向后查找
         */
        int[] result = {-1, -1};
        int left = binarySearchIndex(nums, target, true);
        if (left == nums.length || nums[left] != target) {
            return result;
        }
        result[0] = left;
        result[1] = binarySearchIndex(nums, target, false);
        return null;
    }

    public static int binarySearchIndex(int[] nums, int target, boolean left) {
        //因为是带等号的，因此在得到结果后还需要进行之后的查找，可是此时如果是要找最左边的，如果上次查到目标值，在下次查找时，end需要设置为上次查找到的索引
        int start = 0;
        int end = nums.length;
        while (start < end) {
            int mid = (start + end) / 2;
            if (nums[mid] < target) {
                start = mid + 1;
            } else if (nums[mid] > target || (left && nums[mid] == target)) {
                end = mid;
            }
        }
        return start;
    }

    /**
     * 给定一个部分有序数组（将原本有序的数组分为两部分并反转），并给定一个目标值，返回target在数组中的位置，不存在返回-1
     * <p>
     * 例：Input: nums = [4,5,6,7,0,1,2], target = 0
     * Output: 4
     * Input: nums = [4,5,6,7,0,1,2], target = 3
     * Output: -1
     *
     * @param nums
     * @param target
     * @return
     */
    public int search(int[] nums, int target) {
        /**
         * 使用二分法，将数组分为 {start, mid} {mid, end}两部分，可以确定至少有一部分是有序的（每次使用二分法都符合此条件）
         */
        int start = 0;
        int end = nums.length - 1;
        while (start < end) {
            if (target < nums[start] && target > nums[end]) { //
                return -1;
            }
            int mid = (start + end) / 2;
            if (nums[mid] == target) {
                return mid;
            }
            if (nums[mid] > nums[start]) {      //表示 {start, mid}是有序的
                if (target >= nums[start] && target <= nums[mid]) {
                    end = mid - 1;
                } else {
                    start = mid + 1;
                }
            } else {            //{mid, end}是有序的
                if (target >= nums[mid] && target <= nums[end]) {
                    start = mid + 1;
                } else {
                    end = mid - 1;
                }
            }
        }
        return -1;
    }

    /**
     * 给定字符串s，只包含"("和")"找到最长的合法括号组合
     * Input: "(()"
     * Output: 2
     * Explanation: The longest valid parentheses substring is "()"
     * Input: ")()())"
     * Output: 4
     * Explanation: The longest valid parentheses substring is "()()"
     *
     * @param s
     * @return
     */
    public static int longestValidParentheses(String s) {
        /**
         * 解题思路：先正序遍历，如果等于"("则count+1， 否则count-1，最终count等于0就代表符合规则的，记录长度
         *          可是有可能会出现"("多于")"的情况，因此之后还需要倒序遍历s，使用相反算法，就可以得到最终结果
         */
        int maxLen = 0;
        int index = 0;
        while (index < s.length()) {
            int count = 0;
            int num = 0;
            while (index < s.length() && count >= 0) {
                if (s.charAt(index++) == '(') {
                    count++;
                } else {
                    count--;
                }
                num++;
                if (count == 0) {
                    maxLen = Math.max(maxLen, num);
                }
            }
        }
        index = s.length() - 1;
        while (index >= 0) {
            int count = 0;
            int num = 0;
            while (index >= 0 && count >= 0) {
                if (s.charAt(index++) == ')') {
                    count++;
                } else {
                    count--;
                }
                num++;
                if (count == 0) {
                    maxLen = Math.max(maxLen, num);
                }
            }
        }
        return maxLen;
    }
//

    /**
     * 给定n个单向链表，合并其为一个链表
     * <p>
     * 例：Input:
     * [
     * 1->4->5,
     * 1->3->4,
     * 2->6
     * ]
     * Output: 1->1->2->3->4->4->5->6
     *
     * @param lists
     * @return
     */
    public ListNode mergeKLists(ListNode[] lists) {
        /**
         * 解题思路：拆分为两两合并，最后得到两个再次合并成为最终的有序链表
         *          两个有序链表合并时如果链表长度不同时还可以节省时间及空间复杂度
         */
        return null;
    }

    /**
     * 给定数字n，输出所有合理的括号对字符串
     * 例:
     * 输入 3
     * 输出[
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
        testGenerate(list, "", 0, 0, n);
        return list;
    }

    public static void testGenerate(List<String> list, String s, int l, int r, int n) {
        if (r == n) {
            list.add(s);
            return;
        }
        if (l < n) {
            testGenerate(list, s + "(", l + 1, r, n);
        }
        if (r < l) {
            testGenerate(list, s + ")", l, r + 1, n);
        }
    }

    /**
     * 合并两个有序单向链表,生产一个有序的单向链表
     *
     * @param l1
     * @param l2
     * @return
     */
    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        ListNode result = new ListNode(0);
        ListNode tmp = result;
        while (l1 != null && l2 != null) {
            if (l1.val <= l2.val) {
                tmp.next = new ListNode(l1.val);
                l1 = l1.next;
            } else {
                tmp.next = new ListNode(l2.val);
                l2 = l2.next;
            }
            tmp = tmp.next;
        }
        if (l1 != null) {
            tmp.next = l1;
        } else {
            tmp.next = l2;
        }
        return result.next;
    }

    /**
     * 给定字符串s（只包含  {} () []），判断字符串合法性，所有括号都必须正常开始及结束
     * <p>
     * 例：Input: "{[]}"
     * Output: true
     * Input: "([)]"
     * Output: false
     *
     * @param s
     * @return
     */
    public static boolean isValid(String s) {
        HashMap<Character, Character> map = new HashMap<>();
        map.put('[', ']');
        map.put('{', '}');
        map.put('(', ')');
        Stack<Character> stack = new Stack<>();
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '[' || s.charAt(i) == '{' || s.charAt(i) == '(') {
                stack.push(s.charAt(i));
            } else {
                if (stack.size() == 0) {
                    return false;
                }
                Character pre = stack.pop();
                if (map.get(pre) != s.charAt(i)) {
                    return false;
                }
            }
        }
        return stack.size() == 0;
    }

    /**
     * 给定一个单向链表，删除倒数第n个元素并返回链表头
     * Given linked list: 1->2->3->4->5, and n = 2.
     * <p>
     * After removing the second node from the end, the linked list becomes 1->2->3->5.
     *
     * @param head
     * @param n
     * @return
     */
    public static ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode d = new ListNode(0);
        d.next = head;
        ListNode f = d;
        ListNode s = d;
        for (int i = 0; i < n + 1; i++) {
            f = f.next;
        }
        while (f != null) {
            f = f.next;
            s = s.next;
        }
        s.next = s.next.next;
        return d.next;
    }

    /**
     * 返回所有字母组合 （其中2-9代表26个字母， 顺序， 7->pqrs  9->wxyz 其他均是3个字母）
     * Input: "23"
     * Output: ["ad", "ae", "af", "bd", "be", "bf", "cd", "ce", "cf"].
     *
     * @param digits
     * @return
     */
    static String[][] a = {{}, {},
            {"a", "b", "c"},
            {"d", "e", "f"},
            {"g", "h", "i"},
            {"j", "k", "l"},
            {"m", "n", "o"},
            {"p", "q", "r", "s"},
            {"t", "u", "v"},

            {"w", "x", "y", "z"}};

    public static List<String> letterCombinations(String digits) {
        List<String> result = new ArrayList<>();
        test(result, digits, 0, "");
        return result;
    }

    public static void test(List<String> result, String digits, int index, String s) {
        if (s.length() == digits.length()) {
            result.add(s);
            return;
        }
        for (int i = 0; i < a[digits.charAt(index) - '0'].length; i++) {
            String ss = s;
            ss += a[digits.charAt(index) - '0'][i];
            test(result, digits, index + 1, ss);
        }
    }

    /**
     * 给定一个整形数组及一个目标数字，找到数组中任意三个数字，使其和最接近目标数字
     *
     * @param nums
     * @param target
     * @return
     */
    public int threeSumClosest(int[] nums, int target) {
        Arrays.sort(nums);
        int closetNum = nums[0] + nums[1] + nums[2];
        for (int i = 0; i < nums.length; i++) {
            if (i == 0 || nums[i] != nums[i - 1]) {       //重复的就不需要再次计算了
                int l = i + 1;
                int r = nums.length - 1;
                while (r > l) {
                    int threeSum = nums[i] + nums[l] + nums[r];
                    if (threeSum == target) {
                        return threeSum;
                    }
                    int oldNum = Math.abs(closetNum - target);
                    int newNum = Math.abs(threeSum - target);
                    if (newNum < oldNum) {
                        closetNum = threeSum;
                    }
                    if (threeSum > target) {
                        r--;
                    } else {
                        l++;
                    }
                }
            }
        }
        return closetNum;
    }

    /**
     * 给定一个整型数组 nums，找出数组中3个数字，其和为0  (需要去重)
     * Given array nums = [-1, 0, 1, 2, -1, -4],
     * <p>
     * A solution set is:
     * [
     * [-1, 0, 1],
     * [-1, -1, 2]
     * ]
     *
     * @param nums
     * @return
     */
    public List<List<Integer>> threeSum(int[] nums) {
        /**
         * 解法：先对数组排序，循环数组，因为数组为升序，所以如果最左边的数+最右边的数如果大于当前循环的数则使右侧索引-1，小于则+1，等于0获得的就是目标的三个数字
         */
        Arrays.sort(nums);
        List<List<Integer>> result = new ArrayList<>();
        for (int i = 0; i < nums.length; i++) {
            int l = i + 1;
            int r = nums.length - 1;
            while (r > l) {
                int sum = nums[r] + nums[l];
                if (sum - nums[i] > 0) {        //sum太大
                    r--;
                } else if (sum - nums[i] < 0) {
                    l++;
                } else {
                    result.add(new ArrayList<>(Arrays.asList(nums[i], nums[l], nums[r])));
                    while (r > l && nums[l] == nums[l + 1]) l++;
                    while (r > l && nums[r] == nums[r - 1]) r--;
                    l++;
                    r--;
                }
            }
        }
        return result;
    }

    /**
     * 给定一个整型数组，以数大小为高，两数间距为宽，求最大面积
     *
     * @param height
     * @return
     */
    public int maxArea(int[] height) {
        /**
         * 解题思路：
         */
        int l = 0, r = height.length - 1;
        int maxArea = 0;
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
     * 给定字符串s和匹配规则p， 实现 "."和"*"的匹配规则， 其中"."匹配任意一个字符  "*"匹配任意数量前一个字符（0,n）
     *
     * @param s
     * @param p
     * @return
     */
    public boolean isMatch(String s, String p) {
        /**
         *解题思路： 创建一个二维boolean数组  [s.length+1][p.length+1] 设置 [s.length][p.length] = true
         *           倒序遍历   需要判断如果是*需要判断前一个是否匹配
         */
        return false;
    }

    /**
     * 给定一个32位数字，求其反转数
     * 例：Input: 123
     * Output: 321
     * Input: -123
     * Output: -321
     * Input: 120
     * Output: 21
     *
     * @param x
     * @return
     */
    public int reverse(int x) {
        long result = 0;                //由于使用了negative变量，所以占用空间会大一些，可以不用新的空间，不过这样就得在循环中对result进行合法性判定，大于最大/小于最小等
        boolean negative = x < 0;
        int tmp = Math.abs(x);
        while (tmp > 0) {
            int num = tmp % 10;
            result = result * 10 + num;
            tmp = tmp / 10;
        }
        if (result > Integer.MAX_VALUE) {
            return 0;
        }
        int r = (int) (negative ? -result : result);
        return r;
    }

    /**
     * 找出最长回文字符串
     * Input: "cbbd"
     * Output: "bb"
     * Input: "babad"
     * Output: "bab"
     * Note: "aba" is also a valid answer.
     *
     * @param s
     * @return
     */
    public String longestPalindrome(String s) {
        if (s.length() <= 1) {
            return s;
        }
        int startIndex = 0;
        int endIndex = 0;
        for (int i = 0; i < s.length(); i++) {
            int len1 = maxLen(s, i, i);
            int len2 = maxLen(s, i, i + 1);
            int tmpMaxLen = Math.max(len1, len2);
            if ((endIndex - startIndex) < tmpMaxLen) {
                startIndex = i - (tmpMaxLen - 1) / 2;
                endIndex = i + tmpMaxLen / 2;
            }
        }
        return s.substring(startIndex, endIndex + 1);
    }

    public static int maxLen(String s, int left, int right) {
        int L = left;
        int R = right;
        while (L >= 0 && R < s.length() && s.charAt(R) == s.charAt(L)) {
            L--;
            R++;
        }
        return R - L - 1;
    }

    /**
     * 求两个有序整形数组的中位数
     * <p>
     * 例：nums1 = [1, 3]
     * nums2 = [2]
     * <p>
     * The median is 2.0
     * <p>
     * nums1 = [1, 2]
     * nums2 = [3, 4]
     * <p>
     * The median is (2 + 3)/2 = 2.5
     *
     * @param nums1
     * @param nums2
     * @return
     */
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        /**
         * 1.将nums1分为 {0, i-1}->{i, m} 两段
         * 2.将nums2分为 {0, j-1}->{j, n} 两段
         * 3.其中nums1的{0, i-1} 与 nums2的{0, j-1}段组成左边的， 剩下的组成右边的， 既 leftLen=i+j   rightLen=m-i+n-j(或者m-i+n-j+1)    +1是为了保证(m+n)%2==1时，max(left)就是需要的中位数
         * 4.如果可以找到(i, j)满足 leftLen==rightLen  && max(left) <= min(right)，此时两段有序数组的中位数就是 ((max(left)+min(right))/2
         *
         * P：需要n>=m   因为根据以上条件 i+j=m-i+n-j+1  ->  j=(m+n+1)/2 - i  ，如果n<m，此时获取到的j可能会出现负数
         */
        int[] A = nums1;
        int[] B = nums2;
        int m = A.length, n = B.length;
        if (n < m) {                //交换A和B，为了达到 n>=m的条件
            int[] tmp = A;
            A = B;
            B = tmp;
            m = A.length;
            n = B.length;
        }
        int iMin = 0, iMax = m, halfLen = (m + n + 1) / 2;
        while (iMin <= iMax) {
            int i = (iMin + iMax) / 2;
            int j = halfLen - i;
            if (i > iMin && A[i - 1] > B[j]) {        //i太大
                iMax = i - 1;
            } else if (i < iMax && B[j - 1] > A[i]) {   //i太小
                iMin = i + 1;
            } else {            //满足条件
                int mxLeft = 0;
                if (i == 0) {
                    mxLeft = B[j - 1];
                } else if (j == 0) {
                    mxLeft = A[i - 1];
                } else {
                    mxLeft = Math.max(A[i - 1], B[j - 1]);
                }
                if ((m + n) % 2 == 1) {
                    return mxLeft;
                }
                int minRight = 0;
                if (i == m) {
                    minRight = B[j];
                } else if (j == n) {
                    minRight = A[i];
                } else {
                    minRight = Math.min(A[i], B[j]);
                }
                return (mxLeft + minRight) / 2;
            }
        }
        return 0;
    }

    /**
     * 最长不重复字符串
     * Input: "abcabcbb"
     * Output: 3
     * Explanation: The answer is "abc", with the length of 3.
     *
     * @param s
     * @return
     */
    public static int lengthOfLongestSubstring(String s) {
        int result = 0;
        int[] tmpIndexs = new int[128];
        for (int i = 0, j = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            j = Math.max(tmpIndexs[c], j);      //因为后面重复的字符串索引可能是在当前记录的之前   比如  abba， 循环到最后一个a的时候获取到的索引会是1
            result = Math.max(result, i - j + 1);
            tmpIndexs[c] = i + 1;
        }
        return result;
    }

    /**
     * 例：
     * Input: (2 -> 4 -> 3) + (5 -> 6 -> 4)
     * Output: 7 -> 0 -> 8
     * Explanation: 342 + 465 = 807.
     *
     * @param l1
     * @param l2
     * @return
     */
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        int num = l1.val + l2.val;
        int carry = num / 10;
        num = num - carry * 10;
        ListNode result = new ListNode(0);
        ListNode tmp = new ListNode(num);
        result.next = tmp;
        while (l1.next != null || l2.next != null || carry != 0) {
            num = carry;
            if (l1.next != null) {
                num += l1.next.val;
                l1 = l1.next;
            }
            if (l2.next != null) {
                num += l2.next.val;
                l2 = l2.next;
            }
            carry = num / 10;
            num = num - carry * 10;
            tmp.next = new ListNode(num);
            tmp = tmp.next;
        }
        return result.next;
    }

    /**
     * 给定一个整数数组 nums 和一个目标值 target，请你在该数组中找出和为目标值的那 两个 整数，并返回他们的数组下标。
     * <p>
     * 你可以假设每种输入只会对应一个答案。但是，你不能重复利用这个数组中同样的元素。
     */
    public int[] twoSum(int[] nums, int target) {
        if (nums == null || nums.length == 0) {
            return new int[0];
        }
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int left = target - nums[i];
            Integer index = map.get(left);
            if (index != null) {
                return new int[]{index, i};
            }
            map.put(nums[i], i);
        }
        return new int[0];
    }


}
