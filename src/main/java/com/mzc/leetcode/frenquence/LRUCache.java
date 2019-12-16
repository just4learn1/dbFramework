package com.mzc.leetcode.frenquence;

import java.util.LinkedHashMap;
import java.util.Map;

public class LRUCache {
    LinkedHashMap<Integer, Integer> map = null;
    private final float loadFactor = 0.75f;
    private int capcity;
    public LRUCache(int capacity) {
        this.capcity = capacity;
        map = new LinkedHashMap(capacity, loadFactor, true){
            @Override
            protected boolean removeEldestEntry(Map.Entry eldest) {
                return this.size() > capacity;
            }
        };

    }

    public int get(int key) {
        return map.getOrDefault(key, -1);
    }

    public void put(int key, int value) {
        map.put(key, value);
    }

    public static void main(String[] args) {
        LRUCache cache = new LRUCache(5);
        for (int i = 1; i <= 2; i++) {
            cache.put(i, i);
        }
        System.out.println(cache.get(1));
        cache.put(3, 3);
        System.out.println(cache.get(2));

    }
}
