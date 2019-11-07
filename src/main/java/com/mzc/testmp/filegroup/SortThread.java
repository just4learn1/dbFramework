package com.mzc.testmp.filegroup;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * create by zhencai.ma on 2019/11/7
 */
public class SortThread implements Runnable {

    private LinkedBlockingQueue<GroupEntity> queue;

    /**
     * 这地方可以考虑修改为 Map<String, GroupEntity> 内存中只存储quoto最小的Entity
     */
    private Map<String, PriorityQueue<GroupEntity>> map;

    public SortThread(LinkedBlockingQueue<GroupEntity> queue) {
        this.queue = queue;
        map = new HashMap<>();
    }

    @Override
    public void run() {
        boolean isAllDone = false;
        while (!isAllDone) {
            isAllDone = Thread.currentThread().interrupted();
            while (!queue.isEmpty()) {
                GroupEntity entity = queue.poll();
                map.compute(entity.getGroupId(), (key, oldValue) -> {
                    PriorityQueue<GroupEntity> priorityQueue = oldValue == null ? new PriorityQueue<GroupEntity>((o1, o2)->o1.getQuota() > o2.getQuota() ? 1 : -1) : oldValue;
                    priorityQueue.offer(entity);
                    return priorityQueue;
                });
            }
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public Map<String, PriorityQueue<GroupEntity>> getResult() {
        return this.map;
    }
}
