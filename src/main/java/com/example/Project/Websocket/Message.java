package com.example.Project.Websocket;

public class Message {

    
    private String receiverId;
    private String message;
    private Boolean isInBuySet;

    public String getReceiverId() {
        return receiverId;
    }
    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public Boolean getisInBuySet() {
        return isInBuySet;
    }
    public void setisInBuySet(Boolean isInBuySet) {
        this.isInBuySet = isInBuySet;
    }

    public Message(){}
    public Message(String receiverId, String message, Boolean isInBuySet) {
        this.receiverId = receiverId;
        this.message = message;
        this.isInBuySet = isInBuySet;
    }

}
