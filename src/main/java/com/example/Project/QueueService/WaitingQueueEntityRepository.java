package com.example.Project.QueueService;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface WaitingQueueEntityRepository extends MongoRepository<WaitingQueueEntity, String>{

    WaitingQueueEntity findByEventNameAndUserId(String eventName, String userId);
    
}
