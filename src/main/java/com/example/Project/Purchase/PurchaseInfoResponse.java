package com.example.Project.Purchase;

public class PurchaseInfoResponse {

    private String purchaseId;

    private String userId;

    private String userFullname;

    private String userEmail;

    private String eventName;

    private String eventDate;

    private String ticketId;

    private float ticketPrice;

    private int Category;

    private int seatNum;

    public PurchaseInfoResponse(String purchaseId, String userId, String userFullname, String userEmail, String eventName, String eventDate, String ticketId, float ticketPrice, int category, int seatNum) {
        this.purchaseId = purchaseId;
        this.userId = userId;
        this.userFullname = userFullname;
        this.userEmail = userEmail;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.ticketId = ticketId;
        this.ticketPrice = ticketPrice;
        Category = category;
        this.seatNum = seatNum;
    }

    public String getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(String purchaseId) {
        this.purchaseId = purchaseId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserFullname() {
        return userFullname;
    }

    public void setUserFullname(String userFullname) {
        this.userFullname = userFullname;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public float getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(float ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public int getCategory() {
        return Category;
    }

    public void setCategory(int category) {
        Category = category;
    }

    public int getSeatNum() {
        return seatNum;
    }

    public void setSeatNum(int seatNum) {
        this.seatNum = seatNum;
    }
}
