package common.models;

import java.io.Serializable;

/**
 * Класс координат группы
 *
 * @author petrovviacheslav
 */

public class Coordinates implements Validatable, Serializable {
    private static final long serialVersionUID = -6355064312346031098L;
    private final Integer x; //Максимальное значение поля: 265, Поле не может быть null
    private final double y;

    /**
     * Конструктор класса Location
     *
     * @param x координата x
     * @param y координата y
     */
    public Coordinates(Integer x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Получить координату x
     *
     * @return координата x
     */
    public Integer getX() {
        return x;
    }

    /**
     * Получить координату y
     *
     * @return координата y
     */
    public double getY() {
        return y;
    }

    @Override
    public boolean validate() {
        return !(x > 265);
    }

// дописать hashCode equals

    @Override
    public String toString() {
        return "Coordinates{" + "x=" + x + ", y=" + y + "}";
    }
}