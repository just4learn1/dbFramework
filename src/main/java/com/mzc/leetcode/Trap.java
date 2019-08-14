package com.mzc.leetcode;

public class Trap {

    /**
     * （leetcode 42）给定一组数，计算其储水量，例如[1, 0, 2]  其储水量就为1   [1,0,2,0,5] 其储水量为 3
     * <p>
     * <p>
     * Input: [0,1,0,2,1,0,1,3,2,1,2,1]
     * Output: 6
     *
     * @param A
     * @return
     */
    public int trap(int[] A) {
        if (A.length < 3) return 0;

        int ans = 0;
        int l = 0, r = A.length - 1;

        // find the left and right edge which can hold water
        while (l < r && A[l] <= A[l + 1]) l++;
        while (l < r && A[r] <= A[r - 1]) r--;

        while (l < r) {
            int left = A[l];
            int right = A[r];
            if (left <= right) {
                // add volum until an edge larger than the left edge
                while (l < r && left >= A[++l]) {
                    ans += left - A[l];
                }
            } else {
                // add volum until an edge larger than the right volum
                while (l < r && A[--r] <= right) {
                    ans += right - A[r];
                }
            }
        }
        return ans;
    }
}
