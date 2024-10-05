package client.helpers;

import client.creators.CreateGroup;
import common.actions.*;
import common.exceptions.IncorrectScriptException;
import common.exceptions.ScriptRecursionException;
import common.exceptions.WrongCommandArgsException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Scanner;
import java.util.Stack;

public class UserHandler {


    private Scanner usedScanner;
    private Stack<File> stackFiles = new Stack<>();
    private Stack<Scanner> stackScanners = new Stack<>();

    public UserHandler(Scanner userScanner) {
        this.usedScanner = userScanner;
    }

    private boolean checkFileMode() {
        return !stackScanners.isEmpty();
    }


    private GroupMask generateGroupMask() throws IncorrectScriptException {
        CreateGroup creator = new CreateGroup();
        creator.setScanner(usedScanner);
        if (checkFileMode()) creator.setFileMode();
        return creator.create();
    }

    private StatusOfCode conversionCommand(String command, String commandArgument, User user) {
        try {
            switch (command) {
                case "":
                    return StatusOfCode.ERROR;
                case "add":
                    if (!commandArgument.isEmpty()) throw new WrongCommandArgsException();
                    return StatusOfCode.OBJECT;
                case "clear", "info", "help", "print_ascending", "print_field_ascending_students_count", "remove_first", "show", "reorder", "shuffle":
                    if (!commandArgument.isEmpty()) throw new WrongCommandArgsException();
                    break;
                case "remove_by_id":
                    if (commandArgument.isEmpty()) throw new WrongCommandArgsException();
                    break;
                case "execute_script":
                    if (commandArgument.isEmpty()) throw new WrongCommandArgsException();
                    return StatusOfCode.SCRIPT;
                case "exit":
                    if (!commandArgument.isEmpty()) throw new WrongCommandArgsException();
                    Console.println("Работа клиента завершена");
                    System.exit(0);
                    break;
                case "update_by_id":
                    if (commandArgument.isEmpty()) throw new WrongCommandArgsException();
                    return StatusOfCode.OBJECT;
                default:
                    Console.println("Команда '" + command + "' не найдена. Наберите 'help' для справки.");
                    return StatusOfCode.ERROR;
            }
        } catch (WrongCommandArgsException e) {
            Console.println(e.toString());
            return StatusOfCode.ERROR;
        }
        return StatusOfCode.OK;
    }


    public Request handle(ResponseCode responseCode, User user) {
        String userInput;
        String[] userCommand = new String[0];
        StatusOfCode status = null;
        try {
            do {
                try {
                    if (checkFileMode() && (responseCode == ResponseCode.ERROR)) {
                        throw new IncorrectScriptException();
                    }

                    while (checkFileMode() && !usedScanner.hasNextLine()) {
                        usedScanner.close();
                        usedScanner = stackScanners.pop();
                        //Console.println("Возвращаюсь к скрипту '" + stackFiles.pop().getName() + "'...");
                    }
                    if (!usedScanner.hasNextLine()) {
                        break;
                    }
                    userInput = usedScanner.nextLine();
                    if (checkFileMode() && !userInput.isEmpty()) {
                        Console.println(userInput);
                    }

                    userCommand = (userInput.trim() + " ").split(" ", 2);
                    userCommand[1] = userCommand[1].trim();
                } catch (NoSuchElementException | IllegalStateException e) {
                    Console.printerror("Произошла ошибка при вводе команды!");
                    userCommand = new String[]{"", ""};

                }
                status = conversionCommand(userCommand[0], userCommand[1], user);

            } while (status == StatusOfCode.ERROR && !checkFileMode() || userCommand[0].isEmpty());
            try {
                if (checkFileMode() && responseCode == ResponseCode.ERROR)
                    throw new IncorrectScriptException();
                switch (Objects.requireNonNull(status)) {
                    case OBJECT:
                        GroupMask group = generateGroupMask();
                        return new Request(userCommand[0], userCommand[1], group, user);
                    case SCRIPT:
                        File scriptFile = new File(userCommand[1]);
                        if (!scriptFile.exists()) throw new FileNotFoundException();
                        if (!stackFiles.isEmpty() && stackFiles.search(scriptFile) != -1)
                            throw new ScriptRecursionException();
                        stackScanners.push(usedScanner);
                        stackFiles.push(scriptFile);
                        usedScanner = new Scanner(scriptFile);
                        Console.println("Выполняю скрипт '" + scriptFile.getName() + "'...");
                        break;
                }
            } catch (FileNotFoundException e) {
                Console.println(userCommand[1]);
                Console.printerror("Файл со скриптом не найден!");
            } catch (ScriptRecursionException e) {
                Console.printerror("Обнаружена рекурсия!");
                throw new IncorrectScriptException();
            }
        } catch (IncorrectScriptException e) {
            Console.printerror("Выполнение скрипта прервано!");
            while (!stackScanners.isEmpty()) {
                usedScanner.close();
                usedScanner = stackScanners.pop();
            }
            stackFiles.clear();
            return new Request();
        }
        return new Request(userCommand[0], userCommand[1], user);
    }
}
