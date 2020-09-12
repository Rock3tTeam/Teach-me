package edu.eci.arsw.teachtome.model;

public class Request {
    private Class clase;
    private User user;

    public Request(Class clase, User user) {
        this.clase = clase;
        this.user = user;
    }

    public Class getClase() {
        return clase;
    }

    public void setClase(Class clase) {
        this.clase = clase;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
