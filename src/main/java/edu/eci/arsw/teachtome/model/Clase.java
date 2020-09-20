package edu.eci.arsw.teachtome.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Clase que representa una asignatura que va a ser enseñada dentro de la aplicación TeachToMe
 */
@Entity(name = "Clase")
@Table(name = "classes")
public class Clase {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column(name = "name", length = 255, nullable = false)
    private String nombre;

    @Column(name = "capacity", length = 50, nullable = false)
    private int capacity;

    @Column(name = "description", length = 255, unique = true, nullable = false)
    private String description;

    @Column(name = "amount_of_students", length = 50, nullable = false)
    private int amountOfStudents;

    @Column(name = "date_of_init")
    private Timestamp dateOfInit;

    @Column(name = "date_of_end")
    private Timestamp dateOfEnd;

    @ManyToOne
    @JoinColumn(name = "professor")
    @JsonBackReference
    private User professor;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(name = "enrollments", joinColumns = @JoinColumn(name = "class"), inverseJoinColumns = @JoinColumn(name = "student"))
    private List<User> students = new ArrayList<User>();


    public Clase() {
    }


    public Clase(String nombre, int capacity, String description, int amountOfStudents, Timestamp dateOfInit, Timestamp dateOfEnd) {
        this.nombre = nombre;
        this.capacity = capacity;
        this.description = description;
        this.amountOfStudents = amountOfStudents;
        this.dateOfInit = dateOfInit;
        this.dateOfEnd = dateOfEnd;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getProfessor() {
        return professor;
    }

    public void setProfessor(User professor) {
        this.professor = professor;
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

    public List<User> getStudents() {
        return students;
    }

    public void setStudents(List<User> students) {
        this.students = students;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Clase clase = (Clase) o;
        return capacity == clase.capacity &&
                amountOfStudents == clase.amountOfStudents &&
                Objects.equals(nombre, clase.nombre) &&
                Objects.equals(description, clase.description) &&
                Objects.equals(dateOfInit, clase.dateOfInit) &&
                Objects.equals(dateOfEnd, clase.dateOfEnd);
    }

    public boolean lazyEquals(Clase clase) {
        return capacity == clase.capacity &&
                amountOfStudents == clase.amountOfStudents &&
                Objects.equals(nombre, clase.nombre) &&
                Objects.equals(description, clase.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, capacity, description, amountOfStudents, dateOfInit, dateOfEnd, professor, students);
    }

    @Override
    public String toString() {
        return "{id=" + id +
                ", nombre='" + nombre + '\'' +
                ", capacity=" + capacity +
                ", description='" + description + '\'' +
                ", amountOfStudents=" + amountOfStudents +
                ", dateOfInit=" + dateOfInit +
                ", dateOfEnd=" + dateOfEnd +
                '}';
    }
}
