import org.jinstagram.exceptions.InstagramException;

import java.io.BufferedReader;

public class Main {
    public static void main(String[] args) throws InstagramException {
        System.out.println("Welcome to Instaliker!");
//        System.out.println("Enter account name:" );

        InitialWizard wizard = new InitialWizard();
        wizard.start();

    }
}
