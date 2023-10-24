package com.example.Project.QueueService;

import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@Service
public class WaitingQueue{

    private final ConcurrentHashMap<ObjectId, Queue<String>> waitingRooms = new ConcurrentHashMap<>();

    //functions
    public void addUsers(ObjectId eventId, String userId){
        waitingRooms.computeIfAbsent(eventId, k -> new ConcurrentLinkedQueue<>())
        .add(userId);
    }

    public String getNextUserId(ObjectId eventId){
        Queue<String> eventQueue = waitingRooms.get(eventId);
        if(eventQueue == null){
            return null;
        }
        return eventQueue.poll();
    }

    public boolean isEmpty(ObjectId eventId){
        Queue<String> eventQueue = waitingRooms.get(eventId);
        return eventQueue == null || eventQueue.isEmpty();
    }

    public boolean contains(ObjectId eventId, String userId) {
        Queue<String> eventQueue = waitingRooms.get(eventId);
        return eventQueue != null && eventQueue.contains(userId);
    }

    public void removeUser(ObjectId eventId, String userId){
       Queue<String> eventQueue = waitingRooms.get(eventId);
        if(eventQueue != null){
            eventQueue.remove(userId);
        }
    }

    public int getSize(ObjectId eventId){
        Queue<String> eventQueue = waitingRooms.get(eventId);
        return eventQueue == null ? 0 : eventQueue.size();
    }
}
