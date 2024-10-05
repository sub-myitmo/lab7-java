package client.creators;



import common.actions.Console;
import common.exceptions.IncorrectScriptException;
import common.exceptions.MustBeNotEmptyException;
import common.models.Semester;

import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Класс для создания номера семестра
 *
 * @author petrovviacheslav
 */
public class CreateSemester extends BaseCreator<Semester> {

    static Scanner usedScanner;
    /**
     * Конструктор класса CreateSemester
     *
     */
    public CreateSemester() {
    }

    @Override
    public Semester create() throws IncorrectScriptException {
        Semester semester;
        while (true) {
            Console.println("Список номеров семестров - " + Semester.getNames());
            Console.printf("Введите нужный семестр: ");
            try {
                String strSemester = usedScanner.nextLine().trim();

                if (isScriptRun) Console.println(strSemester);
                if (strSemester.isEmpty()) throw new MustBeNotEmptyException();

                semester = Semester.valueOf(strSemester.toUpperCase());
                break;

            } catch (MustBeNotEmptyException e) {
                Console.printerror(e.toString());
                if (isScriptRun) throw new IncorrectScriptException();
            } catch (NoSuchElementException exception) {
                Console.printerror("Номер семестра не распознан!");
                if (isScriptRun) throw new IncorrectScriptException();
            } catch (IllegalArgumentException exception) {
                Console.printerror("Номера семестра нет в списке!");
                if (isScriptRun) throw new IncorrectScriptException();
            } catch (IllegalStateException e) {
                Console.printerror("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return semester;
    }

}
