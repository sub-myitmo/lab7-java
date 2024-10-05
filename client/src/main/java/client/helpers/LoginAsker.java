package client.helpers;

import common.actions.Console;
import common.exceptions.EmptyNameException;
import common.exceptions.IncorrectInputException;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class LoginAsker {
    private Scanner userScanner;

    public LoginAsker(Scanner userScanner) {
        this.userScanner = userScanner;
    }

    public String askLogin() {
        String login;
        while (true) {
            try {
                Console.println("Введите логин: ");
                login = userScanner.nextLine().trim();
                if (login.isEmpty()) throw new EmptyNameException();
                break;
            } catch (NoSuchElementException e) {
                Console.printerror("Данного логина не существует!");
            } catch (EmptyNameException e) {
                Console.printerror(e.toString());
            } catch (IllegalStateException e) {
                Console.printerror("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return login;
    }

    public String askPassword() {
        String password;
        while (true) {
            try {
                //ввод пароля без его явного отображения (как на хелиосе)
                Console.println("Введите пароль: ");
                java.io.Console console = System.console();
                if (console != null) {
                    char[] symbols = console.readPassword();
                    if (symbols == null) continue;
                    password = String.valueOf(symbols);
                } else password = userScanner.nextLine().trim();
                break;
            } catch (NoSuchElementException e) {
                Console.printerror("Неверный пароль!");
            } catch (IllegalStateException e) {
                Console.printerror("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return password;
    }



    public boolean askQuestion(String question) {
        String answer;
        while (true) {
            try {
                Console.printf(question + " (y/n):");
                answer = userScanner.nextLine().trim();
                if (!answer.equals("y") && !answer.equals("n") && !answer.equals("yes") && !answer.equals("no"))
                    throw new IncorrectInputException();
                break;
            } catch (NoSuchElementException exception) {
                Console.printerror("Ответ не распознан!");
            } catch (IncorrectInputException e) {
                Console.printerror(e.toString());
            } catch (IllegalStateException exception) {
                Console.printerror("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return answer.equals("y") || answer.equals("yes");
    }
}
