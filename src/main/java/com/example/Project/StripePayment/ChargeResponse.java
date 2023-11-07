package com.example.Project.StripePayment;

public class ChargeResponse{
    private Boolean success; //Indicates whether the payment is successfull or not
    private String message; //A message telling the user the result of payment
    private String chargeId; //the ID of the charge if is successfull
    
    public Boolean getSuccess() {
        return success;
    }
    public void setSuccess(Boolean success) {
        this.success = success;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getChargeId() {
        return chargeId;
    }
    public void setChargeId(String chargeId) {
        this.chargeId = chargeId;
    }

    public ChargeResponse(Boolean success, String message, String chargeId) {
        this.success = success;
        this.message = message;
        this.chargeId = chargeId;
    }
    public ChargeResponse() {
    }

    
}