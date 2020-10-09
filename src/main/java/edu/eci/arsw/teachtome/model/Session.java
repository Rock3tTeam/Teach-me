package edu.eci.arsw.teachtome.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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

    /*private List<Message> chat;
    private List<Draw> draws;*/

    public Session(long classId) {
        this.classId = classId;
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
}
