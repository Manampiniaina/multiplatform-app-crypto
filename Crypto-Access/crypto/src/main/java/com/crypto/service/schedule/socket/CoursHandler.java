package com.crypto.service.schedule.socket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class CoursHandler extends TextWebSocketHandler {

    private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Vous pouvez gérer les messages entrants ici si nécessaire
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        System.out.println("Session ajoutée : " + session.getId() + " - Nombre total de sessions : " + sessions.size());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        // System.out.println("Session supprimée : " + session.getId() + " - Nombre total de sessions : " + sessions.size());
    }

    public void signal(String nouveauCours) throws Exception {
        // System.out.println("Nombre de sessions actives : " + sessions.size());
        for (WebSocketSession session : sessions) {
            // System.out.println("Envoi du signal à la session : " + session.getId());
            session.sendMessage(new TextMessage(nouveauCours));
        }
    }

}
