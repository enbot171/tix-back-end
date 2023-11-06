package com.example.Project.Event;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.*;
import com.example.Project.Ticket.Ticket;

@Document(collection = "event")
public class Event {
    @Id
    private ObjectId id;
    private String name;
    private String date; //create 1 document for each date

    private List<Ticket> ticketIds;

    private int categories[]; // i.e. [1,2,3]
    private int num_ticket_per_category[]; // i.e. [100,100,100]
    private float price[];


    //getter

    public ObjectId getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getDate() {
        return date;
    }
    public List<Ticket> getTicketIds() {
        return ticketIds;
    }
    public int[] getCategories() {
        return categories;
    }
    public int[] getNum_ticket_per_category() {
        return num_ticket_per_category;
    }
    public float[] getPrice() {
        return price;
    }

    //setters
    public Event(){}
    public void setId(ObjectId id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setTicketIds(List<Ticket> ticketIds) {
        this.ticketIds = ticketIds;
    }
    public void setCategories(int[] categories) {
        this.categories = categories;
    }
    public void setNum_ticket_per_category(int[] num_ticket_per_category) {
        this.num_ticket_per_category = num_ticket_per_category;
    }
    public void setPrice(float[] price) {
        this.price = price;
    }

    public Event(String name, String date, int[] categories, int[] num_ticket_per_category, float[] price) {
        this.name = name;
        this.date = date;
        this.categories = categories;
        this.num_ticket_per_category = num_ticket_per_category;
        this.price = price;
        this.ticketIds = new ArrayList<Ticket>(); 
    }
}