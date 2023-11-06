package com.example.Project.Event;

import java.util.List;
// import java.util.List;
// import java.util.Optional;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

// import com.example.Ticketing.Ticket.Ticket;

@Repository
public interface EventRepository extends MongoRepository<Event, ObjectId> {
    Optional<Event> findById(ObjectId eventId);
    boolean existsById(ObjectId objectId);
    List<Event> findByName(String eventName);
    List<Event> findByDate(String eventDate);
    Event findByNameAndDate(String eventName, String eventDate);

}