package com.example.Project.QueueService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class QueueAutomationService{
    @Autowired
    private WaitingQueue waitingList; //Service to manage waiting queue

    @Autowired
    private BuyingQueue buyingList; //Service to manage buying queue

    private static final Logger logger = LoggerFactory.getLogger(QueueAutomationService.class);

    @Scheduled(fixedRate = 10000) //Runs every minute (adjustable depending on how fast our app runs)
    public void automaticQueueProcessing(){
        logger.info("Scheduled task is running :)");
        if(!buyingList.isFull()){
            int availableSlots = buyingList.getAvailableSlots();
            
            for(int i = 0; i < availableSlots; i++){
                String userId = waitingList.getNextUserId();
                if(userId != null){
                    buyingList.addToBuyingQueue(userId);
                    System.out.println("Added into buying queue");
                }else{
                    System.out.println("No more users!!");
                    break; //stops when the waiting list is empty
                }
            }
        }
        
    }
}