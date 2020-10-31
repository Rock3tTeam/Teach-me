package edu.eci.arsw.teachtome.controllers.dtos;

import edu.eci.arsw.teachtome.model.Clase;

import java.io.Serializable;
import java.sql.Timestamp;

public class ClaseDTO implements Serializable {

    private long id;

    private String nombre;

    private int capacity;

    private String description;

    private int amountOfStudents;

    private Timestamp dateOfInit;

    private Timestamp dateOfEnd;

    public ClaseDTO() {
    }

    public ClaseDTO(Clase clase) {
        this.id = clase.getId();
        this.nombre = clase.getNombre();
        this.capacity = clase.getCapacity();
        this.description = clase.getDescription();
        this.amountOfStudents = clase.getAmountOfStudents();
        this.dateOfInit = clase.getDateOfInit();
        this.dateOfEnd = clase.getDateOfEnd();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAmountOfStudents() {
        return amountOfStudents;
    }

    public void setAmountOfStudents(int amountOfStudents) {
        this.amountOfStudents = amountOfStudents;
    }

    public Timestamp getDateOfInit() {
        return dateOfInit;
    }

    public void setDateOfInit(Timestamp dateOfInit) {
        this.dateOfInit = dateOfInit;
    }

    public Timestamp getDateOfEnd() {
        return dateOfEnd;
    }

    public void setDateOfEnd(Timestamp dateOfEnd) {
        this.dateOfEnd = dateOfEnd;
    }
}
