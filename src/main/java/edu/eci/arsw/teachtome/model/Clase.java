package edu.eci.arsw.teachtome.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
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

    /**
     * Constructor por defecto de la entidad clase
     */
    public Clase() {
    }

    /**
     * Constructor por defecto de la entidad clase
     *
     * @param nombre           - Nombre de la clase
     * @param capacity         - Capacidad de la clase
     * @param description      - Descripcion de la clase
     * @param amountOfStudents - Cantidad Actual de estudiantes
     * @param dateOfInit       - Fecha de Inicio de la sesion
     * @param dateOfEnd        - Fecha de Fin de la sesion
     */
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

    /**
     * Incrementa la cantidad de estudiantes en uno
     */
    public void increaseAmount() {
        amountOfStudents++;
    }

    /**
     * Verifica si la capacidad de la clase esta completa
     *
     * @return Valor booleano que representa si la capacidad de la clase esta completa
     */
    public boolean isFull() {
        return amountOfStudents == capacity;
    }

    /**
     * Verifica si un usuario es estudiante de la clase
     *
     * @param user - Usuario a verificar
     * @return Valor booleano que representa si un usuario es estudiante de la clase
     */
    public boolean hasStudent(User user) {
        boolean found = false;
        for (User student : students) {
            if (student.equals(user)) {
                found = true;
                break;
            }
        }
        return found;
    }

    /**
     * Compara los valores estaticos con otra clase
     *
     * @param clase Clase con la que se va a comparar
     * @return Valor booleano que determina si los valores estaticos de las dos clases son iguales
     */
    public boolean lazyEquals(Clase clase) {
        return capacity == clase.capacity &&
                Objects.equals(nombre, clase.nombre) &&
                Objects.equals(description, clase.description);
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
