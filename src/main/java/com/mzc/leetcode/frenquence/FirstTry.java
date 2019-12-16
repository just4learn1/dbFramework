package com.mzc.leetcode.frenquence;

import com.mzc.leetcode.inst.ListNode;
import org.apache.http.HeaderIterator;

import java.util.*;

public class FirstTry {
    /**
     * 给定数组和target，从数组中找到两个元素，使其和等于target并返回下标
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
     * 给定一个字符串s，找到s中最长对回文字符串，假定s长度小雨1000
     */
    public static String longestPalindrome(String s) {
        if (s.length() <= 1) {
            return s;
        }
        int l = -1;
        int r = -1;
        for (int i = 0; i < s.length(); i++) {
            int len = Math.max(longestP(s, i, i), longestP(s, i, i + 1));
            if (len > r - l) {
                l = i - (len - 1) / 2;
                r = i + len / 2;
            }
        }
        return s.substring(l, r + 1);


    }

    public static int longestP(String s, int left, int right) {
        int l = left;
        int r = right;
        while (l >= 0 && r < s.length() && s.charAt(l) == s.charAt(r)) {
            l--;
            r++;
        }
        return r - l - 1;
    }

    /**
     * 反转链表
     */
    public ListNode reverseList(ListNode head) {
        ListNode pre = null;
        ListNode hind = head;
        while (hind != null) {
            ListNode tmp = hind.next;
            hind.next = pre;
            if (hind.next == head) head = hind;
            pre = hind;
            hind = tmp;
        }
        return head;
    }

    /**
     * 蓄水能力，给定数组，其中每个元素代表高度，求总蓄水能力
     */
    public int trap(int[] height) {
        int left = 0;
        int ans = 0;
        int right = height.length - 1;
        while (left < right && height[left] <= height[left + 1]) left++;
        while (left < right && height[right] <= height[right - 1]) right--;
        while (left < right) {
            int edge = Math.min(height[right], height[left]);
            if (height[left] > height[right]) { //左边边界大于右边，所以右边界为储水上限
                while (left < right && height[--right] <= edge) {
                    ans += edge - height[right];
                }
            } else {
                while (left < right && height[++left] <= edge) {
                    ans += edge - height[left];
                }
            }
        }
        return ans;
    }

    /**
     * 寻找两个有序数组对中位数 9:43
     */
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        //将数组分为A=nums1, m=A.length; [0, i-1] [i, m] 和 B=˙nums2;n=B.length;[0,j-1] [j, n]
        //只要i-1+j-1=m-1-i+n-1-j既左侧等于右侧,等价于 i = (m+n)/2-j且左侧最大数小于右侧最小数，如果m+n是偶数，那么中位数就是(maxleft+minright)/2,否则为左侧
        //由于边界问题，所以需要m>=n才能保证i不需要判断负值  此处实现使用都是A也就是m<n
        int[] A = nums1;
        int[] B = nums2;
        int m = A.length, n = B.length;
        if (m > n) {
            int[] t = A;
            A = B;
            B = t;
            int t2 = m;
            m = n;
            n = t2;
        }
        int c = (m + n + 1) / 2;
        int iMin = 0, iMax = A.length;
        while (iMin < iMax) {
            int i = (iMin + iMax) / 2;
            int j = c - i;
            if (i < iMax && A[i] < B[j-1]) { //j too big(i too small)
                iMin = i + 1;
            } else if (i > iMin && A[i-1] > B[j]) {
                iMax = i - 1;
            } else {
                int maxLeft, minRight;
                if (i == 0) {
                    maxLeft = B[j-1];
                } else if (j == 0) {
                    maxLeft = A[i-1];
                } else {
                    maxLeft = Math.max(A[i - 1], B[j - 1]);
                }
                if ((m+n) % 2 == 1) {
                    return maxLeft;
                }
                if (i >= m) {
                    minRight = B[j];
                } else if (j >= n) {
                    minRight = A[i];
                } else {
                    minRight = Math.min(A[i], B[j]);
                }
                System.out.printf("minRight:%s, maxLeft:%s\n", minRight, maxLeft);
                return (maxLeft + minRight) / 2d;
            }
        }
        // TODO: 2019/12/15 完成算法
        return 0d;
    }

    public static int getDecimalValue(ListNode head) {
        int cnt = 0;
        int ans = 0;
        ListNode tmp = reverse(head);
        while (tmp != null) {
            if (tmp.val == 1) {
                ans += Math.pow(2, cnt);
            }
            cnt++;
            tmp = tmp.next;
        }
        return ans;
    }

    public static ListNode reverse(ListNode head) {
        ListNode pre = null;
        ListNode hind = head;
        while (hind != null) {
            ListNode tmp = hind.next;
            hind.next = pre;
            if (hind.next == head) head = hind;
            hind.next = tmp;
            pre = hind;
        }
        return head;
    }

    /**
     * 顺次序，每一位都比前一位大于1都整数，求范围内所有顺次序数
     * 输出：low = 100, high = 300
     * 输出：[123,234]
     */
    public static List<Integer> sequentialDigits(int low, int high) {
        List<Integer> list = new ArrayList<>();
        String highStr = String.valueOf(high);
        List<Integer> tmpList = new ArrayList<>();
        for (int j = 1; j <= 10 - highStr.length(); j++) {
            int tmpNum = 0;
            for (int i = j; i < highStr.length() + j; i++) {
                tmpNum *= 10;
                tmpNum += i;
            }
            tmpList.add(tmpNum);
        }
        for (int i = 0; i < tmpList.size()-1; i++) {
            helper(list, tmpList.get(i), highStr.length(), low, high);
        }
        helper(list, tmpList.get(tmpList.size()-1)/10, 0, low, high);
        helper(list, (int)(tmpList.get(tmpList.size()-1) % (Math.pow(10, highStr.length()-1))), 0, low, high);
        List<Integer> result = new ArrayList<>();
        list.stream().sorted(Integer::compareTo).forEach(i->result.add(i));
        return result;
    }

    public static void helper(List<Integer> list, int num, int cnt, int low, int high) {
        if (num < low) {
            return ;
        }
        if (num >= low && num <= high) {
            list.add(num);
        }
        helper(list, num/10, cnt-1, low, high);
//        helper(list, (int)(num%(Math.pow(10, cnt-1))), cnt-1, low, high);

    }


    public static void main(String[] args) {
        sequentialDigits(4000, 9000);
    }
}
