package me.Femhack.util;

import java.util.concurrent.atomic.*;
import java.util.concurrent.*;

public class MultiThreading
{
    private static final AtomicInteger threadCounter;
    private static final ExecutorService SERVICE;

    public static void runAsync(final Runnable task) {
        MultiThreading.SERVICE.execute(task);
    }

    static {
        threadCounter = new AtomicInteger(0);
        final Thread thread = new Thread();
        SERVICE = Executors.newCachedThreadPool(task -> {
            new Thread(task, "Candy Thread " + MultiThreading.threadCounter.getAndIncrement()) {};
            return thread;
        });
    }
}