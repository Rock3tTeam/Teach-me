package edu.eci.arsw.teachtome.controllers.dtos;

import edu.eci.arsw.teachtome.model.RequestPK;

import java.io.Serializable;

public class RequestPKDTO implements Serializable {

    private long student;

    private long clase;

    public RequestPKDTO() {
    }

    public RequestPKDTO(RequestPK requestId) {
        this.student = requestId.getStudent();
        this.clase = requestId.getClase();
    }

    public long getStudent() {
        return student;
    }

    public void setStudent(long student) {
        this.student = student;
    }

    public long getClase() {
        return clase;
    }

    public void setClase(long clase) {
        this.clase = clase;
    }
}
