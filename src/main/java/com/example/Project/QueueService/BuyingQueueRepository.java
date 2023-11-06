package com.example.Project.QueueService;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuyingQueueRepository extends MongoRepository<BuyingQueueEntity, String> {
    
}
