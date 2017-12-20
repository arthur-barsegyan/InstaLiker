package ru.nsu.instaliker.view;

import ru.nsu.instaliker.Liker;
import ru.nsu.instaliker.Manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class ConsoleView extends Thread {
    private Scanner reader = new Scanner(System.in);

    private Manager manager;

    public String readString() { return reader.nextLine(); }
    public void printlnString(String text) { System.out.println(text); }
    public void printString(String text) { System.out.print(text); }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    @Override
    public synchronized void start() {
        boolean isEnd = false;
        while (!isEnd) {
            System.out.print("Enter command > ");
            String command = readString();
            switch (command) {
                case "list":
                    ArrayList<Liker> likers = manager.getLikersList();
                    for (Iterator<Liker> it = likers.iterator(); it.hasNext(); )
                        System.out.println("\t" + it.next());
                    break;

                default:

            }
        }
    }
}
