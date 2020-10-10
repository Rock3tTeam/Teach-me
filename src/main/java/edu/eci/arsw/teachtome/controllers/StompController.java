package edu.eci.arsw.teachtome.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.concurrent.ConcurrentHashMap;


@Controller
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class StompController {

    @Autowired
    SimpMessagingTemplate msgt;

    @MessageMapping("/chat")
    public void handleBuyEvent() throws Exception {
        System.out.println("Nuevo mensaje recibido");
    }
}