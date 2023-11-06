package com.example.Project.QueueService;

import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "buyingQueue")
public class BuyingQueueEntity{
    @Id
    private String eventName;
    private Set<String> buySet;
    private int size;
    
    //getters
    public String getEventName() {
        return eventName;
    }
    public int getSize() {
        return size;
    }
    public Set<String> getBuySet() {
        return buySet;
    }

    //setters
    public void setSize(int size) {
        this.size = size;
    }
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
    public void setUserId(Set<String> buySet) {
        this.buySet = buySet;
    }

    public BuyingQueueEntity(String eventName, Set<String> buySet) {
        this.eventName = eventName;
        this.buySet = buySet;
    }
    public BuyingQueueEntity(){}
    
    
}