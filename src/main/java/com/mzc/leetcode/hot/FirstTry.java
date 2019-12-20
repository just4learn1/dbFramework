package com.mzc.leetcode.hot;

import com.mzc.leetcode.inst.ListNode;
import com.mzc.leetcode.inst.TreeNode;
import javafx.concurrent.Worker;

import java.util.*;

/**
 * create by zhencai.ma on 2019/12/16
 */
public class FirstTry {

    /**
     * 338
     * 给定非负整数num，n的取值范围是{0, num}，求出每个n转换为二进制后包含1的个数
     * 输入: 2
     * 输出: [0,1,1]
     * 思路：最简单的暴力解法是利用 for(;n!=0;ans++) {n&=n-1;}来统计 {0, num}中所有整数中位包含1的个数
     * 解法2：可以找出规律，f(x) = f(x&x-1)+1，这是因为f(x&x-1)等价于求x中'1'个数的最后一步，执行这步时在+1就是f(x)的解
     */
    public int[] countBits(int num) {
        int[] ans = new int[num + 1];
        //初始条件是ans[0]=0，因此可以省略
        for (int i = 1; i <= num; i++) {
            ans[i] = ans[i & i - 1] + 1;
        }
        return ans;
    }

    /**
     * 输入一个无符号整数n，返回n转换为二进制后包含‘1’的个数
     * 思路：因为是无符号整数，因此为32位，如[解法1]，等价于对于32位，对比每一位是否为1
     * 解法2：基本思路是 n&(n-1)总能将n的最低位设置为0
     */
    public int hammingWeight(int n) {
        //解法1
        /*int mask = 1;
        int ans = 0;
        for (int i = 0; i < 32; i++) {
            if ((n & mask) != 0) {
                ans++;
            }
            mask <<= 1;
        }
        return ans;*/
        //解法2
        /*int ans = 0;
        while (n != 0) {
            ans++;
            n &= (n-1);
        }
        return ans;*/
        //解法2的另一种实现方式
        int ans = 0;
        for (; n != 0; ans++) {
            n &= n - 1;
        }
        return ans;
    }

    /**
     * 461
     * 求两个数的汉明距离(汉明距离指两个数对应二进制位中不同的个数)
     */
    public int hammingDistance(int x, int y) {
        int c = x ^ y;
        int ans = 0;
        for (; c != 0; ans++) {
            c &= c - 1;
        }
        return ans;
    }

    /**
     * 538
     * 将二叉搜索树转换为累加树(使得每个节点的值是其所有子节点中大于它的节点值与原始值的和)
     * 输入: 二叉搜索树:
     * 5
     * /   \
     * 2     13
     * <p>
     * 输出: 转换为累加树:
     * 18
     * /   \
     * 20     13
     */
    int add = 0;

    public TreeNode convertBST(TreeNode root) {
        if (root != null) {
            convertBST(root.right);
            add += root.val;
            root.val = add;
            convertBST(root.left);
        }
        return root;
    }

    /**
     * 621
     * 给定一个用字符数组表示的 CPU 需要执行的任务列表。其中包含使用大写的 A - Z 字母表示的26 种不同种类的任务。任务可以以任意顺序执行，
     * 并且每个任务都可以在 1 个单位时间内执行完。CPU 在任何一个单位时间内都可以执行一个任务，或者在待命状态。
     * <p>
     * 然而，两个相同种类的任务之间必须有长度为 n 的冷却时间，因此至少有连续 n 个单位时间内 CPU 在执行不同的任务，或者在待命状态。
     * <p>
     * 你需要计算完成所有任务所需要的最短时间。
     * <p>
     * 示例 1：
     * <p>
     * 输入: tasks = ["A","A","A","B","B","B"], n = 2
     * 输出: 8
     * 执行顺序: A -> B -> (待命) -> A -> B -> (待命) -> A -> B.
     */
    public int leastInterval(char[] tasks, int n) {
        //基本思路，归类统计出每种任务的次数，在每次达到cpu使用间隔时都优先安排剩余次数最多的任务  （更优的解法需要统计规律）
        int[] map = new int[26];
        for (char c : tasks)
            map[c - 'A']++;
        Arrays.sort(map);
        int max_val = map[25] - 1, idle_slots = max_val * n;
        for (int i = 24; i >= 0 && map[i] > 0; i--) {
            idle_slots -= Math.min(map[i], max_val);
        }
        return idle_slots > 0 ? idle_slots + tasks.length : tasks.length;
    }

    /**
     * 72
     * 给定两个单词 word1 和 word2，计算出将 word1 转换成 word2 所使用的最少操作数 。
     * <p>
     * 你可以对一个单词进行如下三种操作：
     * <p>
     * 插入一个字符
     * 删除一个字符
     * 替换一个字符
     * 示例 1:
     * <p>
     * 输入: word1 = "horse", word2 = "ros"
     * 输出: 3
     * 解释:
     * horse -> rorse (将 'h' 替换为 'r')
     * rorse -> rose (删除 'r')
     * rose -> ros (删除 'e')
     */
    public int minDistance(String word1, String word2) {
        int length1 = word1.length();
        int length2 = word2.length();
        int[][] minOp = new int[length2 + 1][length1 + 1];
        for (int i = 1; i <= length1; i++) {
            minOp[0][i] = i;
        }
        for (int i = 1; i <= length2; i++) {
            minOp[i][0] = i;
        }
        for (int i = 1; i <= length2; i++) {
            for (int j = 1; j <= length1; j++) {
                if (word1.charAt(j - 1) == word2.charAt(i - 1)) {
                    minOp[i][j] = minOp[i - 1][j - 1];
                } else {
                    minOp[i][j] = 1 + Math.min(minOp[i - 1][j - 1], Math.min(minOp[i][j - 1], minOp[i - 1][j]));
                }
            }
        }
        return minOp[length2][length1];
    }

    /**
     * 4
     * 求两个有序数组的中位数
     */
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        //{0,i-1} {i, m}   {0, j-1}{j, n}  i+j-2=m+n-i-j   j=(m+n)/2+1-j或者 j=(m+n+1)/2-i  需求n>=m，否则j有可能出现负值
        int[] A = nums1;
        int[] B = nums2;
        int m = A.length, n = B.length;
        if (n < m) {
            int[] t1 = A;
            A = B;
            B = t1;
            int t2 = m;
            m = n;
            n = t2;
        }
        int c = (m + n + 1) / 2;
        int iMin = 0, iMax = m;
        while (iMin <= iMax) {
            int i = (iMin + iMax) / 2;
            int j = c - i;
            if (i > iMin && A[i - 1] > B[j]) {    //i太大
                iMax = i - 1;
            } else if (i < iMax && B[j - 1] > A[i]) {
                iMin = i + 1;
            } else {
                int maxLeft;
                if (i <= 0) {
                    maxLeft = B[j - 1];
                } else if (j <= 0) {
                    maxLeft = A[i - 1];
                } else {
                    maxLeft = Math.max(A[i - 1], B[j - 1]);
                }
                if ((m + n) % 2 == 1) {
                    return maxLeft;
                }
                int minRight;
                if (i >= m) {
                    minRight = B[j];
                } else if (j >= n) {
                    minRight = A[i];
                } else {
                    minRight = Math.min(A[i], B[j]);
                }
                return (maxLeft + minRight) / 2d;
            }
        }
        return 0d;
    }

    /**
     * 85
     * 给定矩阵，其中之包含'0'和'1'，求由'1'组成的矩形的最大面积
     * 输入:
     * [
     * ["1","0","1","0","0"],
     * ["1","0","1","1","1"],
     * ["1","1","1","1","1"],
     * ["1","0","0","1","0"]
     * ]
     * [1, 0, 1, 0, 0]
     * [2, 0, 2, 1, 1]
     * [3, 1, 3, 2, 2]
     * [4, 0, 0, 3, 0]
     * 输出: 6
     */
    public int maximalRectangle(char[][] matrix) {
        int m = matrix.length;
        if (m == 0) {
            return 0;
        }
        int n = matrix[0].length;
        int[][] dp = new int[m][n];
        int ans = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (i == 0) {
                    dp[i][j] = matrix[i][j] == '1' ? 1 : 0;
                } else {
                    dp[i][j] = matrix[i][j] == '0' ? 0 : dp[i - 1][j] + 1;
                }
                int min = dp[i][j];
                for (int k = j; k >= 0; k--) {
                    if (min == 0) {
                        break;
                    }
                    min = Math.min(min, dp[i][k]);
                    ans = Math.max(ans, (j - k + 1) * min);
                }
            }
        }
        return ans;
    }

    /**
     * 3
     * 给定字符串s，求出其不包含重复字符的最长子串的长度
     * 输入: "abcabcbb"
     * 输出: 3
     * 解释: 因为无重复字符的最长子串是 "abc"，所以其长度为 3。
     */
    public int lengthOfLongestSubstring(String s) {
        int ans = 0;
        int[] indexs = new int[128];
        int j = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            j = Math.max(j, indexs[c]);
            ans = Math.max(ans, i - j + 1);
            indexs[c] = i + 1;
        }
        return ans;
    }

    /**
     * 2
     * 给定两个链表，求其和
     * 输入：(2 -> 4 -> 3) + (5 -> 6 -> 4)
     * 输出：7 -> 0 -> 8
     * 原因：342 + 465 = 807
     */
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode root = new ListNode(0);
        ListNode tmp = root;
        int extra = 0;
        int val;
        while (l1 != null || l2 != null) {
            val = extra;
            if (l1 != null) {
                val += l1.val;
                l1 = l1.next;
            }
            if (l2 != null) {
                val += l2.val;
                l2 = l2.next;
            }
            extra = val / 10;
            val = val % 10;
            tmp.next = new ListNode(val);
            tmp = tmp.next;
        }
        if (extra > 0) {        //将此条件从while循环中单独提取出来可以降低在最后一次进位情况发生时代码执行判断的次数，从而提高效率
            tmp.next = new ListNode(extra);
        }
        return root.next;
    }

    /**
     * 300
     * 给定一个无序的整数数组，找到其中最长上升子序列的长度。
     * <p>
     * 示例:
     * <p>
     * 输入: [10,9,2,5,3,7,101,18]
     * 输出: 4
     * 解释: 最长的上升子序列是 [2,3,7,101]，它的长度是 4。
     */
    public int lengthOfLIS(int[] nums) {
        //dp解法，正序遍历nums，dp[i]=oldVal+1, oldVal需要从遍历dp的{0,i}，如果当前值大于nums[j]，则oldVal=Math.max(oldVal, dp[j]) 时间复杂度是O(n^2)
        //以下解法是利用二分查找法和动态规划一块，使时间复杂度降低到O(nlogn)
        int[] dp = new int[nums.length];
        int len = 0;
        for (int n : nums) {
            int i = Arrays.binarySearch(dp, 0, len, n);
            if (i < 0) {        //binarySearch返回值如果大于0代表找到了目标元素，返回其索引位置，小于0则返回值+1的负值就是其插入位置
                i = -(i + 1);
            }
            dp[i] = n;
            if (i == len) {
                len++;
            }
        }
        return len;
    }

    /**
     * 15
     * 从给定数组中找到三个元素，使其和等于0
     * 例如, 给定数组 nums = [-1, 0, 1, 2, -1, -4]，
     * <p>
     * 满足要求的三元组集合为：
     * [
     * [-1, 0, 1],
     * [-1, -1, 2]
     * ]
     */
    public static List<List<Integer>> threeSum(int[] nums) {
        Arrays.sort(nums);
        List<List<Integer>> result = new ArrayList<>();
        for (int i = 0; i < nums.length; i++) {
            if (i > 0 && nums[i] == nums[i-1]) {
                continue;
            }
            int tmp = -nums[i];
            int l = i+1, r = nums.length - 1;
            if (tmp >= 0 && nums[r] < 0) {
                break;
            }
            if (l <= r && tmp <= 0 && nums[l] > 0) {
                break;
            }
            while (l < r) {
                int sum = nums[l] + nums[r];
                if (sum == tmp) {
                    result.add(Arrays.asList(nums[i], nums[l], nums[r]));
                    do {
                        l++;
                    } while(l < r && nums[l] == nums[l-1]);
                    do {
                        r--;
                    } while (l < r && nums[r] == nums[r+1]);
                } else if (sum > tmp) {
                    r--;
                } else {
                    l++;
                }
            }
        }
        return result;
    }

    /**
     * 322
     * 给定硬币面值和目标金额，求使用最少个数的硬币凑成目标金额，并返回使用的硬币数量，如果无法完成返回-1
     * 输入: coins = [1, 2, 5], amount = 11
     * 输出: 3
     * 解释: 11 = 5 + 5 + 1
     * 输入: coins = [2], amount = 3
     * 输出: -1
     */
    public int coinChange(int[] coins, int amount) {
        int max = amount + 1;
        int[] dp = new int[max];
        Arrays.fill(dp, max);
        dp[0] = 0;
        for (int i = 1; i < max; i++) {
            for (int j = 0; j < coins.length; j++) {
                if (coins[j] <= i) {
                    dp[i] = Math.min(dp[i], dp[i-coins[j]]+1);
                }
            }
        }
        return dp[amount] > amount ? -1 : dp[amount];
    }

    /**
     * 394
     * 给定一个经过编码的字符串，返回它解码后的字符串。
     *
     * 编码规则为: k[encoded_string]，表示其中方括号内部的 encoded_string 正好重复 k 次。注意 k 保证为正整数。
     *
     * 你可以认为输入字符串总是有效的；输入字符串中没有额外的空格，且输入的方括号总是符合格式要求的。
     *
     * 此外，你可以认为原始数据不包含数字，所有的数字只表示重复的次数 k ，例如不会出现像 3a 或 2[4] 的输入。
     * s = "3[a]2[bc]", 返回 "aaabcbc".
     * s = "3[a2[c]]", 返回 "accaccacc".
     * s = "2[abc]3[cd]ef]", 返回 "abcabccdcdcdef".
     */
    public String decodeString(String s) {
    }

}
