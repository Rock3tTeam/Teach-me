package edu.eci.arsw.teachtome.services;

import edu.eci.arsw.teachtome.model.Draw;
import edu.eci.arsw.teachtome.model.Message;
import edu.eci.arsw.teachtome.model.Request;
import edu.eci.arsw.teachtome.model.User;

import java.util.List;

public interface TeachToMeServicesInterface {
    public String getClase() throws TeachToMeServiceException;

    public void addClase() throws TeachToMeServiceException;

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
