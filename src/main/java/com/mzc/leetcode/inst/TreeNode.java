package com.mzc.leetcode.inst;

/**
 * create by zhencai.ma on 2019/11/22
 */
public class TreeNode {
    public int val;
    public TreeNode left;
    public TreeNode right;

    public TreeNode(int val) {
        this.val = val;
    }

    @Override
    public String toString() {
        return  val +
                "->" + left +
                "->" + right ;
    }
}
