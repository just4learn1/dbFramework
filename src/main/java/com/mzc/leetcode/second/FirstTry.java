package com.mzc.leetcode.second;

import com.mzc.leetcode.inst.ListNode;
import com.mzc.leetcode.inst.TreeNode;
import com.sun.xml.internal.bind.v2.TODO;

import java.lang.reflect.Array;
import java.util.*;

/**
 * create by zhencai.ma on 2019/11/14
 */
public class FirstTry {

    /**
     * 给定一个二维数组，如果给定二维数组中包含0，就将0所在的整行、整列都设置为0
     * Input:
     * [
     * [1,1,1],
     * [1,0,1],
     * [1,1,1]
     * ]
     * Output:
     * [
     * [1,0,1],
     * [0,0,0],
     * [1,0,1]
     * ]
     */
    public static void setZeroes(int[][] matrix) {
        //解题思路，可以使用第一行和第一列来标记行列是否包含0值，标记出来以后再将标记出来的整行列设置为0，需要先得出第一行列是否需要全部设置为0
        boolean firstRowHasZero = false;
        boolean firstColumnHasZero = false;
        for (int i = 0; i < matrix.length; i++) {
            if (matrix[i][0] == 0) {
                firstColumnHasZero = true;
                break;
            }
        }
        for (int i = 0; i < matrix[0].length; i++) {
            if (matrix[0][i] == 0) {
                firstRowHasZero = true;
                break;
            }
        }
        for (int i = 1; i < matrix.length; i++) {
            for (int j = 1; j < matrix[i].length; j++) {
                if (matrix[i][j] == 0) {
                    matrix[i][0] = 0;
                    matrix[0][j] = 0;
                }
            }
        }
        for (int i = 1; i < matrix[0].length; i++) {
            if (matrix[0][i] == 0) {
                fillColumnZero(matrix, i);
            }
        }
        for (int i = 1; i < matrix.length; i++) {
            if (matrix[i][0] == 0) {
                fillRowZero(matrix, i);
            }
        }
        System.out.println(firstColumnHasZero);
        System.out.println(firstRowHasZero);
        if (firstColumnHasZero) fillColumnZero(matrix, 0);
        if (firstRowHasZero) fillRowZero(matrix, 0);
        Arrays.stream(matrix).forEach(e -> System.out.println(Arrays.toString(e)));

    }

    public static void fillColumnZero(int[][] matrix, int column) {
        for (int i = 0; i < matrix.length; i++) {
            matrix[i][column] = 0;
        }
    }

    public static void fillRowZero(int[][] matrix, int row) {
        for (int i = 0; i < matrix[0].length; i++) {
            matrix[row][i] = 0;
        }
    }

    /**
     * 给定一个纵横有序的二维数组，查找target在nums中的位置，没有则返回{-1,-1}
     * [1, 4, 9]
     * [2, 5, 10]
     * [3, 7, 20]
     */
    public static int[] find(int[][] nums, int target) {
        //解题思路，因为纵横有序，可以考虑从nums[0][len-1]位置开始查找，大于此值可以排除掉一行，小于此值可以排除掉一列
        int i = 0;
        int j = nums[0].length - 1;
        while (i < nums.length && j >= 0) {
            int num = nums[i][j];
            if (num == target) {
                return new int[]{i, j};
            } else if (num > target) {
                j--;
            } else {
                i++;
            }
        }
        return new int[]{-1, -1};
    }

    /**
     * 在纵横有序的二维数组中判断是否存在target
     */
    public static boolean searchMatrix(int[][] matrix, int target) {
        if (matrix == null || matrix.length == 0) {
            return false;
        }
        int i = 0;
        int j = matrix[0].length - 1;
        while (i < matrix.length && j >= 0) {
            if (matrix[i][j] == target) {
                return true;
            } else if (matrix[i][j] < target) {
                i++;
            } else {
                j--;
            }
        }
        return false;
    }

    public static void sortColors(int[] nums) {
        if (nums == null || nums.length == 0) {
            return;
        }
        int lowIndex = 0, len = nums.length, highIndex = len - 1, index = 0;
        while (index < len && index <= highIndex) {
            if (nums[index] == 1) {
                index++;
            } else if (nums[index] == 0) {
                swap(nums, lowIndex, index);
                lowIndex++;
                index++;
            } else {
                swap(nums, highIndex, index);
                highIndex--;
            }
        }
    }

    public static void swap(int[] nums, int l, int r) {
        int tmp = nums[l];
        nums[l] = nums[r];
        nums[r] = tmp;
    }

    /**
     * 给定字符串s和t，从s中找到最短的包含所有t中字符的子串
     * Input: S = "ADOBECODEBANC", T = "ABC"
     * Output: "BANC"
     */
    public static String minWindow(String s, String t) {
        int first = 0;
        int second = 0;
        int[] count = new int[128];
        int counter = t.length();
        int left = -1;
        int right = -1;
        for (int i = 0; i < counter; i++) {
            count[t.charAt(i)]++;
        }

        while (second < s.length()) {
            if (count[s.charAt(second++)]-- > 0) {
                counter--;
            }
            while (counter == 0) {      //second
                if (left == -1 || (second - first) < (right - left)) {
                    left = first;
                    right = second;
                }
                if (count[s.charAt(first++)]++ == 0) {      //
                    counter++;
                }
            }
        }
        if (left == -1) {
            return "";
        }
        return s.substring(left, right);
    }

    /**
     * 给定n和k， 列出所有所有可能组合
     * Input: n = 4, k = 2
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
        generate(result, new ArrayList<>(), 1, n, k);
        return result;
    }

    public static void generate(List<List<Integer>> result, List<Integer> tmp, int start, int n, int k) {
        if (k == 0) {
            result.add(new ArrayList<>(tmp));
            return;
        }
        for (int i = start; i <= n - k + 1; i++) {
            tmp.add(i);
            generate(result, tmp, i + 1, n, k - 1);
            tmp.remove(tmp.size() - 1);
        }
    }

    /**
     * 给定一个没有重复值的数组nums，列出所有可能的数字组合
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
            List<List<Integer>> toAdd = new ArrayList<>();
            for (List<Integer> tmp : result) {
                List<Integer> newLst = new ArrayList<>(tmp);
                newLst.add(num);
                toAdd.add(newLst);
            }
            result.addAll(toAdd);
        }
        return result;
    }

    /**
     * 给定二维数组board以及一个字符串word，找出word是否存在于board中（board中连续字符串子串中包含word）
     * P：只要两个字符之间相邻(同行，同列相邻都算)就算连续(不可以重复使用同一位置字符)
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
    static boolean[][] visited;
    static int rowLen;
    static int columnLen;

    public static boolean exist(char[][] board, String word) {
        rowLen = board.length;
        columnLen = board[0].length;
        visited = new boolean[rowLen][columnLen];
        for (int i = 0; i < rowLen; i++) {
            for (int j = 0; j < columnLen; j++) {
                if (word.charAt(0) == board[i][j] && existJudge(board, word, 0, i, j)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean existJudge(char[][] board, String word, int index, int i, int j) {
        if (index == word.length()) {
            return true;
        }
        if (i < 0 || i >= rowLen || j < 0 || j >= columnLen || visited[i][j] || word.charAt(index) != board[i][j]) {
            return false;
        }
        visited[i][j] = true;
        int next = index + 1;
        if (existJudge(board, word, next, i + 1, j) || existJudge(board, word, next, i - 1, j) || existJudge(board, word, next, i, j + 1)
                || existJudge(board, word, next, i, j - 1)) {
            return true;
        }
        visited[i][j] = false;
        return false;
    }

    /**
     * 给定一个可能有重复元素且是经过一次转换的有序数组的数组nums以及一个target，判断target是否存在
     * Input: nums = [2,5,6,0,0,1,2], target = 3
     * Output: false
     */
    public static boolean search(int[] nums, int target) {
        // TODO: 2019/11/20 没看明白，原来的search方法也有问题，参数如果是{3, 1}; 1，返回会是-1，应该是1才对
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

    /**
     * 删除单向有序链表中所有有重复值的元素
     * Input: 1->2->3->3->4->4->5
     * Output: 1->2->5
     */
    public static ListNode deleteDuplicates2(ListNode head) {
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
     * 删除单向链表中的重复元素
     * Input: 1->1->2
     * Output: 1->2
     */
    public static ListNode deleteDuplicates(ListNode head) {
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
     * 给定一个数组，其中每个元素代表高，每个元素的宽度固定为1，求连续元素组成的最大矩形面积
     * Input: [2,1,5,6,2,3]
     * Output: 10
     */
    public static int largestRectangleArea(int[] heights) {
        // TODO: 2019/11/21 没看明白
        Stack<Integer> st = new Stack<>();
        int maxArea = 0, n = heights.length;
        for (int i = 0; i < n; i++) {
            while (!st.isEmpty() && heights[st.peek()] >= heights[i]) {
                int area = heights[st.pop()] * (st.isEmpty() ? i : i - st.peek() - 1);
                maxArea = Math.max(maxArea, area);
            }
            st.push(i);
        }
        while (!st.isEmpty()) {
            int area = heights[st.pop()] * (st.isEmpty() ? n : n - st.peek() - 1);
            maxArea = Math.max(maxArea, area);
        }
        return maxArea;
        /*
        //执行效率高，，没看懂！
        if (heights == null || heights.length == 0) return 0;
        int[] memo = new int[heights.length];
        int p = 0;
        int indx = 0;
        int result = 0;
        while (indx <= heights.length) {
            if (p != 0 && (indx == heights.length || heights[indx] <= heights[memo[p - 1]])) {
                int cur = memo[--p];
                int leftBound = (p == 0)? -1 : memo[p - 1];
                result = Math.max(result, (indx - leftBound - 1) * heights[cur]);
            } else {
                memo[p++] = indx;
                indx++;
            }
        }
        return result;*/
    }

    /**
     * 给定单向链表head以及一个数组x，重新分隔链表，使得链表中所有小于x的元素都排在大于等于x的值之前
     * 输入: head = 1->4->3->2->5->2, x = 3
     * 输出: 1->2->2->4->3->5
     * 解法：构建两个链表，第一个用来存储小于x的值所组成的链表，第二个用来存储大于等于x的值组成的链表，最终合并两个链表即可
     */
    public static ListNode partition(ListNode head, int x) {
        ListNode list1 = null, list2 = null, pointer = head, pointer1 = list1, pointer2 = list2;
        while (pointer != null) {
            int temp = pointer.val;
            if (temp < x) {
                if (list1 == null) {
                    list1 = pointer;
                    pointer1 = list1;
                } else {
                    pointer1.next = pointer;
                    pointer1 = pointer;
                }
            } else {
                if (list2 == null) {
                    list2 = pointer;
                    pointer2 = list2;
                } else {
                    pointer2.next = pointer;
                    pointer2 = pointer;
                }
            }
            pointer = pointer.next;
        }
        if (list1 != null) {
            pointer1.next = list2;
            if (pointer2 != null) pointer2.next = null;
            return list1;
        } else return list2;
    }

    /**
     * 给定一个可能有重复元素的数组nums，列出其所有元素唯一组合
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
        List<List<Integer>> result = new ArrayList<>();
        result.add(new ArrayList<>());
        List<List<Integer>> tmp = new ArrayList<>();
        for (int i = 0; i < nums.length; i++) {
            if (i > 0 && nums[i] == nums[i-1]) {            //由于此处的判断是与前一位进行比较，所以nums必须是有序的，否则类似于 [4,4,1,4]这种间隔重复的情况会出错
                List<List<Integer>> dupAdd = new ArrayList<>(tmp);
                tmp.clear();
                for (List<Integer> t : dupAdd) {
                    List<Integer> newAdd = new ArrayList<>(t);
                    newAdd.add(nums[i]);
                    tmp.add(newAdd);
                }
            } else {
                tmp.clear();
                for (List<Integer> t : result) {
                    List<Integer> newAdd = new ArrayList<>(t);
                    newAdd.add(nums[i]);
                    tmp.add(newAdd);
                }
            }
            result.addAll(tmp);
        }
        return result;
    }

    /**
     * 反转链表的第m到n位
     * Input: 1->2->3->4->5->NULL, m = 2, n = 4
     * Output: 1->4->3->2->5->NULL
     */
    public static ListNode reverseBetween(ListNode head, int m, int n) {
        if(head == null){
            return null;
        }
        ListNode firstList = null;
        ListNode temp = head;
        Stack<ListNode> st = new Stack<>();     //存储链表中第m至n位元素
        int count = 0;
        while(count < m-1){
            firstList = temp;
            temp = temp.next;
            count++;
        }
        while(count >=m-1 && count<=n-1){
            st.add(temp);
            temp = temp.next;
            count++;
        }
        while(!st.isEmpty()){
            System.out.println(firstList);
            if(firstList!=null){
                firstList.next = new ListNode(st.pop().val);
            }else{
                firstList = head;       //此处仅为了让firstList不等于null，new一个值也可以，不过空间会多用一点
//                firstList = new ListNode(-1);
                firstList.next = new ListNode(st.pop().val);
                head = firstList.next;
            }
            firstList = firstList.next;
        }
        if(firstList!=null){            //连接第三段链表
            firstList.next = temp;
        }
        return head;
    }

    /**
     * 反转链表也可以使用 Stack，利用Stack的先进后出的原则，就可以很容易实现链表反转
     */
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

    public static void printListnode(ListNode node) {
        while (node != null) {
            System.out.println(node.val);
            node = node.next;
        }
        System.out.println("**********************");
    }

    /**
     * 给定一个二叉树，返回其节点值的有序遍历
     * @param root
     * @return
     */
    public static List<Integer> inorderTraversal(TreeNode root) {
        ArrayDeque<TreeNode> stack = new ArrayDeque<>();
        List<Integer> list = new LinkedList<>();
        if (root == null) return list;
        stack.push(root);
        while (stack.peek().left != null) {
            stack.push(stack.peek().left);
        }
        while (!stack.isEmpty()) {
            TreeNode node = stack.pop();
            list.add(node.val);
            if (node.right != null) {
                stack.push(node.right);
                while (stack.peek().left != null) {
                    stack.push(stack.peek().left);
                }
            }

        }
        return list;
    }

    public static void main(String[] args) {
        ListNode node = new ListNode(1);
        node.next = new ListNode(2);
        node.next.next = new ListNode(3);
        node.next.next.next = new ListNode(4);
        node.next.next.next.next = new ListNode(5);
        ListNode rev = reverseBetween(node, 0, 3);
        printListnode(rev);
    }
}
