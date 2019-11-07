package com.mzc.testmp.filegroup;

import java.io.File;
import java.lang.Thread.State;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.*;

/**
 * create by zhencai.ma on 2019/11/7
 */
public class FileReadMain {

    private static final int THREAD_POOL_SIZE = 10;

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ThreadPoolExecutor service = (ThreadPoolExecutor) Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        String dirPath = "E:\\sortFile";
        LinkedBlockingQueue<GroupEntity> queue = new LinkedBlockingQueue<>();
        File dir = new File(dirPath);
        Phaser phaser = new Phaser(1);
        Arrays.stream(dir.listFiles(f -> f.getAbsolutePath().endsWith(".txt")))
                .forEach(f -> {
                    phaser.register();
                    service.execute(new FileReadTask(f, queue, phaser));
                });
        SortThread sortTask = new SortThread(queue);
        Thread sortThread = new Thread(sortTask, "sort_thread");
        sortThread.start();
        phaser.arriveAndAwaitAdvance();
        service.shutdown();
        sortThread.interrupt();
        while (sortThread.getState() != State.TERMINATED) {
            TimeUnit.MILLISECONDS.sleep(100);
        }
        sortTask.getResult().entrySet().stream().sorted(Map.Entry.comparingByKey())
                .forEachOrdered(entry -> {
                    System.out.printf("%s, %s\n", entry.getKey(), entry.getValue().poll());
                });
    }
}
