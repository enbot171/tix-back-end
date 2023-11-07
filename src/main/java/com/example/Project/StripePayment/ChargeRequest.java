package com.example.Project.StripePayment;

public class ChargeRequest {

    private String token; //Payment token 
    private double amount; //Payment amount
    private String currency; //Payment currency (Optional to include)

    //Getters and Setters
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
    public String getCurrency() {
        return currency;
    }
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    //Constructors
    public ChargeRequest(String token, double amount, String currency) {
        this.token = token;
        this.amount = amount;
        this.currency = currency;
    }
    
    

}
