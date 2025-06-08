package com.example.game.DataStructures;

import java.io.Serializable;
import java.util.ArrayList;

public class ConcurrentQueue<T> {
    private final ArrayList<T> queue = new ArrayList<>();

    public synchronized T get() throws InterruptedException {
        while (queue.isEmpty()) {
            wait();
        }
        T task = queue.get(queue.size()-1);
        queue.remove(task);
        return task;
    }

    public synchronized void put(T task) {
        queue.add(task);
        notify();
    }

    public int size() {
        return queue.size();
    }

    public static class Coordinates implements Serializable {
        private final int x;
        private final int y;

        public Coordinates(int x, int y) {
            this.x  = x;
            this.y = y;
        }

        public int x() {
            return this.x;
        }

        public int y() {
            return this.y;
        }
    }
}

