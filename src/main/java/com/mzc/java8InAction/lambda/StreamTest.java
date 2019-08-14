package com.mzc.java8InAction.lambda;

import com.mzc.java8InAction.lambda.instance.CaloricLevel;
import com.mzc.java8InAction.lambda.instance.Dish;
import com.mzc.java8InAction.lambda.instance.Type;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

public class StreamTest {

    public static void main(String[] args) {
        testGroupingBy(Arrays.asList(new Dish(600, "beef", false, Type.MEAT),
                new Dish(800, "beef2", false, Type.MEAT),
                new Dish(450, "fish1", true, Type.FISH),
                new Dish(480, "fish2", true, Type.FISH),
                new Dish(300, "dish1", true, Type.OTHER),
                new Dish(150, "dish2", true, Type.OTHER)
                ));
    }

    /**
     * 利用stream筛选卡路里小于150的菜品,并返回对应菜名列表
     *
     * @param list
     * @return
     */
    public static List<String> getDeCaloresDishNames(List<Dish> list) {
        return list.stream().filter(d -> d.getCalories() < 150).map(d -> d.getName()).collect(toList());
    }

    /**
     * list.stream().map(w -> w.split("")).map(Arrays::stream).distinct().collect(toList());  其中第一个map是将String转换为String[]，第二个map是将String[]转换为stream,因此这样最终得到的是一个stream列表
     * list.stream().map(w -> w.split("")).flatmap(Arrays::stream).distinct().collect(toList());  flatmap作用是将第一个map得到的数组映射成流的内容（相当于是把一个流里的每个值转换为一个流，并且将这些流组合
     * 成一个新的流）
     *
     * @param list
     */
    public static void getDistinctSplitedChars(List<String> list) {
        List<String> result = list.stream().map(w -> w.split("")).flatMap(Arrays::stream).distinct().collect(toList());
        System.out.println(result);
    }

    /**
     * 返回由给定int数组中每个元素的平方组成的数组
     *
     * @param list
     */
    public static void getSquareArray(List<Integer> list) {
        List<Integer> result = list.stream().map(a -> a * a).collect(toList());
        System.out.println(result);
    }

    /**
     * 给定两个数组，返回所有的数对
     * <p>
     * 例：给定列表[1, 2, 3]和列表[3, 4]
     * 返回 [(1, 3), (1, 4), (2, 3), (2, 4), (3, 3), (3, 4)]
     *
     * @param list1
     * @param list2
     */
    public static void recursiveArr(List<Integer> list1, List<Integer> list2) {
        List<Integer[]> result = list1.stream().flatMap(a -> list2.stream().map(b -> new Integer[]{a, b})).collect(toList());
        result.forEach(a -> System.out.println(Arrays.toString(a)));
    }

    /**
     * 扩展recursiveArr，返回和能被3整除的数对
     *
     * @param list1
     * @param list2
     */
    public static void recursiveArr2(List<Integer> list1, List<Integer> list2) {
        List<Integer[]> result = list1.stream().flatMap(a -> list2.stream().filter(b -> (a + b) % 3 == 0).map(c -> new Integer[]{a, c})).collect(toList());
        result.forEach(a -> System.out.println(Arrays.toString(a)));
//        if (result.stream().anyMatch(i->i[0] == i[1])) {
//            System.out.println("=======");
//        }
    }

    /**
     * reduce接收两个参数，第一个是初始值；第二个是BinaryOperator(继承BiFunction)，接收两个参数，第一个是初始值，第二个是流的元素，并且在之后计算中使用返回值为初始值
     *
     * @param numbers
     */
    public static void testReduce(List<Integer> numbers) {
        int result = numbers.stream().reduce((i, n) -> i * n).get();
        System.out.println(result);
    }

    /**
     * 生成勾股数
     * <p>
     * P: range和rangeClosed区别就是rangeClosed包含范围区间值(<=   >=)
     * Math.sqrt()返回值是一个double，因此，只要确定其取余1是0就代表两数和的平方根是整数，也就等于符合勾股数
     */
    public static void pythagoreanTriples() {

//        List<int[]> result = IntStream.rangeClosed(1, 100).boxed().flatMap(a->
//                IntStream.rangeClosed(a, 100).filter(b->Math.sqrt(a*a + b*b)%1==0).mapToObj(b->new int[]{a,b, (int) Math.sqrt(a*a+b*b)})
//        ).limit(3).collect(toList());
        //上面的算法会有两次平方根的计算，所以还有得优化
        List<double[]> result = IntStream.rangeClosed(1, 100).boxed().flatMap(a ->
                IntStream.rangeClosed(a, 100).mapToObj(b -> new double[]{a, b, Math.sqrt(a * a + b * b)}).filter(t -> t[2] % 1 == 0)
        ).limit(3).collect(toList());
        result.forEach(a -> System.out.println(Arrays.toString(a)));
    }

    /**
     * 对应的还有个generate()，也可以生产无限流，该方法接受一个Supplier接口(FunctionInterface)，用来提供流的元素
     */
    public static void unlimitStream() {
        Stream.iterate(500, n -> ThreadLocalRandom.current().nextInt(n)).skip(1).limit(3).forEach(System.out::println);
//        System.out.println(Stream.iterate(500, n-> ThreadLocalRandom.current().nextInt(n)).count());  //对于无界的stream，使用count会抛出IllegalArgumentException
    }

    /**
     * 将dish按照类型和calories分组
     * @param list
     */
    public static void testGroupingBy(List<Dish> list) {
        Map<Type, Map<CaloricLevel, List<Dish>>> result = list.stream().collect(groupingBy(Dish::getType,
                groupingBy(dish->{
                    if (dish.getCalories() <= 400) {
                        return CaloricLevel.DIET;
                    } else if (dish.getCalories() <= 700) {
                        return CaloricLevel.NORMAL;
                    }
                    return CaloricLevel.FAT;
                }))
        );
        result.forEach((k, v)->System.out.printf("%s -> %s\n", k.name(), v));
    }

    static enum QuestType{
        REPORT_BOARD_QUEST("上交悬赏任务次数"),   //触发点完成
        EQUIP_ENCHANCE_CNT("身上装备强化总等级 "),   //触发点完成
        CLIMB_TOWER_LV("爬塔到特定层数"),      //触发点完成
        INLAY_GEM_CNT("镶嵌n颗m级以上宝石"),//触发点完成
        BADGE_LV("勋章达到n阶m星"),//触发点完成
        WING_LV("翅膀达到n阶m星"),//触发点完成
        GEM_COMPOSE("合成n级宝石m次"),//触发点完成
        JOIN_GUILD("加入行会"), //触发点完成
        MINING("挖矿途径获得铜矿"),
        EQUIP_ACQUIRE("特定来源获取n件m级以上装备");

        private String name;

        QuestType(String name) {
            this.name = name;
        }
    }
}
