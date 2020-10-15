package edu.eci.arsw.teachtome.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

/**
 * Clase que representa la Principal Key de una solicitud
 */
@Embeddable
public class RequestPK implements Serializable {
    @Column(name = "student")
    private long student;

    @Column(name = "class")
    private long clase;

    /**
     * Constructor por defecto de la entidad RequestPK
     */
    public RequestPK() {
    }

    /**
     * Constructor por defecto de la entidad RequestPK
     *
     * @param student - Identificador del solicitante
     * @param clase   - Identificador de la clase
     */
    public RequestPK(long student, long clase) {
        this.student = student;
        this.clase = clase;
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

    @Override
    public int hashCode() {
        return Objects.hash(getClase(), getStudent());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RequestPK other = (RequestPK) obj;
        return Objects.equals(getClase(), other.getClase()) && Objects.equals(getStudent(), other.getStudent());
    }
}
