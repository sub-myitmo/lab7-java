package client.creators;


import common.exceptions.IncorrectInputException;
import common.exceptions.IncorrectScriptException;

import java.util.Scanner;

/**
 * Абстрактный класс создания базового объекта
 *
 * @author petrovviacheslav
 */
public abstract class BaseCreator<T> {
    abstract T create() throws IncorrectScriptException, IncorrectInputException;
    protected boolean isScriptRun;
}
