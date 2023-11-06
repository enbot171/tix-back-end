package com.example.Project.QueueService;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

@Service
public class WaitingQueueService{

    @Autowired
    private WaitingQueueEntityRepository waitQueueRepo;

    //functions
    // @Cacheable("QueueNum")
    public List<WaitingQueueEntity> allEventQueue(){
        return waitQueueRepo.findAll();
        
    }
    public List<String> findAllUsersInEventQueue(String eventName){
        WaitingQueueEntity entity = waitQueueRepo.findById(eventName).orElse(null);
        if(entity == null){
            return null;
        }
        List<String> allUserInQueue = entity.getUserIds();
        return allUserInQueue;
    }

    //finding queue number
    public int findQueueNum(String eventName, String userId){
        WaitingQueueEntity entity = waitQueueRepo.findById(eventName).orElse(null);
        if(entity == null){
            return -1;
        }
        List<String> allUserIds = this.findAllUsersInEventQueue(eventName);
        int counter = 0;
        if(!allUserIds.contains(userId)){return -1;}
        for(String user : allUserIds){
            counter++;
            if(user.equals(userId)){
                return counter;
            }
        }
        return entity.getQueueNum();
    }

    public void addWaitingQueues(String eventName, String userId){
        WaitingQueueEntity entity = waitQueueRepo.findById(eventName).orElse(null);
        List<String> l;
        int queueNum;
        if(entity == null){
            l = new ArrayList<>();
            queueNum = 1;            
            if(!l.contains(userId)){
                l.add(userId);
                WaitingQueueEntity e = new WaitingQueueEntity(eventName, l , queueNum);
                waitQueueRepo.save(e);
            }
        }else{
            l = entity.getUserIds();
            queueNum = entity.getQueueNum();
            if(!l.contains(userId)){
                l.add(userId);
                entity.setQueueNum(queueNum + 1);
                entity.setUserId(l);
                waitQueueRepo.save(entity);
            }
        }
        
    }

    //getting next user in the queue
    @CachePut("QueueNum")
    public String getNextUserId(String eventName){
        WaitingQueueEntity entity = waitQueueRepo.findById(eventName).orElse(null);
        if(entity == null){
            return "";
        }
        if(entity.getUserIds() == null){
            return "";
        }
            
        return entity.getUserIds().get(0);
        
    }
    public WaitingQueueEntity findById(String eventName) {
        return waitQueueRepo.findById(eventName).orElse(null);
    }

    public boolean removeUser(String eventName, String userId){
        WaitingQueueEntity entity = waitQueueRepo.findById(eventName).orElse(null);
        if(entity == null){
            return false;
        }
        List<String> allUsers = findAllUsersInEventQueue(eventName);
        if(allUsers == null){
            return false;
        }
        for(String uId : allUsers ){
            if(uId.equals(userId)){
                allUsers.remove(uId);
            }
        }
        entity.setUserId(allUsers);
        waitQueueRepo.save(entity);
        return true;
    }
}
