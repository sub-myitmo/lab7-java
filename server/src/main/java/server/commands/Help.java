package server.commands;

import common.actions.User;
import common.exceptions.WrongCommandArgsException;
import server.managers.ResponseManager;

/**
 * Команда help - вывести справку по доступным командам
 *
 * @author petrovviacheslav
 */
public class Help extends Command {

    public Help() {
        super("help", "вывести справку по доступным командам");

    }

    @Override
    public boolean execute(String arg, Object otherArg, User user) {
        try {
            if (!arg.isEmpty() || otherArg != null) throw new WrongCommandArgsException();

            return true;
        } catch (WrongCommandArgsException e) {
            ResponseManager.addln(e.toString());
            return false;
        }

    }
}