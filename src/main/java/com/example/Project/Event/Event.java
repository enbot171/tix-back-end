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

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Ticket> getTicketIds() {
        return ticketIds;
    }

    public void setTicketIds(List<Ticket> ticketIds) {
        this.ticketIds = ticketIds;
    }

    public int[] getCategories() {
        return categories;
    }

    public void setCategories(int[] categories) {
        this.categories = categories;
    }

    public int[] getNum_ticket_per_category() {
        return num_ticket_per_category;
    }

    public void setNum_ticket_per_category(int[] num_ticket_per_category) {
        this.num_ticket_per_category = num_ticket_per_category;
    }

    public float[] getPrice() {
        return price;
    }

    public void setPrice(float[] price) {
        this.price = price;
    }
}