package com.mzc.leetcode;

/**
 * 生成数独版是否可以考虑先生成完整的数独版，然后从其中扣除部分数字？
 */
public class Sudoku {

    /**
     * 判断数度板是否合法
     * <p>
     * Input:
     * [
     * ["5","3",".",".","7",".",".",".","."],
     * ["6",".",".","1","9","5",".",".","."],
     * [".","9","8",".",".",".",".","6","."],
     * ["8",".",".",".","6",".",".",".","3"],
     * ["4",".",".","8",".","3",".",".","1"],
     * ["7",".",".",".","2",".",".",".","6"],
     * [".","6",".",".",".",".","2","8","."],
     * [".",".",".","4","1","9",".",".","5"],
     * [".",".",".",".","8",".",".","7","9"]
     * ]
     * Output: true
     * <p>
     * Input:
     * [
     * ["8","3",".",".","7",".",".",".","."],
     * ["6",".",".","1","9","5",".",".","."],
     * [".","9","8",".",".",".",".","6","."],
     * ["8",".",".",".","6",".",".",".","3"],
     * ["4",".",".","8",".","3",".",".","1"],
     * ["7",".",".",".","2",".",".",".","6"],
     * [".","6",".",".",".",".","2","8","."],
     * [".",".",".","4","1","9",".",".","5"],
     * [".",".",".",".","8",".",".","7","9"]
     * ]
     * Output: false
     * Explanation: Same as Example 1, except with the 5 in the top left corner being
     * modified to 8. Since there are two 8's in the top left 3x3 sub-box, it is invalid.
     *
     * @param board
     * @return
     */
    public boolean isValidSudoku(char[][] board) {
        int[][] col = new int[9][9];
        int[][] sub = new int[9][9];
        for (int i = 0; i < 9; i++) {
            int[] row = new int[9];
            for (int j = 0; j < 9; j++) {
                if (board[i][j] != '.') {
                    int val = (int) board[i][j] - (int) '0';        //计算当前格中的数  ((int)'0' = 48, (int)'9'=57)
                    int subRowNum = i / 3 * 3 + j / 3;
                    if (row[val - 1] > 0)
                        return false;
                    else
                        row[val - 1] = val;

                    if (col[j][val - 1] > 0)
                        return false;
                    else
                        col[j][val - 1] = val;
                    if (sub[subRowNum][val - 1] > 0)
                        return false;
                    else
                        sub[subRowNum][val - 1] = val;
                }

            }
        }
        return true;
    }

    public static void main(String[] args) {
        int dd = '9' - '0';
        System.out.println(dd);
    }
}
