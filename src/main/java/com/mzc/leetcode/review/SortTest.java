package com.mzc.leetcode.review;

import java.util.Arrays;

/**
 * create by zhencai.ma on 2019/12/9
 */
public class SortTest {

    public static int a = 0;

    public static void main(String[] args) {
        int[] arr = new int[]{5,6,8,8,2,8,12,9,3};
        qsort(arr, 0, arr.length-1);
        System.out.println(Arrays.toString(arr));

    }
    /**
     * 快速排序，核心思想从数组中找到一个元素，循环将所有大于这个数的元素放在其右边，小于的放在左边
     */
    public static void qsort(int[] arr, int left, int right) {
        int pivot = arr[left];
        int l = left;
        int r = right;
        while (l < r) {
            while (l < r && arr[r] > pivot) r--;
            while (l < r && arr[l] < pivot) l++;
            if (arr[l] == arr[r] && l < r) {
                l++;
            } else {
                swap(arr, l, r);
            }
        }
        if (l - 1 > left) qsort(arr, left, l - 1);
        if (r + 1 < right) qsort(arr, r + 1, right);
    }

    /**
     * 希尔排序
     * 先将整个待排序的记录序列分割成为若干子序列分别进行直接插入排序，具体算法描述：
     * <p>
     * 选择一个增量序列t1，t2，…，tk，其中ti>tj，tk=1；
     * 按增量序列个数k，对序列进行k 趟排序；
     * 每趟排序，根据对应的增量ti，将待排序列分割成若干长度为m 的子序列，分别对各子表进行直接插入排序。仅增量因子为1 时，整个序列作为一个表来处理，表长度即为整个序列的长度。
     */
    public static void shellSort(int[] arr) {
        // TODO: 2019/12/13
        int len = arr.length;
        for (int gap = (int) Math.floor(len / 2); gap > 0; gap = (int) Math.floor(gap / 2)) {
            for (int i = gap; i < len; i++) {
                int j = i;
                int current = arr[i];
                while (j - gap >= 0 && current < arr[j - gap]) {
                    arr[j] = arr[j - gap];
                    j = j - gap;
                }
                arr[j] = current;
            }
        }
    }

    /**
     * 插入排序(拿到第n位元素，循环与之前元素比较，小则调换位置)
     */
    public static void insertSort(int[] arr) {
        int start = 1;
        while (start < arr.length) {
            int j = start;
            int current = arr[start];
            while (j > 0 && current < arr[j - 1]) {
                arr[j] = arr[j - 1];
                j--;
            }
            arr[j] = current;
            start++;
        }
    }

    /**
     * 冒泡排序(原则是如果一个数组为升序排序，那么其相邻两个元素必然也是升序排序)
     */
    public static void bubbleSort(int[] arr) {
        int len = arr.length;
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    swap(arr, j, j + 1);
                }
            }
        }
    }

    /**
     * 选择排序(每次选择最大/最小的元素和index交换位置)
     */
    public static void selectionSort(int[] arr) {
        int left = 0;
        int leftIndex;
        while (left < arr.length) {
            leftIndex = left;
            for (int i = left + 1; i < arr.length; i++) {
                if (arr[leftIndex] > arr[i]) {
                    leftIndex = i;
                }
            }
            swap(arr, left++, leftIndex);
        }
    }

    public static void swap(int[] arr, int l, int r) {
        int tmp = arr[l];
        arr[l] = arr[r];
        arr[r] = tmp;
    }
}
