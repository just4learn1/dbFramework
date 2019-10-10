package com.mzc.leetcode;

import java.sql.Timestamp;

public class ArrayFind {

    /**
     * 找到两个有序数组的中位数
     * <p>
     * 例： {1, 2}  {3, 4}   return (2+3)/2 = 2.5
     * <p>
     * solution： m = nums1.length, n=nums2.length  -> i{0, m}, j{0, n}
     * 将nums1和nums2都分为两部分  {0, i-1}->{i, m}   {0, j-1}->{j, n}， 因此只要 （左边总数等于右边总数）i+j = m-i + n-j 且 （左边数都小于右边）nums1[i-1]<nums2[j] && nums2[j-1]=nums1[i] （因为本来有序所以这个条件就可以了）
     * 如果n>=m, j=(m+n+1)/2 - i          //+1主要是为了实现如果(m+n)%2==1时保证maxLeft就是所需的中位数
     *tt
     * @param nums1
     * @param nums2
     * @return
     */
    public static double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int m = nums1.length, n = nums2.length;
        int[] A = nums1, B = nums2;
        if (m > n) {                //保证n>=m
            int[] tmp = B;
            B = A;
            A = tmp;
            int tmpLen = m;
            m = n;
            n = tmpLen;
        }
        int iMin = 0, iMax = m, halfLen = (m + n + 1) / 2;
        while (iMin <= iMax) {
            int i = (iMin + iMax) / 2;
            int j = halfLen - i;
            if (i < iMax && A[i] < B[j - 1]) {          //i 太大
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
            }
        }
        return 0;
    }


    /**
     * 给定一个经过旋转的有序数组与target，查找target在数组中的索引位置，没有返回-1，要求复杂度为O(logn)
     * <p>
     * 例如：
     * Input: nums = [4,5,6,7,0,1,2], target = 0
     * Output: 4
     * <p>
     * Input: nums = [4,5,6,7,0,1,2], target = 3
     * Output: -1
     *
     * @param nums
     * @param target
     * @return
     */
    public static int search(int[] nums, int target) {
        int start = 0;
        int end = nums.length - 1;
        while (start <= end) {
            int mid = (start + end + 1) / 2;
            if (nums[mid] == target) {
                return mid;
            }
            if (start == end) {
                return -1;
            }
            if (target > nums[end] && target < nums[start]) {
                return -1;
            }
            if (nums[mid] >= nums[start]) {     //start 至 mid的数据是升序的
                if (target >= nums[start] && target <= nums[mid]) { //target在start至mid范围内
                    end = mid - 1;
                } else {
                    start = mid + 1;
                }
//            } else if (nums[mid] <= nums[end]){     //mid 至 end 为升序排列
            } else {                //由于nums为升序有序数组旋转后的结果，所以如果start-mid不是有序的话，mid-end肯定为有序
                if (target >= nums[mid] && target <= nums[end]) {       //target处于 mid至end
                    start = mid + 1;
                } else {
                    end = mid - 1;
                }
            }
        }
        return -1;
    }

    /**
     * 从有序数组中找到target最先及最后出现的索引位置
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

    /**
     * 二分法查找target在nums中的位置
     *
     * @param nums
     * @param target
     * @param left   true为返回数组最左侧的匹配到的索引， false返回最右侧索引
     * @return
     */
    private static int extremeInsertionIndex(int[] nums, int target, boolean left) {
        int start = 0;
        int end = nums.length;
        while (start < end) {
            int mid = (start + end) /2 ;
            if (nums[mid] > target || (left && nums[mid] == target)) {      //target小于nums[mid]，或者需要查找最左侧索引且target==nums[mid]
                end = mid;
            } else {
                start = mid + 1;
            }
        }
        return start;
    }

    /**
     * 给定一个有序数组nums和target，如果nums中包含target则返回其索引位置，不包含则返回target在nums中的序列（将target插入该索引位置后nums依旧为有序数组）
     *
     * Input: [1,3,5,6], 5
     * Output: 2
     * Input: [1,3,5,6], 7
     * Output: 4
     * Input: [1,3,5,6], 0
     * Output: 0
     * @param nums
     * @param target
     * @return
     */
    public static int searchInsert(int[] nums, int target) {
        int start = 0;
        int end = nums.length - 1;
        while (start < end) {
            int mid = (start + end + 1) / 2;
            if (nums[mid] == target) {
                return mid;
            } else if (nums[mid] < target) {
                start = mid + 1;
            } else {
                end = mid - 1;
            }
        }
        if (start >= nums.length) {
            return nums.length;
        } else if (nums[start] < target) {
            return start + 1;
        } else {
            return start;
        }
    }

    public static void main(String[] args) {
//        int[] a = new int[]{4,5};
//        int[] b = new int[]{1,2,3};
//        System.out.println(findMedianSortedArrays(a,b));
        System.out.println(new Timestamp(1561719600_000L));
    }

}
