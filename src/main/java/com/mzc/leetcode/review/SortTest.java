package com.mzc.leetcode.review;

import java.util.Arrays;

/**
 * create by zhencai.ma on 2019/12/9
 */
public class SortTest {

    public static void main(String[] args) {
        int[] arr = new int[]{6,9,12,4,82,33,5,7};
        selectionSort(arr);
        System.out.println(Arrays.toString(arr));
    }

    /**
     * 插入排序(拿到第n位元素，循环与之前元素比较，小则调换位置)
     */
    public static void insertSort(int[] arr) {
    }

    /**
     * 冒泡排序(原则是如果一个数组为升序排序，那么其相邻两个元素必然也是升序排序)
     */
    public static void bubbleSort(int[] arr) {
        int len = arr.length;
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len - i - 1; j++) {
                if (arr[j] > arr[j+1]) {
                    swap(arr, j, j+1);
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
            for (int i = left+1; i < arr.length; i++) {
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
