package com.example.Project.Event;

import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EventNotFoundException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public EventNotFoundException(ObjectId eventId) {
        super("Could not find event " + eventId);
    }
    public EventNotFoundException(String eventNameOrDate) {
        super("Could not find event " + eventNameOrDate);
    }

}