package com.example.Project.QueueService;

import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@Service
public class BuyingQueue {
    
    private final ConcurrentHashMap<ObjectId, Queue<String>> buyingQueue = new ConcurrentHashMap<>();
    private static final int MAX_SIZE = 10; //SET the maximum size to limit how many people can buy
    

    public boolean isFull(ObjectId eventId){
        Queue<String> eventQueue = buyingQueue.get(eventId);
        return eventQueue != null && eventQueue.size() >= MAX_SIZE;
    }

    //returns max size so we do not have to deal with null values for buying queue otherwise
    //it requires us to create an additional queueNotFoundException
    public int getAvailableSlots(ObjectId eventId){
        Queue<String> eventQueue = buyingQueue.get(eventId);
        return eventQueue == null ? MAX_SIZE : MAX_SIZE - buyingQueue.size(); 
    }

    public void addToBuyingQueue(ObjectId eventId, String userId){
        if(userId != null){
            buyingQueue.computeIfAbsent(eventId, q -> new ConcurrentLinkedQueue<>())
            .add(userId);
        }
        
    }

    public void removeUser(ObjectId eventId, String userId){
        Queue<String> eventQueue = buyingQueue.get(eventId);
        if(eventQueue != null){
            eventQueue.remove(userId);
        }
    }

    public int getSize(Object eventId){
        Queue<String> eventQueue = buyingQueue.get(eventId);
        return eventQueue == null ? 0 : buyingQueue.size();
    }

}
