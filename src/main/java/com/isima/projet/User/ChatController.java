package com.isima.projet.User;


import com.isima.projet.Messagerie.Message;
import com.isima.projet.Messagerie.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
@CrossOrigin(origins = "http://localhost:4200")
@Controller
@RequestMapping("/api/v1")
public class ChatController {

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private MessageRepository messageRepository;




    @MessageMapping("/messages")
    public void handleMessage(Message message) {
        message.setTimestamp(new Date());
        messageRepository.save(message);
        template.convertAndSend("/channel/chat/" + message.getChannel(), message);
    }
}
