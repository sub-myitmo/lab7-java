package client.creators;

import common.actions.Console;
import common.actions.GroupMask;
import common.exceptions.IncorrectInputException;
import common.exceptions.IncorrectScriptException;
import common.exceptions.MustBeNotEmptyException;
import common.models.Coordinates;
import common.models.Person;
import common.models.Semester;

import java.util.Scanner;

/**
 * Класс для создания группы
 *
 * @author petrovviacheslav
 */
public class CreateGroup extends BaseCreator<GroupMask> {


    static Scanner usedScanner;
    /**
     * Конструктор класса CreateGroup
     */
    public CreateGroup() {
    }

    public void setFileMode() {
        isScriptRun = true;
    }

    public void setScanner(Scanner sc) {
        usedScanner = sc;
        CreateCoordinates.usedScanner = sc;
        CreateLocation.usedScanner = sc;
        CreatePerson.usedScanner = sc;
        CreateSemester.usedScanner = sc;

    }

    @Override
    public GroupMask create() throws IncorrectScriptException {
        try {
            GroupMask group = new GroupMask(
                    requestName(),
                    requestCoordinates(),
                    requestStudentsCount(),
                    requestExpelledStudents(),
                    requestTransferredStudents(),
                    requestSemester(),
                    requestGroupAdmin()
            );
            return group;
        } catch (IncorrectInputException e) {
            Console.printerror(e.toString());
            return create();
        }
    }


    /**
     * Функция, спрашивающая у пользователя строку, которая после приводится к данным типа Long
     *
     * @param request запрос того, что требуется ввести
     * @return number - строка приведенная к типу Long
     * @throws IncorrectScriptException если при чтении скрипта возникла ошибка
     */
    private Long requestLongField(String request) throws IncorrectScriptException {
        Long number;
        while (true) {
            Console.printf(request);
            try {
                String variable = usedScanner.nextLine().trim();

                if (isScriptRun) Console.println(variable + " ");
                if (variable.isEmpty()) throw new MustBeNotEmptyException();

                number = Long.parseLong(variable);
                break;

            } catch (MustBeNotEmptyException e) {
                Console.printerror(e.toString());
                if (isScriptRun) throw new IncorrectScriptException();
            } catch (NumberFormatException e) {
                Console.printerror("Надо ввести число в формате Long!");
                if (isScriptRun) throw new IncorrectScriptException();
            } catch (IllegalStateException e) {
                Console.printerror("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return number;
    }

    private String requestName() throws IncorrectScriptException {
        String name;
        while (true) {
            Console.printf("Введите имя учебной группы: ");
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

    private Coordinates requestCoordinates() throws IncorrectScriptException, IncorrectInputException {
        return (new CreateCoordinates()).create();
    }

    private Long requestStudentsCount() throws IncorrectScriptException {
        return requestLongField("Введите количество студентов в учебной группе: ");
    }

    private Long requestExpelledStudents() throws IncorrectScriptException {
        return requestLongField("Введите количество отчисленных студентов в учебной группе: ");
    }

    private Integer requestTransferredStudents() throws IncorrectScriptException {
        Integer transferredStudents;
        while (true) {
            Console.printf("Введите количество переведённых студентов в учебной группе: ");
            try {
                String variable = usedScanner.nextLine().trim();


                if (isScriptRun) Console.println(variable);
                if (variable.isEmpty()) throw new MustBeNotEmptyException();

                transferredStudents = Integer.parseInt(variable);
                break;

            } catch (MustBeNotEmptyException e) {
                Console.printerror(e.toString());
                if (isScriptRun) throw new IncorrectScriptException();
            } catch (NumberFormatException e) {
                Console.printerror("Надо ввести число в формате Integer!");
                if (isScriptRun) throw new IncorrectScriptException();
            } catch (IllegalStateException e) {
                Console.printerror("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return transferredStudents;
    }

    private Semester requestSemester() throws IncorrectScriptException {
        return (new CreateSemester()).create();
    }

    private Person requestGroupAdmin() throws IncorrectScriptException, IncorrectInputException {
        return (new CreatePerson()).create();
    }

}
