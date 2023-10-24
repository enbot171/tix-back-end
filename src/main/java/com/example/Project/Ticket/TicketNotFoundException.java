package com.example.Project.Ticket;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND) // 404 Error

public class TicketNotFoundException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public TicketNotFoundException(String id) {
        super("Could not find ticket " + id);
    }
    // public TicketNotFoundException(String eventName) {
    //     super("Could not find ticket " + eventName);
    // }
}