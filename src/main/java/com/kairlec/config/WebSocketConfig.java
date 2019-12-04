package com.kairlec.config;

import com.kairlec.contrller.SSHHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(SSHControllor(), "admin/ssh").setAllowedOrigins("*");
    }

    public WebSocketHandler SSHControllor() {
        return new SSHHandler();
    }

}