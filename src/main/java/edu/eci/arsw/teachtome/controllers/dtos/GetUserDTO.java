package edu.eci.arsw.teachtome.controllers.dtos;

import edu.eci.arsw.teachtome.model.Clase;
import edu.eci.arsw.teachtome.model.User;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class GetUserDTO implements Serializable {
    private long id;

    private String email;

    private String firstName;

    private String lastName;

    private String description;

    private List<ClaseDTO> teachingClasses;

    private List<ClaseDTO> studyingClasses;

    public GetUserDTO(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.description = user.getDescription();
        this.teachingClasses = getClassesDTO(user.getTeachingClasses());
        this.studyingClasses = getClassesDTO(user.getStudyingClasses());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ClaseDTO> getTeachingClasses() {
        return teachingClasses;
    }

    public void setTeachingClasses(List<ClaseDTO> teachingClasses) {
        this.teachingClasses = teachingClasses;
    }

    public List<ClaseDTO> getStudyingClasses() {
        return studyingClasses;
    }

    public void setStudyingClasses(List<ClaseDTO> studyingClasses) {
        this.studyingClasses = studyingClasses;
    }

    public static List<ClaseDTO> getClassesDTO(List<Clase> clases) {
        List<ClaseDTO> claseDTOS = new CopyOnWriteArrayList<>();
        for (Clase clase : clases) {
            claseDTOS.add(new ClaseDTO(clase));
        }
        return claseDTOS;
    }
}
