package com.example.Project.Event;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

// import com.example.Project.Ticket.Ticket;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.*;

@RestController
@RequestMapping("/api/v1/")
public class EventController {
    @Autowired
    private EventService eventService;

    @GetMapping("/events")
    public ResponseEntity<List<Event>> getAllEvents() {
        return new ResponseEntity<List<Event>>(eventService.allEvents(), HttpStatus.OK);
    }

    // @GetMapping("/allevents")
    // public List<Event> getallEvents(){
    // return eventService.allEvents();
    // }

    @GetMapping("/events/getEventById/{id}")
    public ResponseEntity<Optional<Event>> getSingleEvent(@PathVariable(value = "id") ObjectId id) {
        return new ResponseEntity<Optional<Event>>(eventService.singleEvent(id), HttpStatus.OK);
    }

    // get by event name
    @GetMapping("/events/getEventByName/{eventName}")
    public ResponseEntity<?> getEventByName(@PathVariable(value = "eventName") String eventName) {
        List<Event> e = eventService.findByName(eventName);
        if(e == null){
            // throw new EventNotFoundException(eventName);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Event not found");
        }else{
            return new ResponseEntity<List<Event>>(e, HttpStatus.OK);
        }
        
    }

    // get by event Date
    @GetMapping("/events/getEventByDate/{eventDate}")
    public ResponseEntity<?> getEventByDate(@PathVariable(value = "eventDate") String eventDate) {
        List<Event> e = eventService.findByDate(eventDate);
        if(e == null){
            // throw new EventNotFoundException(eventDate);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Event not found");
        }else{
            return new ResponseEntity<List<Event>>(e, HttpStatus.OK);
        }
        
    } 

    //find by event date and name 
    /*COS DATABASE MAY HAVE 2 SAME EVENT BUT DIFFERENT DATE */
    @GetMapping("/events/getEventByNameDate/{eventName}/{eventDate}")
    public ResponseEntity<?> getEventByNameDate(@PathVariable(value = "eventName") String eventName, @PathVariable(value = "eventDate") String eventDate) {
        Event e = eventService.findByNameAndDate(eventName, eventDate);
        if(e == null){
            // throw new EventNotFoundException(eventDate);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Event not found");
        }else{
            return new ResponseEntity<Event>(e, HttpStatus.OK);
        }
    } 

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/events/add")
    public Event addEvent(@RequestBody Event event) {
        return eventService.save(event);
    }
}