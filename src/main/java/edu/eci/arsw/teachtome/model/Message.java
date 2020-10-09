package edu.eci.arsw.teachtome.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Clase que representa un mensaje dentro del chat de una sesión dentro de la aplicación TeachToMe
 */
@Entity(name = "Message")
@Table(name = "messages")
public class Message {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "date_of_message", nullable = false)
    private Timestamp date;

    @ManyToOne
    @JoinColumn(name = "session")
    @JsonBackReference
    private Session session;

    public Message() {
    }

    public Message(String content, Session session) {
        this.content = content;
        this.session = session;
        this.date = new Timestamp(new Date().getTime());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public void setActualDate() {
        this.date = new Timestamp(new Date().getTime());
    }
}
