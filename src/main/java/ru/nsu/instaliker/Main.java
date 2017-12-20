package ru.nsu.instaliker;

import org.jinstagram.exceptions.InstagramException;
import ru.nsu.instaliker.view.ConsoleView;

public class Main {
    public static void main(String[] args) throws InstagramException {
        System.out.println("Welcome to Instaliker!");

        Manager manager = new Manager(new Wizard());
        ConsoleView view = new ConsoleView();
        manager.setView(view);

        manager.start();
    }
}
