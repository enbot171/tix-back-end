package com.example.Project.Purchase;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.Project.Event.Event;
import com.example.Project.Event.EventRepository;
import com.example.Project.Ticket.Ticket;
import com.example.Project.Ticket.TicketRepository;
import com.example.Project.User.User;
import com.example.Project.User.UserRepository;
import com.google.zxing.WriterException;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("api/v1/")
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseServices;
    @Autowired
    private EventRepository events;
    @Autowired
    private TicketRepository tickets;
    @Autowired
    private UserRepository userRepo;

    public PurchaseController(PurchaseService purchaseServices) {
        this.purchaseServices = purchaseServices;
    }

    //all purchases
    @GetMapping("/getPurchases")
    public ResponseEntity<List<Purchase>> getAllPurchases(){
        return new ResponseEntity<List<Purchase>>(purchaseServices.findAll(), HttpStatus.OK);
    }
  
    //find purchase by ticket id
    @GetMapping("/{ticketId}/getPurchase")
    public ResponseEntity<?> getSinglePurchaseByTicketId(@PathVariable (value = "ticketId") String ticketId){

        Ticket ticket = tickets.findById(ticketId).get();
        if (ticket == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ticket ID: " + ticketId + " does not exist.");
        }
        Purchase purchase = purchaseServices.findByTicketId(ticketId);
        if (purchase == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No purchases found in " + ticketId);
        }
        Event event = events.findById(ticket.getEventId()).get();
        if (event == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No purchases found in " + ticket.getEventId());
        }

        User user = userRepo.findById(purchase.getUserId()).get();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No purchases found in " + purchase.getUserId());
        }

        PurchaseInfoResponse purchaseInfo = new PurchaseInfoResponse(purchase.getId(), user.getId(), user.getFullname(),
                user.getEmail(), event.getName(), event.getDate(), ticket.getId(), ticket.getPrice(),
                ticket.getCategory(), ticket.getSeatNum());

        return new ResponseEntity<PurchaseInfoResponse>(purchaseInfo, HttpStatus.OK);
    }

    //find purchase by purchase id
    @GetMapping("{purchaseId}/getPurchase")
    public ResponseEntity<?> getSinglePurchase(@PathVariable (value = "purchaseId") String purchaseId){
        Purchase purchase = purchaseServices.findSinglePurchase(purchaseId);
        if (purchase == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No purchases found in " + purchaseId);
        }
        Ticket ticket = tickets.findById(purchase.getTicketId()).get();
        if (ticket == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No purchases found in " + purchase.getTicketId());
        }
        Event event = events.findById(ticket.getEventId()).get();
        if (event == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No purchases found in " + ticket.getEventId());
        }

        User user = userRepo.findById(purchase.getUserId()).get();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No purchases found in " + purchase.getUserId());
        }

        PurchaseInfoResponse purchaseInfo = new PurchaseInfoResponse(purchase.getId(), user.getId(), user.getFullname(),
                user.getEmail(), event.getName(), event.getDate(), ticket.getId(), ticket.getPrice(),
                ticket.getCategory(), ticket.getSeatNum());

        return new ResponseEntity<PurchaseInfoResponse>(purchaseInfo, HttpStatus.OK);
    }

    //find list of purchases by user ID
    @GetMapping("/{userId}/getUserPurchases")
    public ResponseEntity<?> userPurchase(@PathVariable (value = "userId") String userId){
        Optional<User> userOpt = userRepo.findById(userId);
        if (!userOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User ID does not exist" + userId);
        }
        List<Purchase> purchaseList = purchaseServices.findByUserId(userId);
        if (purchaseList == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No purchases for for " + userId);
        }
        return new ResponseEntity<List<Purchase>>(purchaseList, HttpStatus.OK);
    }

    //find specific ticket bought by user id, event name and date
    @GetMapping("/{userId}/{eventName}/{eventDate}/getUserTicketPurchases")
    public ResponseEntity<?> userTicketPurchase(@PathVariable (value = "userId") String userId, @PathVariable (value = "eventName") String eventName,
                                                @PathVariable (value = "eventDate") String eventDate){
        Optional<User> userOpt = userRepo.findById(userId);
        if (!userOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User ID does not exist" + userId);
        }
        Event event = events.findByNameAndDate(eventName, eventDate);
        if (event == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Event does not exist" + eventName + ", " + eventDate);
        }
        Purchase purchase = purchaseServices.findByEventNameDateAndUserId(event, userId);
        return new ResponseEntity<Purchase>(purchase, HttpStatus.OK);

    }

    //delete purchases
    @DeleteMapping("/{purchaseId}/deletePurchase")
    public ResponseEntity<?> deletePurchase(@PathVariable (value = "purchaseId") String purchaseId){
        if(!purchaseServices.existsById(purchaseId)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No purchases found in " + purchaseId);
        }
        Purchase toDelete = purchaseServices.findSinglePurchase(purchaseId);
        Optional<Ticket> ticketOptional = tickets.findById(toDelete.getTicketId());
        if (ticketOptional.isPresent()) {
            Ticket ticket = ticketOptional.get();
            ticket.setSold(false);
            tickets.save(ticket);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ticket not found for purchase " + purchaseId);
        }

        purchaseServices.deletePurchase(toDelete);
        return ResponseEntity.status(HttpStatus.OK).body("Purchase " + purchaseId + " deleted");
    }
    @GetMapping("purchases/{purchaseID}/pdf")
    public void generatePDF(HttpServletResponse response, @PathVariable(value = "purchaseID") String purchaseID)
            throws IOException, WriterException {
        response.setContentType("application/pdf");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=ticket.pdf";
        response.setHeader(headerKey, headerValue);

        this.purchaseServices.export(response, purchaseID);
    }
}
