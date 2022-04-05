package com.isima.projet.Messagerie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class MessagerieController {
    @Autowired
    private ServiceMessage serviceMessage;

    @GetMapping("/message-all")
    public List<messagerie> getAllMessage() {
        return serviceMessage.getAll();
    }

    @GetMapping("/message/{id}")
    public messagerie getMessageById(@PathVariable long id) {
        return serviceMessage.getById(id);
    }
    @PostMapping("/message/ajouter")
    public messagerie createMessage(@RequestBody messagerie messagerie) {
        return serviceMessage.save(messagerie);
    }
    @MessageMapping("/chat.register")
    @SendTo("/topic/public")
    public messagerie register(@Payload messagerie chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", "hello");
        return chatMessage;
    }
}
