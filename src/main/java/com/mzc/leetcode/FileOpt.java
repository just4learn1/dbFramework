package com.mzc.leetcode;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

public class FileOpt {

    public static void main(String[] args) throws IOException, InterruptedException {
        copyFile("D:\\xyj_branch\\xyj_trunk\\client", "D:\\tmp\\test4");
    }

    /**
     * 多线程批量拷贝文件到目标路径
     *
     * @param basePath
     * @param destPath
     * @throws IOException
     * @throws InterruptedException
     */
//    完成， 耗时:1603789    deep=2
//    完成， 耗时:674635    deep=5
    public static void copyFile(String basePath, String destPath) throws IOException, InterruptedException {
        long startTime = System.currentTimeMillis();
        File base = new File(basePath);
        ConcurrentLinkedQueue<File> queue = new ConcurrentLinkedQueue();
        collectFiles(queue, base, 5, basePath, destPath);
        int threadCnt = Runtime.getRuntime().availableProcessors();
        Thread[] threads = new Thread[threadCnt];

        for (int i = 0; i < threadCnt; i++) {
            threads[i] = new Thread(new CopyFileTask(queue, basePath, destPath), "copyThread_" + i);
            threads[i].start();
        }
        boolean allDone = true;
        do {
            allDone = true;
            TimeUnit.SECONDS.sleep(1);
            for (int i = 0; i < threadCnt; i++) {
                if (threads[i].getState() != Thread.State.TERMINATED) {
                    allDone = false;
                    break;
                }
            }
        } while (!allDone);
        System.out.printf("完成， 耗时:%d\n", (System.currentTimeMillis() - startTime));
    }

    private static void collectFiles(ConcurrentLinkedQueue queue, File baseFile, int deep, String basePath, String destPath) {
        if (!baseFile.isDirectory()) {
            queue.offer(baseFile);
        } else {
            String filePath = baseFile.getAbsolutePath();
            String tmpPath = filePath.replace(basePath, "");
            File destDir = new File(destPath + File.separator + tmpPath);
            if (!destDir.exists()) {
                destDir.mkdirs();
            }
            for (File f : baseFile.listFiles()) {
                if (deep > 0) {
                    collectFiles(queue, f, deep - 1, basePath, destPath);
                } else {
                    queue.offer(f);
                }
            }
        }
    }

    private static void copyImp(File f, String basePath, String destPath) {
        String filePath = f.getAbsolutePath();
        String tmpPath = filePath.replace(basePath, "");
        if (f.isDirectory()) {
            File destDir = new File(destPath + File.separator + tmpPath);
            if (!destDir.exists()) {
                destDir.mkdirs();
            }
            for (File ff : f.listFiles()) {
                copyImp(ff, basePath, destPath);
            }
            return;
        }
        String realDest = destPath + File.separator + tmpPath;
        try {
            Files.copy(Paths.get(filePath), Paths.get(realDest));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class CopyFileTask implements Runnable {
        private ConcurrentLinkedQueue<File> queue;
        private String basePath, destPath;

        public CopyFileTask(ConcurrentLinkedQueue<File> queue, String basePath, String destPath) {
            this.queue = queue;
            this.basePath = basePath;
            this.destPath = destPath;
        }

        @Override
        public void run() {
            File f;
            while ((f = queue.poll()) != null) {
//                System.out.println(f.getAbsolutePath());
                FileOpt.copyImp(f, basePath, destPath);
            }
        }
    }
}
