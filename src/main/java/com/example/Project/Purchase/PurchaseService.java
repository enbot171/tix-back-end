package com.example.Project.Purchase;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Project.Event.Event;
import com.example.Project.Ticket.Ticket;
import com.example.Project.Ticket.TicketRepository;

@Service
public class PurchaseService {
    @Autowired
    private PurchaseRepository purchaseRepo;
    @Autowired
    private TicketRepository tickets;

    public void savePurchase(Purchase purchase){
        purchaseRepo.save(purchase);
    }

    public List<Purchase> findAll() {
        return purchaseRepo.findAll();
    }
    public Purchase findSinglePurchase(String purchaseId){
        return purchaseRepo.findById(purchaseId).get();
    }

    public boolean existsById(String purchaseId) {
        return purchaseRepo.existsById(purchaseId);
    }

    public void deletePurchase(Purchase purchase) {
        purchaseRepo.delete(purchase);
    }

    public List<Purchase> findByUserId(String userId) {
        List<Purchase> allPurchases = purchaseRepo.findAll();
        List<Purchase> allPurchasesByUser = new ArrayList<>();

        for(Purchase p : allPurchases){
            if(p.getUserId().equals(userId)){
                allPurchasesByUser.add(p);
            }
        }
        return allPurchasesByUser;
    }

    public Purchase findByEventNameDateAndUserId(Event event, String userId) {
        List<Purchase> allPurchasesByUser = findByUserId(userId);
        for(Purchase p : allPurchasesByUser){
            Ticket t = tickets.findById(p.getTicketId()).get();
            if(t.getEventId().equals(event.getId())){
                return p;
            }
        }
        return null;
    }

    public Purchase findByTicketId(String ticketId) {
        return purchaseRepo.findByTicketId(ticketId);
    }

}
