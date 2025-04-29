package com.crypto.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.crypto.service.schedule.socket.CoursHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final CoursHandler coursHandler;
    
    public WebSocketConfig(CoursHandler coursHandler) {
        this.coursHandler = coursHandler;
    }
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(coursHandler, "/changement-cours")
                .setAllowedOrigins("*"); // Ajustez les origines selon vos besoins
    }
}
