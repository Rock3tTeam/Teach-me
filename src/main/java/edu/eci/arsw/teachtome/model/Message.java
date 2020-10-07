package edu.eci.arsw.teachtome.model;

import java.util.Date;
import java.sql.Timestamp;

/**
 * Clase que representa un mensaje dentro del chat de una sesión dentro de la aplicación TeachToMe
 */
public class Message {
    private String content;
    private Timestamp date;

    public Message(){}

    public Message(String content) {
        this.content = content;
        this.date = new Timestamp(new Date().getTime());
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
}
