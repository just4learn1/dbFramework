package com.mzc.testmp;

import java.util.concurrent.TimeUnit;

/**
 * create by zhencai.ma on 2019/11/7
 */
public class Singleton {

    private Singleton(){
        System.out.println("创建实例");
    }

    public static Singleton getInst() {
        return SingletonInner.inst;
    }

    static class SingletonInner {
        private static Singleton inst = new Singleton();
    }

    public static void main(String[] args) throws InterruptedException {
        TimeUnit.SECONDS.sleep(1);
        for (int i = 0; i < Runtime.getRuntime().availableProcessors(); i++) {
            new Thread(()->{
                System.out.printf("%s, inst hashCode:%s\n", Thread.currentThread().getName(), Singleton.getInst().hashCode());
            }, "thread_" + i).start();
        }
        TimeUnit.SECONDS.sleep(2);
    }
}
