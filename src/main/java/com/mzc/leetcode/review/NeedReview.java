package com.mzc.leetcode.review;

import com.mzc.leetcode.inst.ListNode;
import com.mzc.leetcode.inst.TreeNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

/**
 * create by zhencai.ma on 2019/12/2
 */
public class NeedReview {

    /**
     * 求两个有序数组的中位数
     */
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        return 0;
    }

    /**
     * 转移单向链表k次
     * Input: 1->2->3->4->5->NULL, k = 2
     * Output: 4->5->1->2->3->NULL
     * Explanation:
     * rotate 1 steps to the right: 5->1->2->3->4->NULL
     * rotate 2 steps to the right: 4->5->1->2->3->NULL
     */
    public static ListNode rotateRight(ListNode head, int k) {
        // TODO: 2019/12/10
        int len = 0;
        ListNode tmp = head;
        ListNode last = null;
        while (tmp != null) {
            last = tmp;
            tmp = tmp.next;
            len++;
        }
        if (len <= 1) {
            return head;
        }
        if (k % len == 0) {
            return head;
        }
        int step = len - k % len - 1;
        ListNode tmp2 = head;
        while (step-- > 0) {
            tmp2 = tmp2.next;
        }
        ListNode node = tmp2.next;
        tmp2.next = null;
        last.next = head;
        return node;
    }

    /**
     * 给定颜色数组，0,1,2分别代表红色、白色和蓝色  对其排序
     * Input: [2,0,2,1,1,0]
     * Output: [0,0,1,1,2,2]
     */
    public static void sortColors(int[] nums) {
        // TODO: 2019/12/11
    }

    public static void swap(int[] arr, int l, int r) {
        int tmp = arr[l];
        arr[l] = arr[r];
        arr[r] = tmp;
    }

    /**
     * 给定字符串s和t，从s中找出最短的子串使其包含所有的t中的字符
     * Input: S = "ADOBECODEBANC", T = "ABC"
     * Output: "BANC"
     */
    public static String minWindow(String s, String t) {
        int[] cnts = new int[128];
        int counter = t.length();
        for (int i = 0; i < counter; i++) {
            cnts[t.charAt(i)]++;
        }
        int left = -1;
        int right = -1;
        int f = 0, second = 0;
        while (f < s.length()) {
            if (cnts[s.charAt(f++)]-- > 0) {
                counter--;
            }
            while (counter == 0) {
                if (left == -1 || f - second < right - left) {
                    right = f;
                    left = second;
                }
                if (++cnts[s.charAt(second++)] > 0) {
                    counter++;
                }
            }
        }
        return s.substring(left, right);
    }

    /**
     * 给定不重复的整数数组，找出其所有唯一的子串
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
        List<List<Integer>> ans = new ArrayList<>();
        ans.add(new ArrayList<>());
        for (int n : nums) {
            List<List<Integer>> tmpAdd = new ArrayList<>();
            for (List<Integer> l : ans) {
                List<Integer> tmp = new ArrayList<>(l);
                tmp.add(n);
                tmpAdd.add(tmp);
            }
            ans.addAll(tmpAdd);
        }
        return ans;
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
    public static int boardM, boardN;

    public static boolean exist(char[][] board, String word) {
        boardM = board.length;
        boardN = board[0].length;
        visited = new boolean[boardM][boardN];
        for (int i = 0; i < boardM; i++) {
            for (int j = 0; j < boardN; j++) {
                if (board[i][j] == word.charAt(0) && existB(board, word, 0, i, j)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean existB(char[][] board, String word, int index, int i, int j) {
        if (index == word.length()) {
            return true;
        }
        if (i < 0 || i >= boardM || j < 0 || j >= boardN || visited[i][j] || word.charAt(index) != board[i][j]) {
            return false;
        }
        visited[i][j] = true;
        int nextIndex = index + 1;
        if (existB(board, word, nextIndex, i + 1, j) || existB(board, word, nextIndex, i, j + 1) ||
                existB(board, word, nextIndex, i - 1, j) || existB(board, word, nextIndex, i, j - 1)) {
            return true;
        }
        visited[i][j] = false;
        return false;
    }

    /**
     * 删除有序链表中的重复元素
     * Input: 1->1->2
     * Output: 1->2
     */
    public static ListNode deleteDuplicates2(ListNode head) {
        ListNode tmp = head;
        while (tmp != null) {
            while (tmp != null && tmp.next != null && tmp.val == tmp.next.val) {
                tmp.next = tmp.next.next;
            }
            tmp = tmp.next;
        }
        return head;
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
        int lastIndex = m + n - 1;
        m--;
        n--;
        while (lastIndex >= 0) {
            if (m >= 0 && (n < 0 || nums1[m] > nums2[n])) {     //因为m和n都有可能会出现小于0的情况，所以需要对边界条件加额外处理
                nums1[lastIndex--] = nums1[m--];
            } else {
                nums1[lastIndex--] = nums2[n--];
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
        Arrays.sort(nums);
        List<List<Integer>> ans = new ArrayList<>();
        ans.add(new ArrayList<>());
        List<List<Integer>> newAdd = null;
        for (int i = 0; i < nums.length; i++) {
            if (i > 0 && nums[i] == nums[i - 1]) {
                List<List<Integer>> list = new ArrayList<>(newAdd);
                newAdd = new ArrayList<>();
                for (List<Integer> l : list) {
                    List<Integer> tmp = new ArrayList<>(l);
                    tmp.add(nums[i]);
                    newAdd.add(tmp);
                }
            } else {
                newAdd = new ArrayList<>();
                for (List<Integer> l : ans) {
                    List<Integer> tmp = new ArrayList<>(l);
                    tmp.add(nums[i]);
                    newAdd.add(tmp);
                }
            }
            ans.addAll(newAdd);
        }
        return ans;
    }

    /**
     * 反转链表的第m到n位元素
     * Input: 1->2->3->4->5->NULL, m = 2, n = 4
     * Output: 1->4->3->2->5->NULL
     */
    public static ListNode reverseBetween(ListNode head, int m, int n) {
        ListNode list1 = null;
        ListNode tmp = head;
        int cnt = 0;
        while (cnt < m - 1) {
            list1 = tmp;
            tmp = tmp.next;
            cnt++;
        }
        Stack<ListNode> stack = new Stack<>();
        while (cnt < n) {
            stack.add(tmp);
            tmp = tmp.next;
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
            list1.next = tmp;
        }
        return head;
    }

    /**
     * 给定二叉树的前序和中序遍历数组，构建二叉树(给定遍历数组中没有重复元素)
     * preorder = [3,9,20,15,7]
     * inorder = [9,3,15,20,7]
     * Return the following binary tree:
     * <p>
     * 3
     * / \
     * 9  20
     * /  \
     * 15   7
     */
    public TreeNode buildTree(int[] preorder, int[] inorder) {
        return null;
    }

    /**
     * 给定二叉树的中序和后序遍历数组，构建二叉树
     * inorder = [9,3,15,20,7]
     * postorder = [9,15,7,20,3]
     * Return the following binary tree:
     *
     *     3
     *    / \
     *   9  20
     *     /  \
     *    15   7
     */
    public TreeNode buildTree2(int[] inorder, int[] postorder) {
        return null;
    }
    public static void main(String[] args) {
        ListNode head = new ListNode(1);
        head.next = new ListNode(2);
        head.next.next = new ListNode(3);
        head.next.next.next = new ListNode(4);
        head.next.next.next.next = new ListNode(5);
        ListNode node = reverseBetween(head, 2, 4);
        System.out.println(node);
    }
}
