package edu.eci.arsw.teachtome.model;

import java.sql.Date;

/**
 * Clase que representa un mensaje dentro del chat de una sesión dentro de la aplicación TeachToMe
 */
public class Message {
    private String content;
    private Date date;

    public Message(String content, Date date) {
        this.content = content;
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
