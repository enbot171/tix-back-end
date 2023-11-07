package com.example.Project.QueueService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BuyingQueueService {
    
    @Autowired
    private BuyingQueueRepository buyQueueRepo;
    
    private static final int MAX_SIZE = 1; //SET the maximum size to limit how many people can buy
    
    // @CachePut("buySetCache")
    public Set<String> findSet(String eventName){
        BuyingQueueEntity entity = buyQueueRepo.findById(eventName).orElse(null);
        if(entity == null){
            entity = new BuyingQueueEntity(eventName, new HashSet<>());
        }
        buyQueueRepo.save(entity);
        return entity.getBuySet();
    }
    public boolean isFull(String eventName){
        Set<String> buySet = this.findSet(eventName);
        return buySet != null && buySet.size() >= MAX_SIZE;
    }

    //returns max size so we do not have to deal with null values for buying queue otherwise
    //it requires us to create an additional queueNotFoundException
    public int getAvailableSlots(String eventName){
        Set<String> buySet = this.findSet(eventName);
        return buySet == null ? MAX_SIZE : MAX_SIZE - buySet.size(); 
    }

    //cache is updated whenever the method is called
    // @CachePut("buySetCache")
    public void addToBuyingQueue(String eventName, String userId){
        BuyingQueueEntity buySetEntity = buyQueueRepo.findById(eventName).get();
        //check if user exist first
        if(userId == null){
            return;
        }
        
        Set<String> buySet;
        //create new buy set entity if it doesn't exist 
        if(buySetEntity == null){
            buySet = new HashSet<>();
            buySet.add(userId);
            buySetEntity = new BuyingQueueEntity(eventName, buySet); 
            buySetEntity.setSize(1);
        }else{
            buySet = buySetEntity.getBuySet();
            buySet.add(userId);
            buySetEntity.setSize(buySet.size());
        }
        
        buyQueueRepo.save(buySetEntity);
        
    }

    //remove user from the set
    public void removeUser(String eventName, String userId){
        BuyingQueueEntity buySetEntity = buyQueueRepo.findById(eventName).get();
        if(userId == null || buySetEntity == null){
            return;
        }
        Set<String> buySet = buySetEntity.getBuySet();
        if(buySet != null){
            buySet.remove(userId);
        }
        buySetEntity.setSize(buySet.size());
        buyQueueRepo.save(buySetEntity);
    }

    //get the size of the set
    public int getSize(String eventName){
        Set<String> buySet = this.findSet(eventName);
        return buySet == null ? 0 : buySet.size();
    }

    public BuyingQueueEntity findById(String eventName) {
        return buyQueueRepo.findById(eventName).orElse(null);
    }

    public List<BuyingQueueEntity> allEventSets() {
        return buyQueueRepo.findAll();
    }

}
