package com.mzc.simpleTest;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

public class Test {
    public static CyclicBarrier cyclicBarrier = new CyclicBarrier(2, ()->{
        System.out.println("test");
    });

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 2; i++) {

            new Thread(() -> {

                System.out.printf("%s is start!\n", Thread.currentThread().getName());
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
                System.out.printf("%s is end!\n", Thread.currentThread().getName());
            }, "thread_" + i).start();
        }
        TimeUnit.SECONDS.sleep(1);
    }
}
