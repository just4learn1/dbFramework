package com.mzc.leetcode;

import com.mzc.leetcode.inst.ListNode;

import java.util.ArrayList;
import java.util.List;

public class NodeOpt {

    /*public static class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
        }
    }*/

    /**
     * 给定一个单向链表及n，删除链表中倒数第n个元素
     * <p>
     * Given linked list: 1->2->3->4->5, and n = 2.
     * <p>
     * After removing the second node from the end, the linked list becomes 1->2->3->5.
     * <p>
     * solution:  将first移动到链表的第n位，然后second从链表的第0位开始，循环让first=first.second, second=second.next，当first等于null的时候代表走到了队尾，此时second所处的就是倒数第n位
     *
     * @param head
     * @param n
     * @return
     */
    public static ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode first = dummy;
        ListNode second = dummy;
        for (int i = 0; i < n + 1; i++) {
            first = first.next;
        }
        while (first != null) {
            first = first.next;
            second = second.next;
        }
        second.next = second.next.next;
        return dummy.next;
    }

    /**
     * 两个有序单向链表，将其合并成一个有序的单向链表
     * <p>
     * Input: 1->2->4, 1->3->4
     * Output: 1->1->2->3->4->4
     *
     * @param l1
     * @param l2
     * @return
     */
    public static ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode(0);
        ListNode first = dummy;
        while (l1 != null && l2 != null) {
            if (l1.val <= l2.val) {
                first.next = l1;
                l1 = l1.next;
            } else {
                first.next = l2;
                l2 = l2.next;
            }
            first = first.next;
        }
        if (l1 != null) {
            first.next = l1;
        } else if (l2 != null) {
            first.next = l2;
        }
        return dummy.next;
    }

    /**
     * 将n个有序单向链表合并成一个有序的单向链表
     * Input:
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
    public static ListNode mergeKLists(ListNode[] lists) {
        if (lists == null || lists.length == 0) {
            return null;
        }
        /**
         * 暴力解法，循环执行两个有序链表合并
         */
        /*ListNode result = lists[0];
        for (int i = 1; i < lists.length; i++) {
            result = mergeTwoLists(result, lists[i]);
        }
        return result;*/
        /**
         * 将所有的数放入队列中，重新生成对应链表，或者直接使用PriorityQueue来存ListNode也可以
         *
         * 时间复杂度比较高，得考虑给更低时间复杂度的算法
         */

        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < lists.length; i++) {
            while (lists[i] != null) {
                list.add(lists[i].val);
                lists[i] = lists[i].next;
            }
        }
        ListNode dummy = new ListNode(0);
        ListNode first = dummy;
        list.sort((a, b)->a-b);
        for (Integer a : list) {
            first.next = new ListNode(a);
            first = first.next;
        }
        return dummy.next;
    }

    /**
     * Given a linked list, swap every two adjacent nodes and return its head.
     *
     * You may not modify the values in the list's nodes, only nodes itself may be changed.
     *
     * Given 1->2->3->4, you should return the list as 2->1->4->3.
     * @param head
     * @return
     */
    public static ListNode swapPairs(ListNode head) {
        if(head==null) return null;
        ListNode pre=head.next;
        ListNode hind=new ListNode(-1);
        hind.next=head;
        while(pre!=null){
            hind.next.next=pre.next;
            pre.next=hind.next;
            if(hind.next==head) head=pre;
            hind.next=pre;
            if(pre.next.next!=null){
                if(pre.next.next.next!=null){
                    hind=pre.next;
                    pre=pre.next.next.next;
                }
                else pre=null;
            }
            else pre=null;
        }
        return head;
    }

    /**
     * 给定单向链表及数字n，以n为周期反转链表  (// TODO: 2019/5/9  还没看懂怎么解的)
     *
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
        ListNode temp = head;
        //判定链表之后的元素数量是否大于k
        for (int i = 1; i < k && temp != null; i++) {
            temp = temp.next;
        }
        //判断节点的数量是否能够凑成一组
        if(temp == null) {
            return head;
        }

        ListNode t2 = temp.next;                //t2存储 head第k位之后的元素
        temp.next = null;                       //断开head前k位与之后元素的链  （等同于将链表拆分为前k个元素组成的链表head 与后n位元素组成的链表t2）
        //把当前的组进行逆序
        ListNode result = reverseList(head);    //逆序前k位链表
        //把之后的节点进行分组逆序
        head.next = reverseKGroup(t2, k);       //递归将head.next设置位逆序后的前k位链表，
        return result;
    }
    private static ListNode reverseList(ListNode head) {
        if(head == null || head.next == null)
            return head;
        ListNode result = reverseList(head.next);
        head.next.next = head;
        head.next = null;
        return result;
    }

    public static void main(String[] args) {
        ListNode n = new ListNode(1);
        n.next = new ListNode(2);
        n.next.next = new ListNode(3);
        n.next.next.next = new ListNode(4);
        ListNode l = reverseKGroup(n, 2);
//        ListNode l = reverseList(n);
//        while (l != null) {
//            System.out.println(l.val);
//            l = l.next;
//        }
    }
}
