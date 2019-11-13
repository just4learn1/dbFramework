package com.mzc.leetcode;

import com.mzc.leetcode.inst.ListNode;

import java.util.*;

/**
 * create by zhencai.ma on 2019/11/7
 */
public class Test2 {

    public static void main(String[] args) {
        int a = maxSubArray(new int[]{-2,-1});
        System.out.println(a);
    }

    /**
     * 求数组中连续字符和的最大值
     * Input: [-2,1,-3,4,-1,2,1,-5,4],
     * Output: 6
     * Explanation: [4,-1,2,1] has the largest sum = 6.
     * @param nums
     * @return
     */
    public static int maxSubArray(int[] nums) {
        int startIndex = 0;
        int maxNagetive = nums[0];
        while (startIndex < nums.length && nums[startIndex] <= 0) {
            maxNagetive = Math.max(maxNagetive, nums[startIndex]);
            startIndex++;
        }
        int maxSum = 0;
        int tmpSum = 0;
        for (int i = startIndex; i < nums.length; i++) {
            if (tmpSum == 0 && nums[i] <= 0) {
                continue;
            }
            tmpSum = Math.max(0, tmpSum+nums[i]);
            maxSum = Math.max(tmpSum, maxSum);
        }
        return maxSum == 0 ? maxNagetive : maxSum;
    }

    /**
     * 对给定字符串数组归类
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
    public static List<List<String>> groupAnagrams(String[] strs) {
        //此题的优化关键在于生成key的优化上，下面用的是最简单的字符串生成对应key，效率比较低，目前见到最好的
        //是使用26个质数数组premes=[2,3,5,7,9,11,13,17,19,23,29,31......](因为不同个数的质数相乘后的结果是唯一的，所以使用其作为key)
        //实现就是 key *= premes[strs[i].charAt[i]-'a']   ---- 也不需要统计每个char的个数，完全使用简单的乘法生成唯一key
        Map<String, List<String>> map = new HashMap<>();
        int[] charCnts = new int[26];
        for (int i = 0; i < strs.length; i++) {
            Arrays.fill(charCnts, 0);
            String tmp = strs[i];
            for (int j = 0; j < tmp.length(); j++) {
                charCnts[tmp.charAt(j)-'a']++;
            }
            StringBuffer sb = new StringBuffer();
            for (int j = 0; j < charCnts.length; j++) {
                sb.append(charCnts[j]).append("#");
            }
            map.compute(sb.toString(), (key, oldvalue) -> {
               List<String> list = oldvalue == null ? new ArrayList<>() : oldvalue;
               list.add(tmp);
               return list;
            });
        }
        return new ArrayList<>(map.values());
    }

    /**
     * 给定一个可能有重复数字的数组nums，列出所有不重复的数字组合
     *
     * @param nums
     * @return
     */
    public static List<List<Integer>> permuteUnique(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            map.compute(nums[i], (key, oldValue) -> oldValue == null ? 1 : oldValue + 1);
        }
        Integer[] digits = new Integer[map.size()];
        Integer[] counts = new Integer[map.size()];
        int index = 0;
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            digits[index] = entry.getKey();
            counts[index++] = entry.getValue();
        }
        int len = nums.length;
        generateUnique(result, new LinkedList<>(), digits, counts, len);
        return result;
    }

    public static void generateUnique(List<List<Integer>> result, List<Integer> list, Integer[] digits, Integer[] counts, int len) {
        if (list.size() == len) {
            result.add(new ArrayList<>(list));
            return;
        }
        for (int i = 0; i < counts.length; i++) {
            if (counts[i] > 0) {
                counts[i]--;
                list.add(digits[i]);
                generateUnique(result, list, digits, counts, len);
                list.remove(list.size() - 1);
                counts[i]++;
            }
        }
    }


    /**
     * 给定一组没有重复元素的数组，列出其所有排列
     *
     * @param nums
     * @return
     */
    public static List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        generatePermute(result, nums, 0);
        return result;
    }

    public static void generatePermute(List<List<Integer>> result, int[] nums, int index) {
        if (index == nums.length - 1) {
            List<Integer> tmp = new ArrayList<>();
            for (int i = 0; i < nums.length; i++) {
                tmp.add(nums[i]);
            }
            result.add(tmp);
            return;
        }
        for (int i = index; i < nums.length; i++) {
            swap(nums, index, i);
            generatePermute(result, nums, index + 1);
            swap(nums, index, i);
        }
    }


    /**
     * 给定一个非负整数数组nums，其中每个元素代表可走的最远距离，求最少的到达队尾的步数
     *
     * @param nums
     * @return
     */
    public static int jump(int[] nums) {
        if (nums.length < 2) {
            return 0;
        }
        int maxRange = 0;
        int start = 0;
        int len = nums.length;
        int step = 0;
        int maxPoint = 0;
        while (true) {
            for (int i = start + 1; i < start + nums[start] + 1; i++) {
                if (i == len - 1) {
                    return ++step;
                }
                if (maxRange < i + nums[i]) {
                    maxRange = i + nums[i];
                    maxPoint = i;
                }
            }
            step++;
            if (start == maxPoint) {
                return 0;
            }
            start = maxPoint;
            if (maxRange >= len) {
                return ++step;
            }
        }
    }


    /**
     * 给定一组非负正数，求总的蓄水面积
     *
     * @param height
     * @return
     */
    public static int trap(int[] height) {
        int l = 0;
        int r = height.length - 1;
        //找出蓄水边界
        while (l < r && height[l] <= height[l + 1]) l++;
        while (l < r && height[r] <= height[r - 1]) r--;
        int maxTrap = 0;
        while (l < r) {
            int left = height[l];
            int right = height[r];
            if (left < right) {        //蓄水高点在右侧
                while (l < r && left >= height[++l]) {
                    maxTrap += left - height[l];
                }
            } else {
                while (l < r && right >= height[--r]) {
                    maxTrap += right - height[r];
                }
            }
        }
        return maxTrap;
    }

    /**
     * 找出最小的不存在的正整数
     *
     * @param nums
     * @return
     */
    public static int firstMissingPositive(int[] nums) {
        int l = 0;
        int r = nums.length;
        while (l < r) {
            while (l < r && (nums[l] > r || nums[l] <= 0)) {
                l++;
            }
            if (l < r) {
                if (nums[l] - 1 != l) {
                    if (nums[l] != nums[nums[l] - 1]) {            //如果传入{1, 2, 2} 不加此判断会出现死循环的情况，换位置后如果执行i++可能导致漏数 比如{3, 2, 6, 1},第一次循环将1换到了第0位，不再换位就会导致1不在第二位
                        swap(nums, l, nums[l] - 1);
                    } else {
                        l++;
                    }
                } else {
                    l++;
                }
            }
        }
        for (int i = 1; i <= nums.length; i++) {
            if (nums[i - 1] != i) {
                return i;
            }
        }
        return nums.length + 1;
    }

    /**
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
    public static List<List<Integer>> combinationSum(int[] candidates, int target) {
        Arrays.sort(candidates);
        List<List<Integer>> result = new ArrayList<>();
        combineSum(result, new ArrayList<>(), candidates, 0, target);
        return result;
    }

    public static void combineSum(List<List<Integer>> result, List<Integer> tmp, int[] candidates, int index, int target) {
        if (target == 0) {
            result.add(new ArrayList<>(tmp));
            return;
        }
        for (int i = index; i < candidates.length; i++) {
            if (target < candidates[i]) {
                return;
            }
            tmp.add(candidates[i]);
            combineSum(result, tmp, candidates, i + 1, target - candidates[i]);
            tmp.remove(tmp.size() - 1);
            while (i + 1 < candidates.length && candidates[i] == candidates[i + 1]) {
                i++;
            }
        }
    }

    /**
     * 返回target在有序数组nums中的插入位置
     *
     * @param nums
     * @param target
     * @return
     */
    public static int searchInsert(int[] nums, int target) {
        int start = 0;
        int end = nums.length;
        int mid;
        while (start < end) {
            mid = (start + end) / 2;
            if (nums[mid] >= target) {
                end = mid - 1;
            } else {
                start = mid + 1;
            }
        }
        return start;
    }

    /**
     * 在有序数组nums中查找target首次和最后一次出现的索引位置
     *
     * @param nums
     * @param target
     * @return
     */
    public static int[] searchRange(int[] nums, int target) {
        int[] result = new int[]{-1, -1};
        int leftIndex = extremeInsertionIndex(nums, target, true);
        if (leftIndex == nums.length || nums[leftIndex] != target) {
            return result;
        }
        result[0] = leftIndex;
        result[1] = extremeInsertionIndex(nums, target, false) - 1;
        return result;
    }

    public static int extremeInsertionIndex(int[] nums, int target, boolean left) {
        int start = 0;
        int end = nums.length;
        while (start < end) {
            int mid = (start + end) / 2;
            if (nums[mid] > target || (left && nums[mid] == target)) {      //target小于nums[mid]，或者需要查找最左侧索引且target==nums[mid]
                end = mid;
            } else {
                start = mid + 1;
            }
        }
        return start;
    }

    /**
     * 原本有序的数组被从中切分为两部分并交换位置，从转换后的数组中查找target值所在的索引位置
     *
     * @param nums
     * @param target
     * @return
     */
    public static int search(int[] nums, int target) {
        int start = 0;
        int end = nums.length - 1;
        int mid;
        while (start <= end) {
            mid = (start + end + 1) / 2;
            if (nums[mid] == target) {
                return mid;
            }
            if (nums[start] < nums[mid]) {      //[start, mid]有序
                if (target >= nums[start] && target <= nums[mid]) {
                    end = mid - 1;
                } else {
                    start = mid + 1;
                }
            } else {            //[mid, end]有序
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
     * 找出最长合法括号的字符串长度
     *
     * @param s
     * @return
     */
    public static int longestValidParentheses(String s) {
        int max = 0;
        int count = 0;
        int validLen = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '(') {
                count++;
                validLen++;
            } else {
                if (count <= 0) {
                    validLen = 0;
                } else {
                    count--;
                    validLen++;
                    if (count == 0) {
                        max = Math.max(validLen, max);
                    }
                }
            }
        }
        count = 0;
        validLen = 0;
        for (int i = s.length() - 1; i > 0; i--) {
            char c = s.charAt(i);
            if (c == ')') {
                count++;
                validLen++;
            } else {
                if (count <= 0) {
                    validLen = 0;
                } else {
                    count--;
                    validLen++;
                    if (count == 0) {
                        max = Math.max(max, validLen);
                    }
                }
            }
        }
        return max;
    }


    public static void reverse(int[] arr) {
        int l = 0;
        int r = arr.length - 1;
        while (l < r) {
            swap(arr, l, r);
            l++;
            r--;
        }
    }

    public static void swap(int[] arr, int l, int r) {
        int tmp = arr[l];
        arr[l] = arr[r];
        arr[r] = tmp;
    }

    /**
     * 按照给定数字反转链表
     * Given this linked list: 1->2->3->4->5
     * <p>
     * For k = 2, you should return: 2->1->4->3->5
     * <p>
     * For k = 3, you should return: 3->2->1->4->5
     *
     * @param head
     * @param k
     * @return
     */
    public static ListNode reverseKGroup(ListNode head, int k) {
        ListNode tmp = head;
        for (int i = 1; i < k; i++) {
            if (tmp == null) {
                return head;
            }
            tmp = tmp.next;
        }
        ListNode t2 = tmp.next; //保存之后的元素
        tmp.next = null;        //将链表切分
        ListNode result = reverseNode(head);
        head.next = reverseKGroup(t2, k);       //将切分的链表和反转后的链表段连接起来
        return result;
    }

    public static ListNode reverseNode(ListNode head) {
        ListNode pre = null;
        ListNode next = null;
        while (head != null) {
            next = head.next;       //指向head后一位
            head.next = pre;        //将当前头节点的下一个节点指向前一个节点，这地方实现了反转
            pre = head;             //保存当前头节点为前一个节点
            head = next;            //当前头结点指向原始的下一个节点
        }
        return pre;
    }

    /**
     * 按照顺序调换每两位链表元素的位置
     *
     * @param head
     * @return
     */
    public static ListNode swapPairs(ListNode head) {
        if (head == null) return null;
        ListNode pre = head.next;
        ListNode hind = new ListNode(0);
        hind.next = head;
        while (pre != null) {
            hind.next.next = pre.next;
            pre.next = hind.next;
            if (hind.next == head) head = pre;
            hind.next = pre;
            if (pre.next.next != null) {
                if (pre.next.next.next != null) {
                    hind = pre.next;
                    pre = pre.next.next.next;
                } else pre = null;
            } else pre = null;
        }
        return head;
    }

    /**
     * 生成有效括号对，只包含'(' ')'
     *
     * @param n
     * @return
     */
    public static List<String> generateParenthesis(int n) {
        List<String> result = new ArrayList<>();
        generate2(result, n, 0, 0, "");
        return result;
    }

    private static void generate2(List<String> list, int n, int l, int r, String str) {
        if (r == n) {
            list.add(str);
            return;
        }
        if (l < n) {
            generate2(list, n, l + 1, r, str + "(");
        }
        if (r < l) {
            generate2(list, n, l, r + 1, str + ")");
        }
    }


    /**
     * 合并两个有序单向链表为一个单向链表
     *
     * @param l1
     * @param l2
     * @return
     */
    public static ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        ListNode root = new ListNode(0);
        ListNode second = root;
        while (l1 != null && l2 != null) {
            int val;
            if (l1.val > l2.val) {
                val = l2.val;
                l2 = l2.next;
            } else {
                val = l1.val;
                l1 = l1.next;
            }
            second.next = new ListNode(val);
            second = second.next;
        }
        if (l1 != null) {
            second.next = l1;
        } else if (l2 != null) {
            second.next = l2;
        }
        return root.next;
    }

    /**
     * 判断给定字符串s是否为有效括号组合
     *
     * @param s
     * @return
     */
    public static boolean isValid(String s) {
        Map<Character, Character> map = new HashMap<>();
        map.put('(', ')');
        map.put('[', ']');
        map.put('{', '}');
        Stack<Character> stack = new Stack<>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '{' || c == '(' || c == '[') {
                stack.push(c);
            } else {
                if (stack.isEmpty()) {
                    return false;
                }
                Character lastChar = stack.pop();
                if (map.get(lastChar) != c) {
                    return false;
                }
            }
        }
        return stack.isEmpty();
    }

    /**
     * 删除单向链表中倒数第n位元素
     *
     * @param head
     * @param n
     * @return
     */
    public static ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode d = new ListNode(0);
        d.next = head;
        ListNode first = d;
        ListNode second = d;
        for (int i = 0; i < n + 1; i++) {
            first = first.next;
        }
        while (first != null) {
            first = first.next;
            second = second.next;
        }
        second.next = second.next.next;
        return d.next;
    }

    /**
     * 递归输出所有数字对应的可能组合
     * Input: "23"
     * Output: ["ad", "ae", "af", "bd", "be", "bf", "cd", "ce", "cf"].
     * <p>
     * [1]
     * [2] a, b, c
     * [3] d, e, f
     * [4] g, h, i
     * [5] j, k, l
     * [6] m, n, o
     * [7] p, q, r, s
     * [8] t, u, v
     * [9] w, x, y, z
     *
     * @param digits
     * @return
     */
    public static char[][] chars = new char[][]{
            {},
            {},
            {'a', 'b', 'c'},
            {'d', 'e', 'f'},
            {'g', 'h', 'i'},
            {'j', 'k', 'l'},
            {'m', 'n', 'o'},
            {'p', 'q', 'r', 's'},
            {'t', 'u', 'v'},
            {'w', 'x', 'y', 'z'},
    };

    public static List<String> letterCombinations(String digits) {
        List<String> result = new ArrayList<>();
        generate(result, "", digits, 0);
        return result;
    }

    public static void generate(List<String> result, String str, String digits, int index) {
        if (str.length() == digits.length()) {
            result.add(str);
            return;
        }
        char[] cs = chars[digits.charAt(index) - '0'];
        for (int i = 0; i < cs.length; i++) {
            str += cs[i];
            generate(result, str, digits, index + 1);
            str = str.substring(0, str.length() - 1);
        }
    }

    /**
     * 从数组中找出三个数，使其和最接近target
     * Given array nums = [-1, 2, 1, -4], and target = 1.
     * <p>
     * The sum that is closest to the target is 2. (-1 + 2 + 1 = 2).
     *
     * @param nums
     * @param target
     * @return
     */
    public static int threeSumClosest(int[] nums, int target) {
        Arrays.sort(nums);
        int closetNum = nums[0] + nums[1] + nums[2];
        for (int i = 0; i < nums.length; i++) {
            if (i == 0 || nums[i] != nums[i - 1]) {
                int leftNum = target - nums[i];
                int start = i + 1;
                int end = nums.length - 1;
                while (start < end) {
                    int sum = nums[start] + nums[end];
                    if (sum == leftNum) {
                        return target;
                    }
                    int total = nums[i] + sum;
                    if (Math.abs(target - total) < Math.abs(target - closetNum)) {
                        closetNum = total;
                    }
                    if (sum < leftNum) {
                        if (target - total < 0 && nums[start] > 0) {
                            break;
                        }
                        start++;
                    } else {
                        if (target - total > 0 && nums[end] < 0) {
                            break;
                        }
                        end--;
                    }
                }
            }
        }
        return closetNum;
    }

    /**
     * 从数组nums中找出三个数字组合，其和等于0
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
    public static List<List<Integer>> threeSum(int[] nums) {
        Arrays.sort(nums);
        List<List<Integer>> result = new ArrayList<>();
        for (int i = 0; i < nums.length; i++) {
            int left = 0 - nums[i];
            int start = i + 1;
            int end = nums.length - 1;
            while (start < end) {
                int num1 = nums[start];
                int num2 = nums[end];
                int sum = num1 + num2;
                if (sum == left) {
                    result.add(Arrays.asList(nums[i], nums[start], nums[end]));
                    do {
                        start++;
                    } while (start < end && nums[start] == num1);
                    do {
                        end--;
                    } while (start < end && nums[end] == num2);
                } else if (sum > left) {
                    end--;
                } else {
                    start++;
                }
            }
            while (i < nums.length - 2 && nums[i] == nums[i + 1]) {
                i++;
            }
        }
        return result;
    }

}
