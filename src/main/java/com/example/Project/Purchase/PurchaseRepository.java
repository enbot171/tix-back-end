package com.example.Project.Purchase;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseRepository extends MongoRepository<Purchase, String>{
    Purchase findByTicketId(String ticketId);
}
