package com.example.Project.QueueService;

import java.beans.EventSetDescriptor;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.Project.Event.Event;
import com.example.Project.Event.EventService;

@Component
public class QueueAutomationService{
    @Autowired
    private WaitingQueue waitingList; //Service to manage waiting queue

    @Autowired
    private BuyingQueue buyingList; //Service to manage buying queue

    @Autowired
    private EventService eventService; //Event service manager

    private static final Logger logger = LoggerFactory.getLogger(QueueAutomationService.class);

    private List<Event> getAllEvents(){
        return eventService.allEvents();
    }
    public void processQueue(Event e){
        if(!buyingList.isFull(e.getId())){
            int availableSlots = buyingList.getAvailableSlots(e.getId());
            
            for(int i = 0; i < availableSlots; i++){
                String userId = waitingList.getNextUserId(e.getId());
                if(userId != null){
                    buyingList.addToBuyingQueue(e.getId(), userId);
                    System.out.println("Added into buying queue");
                }else{
                    System.out.println("No more users!!");
                    break; //stops when the waiting list is empty
                }
            }
        }
        
    }

    @Scheduled(fixedRate = 60000) //Runs every minute (adjustable depending on how fast our app runs)
    public void automaticQueueProcessing(){
        logger.info("Scheduled task is running :)");

        for(Event event : getAllEvents()){
            processQueue(event);
        }
    }


}