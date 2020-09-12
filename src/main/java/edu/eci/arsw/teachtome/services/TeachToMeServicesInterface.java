package edu.eci.arsw.teachtome.services;

import edu.eci.arsw.teachtome.model.*;

import java.util.List;

/**
 * Interfaz de la capa de servicios de la aplicación TeachToMe
 */
public interface TeachToMeServicesInterface {
    /**
     * Consulta una clase dentro del modelo
     *
     * @param classId - Identificador de la clase
     * @return Un objeto tipo Clase con su respectivo identificador
     * @throws TeachToMeServiceException - Cuando no existe clase con ese identificador
     */
    public Clase getClase(Long classId) throws TeachToMeServiceException;

    public void addClase(Clase clase) throws TeachToMeServiceException;

    public List<Draw> getDrawsOfAClass(String className) throws TeachToMeServiceException;

    public void addDraw(String className) throws TeachToMeServiceException;

    public void sendRequest(Request request) throws TeachToMeServiceException;

    public void acceptRequest(Request request) throws TeachToMeServiceException;

    public void sendMessage(Message message) throws TeachToMeServiceException;

    public List<Message> getChat(String className) throws TeachToMeServiceException;

    public void addUser(User user) throws TeachToMeServiceException;

    public User getUser(String email) throws TeachToMeServiceException;
    //FALTA METODO DE AUTENTICACION

}
