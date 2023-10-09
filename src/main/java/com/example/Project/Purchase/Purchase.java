package com.example.Project.Purchase;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "purchase")
public class Purchase {
    @Id
    private String id;
    private String userId;
    private String ticketId;
    private LocalDateTime purchaseDate;
    private float totalPrice;

    //getter and setter
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getTicketId() {
        return ticketId;
    }
    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }
    public LocalDateTime getPurchaseDate() {
        return purchaseDate;
    }
    public void setPurchaseDate(LocalDateTime purchaseDate) {
        this.purchaseDate = purchaseDate;
    }
    public double getTotalPrice() {
        return totalPrice;
    }
    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    //constructor
    public Purchase(){}

    public Purchase(String id, String userId, String ticketId, LocalDateTime purchaseDate,
                    float totalPrice) {
        this.id = id;
        this.userId = userId;
        this.ticketId = ticketId;
        this.purchaseDate = purchaseDate;
        this.totalPrice = totalPrice;
    }


}
