package com.example.rjq.myapplication.threadpool;


public class ThreadPoolUtil {
    /**
     * 在线程池中的线程中执行任务
     *
     * @param task
     */
    public static void runTaskInThread(Runnable task) {
        ThreadPoolFactory.getCommonThreadPool().execute(task);
    }

    /**
     * 移除线程池中任务
     *
     * @param task
     */
    public static boolean removeTask(Runnable task) {
        boolean remove = ThreadPoolFactory.getCommonThreadPool().remove(task);
        return remove;
    }

}
