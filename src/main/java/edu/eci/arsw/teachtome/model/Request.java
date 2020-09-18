package edu.eci.arsw.teachtome.model;

import javax.persistence.*;

/**
 * Clase que representa una solicitud de un usuario dentro de una clase en la aplicación TeachToMe
 */

@Entity(name = "Request")
@Table(name = "requests")
public class Request {

    @EmbeddedId
    private RequestPK requestId;

    @Column(name="accepted")
    private Boolean accepted;

    @ManyToOne
    @MapsId("id")
    @JoinColumn(name = "class")
    private Clase clase = new Clase();

    @ManyToOne
    @MapsId("email")
    @JoinColumn(name = "student")
    private User student = new User();


    public Request() {
    }

    public Request(boolean accepted) {
        this.accepted = accepted;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public RequestPK getRequestId() {
        return requestId;
    }

    public void setRequestId(RequestPK requestId) {
        this.requestId = requestId;
    }

    public Clase getClase() {
        return clase;
    }

    public void setClase(Clase clase) {
        this.clase = clase;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }
}
