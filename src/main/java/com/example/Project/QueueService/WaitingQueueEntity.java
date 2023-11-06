package com.example.Project.QueueService;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "waitingQueue")
public class WaitingQueueEntity {
    @Id
    private String eventName;
    private List<String> userIds;
    private int queueNum;

    //getters
    public String getEventName() {
        return eventName;
    }
    public List<String> getUserIds() {
        return userIds;
    }
    public int getQueueNum() {
        return queueNum;
    }

    //setters
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
    public void setUserId(List<String> userIds) {
        this.userIds = userIds;
    }
    public void setQueueNum(int queueNum) {
        this.queueNum = queueNum;
    }

    //constructor
    public WaitingQueueEntity(){}
    public WaitingQueueEntity(String eventName, List<String> userIds, int queueNum) {
        this.eventName = eventName;
        this.userIds = userIds;
        this.queueNum = queueNum;
    }
    
    

    
}
