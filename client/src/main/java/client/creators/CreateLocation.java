package client.creators;


import common.actions.Console;
import common.exceptions.IncorrectScriptException;
import common.exceptions.MustBeNotEmptyException;
import common.models.Location;

import java.util.Scanner;

/**
 * Класс для создания местоположения человека
 *
 * @author petrovviacheslav
 */
public class CreateLocation extends BaseCreator<Location> {


    static Scanner usedScanner;

    /**
     * Конструктор класса CreateLocation
     */
    public CreateLocation() {
    }


    @Override
    public Location create() throws IncorrectScriptException {

        Location location = new Location(
                requestX(),
                requestY(),
                requestZ()
        );
        return location;

    }

    /**
     * Функция, спрашивающая у пользователя координату X в локации
     *
     * @return X - переменная от пользователя
     * @throws IncorrectScriptException если при чтении скрипта возникла ошибка
     */
    private Float requestX() throws IncorrectScriptException {
        Float X;
        while (true) {
            Console.printf("Введите переменную Х (для Location, тип Float): ");
            try {
                String variable = usedScanner.nextLine().trim();

                if (isScriptRun) Console.println(variable);
                if (variable.isEmpty()) throw new MustBeNotEmptyException();

                X = Float.parseFloat(variable);
                break;

            } catch (MustBeNotEmptyException e) {
                Console.printerror(e.toString());
                if (isScriptRun) throw new IncorrectScriptException();
            } catch (NumberFormatException e) {
                Console.printerror("Надо ввести число в формате float!");
                if (isScriptRun) throw new IncorrectScriptException();
            } catch (IllegalStateException e) {
                Console.printerror("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return X;
    }

    /**
     * Функция, спрашивающая у пользователя координату X в локации
     *
     * @return X - переменная от пользователя
     * @throws IncorrectScriptException если при чтении скрипта возникла ошибка
     */
    private Integer requestY() throws IncorrectScriptException {
        Integer Y;
        while (true) {
            Console.printf("Введите переменную Y (для Location, тип Integer): ");
            try {
                String variable = usedScanner.nextLine().trim();

                if (isScriptRun) Console.println(variable);
                if (variable.isEmpty()) throw new MustBeNotEmptyException();

                Y = Integer.parseInt(variable);
                break;

            } catch (NumberFormatException e) {
                Console.printerror("Надо ввести число!");
                if (isScriptRun) throw new IncorrectScriptException();
            } catch (MustBeNotEmptyException e) {
                Console.printerror(e.toString());
                if (isScriptRun) throw new IncorrectScriptException();
            } catch (IllegalStateException e) {
                Console.printerror("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return Y;
    }

    private double requestZ() throws IncorrectScriptException {
        double Z;
        while (true) {
            Console.printf("Введите переменную Z (для Location, тип double): ");
            try {
                String variable = usedScanner.nextLine().trim();

                if (isScriptRun) Console.println(variable);
                if (variable.isEmpty()) throw new MustBeNotEmptyException();

                Z = Double.parseDouble(variable);
                break;

            } catch (MustBeNotEmptyException e) {
                Console.printerror(e.toString());
                if (isScriptRun) throw new IncorrectScriptException();
            } catch (IllegalStateException e) {
                Console.printerror("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return Z;
    }
}
