package com.mzc.leetcode;

import com.mzc.leetcode.inst.ListNode;

import java.util.*;

/**
 * create by zhencai.ma on 2019/11/7
 */
public class Test2 {

    public static void main(String[] args) {
        ListNode l = new ListNode(1);
        l.next = new ListNode(2);
        l.next.next = new ListNode(3);
        l.next.next.next = new ListNode(4);

        ListNode result = reverseKGroup(l, 3);
        while (result != null) {
            System.out.println(result.val);
            result = result.next;
        }
    }

    /**
     * 按照给定数字反转链表
     * Given this linked list: 1->2->3->4->5
     *
     * For k = 2, you should return: 2->1->4->3->5
     *
     * For k = 3, you should return: 3->2->1->4->5
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
