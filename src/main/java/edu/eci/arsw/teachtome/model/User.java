package edu.eci.arsw.teachtome.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Clase que representa un usuario dentro de la aplicación TeachToMe
 */
@Entity
@Table(name = "users")
public class User {
    @Id
    private String email;

    @Column(name = "first_name", length = 255, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 255, nullable = false)
    private String lastName;

    @Column(name = "password", length = 255, nullable = false)
    private String password;

    @Column(name = "description", length = 255, nullable = false)
    private String description;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "professor")
    private List<Clase> teachingClasses;


    public User() {
    }

    public User(String email, String firstName, String lastName, String password, String description, List<Clase> teachingClasses) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.description = description;
        this.teachingClasses = teachingClasses;
    }

    public User(String email, String firstName, String lastName, String password, String description) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.description = description;
        this.teachingClasses = new CopyOnWriteArrayList<>();
    }

    public List<Clase> getTeachingClasses() {
        return teachingClasses;
    }

    public void setTeachingClasses(List<Clase> teachingClasses) {
        this.teachingClasses = teachingClasses;
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

    @Override
    public String toString() {
        return "{email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", password='" + password + '\'' +
                ", description='" + description + '\'' +
                ", teachingClasses=" + teachingClasses +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(email, user.email) &&
                Objects.equals(firstName, user.firstName) &&
                Objects.equals(lastName, user.lastName) &&
                Objects.equals(password, user.password) &&
                Objects.equals(description, user.description) &&
                Objects.equals(teachingClasses, user.teachingClasses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, firstName, lastName, password, description, teachingClasses);
    }
}
