package com.example.Project.Ticket;

import org.bson.types.ObjectId;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.example.Project.Event.Event;

import lombok.*;

@ToString
@EqualsAndHashCode
@Document(collection = "ticket")
public class Ticket {
    @Id
    private String id;
    private int seatNum; // 1 - 400
    private boolean sold = false;
    private int category;
    private float price;

    //setters
    public void setId(String id) {
        this.id = id;
    }
    public void setSeatNum(int seatNum) {
        this.seatNum = seatNum;
    }
    public void setSold(boolean sold) {
        this.sold = sold;
    }
    public void setCategory(int category) {
        this.category = category;
    }
    public void setPrice(float price) {
        this.price = price;
    }
    public void setEventId(ObjectId eventId) {
        this.eventId = eventId;
    }

    //Getters
    public String getId() {
        return id;
    }
    public int getSeatNum() {
        return seatNum;
    }
    public boolean isSold() {
        return sold;
    }
    public int getCategory() {
        return category;
    }
    public float getPrice() {
        return price;
    }
    public ObjectId getEventId() {
        return eventId;
    }


    // @DocumentReference(lookup = "{ 'name' : ?#{Taylor Swift} }")
    private ObjectId eventId;

    public Ticket(){}
    public Ticket(int seatNum, boolean sold, int category){
        this.category = category;
        this.seatNum = seatNum;
        this.sold = sold;
    }
}
