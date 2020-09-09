package edu.eci.arsw.teachtome.model;

import java.util.List;

public class User {
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String description;
    private List<Clase> teachingClases;
    private List<Request> requests;

    public User(String email, String firstName, String lastName, String password, String description, List<Clase> teachingClases, List<Request> requests) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.description = description;
        this.teachingClases = teachingClases;
        this.requests = requests;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Clase> getTeachingClasses() {
        return teachingClases;
    }

    public void setTeachingClasses(List<Clase> teachingClases) {
        this.teachingClases = teachingClases;
    }

    public List<Request> getRequests() {
        return requests;
    }

    public void setRequests(List<Request> requests) {
        this.requests = requests;
    }
}
