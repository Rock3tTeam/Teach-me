package edu.eci.arsw.teachtome.model;

import java.sql.Date;
import java.util.List;

//@Entity
public class Clase {
    //@Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    private String nombre;
    //@OneToMany(mappedBy = "equipo",cascade = CascadeType.ALL)
    //@JsonManagedReference
    //private List<E> elementos;
    //@ManyToOne
    //@JsonBackReference
    //@JoinColumn(name="fk_algo")
    //@Column(name = "algo",nullable = false,length = 255)
    //private Object object;
    private int capacity;
    private String description;
    private int amountOfStudents;
    private Date dateOfInit;
    private Date dateOfEnd;
    private Session session;
    private List<Request> requests;
    private List<User> students;

    public Clase(String nombre, int capacity, String description, Date dateOfInit, Date dateOfEnd, Session session, List<Request> requests, List<User> students) {
        this.nombre = nombre;
        this.capacity = capacity;
        this.description = description;
        this.dateOfInit = dateOfInit;
        this.dateOfEnd = dateOfEnd;
        this.session = session;
        this.requests = requests;
        this.students = students;
        this.amountOfStudents = 0;
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

    public Date getDateOfInit() {
        return dateOfInit;
    }

    public void setDateOfInit(Date dateOfInit) {
        this.dateOfInit = dateOfInit;
    }

    public Date getDateOfEnd() {
        return dateOfEnd;
    }

    public void setDateOfEnd(Date dateOfEnd) {
        this.dateOfEnd = dateOfEnd;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public List<Request> getRequests() {
        return requests;
    }

    public void setRequest(List<Request> requests) {
        this.requests = requests;
    }

    public List<User> getStudents() {
        return students;
    }

    public void setStudents(List<User> students) {
        this.students = students;
    }
}
