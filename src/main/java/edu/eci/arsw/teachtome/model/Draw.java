package edu.eci.arsw.teachtome.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Clase que representa un dibujo de una sesión dentro de la aplicación TeachToMe
 */
@Entity(name = "Draw")
@Table(name = "draws")
public class Draw {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column(name = "date_of_draw")
    private Timestamp dateOfDraw;

    @ManyToOne
    @JoinColumn(name = "session")
    @JsonBackReference
    private Session session;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinColumn(name = "draw")
    @JsonManagedReference
    private List<Point> points = new ArrayList<Point>();

    public Draw() {
    }

    public Draw(Timestamp dateOfDraw, List<Point> points) {
        this.dateOfDraw = dateOfDraw;
        this.points = points;
    }

    public long getId() {
        return id;
    }

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Timestamp getDateOfDraw() {
        return dateOfDraw;
    }

    public void setDateOfDraw(Timestamp dateOfDraw) {
        this.dateOfDraw = dateOfDraw;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}
