package com.laufer.itamar.communication.server;

/**
 * Represents a task that has a specific amount of time to be done and if not something will happen
 */
public abstract class TaskForTime implements Runnable {
    private int time; //in ms
    public TaskForTime(int time) {
        this.time = time;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(time);
            if (!isDone()) {
                handleNotDone();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public abstract boolean isDone();

    public abstract void handleNotDone();
}
