package com.mzc.testmp.filegroup;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Phaser;

/**
 * create by zhencai.ma on 2019/11/7
 */
public class FileReadTask implements Runnable {

    private File file;

    private LinkedBlockingQueue<GroupEntity> queue;

    private Phaser phaser;

    public FileReadTask(File file, LinkedBlockingQueue<GroupEntity> queue, Phaser phaser) {
        this.file = file;
        this.queue = queue;
        this.phaser = phaser;
    }

    @Override
    public void run(){
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(file.getAbsolutePath()), Charset.forName("utf-8"));) {
            String line;
            while ((line = reader.readLine()) != null) {
                GroupEntity entity = new GroupEntity(line);
                queue.offer(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.phaser.arrive();
    }
}
