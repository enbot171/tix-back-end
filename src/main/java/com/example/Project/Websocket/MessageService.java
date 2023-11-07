package com.example.Project.Websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    /*
     * send next guy in waiting queue into buyset
     */
    public void notifyUserEnteringBuySet(String userId){
        String message = userId + "is entering the queue!";
        Message toSend = new Message(userId, message, true);
        System.out.println(toSend.getisInBuySet());
        simpMessagingTemplate.convertAndSendToUser(userId, "/private", toSend);
    }

    /*
     * send next guy in waiting queue into buyset
     */
    public void notifyUserLeavingBuySet(String userId){
        String message = userId + "is leaving the queue!";
        Message toSend = new Message(userId, message, false);
        simpMessagingTemplate.convertAndSend("/leaveBuyset", toSend);
    }
}
