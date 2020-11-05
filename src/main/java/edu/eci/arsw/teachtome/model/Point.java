package edu.eci.arsw.teachtome.model;

import edu.eci.arsw.teachtome.controllers.dtos.PointDTO;

import java.util.Objects;

/**
 * Clase que representa un punto de un dibujo dentro de la aplicación TeachToMe
 */
public class Point {

    private Draw draw;

    private int x;

    private int y;

    private String color;

    public Point() {
    }

    public Point(int x, int y, String color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public Point(PointDTO pointDTO) {
        this.x = pointDTO.getX();
        this.y = pointDTO.getY();
        this.color = pointDTO.getColor();
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Draw getDraw() {
        return draw;
    }

    public void setDraw(Draw draw) {
        this.draw = draw;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return x == point.x &&
                y == point.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Point{" +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
