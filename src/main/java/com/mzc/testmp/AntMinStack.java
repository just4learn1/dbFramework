package com.mzc.testmp;

import java.util.Stack;

/**
 * 设计含最小函数min(),pop(),push()的栈，存储数据元素为int
 * 要求min,push,pop的时间复杂度为O(1)
 */
public class AntMinStack {
    public Stack<Integer> stack;
    private Stack<Integer> min;

    public AntMinStack() {
        stack = new Stack<>();
        min = new Stack<>();
    }

    public void push(Integer num) {
        stack.push(num);
        if (min.isEmpty() || min.peek() > num) {
            min.push(num);
        } else {
            min.push(min.peek());
        }
    }

    public Integer min() {
        if (min.isEmpty()) {
            return null;
        }
        return min.peek();
    }

    public Integer pop() {
        if (stack.isEmpty()) {
            return null;
        }
        min.pop();
        return stack.pop();
    }
}
