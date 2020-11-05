package edu.eci.arsw.teachtome.controllers.dtos;

import edu.eci.arsw.teachtome.model.Point;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class PointDTO implements Serializable {

    private int x;

    private int y;

    private String color;

    public PointDTO() {
    }

    public PointDTO(Point point) {
        this.x = point.getX();
        this.y = point.getY();
        this.color = point.getColor();
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

    public static List<PointDTO> getPointsDTO(List<Point> points) {
        List<PointDTO> pointDTOS = new CopyOnWriteArrayList<>();
        for (Point point : points) {
            pointDTOS.add(new PointDTO(point));
        }
        return pointDTOS;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
