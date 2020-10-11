package edu.eci.arsw.teachtome.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

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

    @Column(name = "sender", nullable = false)
    private String sender;

    @ManyToOne
    @JoinColumn(name = "session")
    @JsonBackReference
    private Session session;

    public Message() {
    }

    public Message(String content , String sender){
        this.content=content;
        this.sender=sender;
    }

    public Message(String content) {
        this.content = content;
        this.session = new Session();
        this.date = new Timestamp(new Date().getTime());
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

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setActualDate() {
        this.date = new Timestamp(new Date().getTime());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(content, message.content) &&
                Objects.equals(date, message.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content, date);
    }

    @Override
    public String toString() {
        return "Message{" +
                "content='" + content + '\'' +
                ", date=" + date +
                ", sender='" + sender + '\'' +
                '}';
    }
}
