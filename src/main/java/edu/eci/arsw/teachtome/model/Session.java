package edu.eci.arsw.teachtome.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa una sesión de una asignatura dentro de la aplicación TeachToMe
 */
@Entity(name = "Session")
@Table(name = "sessions")
public class Session {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "duration", length = 10, nullable = false)
    private int duration;

    @Column(name = "class", length = 10, nullable = false)
    private long classId;

    @Column(name = "date_of_last_draw")
    private Timestamp dateOfLastDraw;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinColumn(name = "session")
    @JsonManagedReference
    private List<Message> chat;

    /**
     * Constructor por defecto de la entidad Sesion
     */
    public Session() {
    }

    /**
     * Constructor por defecto de la entidad Sesion
     *
     * @param classId - Identificador de la clase
     */
    public Session(long classId) {
        this.classId = classId;
        chat = new ArrayList<>();
    }

    public Timestamp getDateOfLastDraw() {
        return dateOfLastDraw;
    }

    public void setDateOfLastDraw(Timestamp dateOfLastDraw) {
        this.dateOfLastDraw = dateOfLastDraw;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public long getClassId() {
        return classId;
    }

    public void setClassId(long classId) {
        this.classId = classId;
    }

    public List<Message> getChat() {
        return chat;
    }

    public void setChat(List<Message> chat) {
        this.chat = chat;
    }

    /**
     * Agrega un mensaje dentro del chat
     *
     * @param message Mensaje a Agregar
     */
    public void addMessage(Message message) {
        chat.add(message);
    }

    @Override
    public String toString() {
        return "Session{" +
                "id=" + id +
                ", duration=" + duration +
                ", classId=" + classId +
                ", chat=" + chat +
                '}';
    }
}
