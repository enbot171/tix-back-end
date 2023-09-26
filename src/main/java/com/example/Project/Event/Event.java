package com.example.Project.Event;

import lombok.Data;
import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import java.util.*;
import com.example.Project.Ticket.Ticket;

@Document(collection = "event")
@Getter
@Setter
@Data
// @AllArgsConstructor
// @NoArgsConstructor
public class Event {
    @Id
    private ObjectId id;

    @Getter 
    @Setter
    private String name;

    @Getter 
    @Setter
    private String date; //create 1 document for each date

    private List<Ticket> ticketIds;

    private int categories[]; // i.e. [1,2,3]
    private int num_ticket_per_category[]; // i.e. [100,100,100]
    private float price[];

    public Event(){}
    public Event(String name, String date, int[] categories, int[] num_ticket_per_category, float[] price) {
        this.name = name;
        this.date = date;
        this.categories = categories;
        this.num_ticket_per_category = num_ticket_per_category;
        this.price = price;
        this.ticketIds = new ArrayList<Ticket>(); // hello
    }
}