package edu.eci.arsw.teachtome.model;

import java.util.List;

/**
 * Clase que representa una sesión de una asignatura dentro de la aplicación TeachToMe
 */
public class Session {
    private List<Message> chat;
    private List<Draw> draws;
    private int duration;

    public Session(List<Message> chat, List<Draw> draws, int duration) {
        this.chat = chat;
        this.draws = draws;
        this.duration = duration;
    }

    public List<Message> getChat() {
        return chat;
    }

    public void setChat(List<Message> chat) {
        this.chat = chat;
    }

    public List<Draw> getDraws() {
        return draws;
    }

    public void setDraws(List<Draw> draws) {
        this.draws = draws;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
