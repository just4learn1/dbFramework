package com.mzc.leetcode.inst;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * create by zhencai.ma on 2019/11/22
 */
@Data
@Accessors(chain = true)
public class TreeNode {
    public int val;
    public TreeNode left;
    public TreeNode right;

    public TreeNode(int val) {
        this.val = val;
    }
}
