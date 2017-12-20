package ru.nsu.instaliker;

import org.jinstagram.Instagram;
import ru.nsu.instaliker.view.ConsoleView;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class Manager extends Thread {
    private Instagram instagram;
    private ConsoleView view;
    private Wizard wizard;
    private ConcurrentLinkedQueue tasks = new ConcurrentLinkedQueue();

    private AtomicBoolean isEnd = new AtomicBoolean(false);

    public Manager(Wizard wizard) {
        this.wizard = wizard;
    }

    @Override
    public synchronized void start() {
        wizard.setView(view);
        instagram = wizard.initAuthorization(view);

        while (!isEnd.get()) {
            try {
                tasks.wait();
            } catch (InterruptedException e) {
                if (tasks.size() == 0)
                    continue;
            }

//            ...
        }
    }

    public void setView(ConsoleView view) {
        this.view = view;
    }
}
