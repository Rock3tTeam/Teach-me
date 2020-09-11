package edu.eci.arsw.teachtome.services;

import edu.eci.arsw.teachtome.model.Draw;
import edu.eci.arsw.teachtome.model.Message;
import edu.eci.arsw.teachtome.model.Request;
import edu.eci.arsw.teachtome.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeachToMeServices implements TeachToMeServicesInterface {

    //@Autowired
    //private ARepository repository = null;

    @Override
    public String getClase() throws TeachToMeServiceException {
        return "Listo";
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
