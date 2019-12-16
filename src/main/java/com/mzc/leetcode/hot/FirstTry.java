package com.mzc.leetcode.hot;

import com.mzc.leetcode.inst.TreeNode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

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
        for (char c: tasks)
            map[c - 'A']++;
        Arrays.sort(map);
        int max_val = map[25] - 1, idle_slots = max_val * n;
        for (int i = 24; i >= 0 && map[i] > 0; i--) {
            idle_slots -= Math.min(map[i], max_val);
        }
        return idle_slots > 0 ? idle_slots + tasks.length : tasks.length;
    }
}
