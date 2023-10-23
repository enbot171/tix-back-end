package com.example.Project.QueueService;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.stereotype.Service;

@Service
public class BuyingQueue {
    
    private Queue<String> buyingQueue = new ConcurrentLinkedQueue<>();
    private static final int MAX_SIZE = 10; //SET the maximum size to limit how many people can buy
    
    public boolean isFull(){
        return buyingQueue.size() >= MAX_SIZE;
    }

    public int getAvailableSlots(){
        return MAX_SIZE - buyingQueue.size();
    }

    public void addToBuyingQueue(String userId){
        buyingQueue.add(userId);
    }

    public void removeUser(String userId){
        buyingQueue.remove(userId);
    }

    public int getSize(){
        return buyingQueue.size();
    }

}
