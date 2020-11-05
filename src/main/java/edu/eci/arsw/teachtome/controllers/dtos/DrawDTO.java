package edu.eci.arsw.teachtome.controllers.dtos;

import edu.eci.arsw.teachtome.model.Draw;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class DrawDTO implements Serializable {

    private Timestamp dateOfDraw;

    private List<PointDTO> points = new CopyOnWriteArrayList<>();

    public DrawDTO() {
    }

    public DrawDTO(Draw draw) {
        this.dateOfDraw = draw.getDateOfDraw();
        this.points = PointDTO.getPointsDTO(draw.getPoints());
    }

    public Timestamp getDateOfDraw() {
        return dateOfDraw;
    }

    public void setDateOfDraw(Timestamp dateOfDraw) {
        this.dateOfDraw = dateOfDraw;
    }

    public List<PointDTO> getPoints() {
        return points;
    }

    public void setPoints(List<PointDTO> points) {
        this.points = points;
    }
}
