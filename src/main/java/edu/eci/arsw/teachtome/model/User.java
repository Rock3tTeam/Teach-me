package edu.eci.arsw.teachtome.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import edu.eci.arsw.teachtome.controllers.dtos.ClaseDTO;
import edu.eci.arsw.teachtome.controllers.dtos.CreateUserDTO;
import edu.eci.arsw.teachtome.controllers.dtos.GetUserDTO;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
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
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column(name = "email", length = 255, unique = true)
    private String email;

    @Column(name = "first_name", length = 255, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 255, nullable = false)
    private String lastName;

    @Column(name = "password", length = 255, nullable = false)
    private String password;

    @Column(name = "description", length = 255)
    private String description;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "professor")
    @JsonManagedReference
    private List<Clase> teachingClasses;

    @ManyToMany(mappedBy = "students", fetch = FetchType.EAGER)
    private List<Clase> studyingClasses = new CopyOnWriteArrayList<>();

    /**
     * Constructor por defecto de la entidad Usuario
     */
    public User() {
    }

    /**
     * Constructor por defecto de la entidad Usuario
     *
     * @param email           - Email del usuario
     * @param firstName       - Nombre del Usuario
     * @param lastName        - Apellido del Usuario
     * @param password        - Clave del Usuario
     * @param description     - Descripcion del Usuario
     * @param teachingClasses - Clases que dicta el Usuario
     */
    public User(String email, String firstName, String lastName, String password, String description, List<Clase> teachingClasses) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.description = description;
        this.teachingClasses = teachingClasses;
    }

    /**
     * Constructor por defecto de la entidad Usuario
     *
     * @param email       - Email del usuario
     * @param firstName   - Nombre del Usuario
     * @param lastName    - Apellido del Usuario
     * @param password    - Clave del Usuario
     * @param description - Descripcion del Usuario
     */
    public User(String email, String firstName, String lastName, String password, String description) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.description = description;
        this.teachingClasses = new CopyOnWriteArrayList<>();
    }

    /**
     * Constructor por defecto de la entidad Usuario
     *
     * @param email     - Email del usuario
     * @param firstName - Nombre del Usuario
     * @param lastName  - Apellido del Usuario
     * @param password  - Clave del Usuario
     */
    public User(String email, String firstName, String lastName, String password) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.teachingClasses = new CopyOnWriteArrayList<>();
    }

    /**
     * Constructor de la clase User con base en la clase GetUserDTO
     *
     * @param getUserDTO POJO version for get an user
     */
    public User(GetUserDTO getUserDTO) {
        this.id = getUserDTO.getId();
        this.email = getUserDTO.getEmail();
        this.firstName = getUserDTO.getFirstName();
        this.lastName = getUserDTO.getLastName();
        this.description = getUserDTO.getDescription();
        this.teachingClasses = getClassesFromDTO(getUserDTO.getTeachingClasses());
        this.studyingClasses = getClassesFromDTO(getUserDTO.getStudyingClasses());
    }

    public User(CreateUserDTO createUserDTO) {
        this.email = createUserDTO.getEmail();
        this.firstName = createUserDTO.getFirstName();
        this.lastName = createUserDTO.getLastName();
        this.password = createUserDTO.getPassword();
        this.description = createUserDTO.getDescription();
        this.teachingClasses = new CopyOnWriteArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public List<Clase> getStudyingClasses() {
        return studyingClasses;
    }

    public void setStudyingClasses(List<Clase> studyingClasses) {
        this.studyingClasses = studyingClasses;
    }

    public List<Clase> getClassesFromDTO(List<ClaseDTO> claseDTOS) {
        List<Clase> clases = new CopyOnWriteArrayList<>();
        for (ClaseDTO claseDTO : claseDTOS) {
            clases.add(new Clase(claseDTO));
        }
        return clases;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", password='" + password + '\'' +
                ", description='" + description + '\'' +
                ", teachingClasses=" + teachingClasses +
                ", studyingClasses=" + studyingClasses +
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
                Objects.equals(description, user.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, firstName, lastName, password, description, teachingClasses);
    }
}
