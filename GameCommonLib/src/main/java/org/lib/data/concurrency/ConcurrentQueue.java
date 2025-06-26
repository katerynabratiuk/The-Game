package org.lib.data.concurrency;

import java.util.ArrayList;

public class ConcurrentQueue<T> {
    private final ArrayList<T> queue = new ArrayList<>();

    public synchronized T get() throws InterruptedException {
        while (queue.isEmpty()) {
            wait();
        }
        T task = queue.remove(0);
        queue.remove(task);
        return task;
    }

    public synchronized void put(T task) {
        queue.add(task);
        notify();
    }

    public synchronized int size() {
        return queue.size();
    }
}

