package com.example.Project.Event;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;

    public List<Event> allEvents() {
        return eventRepository.findAll();
    }

    public Optional<Event> singleEvent(ObjectId id) {
        return eventRepository.findById(id);
    }

    public Event save(Event e){
        return eventRepository.save(e);
    }

    public List<Event> findByName(String eventName) {
        List<Event> eventList = eventRepository.findAll();
        List<Event> output = new ArrayList<>();
        for(Event e : eventList){
            if(e.getName().equals(eventName)){
                output.add(e);
            }
        }
        return output;
    }

    public List<Event> findByDate(String eventDate) {
        List<Event> eventList = eventRepository.findAll();
        List<Event> output = new ArrayList<>();
        for(Event e : eventList){
            if(e.getDate().equals(eventDate)){
                output.add(e);
            }
        }
        return output;
    }

    public Event findByNameAndDate(String eventName, String eventDate) {
        List<Event> eventList = eventRepository.findByName(eventName);

        for(Event e : eventList){
            if(e.getDate().equals(eventDate)){
                return e;
            }
        }
        return null;
    }
}