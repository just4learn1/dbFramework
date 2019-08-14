package com.mzc.java8InAction.lambda;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class RunnableFutureTest {
    public static void main(String[] args) {
        test();
    }

    public static List<PriceCheck> shops = Arrays.asList(new PriceCheck("p1"), new PriceCheck("p2"), new PriceCheck("p3"), new PriceCheck("p4"));

    public static void test() {
        long start = System.currentTimeMillis();
        Stream<CompletableFuture<Integer>> stream = findPriceStream();
        CompletableFuture[] completableFutures =stream.map(f -> f.thenAccept(System.out::println)).peek(f-> System.out.println(f)).toArray(size -> new CompletableFuture[size]);//.join();
        CompletableFuture.allOf(completableFutures).join();
        System.out.println("end:" + (System.currentTimeMillis() - start));
    }

    public static Stream<CompletableFuture<Integer>> findPriceStream() {
        return shops.stream()
                .map(shop -> CompletableFuture.supplyAsync(
                        () -> shop.getPrice()));
    }

    static class PriceCheck {
        public String name;

        public PriceCheck(String name) {
            this.name = name;
        }

        public int getPrice() {
            int delayTime = ThreadLocalRandom.current().nextInt(5000);
//            System.out.printf("%s, getPirce delayTime %d\n", name, delayTime);
            try {
                TimeUnit.MILLISECONDS.sleep(delayTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int price = ThreadLocalRandom.current().nextInt(500);
            System.out.printf("%s price is %d  delayTime:%d\n", name, price, delayTime);
            return price;
        }
    }
}
