package ru.nsu.instaliker;

import ru.nsu.instaliker.view.ConsoleView;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class Manager extends Thread {
    private ConsoleView view;
    private Wizard wizard;

    private Liker currentTask = null;
    private AtomicBoolean isEnd = new AtomicBoolean(false);

    private BlockingQueue<Liker> activeTasks = new ArrayBlockingQueue<>(10);
//    private ArrayList<Liker> executedTasks = new ArrayList<>();

    public Manager(Wizard wizard) {
        this.wizard = wizard;
    }

    @Override
    public void run() {
        wizard.setView(view);
        wizard.initAuthorization();

        view.setManager(this);
        view.start();

        while (!isEnd.get()) {
            try {
                synchronized (activeTasks) {
                    activeTasks.wait();
                }
            } catch (InterruptedException e) {
                if ((currentTask == null || (currentTask != null && currentTask.isPaused())) &&
                        activeTasks.size() == 0)
                    continue;
            }

            try {
                if (currentTask != null)
                    currentTask.run();
                else {
                    currentTask = activeTasks.take();
                    currentTask.run();
                }
            } catch (Exception e) {
                System.out.println("MANAGER: " + e.getMessage());
            }
        }
    }

    public void setView(ConsoleView view) {
        this.view = view;
    }

    public ArrayList<Liker> getLikersList() {
        return new ArrayList<>(activeTasks);
    }

    public void addTask() {
        Liker newLiker = wizard.getLiker();
        try {
            activeTasks.put(newLiker);
            synchronized (activeTasks) {
                activeTasks.notify();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean pauseCurrentTask() {
        if (currentTask != null) {
            synchronized (currentTask) {
                currentTask.pause();
                this.interrupt();
            }

            return true;
        }

        return false;
    }

    public void quit() {
        isEnd.set(true);
        cancelCurrentTask();
    }

    public boolean cancelCurrentTask() {
        if (currentTask != null) {
            synchronized (currentTask) {
                currentTask.cancel();
                this.interrupt();
            }

            return true;
        }

        return false;
    }

    public boolean resumeCurrentTask() {
        if (currentTask != null) {
            synchronized (currentTask) {
                currentTask.resume();
                this.interrupt();
            }

            return true;
        }

        return false;
    }
}
