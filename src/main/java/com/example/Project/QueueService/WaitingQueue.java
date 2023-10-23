package com.example.Project.QueueService;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.stereotype.Service;

@Service
public class WaitingQueue{

    private Queue<String> waitingRoom = new ConcurrentLinkedQueue<>();

    //functions
    public void addUsers(String userId){
        waitingRoom.add(userId);
    }

    public String getNextUserId(){
        return waitingRoom.poll();
    }

    public boolean isEmpty(){
        return waitingRoom.isEmpty();
    }

    public boolean contains(String userId) {

        for(String Ids : waitingRoom){

            if(Ids.equals(userId)){
                return true;
            }
        }
        return false;
    }

    public void removeUser(String userId){
        waitingRoom.remove(userId);
    }
}
