package edu.eci.arsw.teachtome.controllers;

import edu.eci.arsw.teachtome.model.Message;
import edu.eci.arsw.teachtome.model.Point;
import edu.eci.arsw.teachtome.services.TeachToMeServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

/**
 * Controlador de estilo publish suscribe para los eventos ocurridos en las sesiones de clase de Teach To Me
 */
@Controller
public class StompController {

    @Autowired
    private SimpMessagingTemplate msgt;

    @Autowired
    private TeachToMeServices services;

    /**
     * Manejador de Eventos Relacionados con el chat de la sesion de clase
     *
     * @param message Mensaje Enviado en el chat
     * @param classId Identificador de la clase sobre la cual se establecio la comunicacion
     * @throws Exception Cuando Falla en Transmitir el mensaje o el envio tuvo algun error
     */
    @MessageMapping("/messages.{classId}")
    public void handleChatEvent(Message message, @DestinationVariable Long classId) throws Exception {
        services.sendMessage(message, classId, message.getSender());
        msgt.convertAndSend("/topic/messages." + classId, message);
    }

    /**
     * Manejador de Eventos Relacionados con el los dibujos del tablero de la sesión de clase
     *
     * @param point   punto del Dibujo enviado en el tablero
     * @param classId Identificador de la clase sobre la cual es estableció la comunicación
     * @throws Exception Cuando Falla Al Transmitir el Nuevo Punto
     */
    @MessageMapping("/draws.{classId}")
    public void handleDrawEvent(Point point, @DestinationVariable Long classId) throws Exception {
        /*Draw draw = new Draw();
        ArrayList<Point> points = new ArrayList<>();
        points.add(point);
        draw.setPoints(points);
        services.addDrawToCache(classId, draw);*/
        msgt.convertAndSend("/topic/draws." + classId, point);
    }

    /**
     * Manejador de Eventos Relacionados con el tablero de la sesión de clase
     *
     * @param classId Identificador de la clase sobre la cual es estableció la comunicación
     * @throws Exception Cuando Falla Al Transmitir el mensaje
     */
    @MessageMapping("/board.{classId}")
    public void handleBoardEvent(String clear, @DestinationVariable Long classId) throws Exception {
        //services.deleteDrawFromCache(classId);
        msgt.convertAndSend("/topic/board." + classId, clear);
    }
}