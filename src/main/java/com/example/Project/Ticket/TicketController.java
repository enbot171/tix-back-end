package com.example.Project.Ticket;

import java.time.LocalDateTime;
//ticketing
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.Project.Event.Event;
import com.example.Project.Event.EventNotFoundException;
import com.example.Project.Event.EventRepository;
import com.example.Project.Purchase.Purchase;
import com.example.Project.Purchase.PurchaseService;
import com.example.Project.QueueService.BuyingQueueService;
import com.example.Project.QueueService.WaitingQueueService;
import com.example.Project.User.User;
import com.example.Project.User.UserRepository;
import com.example.Project.Websocket.MessageService;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/")
public class TicketController {

    // private static final Logger logger = LoggerFactory.getLogger(TicketController.class);
    @Autowired
    private EventRepository events;
    @Autowired
    private TicketRepository tickets;
    @Autowired
    private PurchaseService purchaseServices;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private BuyingQueueService buyService;
    @Autowired
    private WaitingQueueService waitService;
    @Autowired
    private MessageService messageServices;

    public TicketController(TicketRepository tickets, EventRepository events, PurchaseService purchaseServices) {
        this.tickets = tickets;
        this.events = events;
        this.purchaseServices = purchaseServices;
    }

    // @GetMapping("/events/{eventId}/tickets")
    // public List<Ticket> getAllTicketsByEventId(@PathVariable ( value = "eventId")
    // ObjectId eventId) {
    // if(!events.existsById(eventId)) {
    // throw new EventNotFoundException(eventId);
    // }
    // return tickets.findByEventId(eventId);
    // }

    /*------------------------------ Find by Event ID & other Fields ---------------------------------- */
    @GetMapping("/events/{eventId}/tickets")
    public List<Ticket> getAllTicketsByEventId(@PathVariable(value = "eventId") ObjectId eventId) {
        if (!events.existsById(eventId)) {
            throw new EventNotFoundException(eventId);
        }
        return tickets.findByEventId(eventId);
    }

    // sets boolean sold to true
    @PutMapping("/events/{eventId}/tickets/{ticketId}/sell")
    public ResponseEntity<?> sellTicket(@PathVariable(value = "eventId") ObjectId eventId,
                                        @PathVariable(value = "ticketId") String ticketId,
                                        @RequestBody Ticket newTicket) {
        if (!events.existsById(eventId)) {
            throw new EventNotFoundException(eventId);
        }
        return tickets.findByIdAndEventId(ticketId, eventId).map(ticket -> {
            if (!ticket.isSold()) {
                ticket.setSold(true);
                tickets.save(ticket);
                return new ResponseEntity<Ticket>(ticket, HttpStatus.OK);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ticket is sold");
            }

        }).orElseThrow(() -> new TicketNotFoundException(ticketId));
    }

    //get ticket by event ID and category
    @GetMapping("/events/{eventId}/ticketByCategory/{category}")
    public Optional<List<Ticket>> getTicketByEventIdAndCategory(@PathVariable(value = "eventId") ObjectId eventId,
                                                                @PathVariable(value = "category") int category) {

        if (!events.existsById(eventId)) {
            throw new EventNotFoundException(eventId);
        }

        return tickets.findByEventIdAndCategory(eventId, category);
    }

    //get ticket by event ID and seat Number
    @GetMapping("/events/{eventId}/ticketBySeat/{seat_num}")
    public Optional<Ticket> getTicketByEventIdAndSeatNum(@PathVariable(value = "eventId") ObjectId eventId, @PathVariable(value = "seat_num") int seat_num) {
        if (!events.existsById(eventId)) {
            throw new EventNotFoundException(eventId);
        }
        return tickets.findByEventIdAndSeatNum(eventId, seat_num);
    }

    // adds ticket to ticket list in Event
    // http body -> int seatNum, boolean sold, int category
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/events/{eventId}/tickets")
    public Ticket addTicket(@PathVariable(value = "eventId") ObjectId eventId, @RequestBody Ticket ticket) {
        // using "map" to handle the returned Optional object from "findById(eventId)"
        return events.findById(eventId).map(event -> {
            event.getTicketIds().add(ticket);
            ticket.setEventId(eventId);
            ticket.setPrice(event.getPrice()[(ticket.getCategory() - 1)]);
            events.save(event);
            return tickets.save(ticket);
        }).orElseThrow(() -> new EventNotFoundException(eventId));
    }

    /*
     * get ticket by EventName and Category
     * returning a LIST
     */
    @GetMapping("/events/getEventByNameDate/{eventName}/{eventDate}/ticketByCategory/{category}")
    public List<Ticket> getTicketByEventNameAndCategory(@PathVariable(value = "eventName") String eventName, @PathVariable(value = "eventDate") String eventDate,
                                                        @PathVariable(value = "category") int category) {

        Event e = events.findByNameAndDate(eventName, eventDate); //find event by name & date
        if (e == null) {
            throw new EventNotFoundException(eventName);
        } else if (!events.existsById(e.getId())) {
            throw new EventNotFoundException(eventName);
        }

        Optional<List<Ticket>> outputList = tickets.findByEventIdAndCategory(e.getId(), category);
        if (outputList.isPresent()) {
            //  List<Ticket> ticketList = outputList.get();
            return outputList.get();

        } else {
            throw new TicketNotFoundException(eventName);
        }
    }

    /*
     * Get available tickets by EventName, Date and Category
     * returns a list of integers of seat numbers
     */
    @GetMapping("/events/getEventByNameDate/{eventName}/{eventDate}/ticketByCategory/{category}/allAvailableSeats")
    public ResponseEntity<?> getTicketSeatNumberByEventNameAndCategory(@PathVariable(value = "eventName") String eventName, @PathVariable(value = "eventDate") String eventDate,
                                                                       @PathVariable(value = "category") int category) {
        Event e = events.findByNameAndDate(eventName, eventDate); //find event by name & date
        if (e == null) {
            throw new EventNotFoundException(eventName);
        } else if (!events.existsById(e.getId())) {
            throw new EventNotFoundException(eventName);
        }
        Optional<List<Ticket>> availTickets = tickets.findByEventIdAndCategoryAndSold(e.getId(), category, false);
        List<Integer> availSeats = new ArrayList<>();
        if (availTickets.isPresent()) {
            List<Ticket> ticketList = availTickets.get();
            for (Ticket t : ticketList) {
                if (!t.isSold()) {
                    availSeats.add(t.getSeatNum());
                }
            }
            return new ResponseEntity<List<Integer>>(availSeats, HttpStatus.OK);
        } else {
            throw new TicketNotFoundException(eventName);
        }
    }

    /*
     * Get all Seat numbers in the category
     * returns a list of integers of seat numbers
     */
    @GetMapping("/events/getEventByNameDate/{eventName}/{eventDate}/ticketByCategory/{category}/allSeatNumbers")
    public ResponseEntity<?> getTicketAllSeatNumberByEventNameAndCategory(@PathVariable(value = "eventName") String eventName, @PathVariable(value = "eventDate") String eventDate,
                                                                          @PathVariable(value = "category") int category) {
        Event e = events.findByNameAndDate(eventName, eventDate); //find event by name & date
        if (e == null) {
            throw new EventNotFoundException(eventName);
        } else if (!events.existsById(e.getId())) {
            throw new EventNotFoundException(eventName);
        }
        Optional<List<Ticket>> allTicketsInCategory = tickets.findByEventIdAndCategory(e.getId(), category);
        List<Integer> allSeatNumbers = new ArrayList<>();
        if (allTicketsInCategory.isPresent()) {
            List<Ticket> ticketList = allTicketsInCategory.get();
            for (Ticket t : ticketList) {
                allSeatNumbers.add(t.getSeatNum());
            }
            return new ResponseEntity<List<Integer>>(allSeatNumbers, HttpStatus.OK);
        } else {
            throw new TicketNotFoundException(eventName);
        }
    }

    /*
     * get ticket by event Name, Date, Category and seat number
     * returns a specific Ticket
     */
    @GetMapping("/events/getEventByNameDate/{eventName}/{eventDate}/ticketByCategory/{category}/allSeats/{seatNum}")
    public ResponseEntity<?> getTicketByEventNameAndCategory(@PathVariable(value = "eventName") String eventName, @PathVariable(value = "eventDate") String eventDate,
                                                             @PathVariable(value = "category") int category, @PathVariable(value = "seatNum") int seatNum) {

        Event e = events.findByNameAndDate(eventName, eventDate); //find event by name & date

        if (e == null) {
            throw new EventNotFoundException(eventName);
        } else if (!events.existsById(e.getId())) {
            throw new EventNotFoundException(eventName);
        }

        Ticket output = tickets.findByEventIdAndSeatNumAndCategory(e.getId(), seatNum, category);
        if (output == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No such ticket found");
        }
        return new ResponseEntity<Ticket>(output, HttpStatus.OK);
    }

    @PutMapping("/events/getEventByNameDate/{eventName}/{eventDate}/ticketByCategory/{category}/allSeats/{seatNum}/cancel")
    public ResponseEntity<?> cancelTicket(@PathVariable(value = "eventName") String eventName, @PathVariable(value = "eventDate") String eventDate,
                                          @PathVariable(value = "category") int category, @PathVariable(value = "seatNum") int seatNum) {

        Event e = events.findByNameAndDate(eventName, eventDate); //find event by name & date
        if (!events.existsById(e.getId())) {
            throw new EventNotFoundException(e.getId());
        }
        Ticket output = tickets.findByEventIdAndSeatNumAndCategory(e.getId(), seatNum, category);
        output.setSold(false);
        tickets.save(output);
        return new ResponseEntity<Ticket>(output, HttpStatus.OK);
    }

    // @DeleteMapping("/events/{eventId}/tickets/{ticketId}")
    // public ResponseEntity<?> deleteTicket(@PathVariable (value = "eventId") Long
    // eventId,
    // @PathVariable (value = "ticketId") Long ticketId) {

    // if(!events.existsById(eventId)) {
    // throw new EventNotFoundException(eventId);
    // }

    // return tickets.findByIdAndEventId(ticketId, eventId).map(ticket -> {
    // tickets.delete(ticket);
    // return ResponseEntity.ok().build();
    // }).orElseThrow(() -> new TicketNotFoundException(ticketId));
    // }

    //testing out
    /*
     * Buying page where user is able to purchase ticket and purchase information will be generated
     */

    @PostMapping("/events/{eventName}/{eventDate}/{category}/{seatNum}/{userId}")
    public ResponseEntity<?> purchaseTicket(@PathVariable(value = "eventName") String eventName, @PathVariable(value = "eventDate") String eventDate,
                                            @PathVariable(value = "category") int category, @PathVariable(value = "seatNum") int seatNum,
                                            @PathVariable(value = "userId") String userId) {

        Event e = events.findByNameAndDate(eventName, eventDate); //find event by name & date
        if (!events.existsById(e.getId())) {
            throw new EventNotFoundException(e.getId());
        }
        Ticket output = tickets.findByEventIdAndSeatNumAndCategory(e.getId(), seatNum, category);
        if (output.isSold()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ticket Sold");
        }
        output.setSold(true);
        tickets.save(output);

        User user = userRepo.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid User");
        }

        Purchase purchase = new Purchase();
        purchase.setUserId(userId);
        purchase.setPurchaseDate(LocalDateTime.now());
        purchase.setTicketId(output.getId());
        purchase.setTotalPrice(output.getPrice());

        //save new purchase enitity created
        purchaseServices.savePurchase(purchase);

        //remove user from buySet and set the boolean InBuySet to false
        buyService.removeUser(eventName, userId);
        user.setInBuySet(false);
        userRepo.save(user);

        //notify current user that he is done
        if (buyService.isFull(eventName)) {
            return new ResponseEntity<Purchase>(purchase, HttpStatus.OK);
        } else {
            //find next user and notify them
            String nextUserId = waitService.getNextUserId(eventName);
            if (!nextUserId.equals("")) {
                messageServices.notifyUserEnteringBuySet(nextUserId);
            }
            return new ResponseEntity<Purchase>(purchase, HttpStatus.OK);
        }
    }

    @PutMapping("/home/{eventName}/{userId}/deleteAndNotify")
    public ResponseEntity<?> sendUsertoHomePage(@PathVariable(value = "userId") String userId, @PathVariable(value = "eventName") String eventName){
        User user = userRepo.findById(userId).orElse(null);
        if(user == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid User");
        }

        //remove user from buySet and set the boolean InBuySet to false
        buyService.removeUser(eventName, userId);
        user.setInBuySet(false);
        userRepo.save(user);

        if (buyService.isFull(eventName)){
            return new ResponseEntity<Boolean>(true, HttpStatus.OK);
        } else {
            //find next user and notify them
            String nextUserId = waitService.getNextUserId(eventName);
            if (!nextUserId.equals("")) {
                messageServices.notifyUserEnteringBuySet(nextUserId);
            }

            return new ResponseEntity<Boolean>(true, HttpStatus.OK);
        }
    }
}