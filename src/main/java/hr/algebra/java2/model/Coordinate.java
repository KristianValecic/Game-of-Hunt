package hr.algebra.java2.model;

import java.io.Serializable;
import java.util.Objects;

public class Coordinate implements Serializable {
    public static final String DELIMITER = ",";
    private double x;
    private double y;

    public static Coordinate getCoordinateFormString(String coordinate) {
        if (!coordinate.contains(DELIMITER)){
            return new Coordinate(-404, -404);
        }
        String[] split = coordinate.split(DELIMITER);
        double x = Double.parseDouble(split[0]);
        double y = Double.parseDouble(split[1]);
        return new Coordinate(x, y);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return x + DELIMITER + y ;
    }

    public Coordinate(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate that = (Coordinate) o;
        return Double.compare(that.x, x) == 0 && Double.compare(that.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
