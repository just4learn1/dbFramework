package com.mzc.java8InAction.lambda;

import com.mzc.java8InAction.lambda.instance.Apple;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;

public class FilterApples {

    public static void main(String[] args) {
        filter();
//        List<Apple> list = Arrays.asList(new Apple(1,"green"), new Apple(3, "red"), new Apple(2, "yellow"));
//        sort(list);
    }

    public static void filter() {
        List<Apple> list = Arrays.asList(new Apple(1, "green"), new Apple(3, "red"), new Apple(2, "yellow"));

//        List<Apple> resultList = filter(list, FilterApples::colorFilter);           //利用lambda表达式，将Predicate作为参数传入方法中，在具体执行逻辑时就可以做到类似于将方法作为参数传递的功能
//        List<Apple> resultList = filter(list, a->a.color.equals("red"));              //在使用方法时可以使用匿名方法也可以使用定义好的方法
//        List<Apple> resultList = filter(list, a->FilterApples.weightFilter(a, 2));           //可以扩展过滤方法，让其变得更加灵活，不过就无法使用 :: 操作符来调用了
        List<Apple> resultList = filter(list, testWeight);
        System.out.println(resultList);
    }

    public void nonStaticFilter() {
        List<Apple> list = Arrays.asList(new Apple(1, "green"), new Apple(3, "red"), new Apple(2, "yellow"));
        List<Apple> resultList = filter(list, this::colorFilter2);           //在非静态方法中，可以使用this关键字代替FilterApples，实现对非静态方法的调用
    }

    public static Predicate<Apple> testWeight = (a) -> a.weight <= 2;           //可以通过lambda表达式及FunctionInterface接口将方法定义为参数，从而使方法也变为一等公民，在使用时直接作为参数传输即可

    public static boolean weightFilter(Apple a) {
        return a.weight < 2;
    }

    public static boolean weightFilter(Apple a, int weight) {
        return a.weight < weight;
    }

    public static boolean colorFilter(Apple a) {
        return a.color.equals("green");
    }

    public boolean colorFilter2(Apple a) {
        return a.color.equals("green");
    }

    /**
     * Predicate可以扩展为其他的，比如自定义FunctionInterface接口，预定义一个接口方法，在外部就可以利用此方法对传入的数据做具体的处理，并返回特定内容
     * <p>
     * 类似Predicate的有Function（接收一个参数，返回另一个值）   Consumer（接收一个参数，返回void） Supplier （不接收任何参数，返回特定类型值）
     * <p>
     * 由于装箱和拆箱操作，jdk提供了一系列的基础类型对应的FuncitonInterface操作的抽象接口，以适用于基础数据类型（int， double， boolean， float等）
     *
     * @param list
     * @param predicate
     * @return
     */
    public static List<Apple> filter(List<Apple> list, Predicate<Apple> predicate) {
        return list.stream().filter(a -> predicate.test(a)).collect(toList());
    }

    public static int sortedByWeight(Apple a1, Apple a2) {
        return a1.weight - a2.weight;
    }


    public static List<Apple> sort(List<Apple> list) {
        List<Apple> resultList = list.stream().sorted(FilterApples::sortedByWeight).collect(toList());
//        List<Apple> resultList = list.stream().sorted((a1, a2)->a1.weight-a2.weight).collect(toList());
        System.out.println(resultList);
        return resultList;
    }

    /**
     * 自定义foreach方法，遍历list并以list中的每个Apple对象作为参数执行consumer方法
     *
     * @param list
     * @param consumer
     */
    public static void forEach(List<Apple> list, Consumer<Apple> consumer) {
        for (Apple a : list) {
            consumer.accept(a);
        }
    }

    public static void sortApples(List<Apple> list) {
        list.sort(Comparator.comparing((a)->a.weight));
        list.sort(Comparator.comparing(Apple::getWeight).reversed());
        //thenComparing是在getWeight相同的情况下再根据GetColor返回的值来进行排序 (俗称比较器链)
        list.sort(Comparator.comparing(Apple::getWeight).reversed().thenComparing(Apple::getColor));
    }
}
