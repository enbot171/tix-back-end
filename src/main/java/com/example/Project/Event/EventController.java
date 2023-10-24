package com.example.Project.Event;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.Project.QueueService.WaitingQueue;
import com.example.Project.User.User;
import com.example.Project.User.UserRepository;

// import com.example.Ticketing.Ticket.Ticket;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/")
public class EventController {
    @Autowired
    private EventService eventService;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private WaitingQueue queueService;

    // @Autowired
    // private BuyingQueue buyList;

    // /*
    //  * Checking if the automation works
    //  */
    // @GetMapping("/buyList")
    // public int checkBuyList(){
    //     return buyList.getSize();
    // }
    // @GetMapping("/queue")
    // public int checkQueueList(String eventId){
    //     return queueService.getSize(eventId);
    // }

    //find all events
    @GetMapping("/events")
    public ResponseEntity<List<Event>> getAllEvents() {
        return new ResponseEntity<List<Event>>(eventService.allEvents(), HttpStatus.OK);
    }

    @GetMapping("/events/getEventById/{id}")
    public ResponseEntity<Optional<Event>> getSingleEvent(@PathVariable(value = "id") ObjectId id) {
        return new ResponseEntity<Optional<Event>>(eventService.singleEvent(id), HttpStatus.OK);
    }

    /*
     * Throwing user to buying Queue or Waiting Queue when they click buy button
     */
    @PostMapping("/buy/{userId}/{eventName}/{eventDate}")
    public ResponseEntity<String> addToWaitingList(@PathVariable(value = "userId") String userId,
        @PathVariable(value = "eventName") String eventName, @PathVariable(value = "eventDate") String eventDate ){
        Optional<User> userOptional = userRepo.findById(userId);
        if(userOptional.isPresent()){
            User cfmUser = userOptional.get();
            if(cfmUser == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
            Event event = eventService.findByNameAndDate(eventName, eventDate);

            if(!queueService.contains(event.getId(), cfmUser.getId())){

                queueService.addUsers(event.getId(), cfmUser.getId());

                return ResponseEntity.status(HttpStatus.OK).body("User added to waiting room.");
            
            }else{

                return ResponseEntity.status(HttpStatus.OK).body("User already in the waiting room.");
            }
        }
        
        
        // System.out.println(queueService.getNextUserId());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        

    }

    /*-------------------------------- Find by Event Name, Date ---------------------------------------- */
    // get by event name
    // @GetMapping("/events/getEventByName/{eventName}")
    // public ResponseEntity<?> getEventByName(@PathVariable(value = "eventName") String eventName) {
    //     List<Event> e = eventService.findByName(eventName);
    //     if(e == null){
    //         // throw new EventNotFoundException(eventName);
    //         return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Event not found");
    //     }else{
    //         return new ResponseEntity<List<Event>>(e, HttpStatus.OK);
    //     }

    // }
    /*------------------------------------------ For Demo --------------------------------- */

    //Returns the date
    @GetMapping("events/getDatesByName/{eventName}")
    public ResponseEntity<?> getEventByName(@PathVariable(value = "eventName") String eventName) {
        List<Event> e = eventService.findByName(eventName);
        List<String> dates = new ArrayList<>();
        if(e == null){
            // throw new EventNotFoundException(eventName);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Event not found");
        } else{
            for(Event event : e){
                dates.add(event.getDate());
            }
            return new ResponseEntity<List<String>>(dates, HttpStatus.OK);
        }

    }
    @GetMapping("events/getCategoriesByName/{eventName}")
    public ResponseEntity<?> getCategoriesByName(@PathVariable(value = "eventName") String eventName) {
        List<Event> e = eventService.findByName(eventName);
        if(e == null){
            // throw new EventNotFoundException(eventName);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Event not found");
        }else{
            return new ResponseEntity<int[]>(e.get(0).getCategories(),HttpStatus.OK);
        }
    }

    /*------------------------------------------ For Demo --------------------------------- */


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

    //add event
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/events/add")
    public Event addEvent(@RequestBody Event event) {
        return eventService.save(event);
    }
}