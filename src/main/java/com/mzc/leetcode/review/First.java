package com.mzc.leetcode.review;

import com.mzc.leetcode.NumSum;
import com.mzc.leetcode.inst.ListNode;

import java.sql.SQLOutput;
import java.util.*;
import java.util.Map.Entry;

/**
 * create by zhencai.ma on 2019/12/2
 */
public class First {
    /**
     * 给定一个数组及目标数，在数组中找到两个数使其和等于target，返回这两个数的index
     */
    public int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int left = target - nums[i];
            if (map.containsKey(left)) {
                return new int[]{map.get(left), i};
            }
            map.put(nums[i], i);
        }
        return new int[]{-1, -1};
    }

    /**
     * 给定两个非空单向链表，返回其和
     * Input: (2 -> 4 -> 3) + (5 -> 6 -> 4)
     * Output: 7 -> 0 -> 8
     * Explanation: 342 + 465 = 807.
     */
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        int extra = 0;
        int val;
        ListNode root = new ListNode(0);
        ListNode tmp = root;
        while (extra > 0 || l1 != null || l2 != null) {
            val = extra;
            if (l1 != null) {
                val += l1.val;
                l1 = l1.next;
            }
            if (l2 != null) {
                val += l2.val;
                l2 = l2.next;
            }
            extra = val / 10;
            val = val % 10;
            tmp.next = new ListNode(val);
            tmp = tmp.next;
        }
        return root.next;
    }

    /**
     * 找出s中最长不包含重复子串的长度
     * Input: "abcabcbb"
     * Output: 3
     * Explanation: The answer is "abc", with the length of 3.
     */
    public int lengthOfLongestSubstring(String s) {
        int[] oldIndexs = new int[128];
        int ans = 0;
        for (int i = 0, j = 0; i < s.length(); i++) {
            j = Math.max(j, oldIndexs[s.charAt(i)]);
            ans = Math.max(ans, i - j + 1);
            oldIndexs[s.charAt(i)] = i + 1;
        }
        return ans;
    }

    /**
     * 求出两个有序数组的中位数
     * nums1 = [1, 2]
     * nums2 = [3, 4]
     * <p>
     * The median is (2 + 3)/2 = 2.5
     */
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        //N=nums1, M= nums2, n=N.length, m = M.length
        //N分为 {0, i-1}, {i, n-1}   M分为  {0, j-1} {j, m-1}    需要满足 (i-1)+(j-1) = (n-1-i) + (m-1-j) --> i = (n+m)/2 - j  需要保证n>=m，否则i可能会出现负值
        //满足以上条件后，就可以确定 (maxleft + minright)/2 就是中位数，  当然，前提条件是左边最大小于右边最小，因为本身M，N有序，所以只需要判断 M[j-1]<=N[i] && N[i-1]<=M[j]
        int[] M = nums1;
        int[] N = nums2;
        int m = M.length;
        int n = N.length;
        if (m > n) {
            int[] tmp = M;
            M = N;
            N = tmp;
            int tmpL = m;
            m = n;
            n = tmpL;
        }
        int c = (n + m + 1) / 2;
        int start = 0, end = m;
        while (start < end) {
            int j = (start + end) / 2;
            int i = c - j;
//            if (i > start && N[i - 1] > M[j]) {        //j太小
//                end =  - 1;
//            }
            /*if (i < iMax && A[i] < B[j - 1]) {          //i 太大
                iMin = i + 1;
            } else if (i > iMin && A[i - 1] > B[j]) {   //i 太小
                iMax = i - 1;
            } else {
                int maxLeft = 0;
                if (i == 0) {
                    maxLeft = B[j - 1];
                } else if (j == 0) {
                    maxLeft = A[i - 1];
                } else {
                    maxLeft = Math.max(A[i - 1], B[j - 1]);
                }
                if ((m + n) % 2 == 1) {
                    System.out.println(maxLeft);
                    return maxLeft;
                }
                int minRight = 0;
                if (i == m) {
                    minRight = B[j];
                } else if (j == n) {
                    minRight = A[i];
                } else {
                    minRight = Math.min(A[i], B[j]);
                }
                System.out.printf("[minRight: %d] [maxLeft:%d]\n", minRight, maxLeft);
                return (minRight + maxLeft) / 2d;
            }*/
        }
        return 0;
    }

    /**
     * 求最长回文字符串
     * Input: "babbad"
     * Output: "bab"
     * Note: "aba" is also a valid answer.
     */
    public static String longestPalindrome(String s) {
        int start = 0;
        int end = 0;
        for (int i = 0; i < s.length(); i++) {
//            if (i> 0 && s.charAt(i) == s.charAt(i-1)) {       //不能进行去重，因为可能输入的字符串会是 "ccc" 这种
//                continue;
//            }
            int len1 = longestP(s, i, i);
            int len2 = longestP(s, i, i + 1);
            int len = Math.max(len1, len2);
            if (end - start < len) {
                start = i - (len - 1) / 2;
                end = i + len / 2;
            }
        }
        return s.substring(start, end + 1);
    }

    public static int longestP(String s, int l, int r) {
        int L = l;
        int R = r;
        while (L >= 0 && R < s.length() && s.charAt(L) == s.charAt(R)) {
            L--;
            R++;
        }
        return R - L - 1;
    }

    /**
     * 反转数字
     * Input: 123
     * Output: 321
     */
    public int reverse(int x) {
        long tmp = 0;
        boolean nagetive = x < 0;
        while (x > 0) {
            int t = x % 10;
            x = x / 10;
            tmp += t * 10;
        }
        if (tmp > Integer.MAX_VALUE) {
            return 0;
        }
        return (int) (nagetive ? -tmp : tmp);
    }

    /**
     * 给定数组，其中每个元素代表其高，求最大蓄水量
     */
    public int maxArea(int[] height) {
        int max = 0;
        int start = 0;
        int end = height.length - 1;
        while (start < end) {
            int min = Math.min(height[start], height[end]);
            max = Math.max(max, (end - start) * min);
            if (height[start] < height[end]) {
                do {
                    start++;
                } while (start < end && height[start] <= min);
            } else {
                do {
                    end--;
                } while (start < end && height[end] <= min);
            }
        }
        return max;
    }

    /**
     * 给定数组nums，从其中找出所有唯一的三个元素组合，使其和等于0
     * Given array nums = [-1, 0, 1, 2, -1, -4],
     * <p>
     * A solution set is:
     * [
     * [-1, 0, 1],
     * [-1, -1, 2]
     * ]
     */
    public List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        Arrays.sort(nums);
        int len = nums.length;
        for (int i = 0; i < len; i++) {
            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }
            int left = 0 - nums[i];
            int start = i + 1;
            int end = len - 1;
            if (left >= 0 && nums[end] < 0) {
                break;
            }
            if (left <= 0 && nums[start] > 0) {
                break;
            }
            while (start < end) {
                if (nums[start] + nums[end] < left) {
                    start++;
                } else if (nums[start] + nums[end] > left) {
                    end--;
                } else {
                    result.add(new ArrayList<>(Arrays.asList(nums[i], nums[start], nums[end])));
                    do {
                        start++;
                    } while (start < end && nums[start] == nums[start - 1]);
                    do {
                        end--;
                    } while (start < end && nums[end] == nums[end + 1]);
                }
            }
        }
        return result;
    }

    /**
     * 给定数组nums和目标值target，从数组中找到三个数组合使其和最接近target，并返回三数的和
     * Given array nums = [-1, 2, 1, -4], and target = 1.
     * <p>
     * The sum that is closest to the target is 2. (-1 + 2 + 1 = 2).
     */
    public static int threeSumClosest(int[] nums, int target) {
        Arrays.sort(nums);
        int closetNum = nums[0] + nums[1] + nums[2];
        int len = nums.length;
        for (int i = 0; i < len; i++) {
            int start = i + 1;
            int end = len - 1;
            while (start < end) {
                int total = nums[i] + nums[start] + nums[end];
                if (total == target) {
                    return target;
                }
                if (Math.abs(target - total) < Math.abs(target - closetNum)) {
                    closetNum = total;
                    if (target - total > 0) {
                        start++;
                    } else {
                        end--;
                    }
                } else if (total > target) {
                    end--;
                } else {
                    start++;
                }
            }
        }
        return closetNum;
    }

    /**
     * 根据输入的电话号码返回其可能的所有字母组合
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
        if (digits.isEmpty()) {
            return new ArrayList<>();
        }
        List<String> result = new ArrayList<>();
        letterGenerate(result, digits, 0, "");
        return result;
    }

    public static void letterGenerate(List<String> result, String digits, int index, String str) {
        if (str.length() == digits.length()) {
            result.add(str);
            return;
        }
        String[] tmp = a[digits.charAt(index) - '0'];
        int nextIndex = index + 1;
        for (int i = 0; i < tmp.length; i++) {
            str += tmp[i];
            letterGenerate(result, digits, nextIndex, str);
            str = str.substring(0, str.length() - 1);
        }
    }

    /**
     * 删除单向链表中倒数第n个元素
     * Given linked list: 1->2->3->4->5, and n = 2.
     * <p>
     * After removing the second node from the end, the linked list becomes 1->2->3->5.
     */
    public ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode f = dummy;
        ListNode s = dummy;
        for (int i = 0; i < n + 1; i++) {
            f = f.next;
        }
        while (f != null) {
            f = f.next;
            s = s.next;
        }
        s.next = s.next.next;
        return dummy.next;
    }

    /**
     * 给定字符串s，只包含 "(" ")" "[" "]" "{" "}",判定括号的合法性
     */
    public boolean isValid(String s) {
        Map<Character, Character> map = new HashMap<>();
        map.put('(', ')');
        map.put('[', ']');
        map.put('{', '}');
        Stack<Character> stack = new Stack<>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '(' || c == '[' || c == '{') {
                stack.push(c);
            } else {
                if (stack.isEmpty()) {
                    return false;
                }
                if (c != map.get(stack.pop())) {
                    return false;
                }
            }
        }
        return stack.isEmpty();
    }

    /**
     * 合并两个有序链表为一个有序链表
     * Input: 1->2->4, 1->3->4
     * Output: 1->1->2->3->4->4
     */
    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        ListNode result = new ListNode(0);
        ListNode node = result;
        while (l1 != null && l2 != null) {
            int val;
            if (l1.val > l2.val) {
                val = l2.val;
                l2 = l2.next;
            } else {
                val = l1.val;
                l1 = l1.next;
            }
            node.next = new ListNode(val);
            node = node.next;
        }
        if (l1 != null) {
            node.next = l1;
        }
        if (l2 != null) {
            node.next = l2;
        }
        return result.next;
    }

    /**
     * 给定数字n，生成对应的合法括号，字符串中只包含 '('和')'
     * n = 3
     * [
     * "((()))",
     * "(()())",
     * "(())()",
     * "()(())",
     * "()()()"
     * ]
     */
    public List<String> generateParenthesis(int n) {
        List<String> result = new ArrayList<>();
        generateParenthesis(result, "", n, 0, 0);
        return result;
    }

    public static void generateParenthesis(List<String> result, String str, int n, int l, int r) {
        if (r == n) {
            result.add(str);
            return;
        }
        if (l < n) {
            generateParenthesis(result, str + "(", n, l + 1, r);
        }
        if (r < l) {
            generateParenthesis(result, str + ")", n, l, r + 1);
        }
    }

    /**
     * 在不改变链表元素值只交换ListNode的前提下两两交换链表元素
     * Given 1->2->3->4, you should return the list as 2->1->4->3.
     */
    public static ListNode swapPairs(ListNode head) {
        ListNode pre = head.next;
        ListNode hind = new ListNode(0);        //等价于每次hind设置为pre的前一位
        hind.next = head;
        while (pre != null) {
            hind.next.next = pre.next;
            pre.next = hind.next;       //此时pre已经是经过一次交换的链表了
            if (pre.next == head) {
                head = pre;
            }
            hind.next = pre;            //等同于hind.next设置为交换过 一次的链表
            if (pre.next.next != null) {
                if (pre.next.next.next != null) {
                    hind = pre.next;        //
                    pre = pre.next.next.next;
                } else pre = null;
            } else pre = null;
        }
        return head;
    }

    /**
     * 给定单向链表和k，将链表的每k位进行反转
     * Given this linked list: 1->2->3->4->5
     * <p>
     * For k = 2, you should return: 2->1->4->3->5
     * <p>
     * For k = 3, you should return: 3->2->1->4->5
     */
    public static ListNode reverseKGroup(ListNode head, int k) {
        //核心解题思路是将链表分段，然后将不同段反转，再将段连接起来组成完整的链表
        ListNode tmp = head;
        for (int i = 1; i < k && tmp != null; i++) {
            tmp = tmp.next;
        }
        if (tmp == null) return head;
        ListNode t2 = tmp.next;
        tmp.next = null;
        ListNode result = reverse(head);
        head.next = reverseKGroup(t2, k);
        return result;
    }

    public static ListNode reverse(ListNode head) {
        ListNode pre = null;
        ListNode hind = head;
        while (hind != null) {
            ListNode tmp = hind.next;
            hind.next = pre;
            pre = hind;
            hind = tmp;
        }
        return pre;
    }

    /**
     * 把有序的序列作为已有序列，然后依次计算当前排列的下一个字典序列(如果序列为升序，将序列最大数放到第二位(找出字典的tail？)，如果为降序则变为升序)
     * ----对题目的理解不对
     * 1,2,3 → 1,3,2
     * 3,2,1 → 1,2,3
     * 1,1,5 → 1,5,1
     */
    public static void nextPermutation(int[] nums) {
        if (nums[0] > nums[nums.length - 1]) {
            reverseArray(nums);
        } else {
            for (int i = nums.length - 1; i > 1; i--) {
                swapArr(nums, i, i - 1);
            }
        }
    }

    public static void swapArr(int[] nums, int l, int r) {
        int tmp = nums[l];
        nums[l] = nums[r];
        nums[r] = tmp;
    }

    public static void reverseArray(int[] nums) {
        int l = 0;
        int r = nums.length - 1;
        while (l < r) {
            int tmp = nums[l];
            nums[l++] = nums[r];
            nums[r--] = tmp;
        }
    }

    /**
     * 给定字符串，只包含"("和")"，求出最长合法括号长度
     * Input: "(()"
     * Output: 2
     * Explanation: The longest valid parentheses substring is "()"
     */
    public static int longestValidParentheses(String s) {
        int cnt = 0;
        int total = 0;
        int maxLen = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(') {
                cnt++;
                total++;
            } else if (cnt > 0) {
                total++;
                if (--cnt == 0) {
                    maxLen = Math.max(maxLen, total);
                }
            } else {
                total = 0;
            }
        }
        cnt = 0;
        total = 0;
        for (int i = s.length() - 1; i >= 0; i--) {
            if (s.charAt(i) == ')') {
                cnt++;
                total++;
            } else if (cnt > 0) {
                total++;
                if (--cnt == 0) {
                    maxLen = Math.max(maxLen, total);
                }
            } else {
                total = 0;
            }
        }
        return maxLen;
    }

    /**
     * 在一个被打乱一次的无重复升序排序的数组中查找target，有则返回数组下标，没有返回-1
     * Input: nums = [4,5,6,7,0,1,2], target = 0
     * Output: 4
     */
    public static int search(int[] nums, int target) {
        int l = 0;
        int r = nums.length - 1;
        while (l <= r) {
            int mid = (l + r + 1) / 2;
            if (nums[mid] == target) {
                return mid;
            }
            if (nums[l] <= nums[mid]) {      //l->mid 升序
                if (nums[l] <= target && nums[mid] > target) {
                    r = mid - 1;
                } else {
                    l = mid + 1;
                }
            } else {        //mid -> r 升序
                if (nums[mid] < target && target <= nums[r]) {
                    l = mid + 1;
                } else {
                    r = mid - 1;
                }
            }
        }
        return -1;
    }

    /**
     * 给定升序排序的有序数组，找出target在数组中首次和最后一次出现的位置
     * Input: nums = [5,7,7,8,8,10], target = 8
     * Output: [3,4]
     */
    public static int[] searchRange(int[] nums, int target) {
        if (nums == null || nums.length == 0) {
            return new int[]{-1, -1};
        }

        int leftIndex = searchIndex(nums, target, true);
        if (leftIndex < 0 || leftIndex >= nums.length || nums[leftIndex] != target) {       //因为使用的是二分法查找，而且返回的是左侧数据，所以返回的可能不是实际匹配的索引
            return new int[]{-1, -1};
        }
        int[] result = new int[2];
        result[0] = leftIndex;
        if (result[0] >= 0) {
            result[1] = searchIndex(nums, target, false) - 1;
        } else {
            result[1] = -1;
        }
        return result;
    }

    public static int searchIndex(int[] nums, int target, boolean left) {
        int l = 0;
        int r = nums.length;
        while (l < r) {
            int mid = (l + r) / 2;
            if (nums[mid] > target || (left && nums[mid] == target)) {
                r = mid;
            } else {
                l = mid + 1;
            }
        }
        return l;
    }

    /**
     * 给定一个排序好的数组和一个数字，如果查找到返回在数组中的索引，否则返回其插入索引，且插入后数组还保持有序
     * Input: [1,3,5,6], 5
     * Output: 2
     */
    public static int searchInsert(int[] nums, int target) {
        //关键在于查找到以后对返回值的判定
        int l = 0;
        int r = nums.length;
        while (l < r) {
            int mid = (l + r) / 2;
            if (nums[mid] == target) {
                return mid;
            }
            if (nums[mid] > target) {
                r = mid - 1;
            } else {
                l = mid + 1;
            }
        }
        if (l >= nums.length) {
            return nums.length;
        } else if (nums[l] < target) {
            return l + 1;
        }
        return l;
    }

    /**
     * 给定一组无重复候选数字及targer，从候选数字中找出所有组合使其和等于target
     * Input: candidates = [2,3,6,7], target = 7,
     * A solution set is:
     * [
     * [7],
     * [2,2,3]
     * ]
     */
    public static List<List<Integer>> combinationSum(int[] candidates, int target) {
        Arrays.sort(candidates);
        List<List<Integer>> result = new ArrayList<>();
        generateCombin(result, new ArrayList<>(), candidates, target, 0);
        return result;
    }

    public static void generateCombin(List<List<Integer>> result, List<Integer> list, int[] candidates, int target, int index) {
        if (target == 0) {
            result.add(new ArrayList<>(list));
        } else if (target < 0) {
            return;
        }
        for (int i = index; i < candidates.length; i++) {
            if (candidates[i] > target) {
                return;
            }
            list.add(candidates[i]);
            generateCombin(result, list, candidates, target - candidates[i], i);
            list.remove(list.size() - 1);
        }
    }

    /**
     * 给定一组候选数字以及一个目标值target，从候选数字中找出所有唯一的组合使其和等于target，候选数字中每个元素只能被使用一次
     * Input: candidates = [10,1,2,7,6,1,5], target = 8,
     * A solution set is:
     * [
     * [1, 7],
     * [1, 2, 5],
     * [2, 6],
     * [1, 1, 6]
     * ]
     */
    public static List<List<Integer>> combinationSum2(int[] candidates, int target) {
        Arrays.sort(candidates);
        List<List<Integer>> result = new ArrayList<>();
        generateCombin2(result, new ArrayList<>(), candidates, target, 0);
        return result;
    }

    public static void generateCombin2(List<List<Integer>> result, List<Integer> list, int[] candidates, int target, int index) {
        if (target == 0) {
            result.add(new ArrayList<>(list));
        } else if (target < 0) {
            return;
        }
        for (int i = index; i < candidates.length; i++) {
            if (candidates[i] > target) {
                break;
            }
            list.add(candidates[i]);
            generateCombin2(result, list, candidates, (target - candidates[i]), i + 1);
            list.remove(list.size() - 1);
            while (i + 1 < candidates.length && candidates[i] == candidates[i + 1]) {
                i++;
            }
        }
    }

    /**
     * 给定一个数组，找出其中缺少的最小正整数
     * Input: [1,2,0]
     * Output: 3
     */
    public static int firstMissingPositive(int[] nums) {
        int i = 0;
        while (i < nums.length) {
            while (i < nums.length && (nums[i] > nums.length || nums[i] < 1)) {
                i++;
            }
            if (i < nums.length) {
                if ((i + 1) != nums[i] && nums[nums[i] - 1] != nums[i]) {
                    swapArr(nums, nums[i] - 1, i);
                } else {
                    i++;
                }
            }
        }
        for (int j = 0; j < nums.length; j++) {
            if ((j + 1) != nums[j]) {
                return j + 1;
            }
        }
        if (nums.length == 0 || nums[0] != 1) {
            return 1;
        }
        return nums.length + 1;
    }

    /**
     * 给定非负数组，其中每个元素代表其高，求蓄水能力
     * Input: [0,1,0,2,1,0,1,3,2,1,2,1]
     * Output: 6
     */
    public static int trap(int[] height) {
        //首先分别找到左右两侧可以储水的起始位置(左侧就是比前一位小的点，右侧是则是从右向左比前一位大的点)
        //如果左侧储水边界大于右侧，则从右侧开始计算储水量，因为此时右侧的边就决定了储水上限，知道遇到比原右侧边大的边跳出再重新设置右侧边为新的点，继续比较左右边界大小，重复直到计算出总的储水量
        // TODO: 2019/12/3
        return 0;
    }

    /**
     * 给定一个数组，其中每个元素代表可以行走的最大距离，求最少使用几步可以走到数组尾
     * Input: [2,3,1,1,4]
     * Output: 2
     * Explanation: The minimum number of jumps to reach the last index is 2.
     * Jump 1 step from index 0 to 1, then 3 steps to the last index.
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
     * 给定一组不重复的数字，求其所有可能的排列组合
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
     */
    public static List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        generatePremute(result, nums, 0);
        return result;
    }

    public static void generatePremute(List<List<Integer>> result, int[] nums, int index) {
        if (index == nums.length) {
            List<Integer> list = new ArrayList<>();
            for (int n : nums) {
                list.add(n);
            }
            result.add(list);
            return;
        }
        for (int i = index; i < nums.length; i++) {
            swapArr(nums, i, index);
            generatePremute(result, nums, index + 1);
            swapArr(nums, i, index);
        }
    }

    /**
     * 给定可能有重复元素的数组，统计出其中所有不重复的组合
     * Input: [1,1,2]
     * Output:
     * [
     * [1,1,2],
     * [1,2,1],
     * [2,1,1]
     * ]
     */
    public static List<List<Integer>> permuteUnique(int[] nums) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int n : nums) {
            map.compute(n, (key, oldValue) -> oldValue == null ? 1 : oldValue + 1);
        }
        int[] cnts = new int[map.size()];
        int[] tmpNums = new int[map.size()];
        int index = 0;
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            cnts[index] = entry.getValue();
            tmpNums[index++] = entry.getKey();
        }
        List<List<Integer>> result = new ArrayList<>();
        generatePermute(result, new ArrayList<>(), tmpNums, cnts, nums.length);
        return result;
    }

    public static void generatePermute(List<List<Integer>> result, List<Integer> list, int[] tmpNums, int[] cnts, int n) {
        if (list.size() == n) {
            result.add(new ArrayList<>(list));
        } else {
            for (int i = 0; i < tmpNums.length; i++) {
                if (cnts[i] <= 0) {
                    continue;
                }
                cnts[i]--;
                list.add(tmpNums[i]);
                generatePermute(result, list, tmpNums, cnts, n);
                list.remove(list.size() - 1);
                cnts[i]++;
            }
        }
    }

    /**
     * 对给定字符串数组进行归类
     * Input: ["eat", "tea", "tan", "ate", "nat", "bat"],
     * Output:
     * [
     *   ["ate","eat","tea"],
     *   ["nat","tan"],
     *   ["bat"]
     * ]
     */
    public List<List<String>> groupAnagrams(String[] strs) {
        //核心点在于统计出字符串规律，可以使用int[128]数组，循环字符串中的所有char，进行计数，之后利用数组构建key，因此关键性能区分在于key的设计
        //目前见到的最高效的算法是利用素数的乘法构建唯一key，int[26]数组中使用不同是素数， key*=arr[s.charAt[i]-'a']
        return null;
    }

    /**
     * 给定一组数字，找到其子数组，使其和最大，并返回最大和
     * Input: [-2,1,-3,4,-1,2,1,-5,4],
     * Output: 6
     * Explanation: [4,-1,2,1] has the largest sum = 6.
     */
    public static int maxSubArray(int[] nums) {
        if (nums.length == 1) {
            return nums[0];
        }
        int start = 0;
        int maxSum = nums[0];
        while (start < nums.length) {
            if (nums[start] > 0) {
                break;
            }
            maxSum = Math.max(maxSum, nums[start]);
            start++;
        }
        int tmp = 0;
        for (int i = start; i < nums.length; i++) {
            tmp = Math.max(0, tmp+nums[i]);
            maxSum = Math.max(tmp, maxSum);
        }
        return maxSum;
    }

    /**
     * 给定数组，其中每个元素代表可行走的最大步数，判断是否可以走到队尾
     */
    public boolean canJump(int[] nums) {
        int position = nums.length;
        for (int i = nums.length-1; i >= 0; i--) {
            if (nums[i] + i >= position) {
                position = i;
            }
        }
        return position==0;
    }

    /**
     * 给定数组，其中每组元素代表区间，合并重合区间
     * Input: [[1,3],[2,6],[8,10],[15,18]]
     * Output: [[1,6],[8,10],[15,18]]
     * Explanation: Since intervals [1,3] and [2,6] overlaps, merge them into [1,6].
     */
    public static int[][] merge(int[][] intervals) {
        int[] tmp = new int[intervals.length*2];
        Arrays.sort(intervals, (a1, a2)->a1[0]-a2[0]);
        int cnt = 0;
        for (int i = 0; i < intervals.length; i++) {
            if (cnt == 0 || intervals[i][0] > tmp[cnt-1]) {
                tmp[cnt++] = intervals[i][0];
                tmp[cnt++] = intervals[i][1];
            } else {
                tmp[cnt-1] = Math.max(intervals[i][1], tmp[cnt-1]);
            }
        }
        int[][] result = new int[cnt/2][2];
        for (int i = 0; i < result.length; i++) {
            result[i][0] = tmp[i*2];
            result[i][1] = tmp[i*2+1];
        }
        return result;
    }

    /**
     * 给定按照区间起始位置有序的数组以及一个新的区间范围，将其插入到原区间数组中并合并
     */
    public int[][] insert(int[][] intervals, int[] newInterval) {

    }

    public static void main(String[] args) {
        merge(new int[][]{{1,3}, {2,6}, {8,10}, {15,18}});
    }
}
