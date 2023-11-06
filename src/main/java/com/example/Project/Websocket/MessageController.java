package com.example.Project.Websocket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {
    
    @MessageMapping("/joinBuyset")
    @SendTo("/tickets")
    public String joinBuySetMessage(@Payload Message message){
        System.out.println(message.getMessage());
        return message.getMessage();
    }

    @MessageMapping("/leaveBuyset")
    @SendTo("/purchases")
    public String leaveBuySetMessage(@Payload Message message){
        System.out.println(message.getMessage());
        return message.getMessage();
    }
}
