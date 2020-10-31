package edu.eci.arsw.teachtome.controllers.dtos;

import edu.eci.arsw.teachtome.model.Point;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class PointDTO implements Serializable {

    private long id;

    private int x;

    private int y;

    public PointDTO() {
    }

    public PointDTO(Point point) {
        this.id = point.getId();
        this.x = point.getX();
        this.y = point.getY();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
}
