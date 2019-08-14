package com.mzc.leetcode;

public class Image {

    /**
     * 给定一个 n*n 2D图片，将其90度向右旋转
     * 例：
     * Given input matrix =
     * [
     * [1,2,3],
     * [4,5,6],
     * [7,8,9]
     * ],
     * <p>
     * rotate the input matrix in-place such that it becomes:
     * [
     * [7,4,1],
     * [8,5,2],
     * [9,6,3]
     * ]
     * <p>
     * solution：90度旋转后，原有列数转为行，原有行数转为 总长度-行-1
     *
     * @param matrix
     */
    public static void rotate(int[][] matrix) {
        int n = matrix.length;
        int i = 0;
        while (i < n / 2) {
            for (int j = i; j < n - i - 1; j++) {
                int temp = matrix[i][j];            //基础值
                matrix[i][j] = matrix[n - j - 1][i];    //row=n-j-1  column=i
                matrix[n - j - 1][i] = matrix[n - i - 1][n - j - 1];    //row=n-i-1, column=n-j-1
                matrix[n - i - 1][n - j - 1] = matrix[j][n - i - 1];    //row=n-(n-j-1)-1=j, column=n-i-1
                matrix[j][n - i - 1] = temp;
            }
            i++;
        }
    }
}
