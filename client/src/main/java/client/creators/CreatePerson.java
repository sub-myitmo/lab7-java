package client.creators;




import common.exceptions.IncorrectInputException;
import common.exceptions.IncorrectScriptException;
import common.exceptions.MustBeNotEmptyException;
import common.actions.Console;
import common.models.Location;
import common.models.Person;

import java.time.LocalDateTime;
import java.util.Scanner;

/**
 * Класс для создания человека
 *
 * @author petrovviacheslav
 */
public class CreatePerson extends BaseCreator<Person> {

    static Scanner usedScanner;
    /**
     * Конструктор класса CreatePerson
     *
     */
    public CreatePerson() {
    }


    @Override
    public Person create() throws IncorrectScriptException, IncorrectInputException {
        Person groupAdmin = new Person(
                requestNamePerson(),
                requestBirthday(),
                requestWeight(),
                requestLocation()
        );
        return groupAdmin;
    }

    private String requestNamePerson() throws IncorrectScriptException {
        String name;
        while (true) {
            Console.printf("Введите имя админа группы:");
            try {
                name = usedScanner.nextLine().trim();

                if (isScriptRun) Console.println(name);
                if (name.isEmpty()) throw new MustBeNotEmptyException();
                break;

            } catch (MustBeNotEmptyException e) {
                Console.printerror(e.toString());
                if (isScriptRun) throw new IncorrectScriptException();
            } catch (IllegalStateException e) {
                Console.printerror("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return name;
    }

    private LocalDateTime requestBirthday() throws IncorrectScriptException {
        LocalDateTime birthday;
        while (true) {
            Console.printf("Введите день рождения (тип LocalDateTime yyyy-MM-ddTHH:mm:ss): ");
            try {
                String variable = usedScanner.nextLine().trim();

                if (isScriptRun) Console.println(variable);
                if (variable.isEmpty()) throw new MustBeNotEmptyException();

                birthday = LocalDateTime.parse(variable);
                break;

            } catch (MustBeNotEmptyException e) {
                Console.printerror(e.toString());
                if (isScriptRun) throw new IncorrectScriptException();
            } catch (IllegalStateException e) {
                Console.printerror("Непредвиденная ошибка!");
                System.exit(0);
            } catch (Exception e) {
                Console.printerror("Неверно ввели дату");
                if (isScriptRun) throw new IncorrectScriptException();
            }
        }
        return birthday;
    }

    private double requestWeight() throws IncorrectScriptException {
        double weight;
        while (true) {
            Console.printf("Введите вес (тип double): ");
            try {
                String variable = usedScanner.nextLine().trim();

                if (isScriptRun) Console.println(variable);
                if (variable.isEmpty()) throw new MustBeNotEmptyException();

                weight = Double.parseDouble(variable);
                break;

            } catch (MustBeNotEmptyException e) {
                Console.printerror(e.toString());
                if (isScriptRun) throw new IncorrectScriptException();
            } catch (NumberFormatException e) {
                Console.printerror("Надо ввести число!");
                if (isScriptRun) throw new IncorrectScriptException();
            } catch (IllegalStateException e) {
                Console.printerror("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return weight;
    }

    private Location requestLocation() throws IncorrectScriptException {
        return (new CreateLocation()).create();
    }

}
