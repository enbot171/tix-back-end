// package com.example.Project.Queue;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.test.mock.mockito.MockBean;

// import com.example.Project.QueueService.BuyingQueue;
// import com.example.Project.QueueService.QueueAutomationService;
// import com.example.Project.QueueService.WaitingQueue;

// import static org.mockito.Mockito.when;

// import org.junit.jupiter.api.Test;

// @SpringBootTest
// public class QueueAutomationServiceTest {

//     @Autowired
//     private QueueAutomationService queueAutomationService;

//     @MockBean
//     private WaitingQueue waitingList;

//     @MockBean
//     private BuyingQueue buyingList;

//     @Test
//     public void testAutomateQueueProcessing_AddsUsersToBuyingQueue() {
//         // Arrange: Set up the test scenario
//         when(buyingList.isFull()).thenReturn(false);
//         when(buyingList.getAvailableSlots()).thenReturn(3);

//         when(waitingList.getNextUserId())
//             .thenReturn("user1")
//             .thenReturn("user2")
//             .thenReturn("user3")
//             .thenReturn(null); // Simulate that the waiting list becomes empty

//         // Act: Trigger the automated queue processing
//         queueAutomationService.automaticQueueProcessing();

//         // Assert: Verify that the expected behavior occurred
//         // Add assertions based on your specific test requirements
//     }

//     @Test
//     public void testAutomateQueueProcessing_DoesNotAddUsersWhenBuyingQueueIsFull() {
//         // Arrange: Set up the test scenario
//         when(buyingList.isFull()).thenReturn(true);

//         // Act: Trigger the automated queue processing
//         queueAutomationService.automaticQueueProcessing();

//         // Assert: Verify that no users are added to the buying queue when it's full
//         // Add assertions based on your specific test requirements
//     }
// }


