package edu.eci.arsw.teachtome.services;

import edu.eci.arsw.teachtome.model.*;
import edu.eci.arsw.teachtome.persistence.TeachToMePersistence;
import edu.eci.arsw.teachtome.persistence.TeachToMePersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeachToMeServices implements TeachToMeServicesInterface {

    @Autowired
    private TeachToMePersistence persistence;

    @Override
    public Clase getClase(Long classId) throws TeachToMeServiceException {
        try {
            return persistence.getClase(classId);
        } catch (TeachToMePersistenceException e) {
            throw new TeachToMeServiceException(e.getMessage(), e);
        }
    }

    @Override
    public void addClase() throws TeachToMeServiceException {
    }

    @Override
    public List<Draw> getDrawsOfAClass(String className) throws TeachToMeServiceException {
        return null;
    }

    @Override
    public void addDraw(String className) throws TeachToMeServiceException {

    }

    @Override
    public void sendRequest(Request request) throws TeachToMeServiceException {

    }

    @Override
    public void acceptRequest(Request request) throws TeachToMeServiceException {

    }

    @Override
    public void sendMessage(Message message) throws TeachToMeServiceException {

    }

    @Override
    public List<Message> getChat(String className) throws TeachToMeServiceException {
        return null;
    }

    @Override
    public void addUser(User user) throws TeachToMeServiceException {

    }

    @Override
    public User getUser(String email) throws TeachToMeServiceException {
        return null;
    }

}
