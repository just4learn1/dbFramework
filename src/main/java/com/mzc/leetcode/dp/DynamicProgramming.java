package com.mzc.leetcode.dp;

import com.mzc.leetcode.inst.TreeNode;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * create by zhencai.ma on 2019/11/14
 * 动态规划问题
 * <p>
 * 经典的动态规划问题是有充足的面值为1、5、11的钞票，给定一个数字，求最少使用多少张钞票可以凑齐数字
 * 此题基准为 f(1)=1  f(5)=5 f(11)=11
 * 然后以基准推出f(n)的解法-
 * 这种算法与暴力解法（列出所有可能）不同为其从基准起求出了所有的f(n)的最优解并记录下来，比如  f(15)=1+f(4) or f(15)=1+f(10) or f(15)=1+f(14)
 * 因此如果求出了f(4) f(10) f(14)的组合个数，进行比较就可以得到最优解
 */
public class DynamicProgramming {

    /**
     * 给定两个字符串，求最少几步可以将word1转换为word2，每一步只能对一个字符执行insert/delete/replace
     * Input: word1 = "horse", word2 = "ros"
     * Output: 3
     * Explanation:
     * horse -> rorse (replace 'h' with 'r')
     * rorse -> rose (remove 'r')
     * rose -> ros (remove 'e')
     */
    public static int minDistance(String word1, String word2) {
        int length1 = word1.length();
        int length2 = word2.length();
        int[][] minOp = new int[length2 + 1][length1 + 1];
            for (int i = 1; i <= length1; i++) {
                minOp[0][i] = i;
        }
        for (int j = 1; j <= length2; j++) {
            minOp[j][0] = j;
        }
        for (int i = 1; i <= length2; i++) {
            for (int j = 1; j <= length1; j++) {
                if (word2.charAt(i - 1) == word1.charAt(j - 1)) {
                    minOp[i][j] = minOp[i - 1][j - 1];
                } else {
                    minOp[i][j] = 1 + Math.min(minOp[i - 1][j - 1], Math.min(minOp[i][j - 1], minOp[i - 1][j]));
                }
            }
        }
        return minOp[length2][length1];
    }

    /**
     * 爬楼，每一次可以爬一个或者两个台阶，求有多少种唯一的走法可以到n阶
     * Input: 3
     * Output: 3
     * Explanation: There are three ways to climb to the top.
     * 1. 1 step + 1 step + 1 step
     * 2. 1 step + 2 steps
     * 3. 2 steps + 1 step
     *
     * @param n
     * @return
     */
    public static int climbStairs(int n) {
        int[] tmp = new int[n + 1];
        tmp[0] = 1;
        tmp[1] = 1;
        for (int i = 2; i <= n; i++) {
            tmp[i] = tmp[i - 2] + tmp[i - 1];
        }
        return tmp[n];
    }

    /**
     * 假设有一个机器人，站在一个m*n的格子地图上，求其从地图的左上角走到右下角有多少种唯一走法
     * 假定机器人只能向右和下方向行走
     * Input: m = 3, n = 2
     * Output: 3
     * Explanation:
     * From the top-left corner, there are a total of 3 ways to reach the bottom-right corner:
     * 1. Right -> Right -> Down
     * 2. Right -> Down -> Right
     * 3. Down -> Right -> Right
     */
    public static int uniquePaths(int m, int n) {
        int[][] tmp = new int[m][n];
        for (int i = 0; i < m; i++) {
            tmp[i][0] = 1;
        }
        for (int i = 0; i < n; i++) {
            tmp[0][i] = 1;
        }
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                tmp[i][j] = tmp[i - 1][j] + tmp[i][j - 1];
            }
        }
        return tmp[m - 1][n - 1];
    }

    /**
     * 还是机器人从左上角走到右下角并且只能向右或者向下移动，在给定的二维数组obstacleGrid中，元素等于1代表不可行走，求有多少条唯一的行走路线
     * Input:
     * [
     * [0,0,0],
     * [0,1,0],
     * [0,0,0]
     * ]
     * Output: 2
     * Explanation:
     * There is one obstacle in the middle of the 3x3 grid above.
     * There are two ways to reach the bottom-right corner:
     * 1. Right -> Right -> Down -> Down
     * 2. Down -> Down -> Right -> Right
     */
    public static int uniquePathsWithObstacles(int[][] obstacleGrid) {
        int m = obstacleGrid.length;
        int n = obstacleGrid[0].length;
        int[][] tmp = new int[m][n];
        for (int i = 0; i < m; i++) {
            if (i == 0 || tmp[i - 1][0] == 1) {
                tmp[i][0] = 1 - obstacleGrid[i][0];
            }
        }
        for (int i = 0; i < n; i++) {
            if (i == 0 || tmp[0][i - 1] == 1) {
                tmp[0][i] = 1 - obstacleGrid[0][i];
            }
        }
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                if (obstacleGrid[i][j] == 1) {
                    tmp[i][j] = 0;
                } else {
                    tmp[i][j] = tmp[i - 1][j] + tmp[i][j - 1];
                }
            }
        }
        return tmp[m - 1][n - 1];
    }

    /**
     * 给定一个二维数组，其中每个元素代表其价值，求从左上角到右下角价值最低的路线
     * 只能向下或者向右行走
     * [1, 3, 1]
     * [1, 5, 1]
     * [4, 2, 1]
     */
    public static int minPathSum(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        int[][] tmp = new int[m][n];
        for (int i = 0; i < m; i++) {           //第一行和第一列为基准
            if (i == 0) {
                tmp[i][0] = grid[i][0];
            } else {
                tmp[i][0] = grid[i][0] + tmp[i - 1][0];
            }
        }
        for (int i = 0; i < n; i++) {
            if (i == 0) {
                tmp[0][i] = grid[0][i];
            } else {
                tmp[0][i] = grid[0][i] + tmp[0][i - 1];
            }
        }
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                tmp[i][j] = Math.min(tmp[i - 1][j], tmp[i][j - 1]) + grid[i][j];
            }
        }
        return tmp[m - 1][n - 1];
    }
    /**
     * 给定一个二维数组，其中所有元素都是1或者0，找出只有1组成的矩形的最大面积
     * Input:
     * [
     *   ["1","0","1","0","0"],
     *   ["1","0","1","1","1"],
     *   ["1","1","1","1","1"],
     *   ["1","0","0","1","0"]
     * ]
     * Output: 6
     *  //dp解法，设置每个值为 if(curNum==0?0:matrix[i-1][j]+1)---需要特殊处理i==0的情况
     *  //此时等于dp[][]中每个元素代表的是其向上有几个为1的连续元素，所以最大面就就等同于反向遍历列j，求最大面积
     *   ["1","0","1","0","0"],
     *   ["2","0","2","1","1"],
     *   ["3","1","3","2","2"],
     *   ["4","0","0","3","0"]
     */
    public static int maximalRectangle(char[][] matrix) {
        if (matrix.length <= 0) return 0;
        int n = matrix.length;
        int m = matrix[0].length;
        int[][] dp = new int[n][m];
        int maxArea = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (i == 0)
                    dp[i][j] = matrix[i][j] == '1' ? 1 : 0;
                else
                    dp[i][j] = matrix[i][j] == '1' ? (dp[i-1][j] + 1) : 0;
                int min = dp[i][j];
                for (int k = j; k >= 0; k--) {
                    if (min == 0) break;
                    if (dp[i][k] < min) min = dp[i][k];
                    maxArea = Math.max(maxArea, min * (j - k + 1));
                }
            }
        }
        return maxArea;
    }

    /**
     * 给定s1，s2，s3，判断s3是否由s1，s2交错形成
     * Input: s1 = "aabcc", s2 = "dbbca", s3 = "aadbbcbcac"
     * Output: true
     * Input: s1 = "aabcc", s2 = "dbbca", s3 = "aadbbbaccc"
     * Output: false
     */
    public boolean isInterleave(String s1, String s2, String s3) {
        //dp[i][j] = case1 || case2
        //case1. dp[i-1][j]&&s1[i]==s3[i+j]
        //case2. dp[i][j-1]&&s2[j]==s3[i+j]
        if (s3.length() != s1.length() + s2.length())
            return false;
        boolean[][] dp = new boolean[s1.length() + 1][s2.length() + 1];
        dp[0][0] = true;
        for (int i = 1; i < dp.length; i++)
            dp[i][0] = (s1.charAt(i - 1) == s3.charAt(i - 1)) && dp[i - 1][0];
        for (int j = 1; j < dp[0].length; j++)
            dp[0][j] = (s2.charAt(j - 1) == s3.charAt(j - 1)) && dp[0][j - 1];
        for (int i = 1; i < dp.length; i++) {
            for (int j = 1; j < dp[0].length; j++)
                dp[i][j] = dp[i - 1][j] && (s1.charAt(i - 1) == s3.charAt(i + j - 1)) || dp[i][j - 1] && (s2.charAt(j - 1) == s3.charAt(i + j - 1));
        }
        return dp[dp.length - 1][dp[0].length - 1];
    }

    private static List<TreeNode> nullTreeNode = new ArrayList<>();
    static {
        nullTreeNode.add(null);
    }

    public static void main(String[] args) {
        generateTrees(3).stream().forEach(System.out::println);
    }

    public static List<TreeNode> generateTrees(int n) {
        List<TreeNode> dp[][] = new List[n + 1][n + 1];
        for (int i = 1; i <= n; i++) {
            dp[i][i] = Arrays.asList(new TreeNode(i));
        }
        for (int l = 2; l <= n; l++) {
            for (int start = 1; start <= n - l + 1; start++) {
                int end = l + start - 1;
                dp[start][end] = new ArrayList<>();
                for (int root = start; root <= end; root++) {
                    combine(root == start ? null : dp[start][root - 1], root == end ? null : dp[root + 1][end], dp[start][end], root);
                }
            }
        }
        return dp[1][n];
    }

    private static void combine(List<TreeNode> lefts, List<TreeNode> rights, List<TreeNode> ret, int val) {
        if (lefts == null) lefts = nullTreeNode;
        if (rights == null) rights = nullTreeNode;
        for (TreeNode left : lefts) {
            for (TreeNode right : rights) {
                TreeNode node = new TreeNode(val);
                node.left = left;
                node.right = right;
                ret.add(node);
            }
        }
    }
}
