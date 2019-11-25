package com.mzc.leetcode.tree;

import com.mzc.leetcode.inst.TreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * create by zhencai.ma on 2019/11/25
 */
public class TreeTest {
    /**
     * 判断给定的两个二叉树是否相同（节点数量、位置以及值都一样）
     * Input:     1         1
     *           / \       / \
     *          2   3     2   3
     *
     *         [1,2,3],   [1,2,3]
     *
     * Output: true
     */
    public boolean isSameTree(TreeNode p, TreeNode q) {
        if (p == null || q == null) {
            return p == null && q == null;
        }
        if (p.val != q.val) {
            return false;
        }
        boolean result = true;
        if (p.left != null || q.left != null) {
            result = isSameTree(p.left, q.left);
        }
        if (result && (p.right != null || q.right != null)) {
            result = isSameTree(p.right, q.right);
        }
        return result;
    }

    /**
     * 给定一个二叉树，判断其是否为镜面对称
     *     1
     *    / \
     *   2   2
     *  / \ / \
     * 3  4 4  3    true
     *
     *     1
     *    / \
     *   2   2
     *    \   \
     *    3    3    false
     */
    public boolean isSymmetric(TreeNode root) {
        if (root == null) {
            return true;
        }
        return isSymetricSame(root.left, root.right);
    }

    public boolean isSymetricSame(TreeNode t1, TreeNode t2) {
        if (t1 == null || t2 == null) {
            return t1 == null && t2 == null;
        }
        if (t1.val != t2.val) {
            return false;
        }
        boolean same = true;
        if (t1.left != null || t2.right != null) {
            same = isSymetricSame(t1.left, t2.right);
        }
        if (same) {
            if (t1.right != null || t2.left != null) {
                same = isSymetricSame(t1.right, t2.left);
            }
        }
        return same;
    }

    /**
     *  按照层级统计出二叉树元素
     *     3
     *    / \
     *   9  20
     *     /  \
     *    15   7
     *   return
     *   [
     *     [3],
     *     [9,20],
     *     [15,7]
     *   ]
     */
    public List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        genLevelOrder(result, root, 0);
        return result;
    }

    public void genLevelOrder(List<List<Integer>> result, TreeNode node, int lv) {
        if (node == null) {
            return ;
        }
        if (result.size() <= lv) {
            result.add(new ArrayList<>());
        }
        result.get(lv).add(node.val);
        genLevelOrder(result, node.left, lv+1);
        genLevelOrder(result, node.right, lv+1);
    }

}
