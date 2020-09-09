package edu.eci.arsw.teachtome.services;

import org.springframework.stereotype.Service;

@Service
public class TeachToMeServices implements TeachToMeServicesInterface {

    //@Autowired
    //private ARepository repository = null;

    @Override
    public String getClase() {
        return "Listo";
    }

}
