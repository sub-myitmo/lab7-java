package client.creators;


import common.actions.Console;
import common.exceptions.*;
import common.models.Coordinates;

import java.util.Scanner;

/**
 * Класс для создания координат
 *
 * @author petrovviacheslav
 */
public class CreateCoordinates extends BaseCreator<Coordinates> {


    static Scanner usedScanner;

    /**
     * Конструктор класса CreateCoordinates
     */
    public CreateCoordinates() {
    }


    @Override
    public Coordinates create() throws IncorrectScriptException {

        Coordinates coordinates = new Coordinates(
                requestX(),
                requestY()
        );
        return coordinates;

    }

    /**
     * Функция, спрашивающая у пользователя координату X
     *
     * @return X - переменная от пользователя
     * @throws IncorrectScriptException если при чтении скрипта возникла ошибка
     */
    private Integer requestX() throws IncorrectScriptException {
        Integer X;
        while (true) {
            Console.printf("Введите переменную Х (для Coordinates, тип Integer): ");
            try {

                String variable = usedScanner.nextLine().trim();

                if (variable.isEmpty()) throw new MustBeNotEmptyException();
                if (isScriptRun) Console.println(variable);

                X = Integer.parseInt(variable);
                if (X > 265) throw new NumberFormatException();
                break;

            } catch (MustBeNotEmptyException e) {
                Console.printerror(e.toString());
                if (isScriptRun) throw new IncorrectScriptException();
            } catch (NumberFormatException e) {
                Console.printerror("Надо ввести число в формате Integer и не больше 265!");
                if (isScriptRun) throw new IncorrectScriptException();
            } catch (IllegalStateException e) {
                Console.printerror("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return X;
    }

    private double requestY() throws IncorrectScriptException {
        double Y;
        while (true) {
            Console.printf("Введите переменную Y (для Coordinates, тип double): ");
            try {
                String variable = usedScanner.nextLine().trim();

                if (variable.isEmpty()) throw new MustBeNotEmptyException();
                if (isScriptRun) Console.println(variable);

                Y = Double.parseDouble(variable);
                break;

            } catch (NumberFormatException e) {
                Console.printerror("Надо ввести число в формате double!");
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

}
