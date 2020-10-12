package edu.eci.arsw.teachtome.controllers;


import edu.eci.arsw.teachtome.model.Message;
import edu.eci.arsw.teachtome.services.TeachToMeServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;


@Controller
public class StompController {

    @Autowired
    SimpMessagingTemplate msgt;

    @Autowired
    TeachToMeServices services;


    @MessageMapping("/messages.{classId}")
    public void handleBuyEvent(Message message, @DestinationVariable Long classId) throws Exception {
        System.out.println("Nuevo mensage recibido en el servido:" + message.getContent());
        services.sendMessage(message, classId, message.getSender());
        msgt.convertAndSend("/topic/messages." + classId, message);
    }
}