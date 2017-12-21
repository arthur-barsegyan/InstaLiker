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
    public void run() {
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
                case "new":
                    manager.addTask();
                    break;
                case "pause":
                    if (manager.pauseCurrentTask())
                        System.out.println("Paused current task");
                    else
                        System.out.println("You don't have active task!");
                    break;
                case "resume":
                    manager.resumeCurrentTask();
                    break;
                case "remove":
                    if (manager.cancelCurrentTask())
                        System.out.println("Canceled current task");
                    else
                        System.out.println("You don't have active task!");
                    break;
                case "exit":
                    System.out.println("Do you really want to exit? (Y/N)");
                    if (readString().equals("Y"))
                        manager.quit();
                    break;
                default:
                    System.out.println("Unrecognized command! Please try again");
            }
        }
    }
}
