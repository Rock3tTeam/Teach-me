package edu.eci.arsw.teachtome.model;

import java.util.List;

public class Draw {
    private List<Point> points;

    public Draw(List<Point> points) {
        this.points = points;
    }

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }
}
