package com.mzc.leetcode.review;

import com.mzc.leetcode.NumSum;
import com.mzc.leetcode.inst.ListNode;
import com.mzc.leetcode.inst.TreeNode;
import com.sun.xml.internal.bind.v2.TODO;

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
     * 给定一组无重复候选数字及target，从候选数字中找出所有组合使其和等于target
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
     * ["ate","eat","tea"],
     * ["nat","tan"],
     * ["bat"]
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
            tmp = Math.max(0, tmp + nums[i]);
            maxSum = Math.max(tmp, maxSum);
        }
        return maxSum;
    }

    /**
     * 给定数组，其中每个元素代表可行走的最大步数，判断是否可以走到队尾
     */
    public boolean canJump(int[] nums) {
        int position = nums.length;
        for (int i = nums.length - 1; i >= 0; i--) {
            if (nums[i] + i >= position) {
                position = i;
            }
        }
        return position == 0;
    }

    /**
     * 给定数组，其中每组元素代表区间，合并重合区间
     * Input: [[1,3],[2,6],[8,10],[15,18]]
     * Output: [[1,6],[8,10],[15,18]]
     * Explanation: Since intervals [1,3] and [2,6] overlaps, merge them into [1,6].
     */
    public static int[][] merge(int[][] intervals) {
        int[] tmp = new int[intervals.length * 2];
        Arrays.sort(intervals, (a1, a2) -> a1[0] - a2[0]);
        int cnt = 0;
        for (int i = 0; i < intervals.length; i++) {
            if (cnt == 0 || intervals[i][0] > tmp[cnt - 1]) {
                tmp[cnt++] = intervals[i][0];
                tmp[cnt++] = intervals[i][1];
            } else {
                tmp[cnt - 1] = Math.max(intervals[i][1], tmp[cnt - 1]);
            }
        }
        int[][] result = new int[cnt / 2][2];
        for (int i = 0; i < result.length; i++) {
            result[i][0] = tmp[i * 2];
            result[i][1] = tmp[i * 2 + 1];
        }
        return result;
    }

    /**
     * 给定数字n以及索引k，返回[1,n]n个数字唯一组合出来的串按照升序排序后第k位字符串
     * "123"
     * "132"
     * "213"
     * "231"
     * "312"
     * "321"
     * 给定 n=3，k=3  返回 "213"
     */
    public static String getPermutation(int n, int k) {
        //关键在于规律，对于n位数字组合升序，确定高位以后，低位总共有factorial(k-1)种组合
        if (n == 1) {
            return String.valueOf(n);
        } else if (n == 0) {
            return new String();
        }
        List<Integer> list = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            list.add(i);
        }
        String result = "";
        k--;        //因为程序从0开始计数？ 所以第k位等价于k-1位
        for (int i = 0; i < n - 2; i++) {
            int f = factorial(n - i - 1);
            int div = k / f;
            k = k % f;
            result += list.remove(div);
        }
        result += list.remove(k % 2);
        result += list.remove(0);
        return result;
    }

    public static int factorial(int n) {
        int result = n;
        for (int i = n - 1; i > 0; i--) {
            result *= i;
        }
        return result;
    }

    /**
     * 转移单向链表k此
     * Input: 1->2->3->4->5->NULL, k = 2
     * Output: 4->5->1->2->3->NULL
     * Explanation:
     * rotate 1 steps to the right: 5->1->2->3->4->NULL
     * rotate 2 steps to the right: 4->5->1->2->3->NULL
     */
    public ListNode rotateRight(ListNode head, int k) {
        //此题关键点在于确定链表移动的有效步数，正序(多轮转换属于无效操作)，然后找到节点，将最后的末尾节点的next指向原头节点，将第step位节点的next设置为null
        int len = 0;
        ListNode f = head;
        ListNode last = null;
        while (f != null) {
            last = f;
            f = f.next;
            len++;
        }
        if (len == 0) {
            return head;
        }
        if (k % len == 0) {
            return head;
        }
        int step = len - k % len - 1;
        f = head;
        while (--step >= 0) {
            f = f.next;
        }
        ListNode res = f.next;      //新的头部节点
        f.next = null;
        last.next = head;
        return res;
    }

    /**
     * 给定m*n大小的地图，假设一个机器人从地图左上角要走到右下角，每次只能向前或者向下移动，求总共有多少种唯一的走法
     * Input: m = 3, n = 2
     * Output: 3
     */
    public static int uniquePaths(int m, int n) {
        int[][] dp = new int[m][n];
        for (int i = 0; i < m; i++) {
            dp[i][0] = 1;
        }
        for (int i = 0; i < n; i++) {
            dp[0][i] = 1;
        }
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                dp[i][j] = dp[i][j - 1] + dp[i - 1][j];
            }
        }
        return dp[m - 1][n - 1];
    }

    /**
     * 给定机器人，每次只能向下或者向右行走，给定格子，其中1代表有阻挡，求机器人从左上角移动到右下角有多少种唯一走法
     * Input:
     * [
     * [0,0,0],
     * [0,1,0],
     * [0,0,0]
     * ]
     * Output: 2
     */
    public int uniquePathsWithObstacles(int[][] obstacleGrid) {
        int m = obstacleGrid.length;
        int n = obstacleGrid[0].length;
        int[][] dp = new int[m][n];
        for (int i = 0; i < m; i++) {
            if (i > 0) {
                dp[i][0] = obstacleGrid[i][0] == 1 ? 0 : dp[i - 1][0];
            } else {
                dp[i][0] = obstacleGrid[i][0] == 1 ? 0 : 1;
            }
        }
        for (int i = 0; i < n; i++) {
            if (i > 0) {
                dp[0][i] = obstacleGrid[0][i] == 1 ? 0 : dp[0][i - 1];
            } else {
                dp[0][i] = obstacleGrid[0][i] == 1 ? 0 : 1;
            }
        }
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                if (obstacleGrid[i][j] == 1) {
                    dp[i][j] = 0;
                } else {
                    dp[i][j] = dp[i - 1][j] + dp[i][j - 1];
                }
            }
        }
        return dp[m - 1][n - 1];
    }

    /**
     * 给定一个矩阵，求出从左上角走到右下角和最小的路径，并返回其和
     * Input:
     * [
     * [1,3,1],
     * [1,5,1],
     * [4,2,1]
     * ]
     * Output: 7
     */
    public int minPathSum(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        int[][] dp = new int[m][n];
        for (int i = 0; i < m; i++) {
            if (i > 0) {
                dp[i][0] = dp[i - 1][0] + grid[i][0];
            } else {
                dp[i][0] = grid[i][0];
            }
        }
        for (int i = 0; i < n; i++) {
            if (i > 0) {
                dp[0][i] = dp[0][i - 1] + grid[0][i];
            } else {
                dp[0][i] = grid[0][i];
            }
        }
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                dp[i][j] = Math.min(dp[i - 1][j], dp[i][j - 1]) + grid[i][j];
            }
        }
        return dp[m - 1][n - 1];
    }

    /**
     * 爬楼，每次只能走一个或者两个台阶，求走到n阶有多少种唯一的走法
     * Input: 2
     * Output: 2
     * Explanation: There are two ways to climb to the top.
     * 1. 1 step + 1 step
     * 2. 2 steps
     */
    public static int climbStairs(int n) {
        int[] dp = new int[n + 1];            //空间可以优化，因为只用到前两位数，没必要在存储那么多数据，所以需要3个int数就可以了
        dp[0] = 1;
        dp[1] = 1;
        for (int i = 2; i <= n; i++) {
            dp[i] = dp[i - 1] + dp[i - 2];
        }
        return dp[n];
    }

    /**
     * 给定两个字符串word1和word2，每次只能进行增删改操作，求最少步数将word1转换为word2
     * Input: word1 = "horse", word2 = "ros"
     * Output: 3
     * Explanation:
     * horse -> rorse (replace 'h' with 'r')
     * rorse -> rose (remove 'r')
     * rose -> ros (remove 'e')
     */
    public int minDistance(String word1, String word2) {
        int m = word1.length();
        int n = word2.length();
        int[][] dp = new int[m + 1][n + 1];
        for (int i = 1; i <= m; i++) {
            dp[i][0] = i;
        }
        for (int i = 0; i <= n; i++) {
            dp[0][i] = i;
        }
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = Math.min(dp[i - 1][j], Math.min(dp[i - 1][j - 1], dp[i][j - 1])) + 1;
                }
            }
        }
        return dp[m][n];
    }

    /**
     * 给定一个矩阵，每行或者列都是升序排序的，求target是否存在于矩阵中
     * Input:
     * matrix = [
     * [1,   3,  5,  7],
     * [10, 11, 16, 20],
     * [23, 30, 34, 50]
     * ]
     * target = 3
     * Output: true
     */
    public static boolean searchMatrix(int[][] matrix, int target) {
        if (matrix.length == 0) {
            return false;
        }
        int i = 0;
        int j = matrix[0].length - 1;
        while (i < matrix.length && j >= 0) {
            if (target == matrix[i][j]) {
                return true;
            } else if (target > matrix[i][j]) {
                i++;
            } else {
                j--;
            }
        }
        return false;
    }

    /**
     * 给定颜色数组，0,1,2分别代表红色、白色和蓝色  对其排序
     * Input: [2,0,2,1,1,0]
     * Output: [0,0,1,1,2,2]
     */
    public static void sortColors(int[] nums) {
        int redIndex = 0;
        int blueIndex = nums.length - 1;
        int start = 0;
        while (start < nums.length && start <= blueIndex) {
            if (nums[start] == 0) {
                swapArr(nums, start, redIndex);
                redIndex++;
                start++;
            } else if (nums[start] == 2) {
                swapArr(nums, start, blueIndex);
                blueIndex--;
            } else {
                start++;
            }
        }
    }

    /**
     * 给定字符串s和t，从s中找出最短的子串使其包含所有的t中的字符
     * Input: S = "ADOBECODEBANC", T = "ABC"
     * Output: "BANC"
     */
    public static String minWindow(String s, String t) {
        int[] cnt = new int[128];
        for (int i = 0; i < t.length(); i++) {
            cnt[t.charAt(i)]++;
        }
        int count = t.length();
        int first = 0;
        int second = 0;
        int left = -1;
        int right = -1;
        while (first < s.length()) {
            if (cnt[s.charAt(first++)]-- > 0) {
                count--;
            }
            while (count == 0) {
                if (left == -1 || first - second < right - left) {
                    left = second;
                    right = first;
                }
                if (cnt[s.charAt(second++)]++ == 0) {
                    count++;
                }
            }
        }
        if (left == -1) {
            return "";
        }
        return s.substring(left, right);
    }

    /**
     * 给定两个正整数n和k，从1...n中找出k个元素的所有不同组合
     * nput: n = 4, k = 2
     * Output:
     * [
     * [2,4],
     * [3,4],
     * [2,3],
     * [1,2],
     * [1,3],
     * [1,4],
     * ]
     */
    public static List<List<Integer>> combine(int n, int k) {
        List<List<Integer>> result = new ArrayList<>();
        geneCombine(result, new ArrayList<>(), n, k, 1);
        return result;
    }

    public static void geneCombine(List<List<Integer>> result, List<Integer> tmp, int n, int k, int start) {
        if (k == 0) {
            result.add(new ArrayList<>(tmp));
            return;
        }
        for (int i = start; i <= n - k + 1; i++) {
            tmp.add(i);
            geneCombine(result, tmp, n, k - 1, i + 1);
            tmp.remove(tmp.size() - 1);
        }
    }

    /**
     * 给定整数数组，找出其所有唯一的子串
     * Input: nums = [1,2,3]
     * Output:
     * [
     * [3],
     * [1],
     * [2],
     * [1,2,3],
     * [1,3],
     * [2,3],
     * [1,2],
     * []
     * ]
     */
    public static List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        result.add(new ArrayList<>());
        for (int num : nums) {
            List<List<Integer>> tmp = new ArrayList<>();
            for (List<Integer> l : result) {
                List<Integer> list = new ArrayList<>(l);
                list.add(num);
                tmp.add(list);
            }
            result.addAll(tmp);
        }
        return result;
    }

    /**
     * 给定二维字符数组，判断给定字符串是否可以在数组中查到，每个字符只能查找到其相邻的字符(上下左右)
     * board =
     * [
     * ['A','B','C','E'],
     * ['S','F','C','S'],
     * ['A','D','E','E']
     * ]
     * <p>
     * Given word = "ABCCED", return true.
     * Given word = "SEE", return true.
     * Given word = "ABCB", return false.
     */
    public static boolean[][] visited;
    public static int m;
    public static int n;

    public static boolean exist(char[][] board, String word) {
        m = board.length;
        n = board[0].length;
        visited = new boolean[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] == word.charAt(0)) {
                    if (exist2(board, word, 0, i, j)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean exist2(char[][] board, String word, int index, int i, int j) {
        if (index == word.length()) {
            return true;
        }
        if (i < 0 || i >= m) {
            return false;
        }
        if (j < 0 || j >= n) {
            return false;
        }
        if (visited[i][j] || word.charAt(index) != board[i][j]) {
            return false;
        }
        visited[i][j] = true;
        if (exist2(board, word, index + 1, i - 1, j) || exist2(board, word, index + 1, i, j - 1) ||
                exist2(board, word, index + 1, i + 1, j) || exist2(board, word, index + 1, i, j + 1)) {
            return true;
        }
        visited[i][j] = false;
        return false;
    }

    /**
     * 删除升序排序的数组中多余的重复元素，保证每个元素最多出现2次，并返回修改后的数组长度，
     * Given nums = [1,1,1,2,2,3],
     * <p>
     * Your function should return length = 5, with the first five elements of nums being 1, 1, 2, 2 and 3 respectively.
     * <p>
     * It doesn't matter what you leave beyond the returned length.
     */
    public int removeDuplicates(int[] nums) {
        if (nums == null || nums.length == 0) return 0;

        int j = 1, i = 2;
        while (i < nums.length) {
            if (nums[j] == nums[i] && nums[j - 1] == nums[i]) i++;
            else nums[++j] = nums[i++];
        }

        return j + 1;
    }

    /**
     * 在可能有重复元素的经过翻转的有序数组中判断target是否存在于数组中
     * Input: nums = [2,5,6,0,0,1,2], target = 0
     * Output: true
     */
    public boolean search33(int[] nums, int target) {
        if (nums.length == 0)
            return false;
        int start = 0;
        int end = nums.length - 1;
        while (start + 1 < end) {
            int mid = start + (end - start) / 2;
            if (nums[start] < nums[mid]) {
                if (target >= nums[start] && target <= nums[mid])
                    end = mid;
                else
                    start = mid;
            } else if (nums[start] > nums[mid]) {
                if (target >= nums[mid] && target <= nums[end])
                    start = mid;
                else
                    end = mid;
            } else {
                if (nums[start] == target)
                    return true;
                start++;
                // actually bad as O(n) in the case {search 5 in [1,1,1,5]}, no way to improve
            }
        }
        if (nums[start] == target || nums[end] == target)
            return true;
        return false;
    }

    // TODO: 2019/12/10
    public ListNode deleteDuplicates(ListNode head) {
        /* If head is null or there is only one node */
        if (head == null || head.next == null) {
            return head;
        }

        ListNode prev = null;
        ListNode curr = head;
        /*  Keep track of the value of node before curr node */
        int prevVal = (head.val == Integer.MIN_VALUE) ? Integer.MIN_VALUE + 1 : Integer.MIN_VALUE;

        while (curr != null) {
            ListNode tmp = curr.next;
            /*  If val of node ahead of curr is same as curr OR val of curr node is same as val of prevVal */
            if ((tmp != null && curr.val == tmp.val) || curr.val == prevVal) {
                prevVal = curr.val;
                curr.next = null;
                curr = tmp;
                /*  Head node deleted, need to move head to curr */
                if (prev == null) {
                    head = curr;
                } else {
                    prev.next = curr;
                }
                continue;
            }
            prev = curr;
            prevVal = curr.val;
            curr = curr.next;
        }
        return head;
    }

    /**
     * 删除链表中重复元素
     * Input: 1->1->2
     * Output: 1->2
     */
    public ListNode deleteDuplicates2(ListNode head) {
        ListNode root = new ListNode(0);
        root.next = head;
        ListNode pre = root;
        ListNode tmp = head;
        while (tmp != null) {
            while (tmp != null && tmp.next != null && tmp.val == tmp.next.val) {
                tmp = tmp.next;
            }
            pre.next = tmp;
            pre = pre.next;
            tmp = tmp.next;
        }
        return root.next;
    }

    /**
     * 给定矩阵，其中元素为"0"或者"1"，求由"1"组成的矩形的最大面积
     * Input:
     * [
     * ["1","0","1","0","0"],
     * ["1","0","1","1","1"],
     * ["1","1","1","1","1"],
     * ["1","0","0","1","0"]
     * ]
     * Output: 6
     */
    public int maximalRectangle(char[][] matrix) {
        if (matrix.length == 0) {
            return 0;
        }
        int m = matrix.length;
        int n = matrix[0].length;
        int maxRange = 0;
        int[][] dp = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (i == 0) {
                    dp[i][j] = matrix[i][j] == '1' ? 1 : 0;
                } else {
                    dp[i][j] = matrix[i][j] == '1' ? dp[i - 1][j] + 1 : 0;
                }
                int min = dp[i][j];
                for (int k = j; k >= 0; k--) {
                    if (min == 0) {
                        break;
                    }
                    min = Math.min(min, dp[i][k]);
                    maxRange = Math.max(maxRange, min * (j - k + 1));
                }
            }
        }
        return maxRange;
    }

    /**
     * 给定链表，和数字x，将所有小于x的元素都放大于等于x的元素之前
     * Input: head = 1->4->3->2->5->2, x = 3
     * Output: 1->2->2->4->3->5
     */
    public static ListNode partition(ListNode head, int x) {
        ListNode list1 = null, list2 = null, p = head, p1 = list1, p2 = list2;
        while (p != null) {
            int tmp = p.val;
            if (tmp < x) {
                if (list1 == null) {
                    list1 = p;
                    p1 = p;
                } else {
                    p1.next = p;
                    p1 = p1.next;
                }
            } else {
                if (list2 == null) {
                    list2 = p;
                    p2 = p;
                } else {
                    p2.next = p;
                    p2 = p2.next;
                }
            }
            p = p.next;
        }
        if (list1 != null) {
            p1.next = list2;
            if (p2 != null) {
                p2.next = null;
            }
            return list1;
        } else return list2;
    }

    /**
     * 给定升序排序的数组nums1和nums2，以及m和n，其中m和n代表nums1和nums2中的有效元素，合并两个数组为一个有序数组
     * 假定nums1中有足够的空间可以容纳所有nums2中的元素
     * Input:
     * nums1 = [1,2,3,0,0,0], m = 3
     * nums2 = [2,5,6],       n = 3
     * <p>
     * Output: [1,2,2,3,5,6]
     */
    public static void merge(int[] nums1, int m, int[] nums2, int n) {
        //解题思路：因为给定的nums1的长度刚好是nums1和nums2有效数组的总长度，而且两个数组都是有序的
        //所以可以直接从最大的数开始比较，也就是说nums1[nums1.length-1]和nums2[nums2.length-1]逆序
        //比较，将大的数放在末位(选出一个就需要末位减1)，复杂度为O(nums1.length)----等价于选择排序的方法解决问题
        int i = nums1.length - 1;
        m--;
        n--;
        while (i >= 0) {
            if (n < 0 || (m >= 0 && nums1[m] >= nums2[n])) {
                nums1[i--] = nums1[m--];
            } else {
                nums1[i--] = nums2[n--];
            }
        }
    }

    /**
     * 给定有重复数字的数组，求出所有的数字唯一组合
     * Input: [1,2,2]
     * Output:
     * [
     * [2],
     * [1],
     * [1,2,2],
     * [2,2],
     * [1,2],
     * []
     * ]
     */
    public static List<List<Integer>> subsetsWithDup(int[] nums) {
        Arrays.sort(nums);          //由于之后实现的逻辑是基于相同的元素都处于相邻的位置的，因此必须先对数组进行排序
        List<List<Integer>> result = new ArrayList<>();
        result.add(new ArrayList<>());
        List<List<Integer>> newAdd = null;
        for (int i = 0; i < nums.length; i++) {
            if (i > 0 && nums[i] == nums[i - 1]) {
                List<List<Integer>> t = new ArrayList<>(newAdd);
                newAdd = new ArrayList<>();
                for (List<Integer> l : t) {
                    List<Integer> tt = new ArrayList<>(l);
                    tt.add(nums[i]);
                    newAdd.add(tt);
                }
            } else {
                newAdd = new ArrayList<>();
                for (List<Integer> l : result) {
                    List<Integer> tmp = new ArrayList<>(l);
                    tmp.add(nums[i]);
                    newAdd.add(tmp);
                }
            }
            result.addAll(newAdd);
        }
        return result;
    }

    /**
     * 反转链表的第m到n位元素
     * Input: 1->2->3->4->5->NULL, m = 2, n = 4
     * Output: 1->4->3->2->5->NULL
     */
    public static ListNode reverseBetween(ListNode head, int m, int n) {
        ListNode list1 = null;
        int cnt = 0;
        ListNode temp = head;
        while (cnt < m - 1) {
            list1 = temp;       //需要在之前
            temp = temp.next;
            cnt++;
        }
        Stack<ListNode> stack = new Stack<>();
        while (cnt < n) {
            stack.push(temp);
            temp = temp.next;
            cnt++;
        }
        while (!stack.isEmpty()) {
            if (list1 != null) {
                list1.next = new ListNode(stack.pop().val);
            } else {
                list1 = head;
                list1.next = new ListNode(stack.pop().val);
                head = list1.next;
            }
            list1 = list1.next;
        }
        if (list1 != null) {
            list1.next = temp;
        }
        return head;
    }

    /**
     * 中序遍历(inorder traversal)，等价于遍历的顺序是 “左根右”
     * <p>
     * P：二叉树遍历： 先序(preorder travelsal)：根左右   后序(postorder travelsal)：左右根
     *
     * P：二叉搜索树：左子树上所有的节点都小于其根节点的数， 右子树上所有的节点都大于其根节点的数， 其所有子树也是二叉搜索树
     * Input: [1,null,2,3]
     * 1
     * \
     * 2
     * /
     * 3
     * <p>
     * Output: [1,3,2]
     */
    public static List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> list = new ArrayList<>();
        genInorderT(list, root);
        return list;
    }

    public static void genInorderT(List<Integer> list, TreeNode node) {
        if (node == null) {
            return;
        }
        genInorderT(list, node.left);
        list.add(node.val);
        genInorderT(list, node.right);
    }

    /**
     * 给定数字n，构建出所有由1...n组成的唯一可能的二叉查找树(BST's)
     * Input: 3
     * Output:
     * [
     * [1,null,3,2],
     * [3,2,null,1],
     * [3,1,null,null,2],
     * [2,1,3],
     * [1,null,2,null,3]
     * ]
     * Explanation:
     * The above output corresponds to the 5 unique BST's shown below:
     * <p>
     * 1         3     3      2      1
     * \       /     /      / \      \
     * 3     2     1      1   3      2
     * /     /       \                 \
     * 2     1         2                 3
     */
    public static List<TreeNode> generateTrees(int n) {
        return generate(1, n);
    }

    public static List<TreeNode> generate(int left, int right) {
        List<TreeNode> result = new ArrayList<>();
        if (right - left == 0) {
            result.add(new TreeNode(left));
            return result;
        }

        List<TreeNode> leftNodes, rightNodes;
        for (int i = left; i <= right; i++) {
            leftNodes = new ArrayList<>();
            rightNodes = new ArrayList<>();
            if (left <= i - 1) {
                leftNodes = generate(left, i - 1);
            }
            if (i + 1 <= right) {
                rightNodes = generate(i + 1, right);
            }
            List<TreeNode> temp = new ArrayList<>();
            for (int j = 0; j < leftNodes.size(); j++) {
                TreeNode node = new TreeNode(i);
                TreeNode leftChild = leftNodes.get(j);
                node.left = leftChild;
                if (rightNodes.size() > 0) {
                    temp.add(node);
                } else {
                    result.add(node);
                }
            }

            for (int j = 0; j < rightNodes.size(); j++) {
                TreeNode rightChild = rightNodes.get(j);
                if (temp.size() == 0) {
                    TreeNode node = new TreeNode(i);
                    node.right = rightChild;
                    result.add(node);
                } else {
                    for (int k = 0; k < temp.size(); k++) {
                        TreeNode node = cloneTree(temp.get(k));
                        rightChild = cloneTree(rightChild);
                        node.right = rightChild;
                        result.add(node);
                    }
                }
            }
        }
        return result;
    }


    public static TreeNode cloneTree(TreeNode node) {
        if (node == null)
            return null;
        TreeNode nodeCopy = new TreeNode(node.val);
        nodeCopy.left = cloneTree(node.left);
        nodeCopy.right = cloneTree(node.right);
        return nodeCopy;
    }

    private Map<Integer, Integer> m2 = new HashMap<>();

    public int numTrees(int n) {
        //从2到n依次以每个数字作为根节点，先决条件是n<=1时只有一种组合。 大于1时，可以依次将n分为[1,i]和[i, n]两边，
        //等价于F(1,n) = G(1,i) * G(i,n)---------这样做是因为以i为根节点，[1,i]肯定是其左子树，[i,n]组成其右子树,
        //而且所有的左子树也可以添加在其所有的右子树下，因此最终结果集为左右子树的笛卡尔积，等价于G(1,i)*G(i,n)，使用dp记录每次计算出来的值，减少重复计算
        if (n <= 1) return 1;
        if (m2.containsKey(n)) return m2.get(n);
        int res = 0;
        for (int i = 1; i <= n; i++) {
            res += (numTrees(i - 1) * numTrees(n - i));
        }
        m2.put(n, res);
        return res;
    }

    /**
     * 给定二叉树，判断其是否为二叉查找树
     *    2
     *    / \
     *   1   3
     *
     * Input: [2,1,3]
     * Output: true
     * [10,5,15,null,null,6,20]  -----因为又子节点7小于根节点10，所以这不是二叉搜索树
     */
    public static boolean isValidBST(TreeNode root) {
        /*if (root == null) {               //这种做法会将[10,5,15,null,null,6,20]这样的二叉树判定为二叉搜索树，是错误的
            return true;
        }
        if (root.left != null && root.left.val >= root.val) {
            return false;
        }
        if (root.right != null &&root.right.val <= root.val) {
            return false;
        }
        return isValidBST(root.left) && isValidBST(root.right);*/
        return isValidBST(root, Long.MIN_VALUE, Long.MAX_VALUE);    //因为TreeNode的val定义是int类型，此处使用Long就是为了防止传入参数是Integer.MAX_VALUE的情况
    }

    public static boolean isValidBST(TreeNode node, long min, long max) {
        if (node == null) {
            return true;
        }
        if (node.val > min && node.val < max) {
            return isValidBST(node.left, min, node.val) && isValidBST(node.right, node.val, max);
        }
        return false;
    }

    /**
     * 给定二叉搜索树，可是其中有一个元素位置有错，在不改变结构体的前提下修复二叉搜索树
     * Input: [1,3,null,null,2]
     *
     *    1
     *   /
     *  3
     *   \
     *    2
     *
     * Output: [3,1,null,null,2]
     *
     *    3
     *   /
     *  1
     *   \
     *    2
     */
    TreeNode prev, first, second;
    public void recoverTree(TreeNode root) {
        // TODO: 2019/12/11 没看懂
        recoverTreeHelper(root);
        int tmp = first.val;
        first.val = second.val;
        second.val = tmp;
    }
    private void recoverTreeHelper(TreeNode cur) {
        if (cur != null) {
            recoverTreeHelper(cur.left);
            if (prev != null && prev.val > cur.val) {
                if (first == null) {
                    first = prev;
                    second = cur;
                } else second = cur;
            }
            prev = cur;
            recoverTreeHelper(cur.right);
        }
    }

    /**
     * 判断两个树是否为相同的树
     * Input:     1         1
     *           / \       / \
     *          2   3     2   3
     *
     *         [1,2,3],   [1,2,3]
     *
     * Output: true
     */
    public boolean isSameTree(TreeNode p, TreeNode q) {
        if (p != null && q != null) {
            if (p.val != q.val) {
                return false;
            }
            return isSameTree(p.left, q.left) && isSameTree(p.right, q.right);
        }
        return p == null && q == null;
    }

    /**
     * 判断树是否为镜面对称
     *     1
     *    / \
     *   2   2
     *  / \ / \
     * 3  4 4  3  true
     */
    public boolean isSymmetric(TreeNode root) {
        if (root == null) return true;
        return isSymmetric(root.left, root.right);
    }

    public boolean isSymmetric(TreeNode left, TreeNode right) {
        if (left != null && right != null) {
            if (left.val != right.val) {
                return false;
            }
            return isSymmetric(left.left, right.right) && isSymmetric(left.right, right.left);
        }
        return left == null && right == null;
    }

    /**
     * 给定二叉树，按照层级遍历，从左到右列出每层的数据
     *    3
     *    / \
     *   9  20
     *     /  \
     *    15   7
     * return its level order traversal as:
     * [
     *   [3],
     *   [9,20],
     *   [15,7]
     * ]
     */
    public List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        levelOrderTraversal(result, 0, root);
        return result;
    }

    public void levelOrderTraversal(List<List<Integer>> result, int level, TreeNode node) {
        if (node == null) {
            return ;
        }
        if (result.size() == level) {
            result.add(new ArrayList<>());
        }
        result.get(level).add(node.val);
        levelOrderTraversal(result, level+1, node.left);
        levelOrderTraversal(result, level+1, node.right);
    }

    /**
     * 之字形层级遍历
     * Given binary tree [3,9,20,null,null,15,7],
     *     3
     *    / \
     *   9  20
     *     /  \
     *    15   7
     * return its zigzag level order traversal as:
     * [
     *   [3],
     *   [20,9],
     *   [15,7]
     * ]
     */
    public List<List<Integer>> zigzagLevelOrder(TreeNode root) {
        return null;
    }

    public static void main(String[] args) {
        List<List<Integer>> result = subsetsWithDup(new int[]{1, 2, 2});
        result.stream().forEach(l -> System.out.println(l));
    }
}
