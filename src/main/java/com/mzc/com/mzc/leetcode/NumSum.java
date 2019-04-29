package com.mzc.com.mzc.leetcode;

import org.w3c.dom.NodeList;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

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

    static class ListNode {
        public int val;
        public ListNode next;

        public ListNode(int val) {
            this.val = val;
        }
    }

    /**
     * 两个ListNode中的数相加，需要考虑进位问题
     * 例： {2, 4, 3} + {5, 6} = {7, 0, 4}
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

    public static void main(String[] args) {
        ListNode l1 = new ListNode(2);
        l1.next = new ListNode(4);
        l1.next.next = new ListNode(3);

        ListNode l2 = new ListNode(5);
        l2.next = new ListNode(6);
        l2.next.next = new ListNode(4);

        ListNode result = addTwoNumbers(l1, l2);
        ListNode node = result;
        while (node != null) {
            System.out.println(node.val);
            node = node.next;
        }
    }
}
