package com.example.Project.Event;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.Project.QueueService.BuyingQueueEntity;
import com.example.Project.QueueService.BuyingQueueService;
import com.example.Project.QueueService.WaitingQueueEntity;
import com.example.Project.QueueService.WaitingQueueService;
import com.example.Project.User.User;
import com.example.Project.User.UserRepository;

// import com.example.Ticketing.Ticket.Ticket;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/event")
public class EventController {
    @Autowired
    private EventService eventService;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private WaitingQueueService waitService;

    @Autowired
    private BuyingQueueService buyService;
    

    /*-------------------------------------------- Queue Control Start------------------------------- */
    
    /*
     * Get all event waiting queues
     */
    @GetMapping("/waitQueue")
    public ResponseEntity<List<WaitingQueueEntity>> getAllQueues(){
        
        return new ResponseEntity<List<WaitingQueueEntity>>(waitService.allEventQueue(), HttpStatus.OK);
    }
    @GetMapping("/buySet")
    public ResponseEntity<List<BuyingQueueEntity>> getBuyQueues(){
        
        return new ResponseEntity<List<BuyingQueueEntity>>(buyService.allEventSets(), HttpStatus.OK);
    }

    /*
     * Get waiting queue number using the eventName and UserId
     */
    @GetMapping("/{eventName}/{userId}/getQueueNum")
    public ResponseEntity<?> findQueueNumber(@PathVariable(value = "eventName") String eventName, @PathVariable(value = "userId") String userId){
        Optional<User> optUser = userRepo.findById(userId);
        // User user = userRepo.findById(userId).orElse(null);
        if(!optUser.isPresent()){
            return new ResponseEntity<String>("User not found", HttpStatus.OK);
        }
        User user = optUser.get();
        if(user == null){
            return new ResponseEntity<String>("User not found", HttpStatus.OK);
        }
        int queueNum = waitService.findQueueNum(eventName, userId);
        
        
        return new ResponseEntity<Integer>( queueNum, HttpStatus.OK);
    }

    /*
     * When User clicks buy button at event page
     * Throw user to buying Set or Waiting Queue depending on how many ppl is in the queue
     */
    @PostMapping("/{userId}/{eventName}/enqueue")
    public ResponseEntity<?> addToWaitingList(@PathVariable(value = "userId") String userId,
        @PathVariable(value = "eventName") String eventName){
        Optional<User> userOptional = userRepo.findById(userId);
        if(!userOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        User cfmUser = userOptional.get();
        if(cfmUser == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        //check if user is in buy set before adding him to the set
        if(cfmUser.getInBuySet()){
            // cfmUser.setInBuySet(false);
            // userRepo.save(cfmUser);
            return new ResponseEntity<Boolean>(false, HttpStatus.OK);
        }
        //check if buying set < 100, if yes, add stright to buy set
        //if no then add to waiting queue
        Set<String> eventSet = buyService.findSet(eventName);
        if (eventSet.size() < 1){
            buyService.addToBuyingQueue(eventName, userId);

            cfmUser.setInBuySet(true);

            userRepo.save(cfmUser);
            return new ResponseEntity<Boolean>(true, HttpStatus.OK);
        }else{
            waitService.addWaitingQueues(eventName, userId);
            // int queueNumber = waitService.findQueueNum(eventName, userId);
            cfmUser.setInBuySet(false);
            
            return new ResponseEntity<Boolean>(false, HttpStatus.OK);
        }

    }


    /*----------------------------------------- Queue Control End ------------------------------- */




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



    //find all events
    @GetMapping("/getEvents")
    public ResponseEntity<List<Event>> getAllEvents() {
        return new ResponseEntity<List<Event>>(eventService.allEvents(), HttpStatus.OK);
    }

    @GetMapping("/{id}/getEvent")
    public ResponseEntity<Optional<Event>> getSingleEvent(@PathVariable(value = "id") ObjectId id) {
        return new ResponseEntity<Optional<Event>>(eventService.singleEvent(id), HttpStatus.OK);
    }

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
    @GetMapping("/{eventName}/Categories")
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
    @GetMapping("/{eventDate}/Dates")
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
    @GetMapping("/{eventName}/{eventDate}/getEvent")
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
    @PostMapping("/addEvent")
    public Event addEvent(@RequestBody Event event) {
        return eventService.save(event);
    }
}