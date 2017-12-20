package ru.nsu.instaliker.view;

import ru.nsu.instaliker.Manager;

import java.util.Scanner;

public class ConsoleView extends Thread {
    private Scanner reader = new Scanner(System.in);

    private Manager manager;

    public String readString() { return reader.nextLine(); }
    public void printString(String text) { System.out.println(text); }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    @Override
    public synchronized void start() {
        boolean isEnd = false;
        while (!isEnd) {
            String command = readString();
            switch (command) {
                case "list":

                    default:

            }
        }
    }
}
