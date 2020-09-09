package edu.eci.arsw.teachtome.model;

public class Request {
    private Clase clase;
    private User user;

    public Request(Clase clase, User user) {
        this.clase = clase;
        this.user = user;
    }

    public Clase getClase() {
        return clase;
    }

    public void setClase(Clase clase) {
        this.clase = clase;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
