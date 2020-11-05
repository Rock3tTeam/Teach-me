package edu.eci.arsw.teachtome.model;

import edu.eci.arsw.teachtome.controllers.dtos.DrawDTO;
import edu.eci.arsw.teachtome.controllers.dtos.PointDTO;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Clase que representa un dibujo de una sesión dentro de la aplicación TeachToMe
 */
public class Draw {

    private Timestamp dateOfDraw;

    private List<Point> points = new CopyOnWriteArrayList<>();

    public Draw() {
    }

    public Draw(Timestamp dateOfDraw, List<Point> points) {
        this.dateOfDraw = dateOfDraw;
        this.points = points;
    }

    public Draw(List<Point> points) {
        this.points = points;
    }

    public Draw(DrawDTO drawDTO) {
        this.dateOfDraw = drawDTO.getDateOfDraw();
        this.points = getPointsFromDTO(drawDTO.getPoints());
    }

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }

    public Timestamp getDateOfDraw() {
        return dateOfDraw;
    }

    public void setDateOfDraw(Timestamp dateOfDraw) {
        this.dateOfDraw = dateOfDraw;
    }

    public boolean isEmpty() {
        return points.isEmpty();
    }

    private List<Point> getPointsFromDTO(List<PointDTO> points) {
        List<Point> pointList = new CopyOnWriteArrayList<>();
        for (PointDTO pointDTO : points) {
            pointList.add(new Point(pointDTO));
        }
        return pointList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Draw draw = (Draw) o;
        return Objects.equals(points, draw.points);
    }

    @Override
    public int hashCode() {
        return Objects.hash(points);
    }

    @Override
    public String toString() {
        return "Draw{" +
                ", dateOfDraw=" + dateOfDraw +
                ", points=" + points +
                '}';
    }
}
