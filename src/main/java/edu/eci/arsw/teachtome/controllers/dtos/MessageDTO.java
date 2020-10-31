package edu.eci.arsw.teachtome.controllers.dtos;

import edu.eci.arsw.teachtome.model.Message;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MessageDTO implements Serializable {

    private long id;

    private String content;

    private Timestamp date;

    private String sender;

    public MessageDTO() {
    }

    public MessageDTO(Message message) {
        this.id = message.getId();
        this.content = message.getContent();
        this.date = message.getDate();
        this.sender = message.getSender();
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

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public static List<MessageDTO> getMessagesDTO(List<Message> messages) {
        List<MessageDTO> messageDTOS = new CopyOnWriteArrayList<>();
        for (Message message : messages) {
            messageDTOS.add(new MessageDTO(message));
        }
        return messageDTOS;
    }
}
