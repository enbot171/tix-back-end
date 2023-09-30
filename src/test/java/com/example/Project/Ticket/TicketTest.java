package com.example.Project.Ticket;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.Project.Event.Event;
import com.example.Project.Event.EventRepository;

@ExtendWith(MockitoExtension.class)
public class TicketTest {
    @Mock
    private EventRepository eventRepository;

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private TicketController ticketController;

    @AfterEach
	void tearDown(){
		// clear the database after each test
		ticketRepository.deleteAll();
		eventRepository.deleteAll();
	}

    @Test
    public void testGetAllTicketsByEventId() {
        // Arrange
        int[] category = {1,2,3,4};
        int[] seats = {100,100,100,100};
        float[] price = {10,20,30,40};
        Event mockEvent = new Event("Taylor Swift", "2nd Sept",category, seats,price);
        ObjectId eventId = mockEvent.getId();
        List<Ticket> mockTickets = Arrays.asList(
                new Ticket(1,false,1),
                new Ticket(2,false,1)
        );

        when(eventRepository.existsById(eventId)).thenReturn(true);
        when(ticketRepository.findByEventId(eventId)).thenReturn(mockTickets);

        // Act
        List<Ticket> tickets = ticketController.getAllTicketsByEventId(eventId);

        // Assert
        verify(eventRepository).existsById(eventId);
        verify(ticketRepository).findByEventId(eventId);
        assertNotNull(tickets);
        assertEquals(2,tickets.size());
        assertEquals(1,tickets.get(0).getSeatNum());
        assertEquals(2,tickets.get(1).getSeatNum());
    }
}
