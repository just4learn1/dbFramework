package com.mzc.java8InAction.lambda;

import java.util.function.Function;

public class FunctionCompose {
    Function<Integer, Integer> f = x->x+1;
    Function<Integer, Integer> g = x->x*3;
    /**
     * 代表先执行 f 之后再执行 g   等同于 (x+1)*3 只不过用更接近自然语言的形式表达出来，更易懂
     * 类似的再Function接口中还定义了andthen方法，与compose执行顺序相反(andthen是先执行调用者的apply，之后执行参数的apply)
     */
    Function<Integer, Integer> h = g.compose(f).andThen(f);
    int result = h.apply(5);

    public static void main(String[] args) {
        System.out.println(new FunctionCompose().result);
    }
}
