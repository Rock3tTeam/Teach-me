package edu.eci.arsw.teachtome.model;

import edu.eci.arsw.teachtome.controllers.dtos.RequestDTO;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

/**
 * Clase que representa una solicitud de un usuario dentro de una clase en la aplicación TeachToMe
 */

@Entity(name = "Request")
@Table(name = "requests")
public class Request implements Serializable {

    @EmbeddedId
    private RequestPK requestId;

    @Column(name = "accepted")
    private Boolean accepted;

    @ManyToOne
    @MapsId("id")
    @JoinColumn(name = "class")
    private Clase clase = new Clase();

    @ManyToOne
    @MapsId("id")
    @JoinColumn(name = "student")
    private User student = new User();

    /**
     * Constructor por defecto de la entidad Solicitud
     */
    public Request() {
    }

    /**
     * Constructor por defecto de la entidad Solicitud
     *
     * @param requestId - RequestPK de la solicitud
     */
    public Request(RequestPK requestId) {
        this.requestId = requestId;
    }

    /**
     * Constructor por defecto de la entidad Solicitud
     *
     * @param requestId - RequestPK de la solicitud
     * @param accepted  - Valor booleano que determina si la solicitud fue aprobada o rechazada
     */
    public Request(RequestPK requestId, Boolean accepted) {
        this.requestId = requestId;
        this.accepted = accepted;
    }

    public Request(boolean accepted) {
        this.accepted = accepted;
    }

    public Request(RequestDTO requestDTO) {
        this.requestId = new RequestPK(requestDTO.getRequestId());
        if(requestDTO.hasAnswer()){
            this.accepted = requestDTO.getAccepted();
        }
    }

    public Boolean isAccepted() {
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

    /**
     * Determina si la solicitud ya fue contestada
     *
     * @return Valor booleano que determina si la solicitud ya fue contestada
     */
    public boolean hasAnswer() {
        return accepted != null;
    }

    @Override
    public String toString() {
        return "{accepted=" + accepted +
                ", clase=" + clase +
                ", student=" + student +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request request = (Request) o;
        return Objects.equals(requestId, request.requestId) &&
                Objects.equals(accepted, request.accepted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestId, accepted);
    }


}
