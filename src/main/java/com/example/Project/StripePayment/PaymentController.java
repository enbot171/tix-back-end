package com.example.Project.StripePayment;

import com.stripe.model.Charge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
// @CrossOrigin("*")
@RequestMapping("/api/v1/payment")
public class PaymentController {
    
    private StripeClient stripeClient;

    @Autowired
    PaymentController(StripeClient stripeClient) {
        this.stripeClient = stripeClient;
    }
    
    @PostMapping("/charge")
    public ResponseEntity<ChargeResponse> chargeCard(@RequestBody ChargeRequest chargeRequest) {
        try{
            Charge charge = stripeClient.chargeNewCard(chargeRequest.getToken(), chargeRequest.getAmount());

            ChargeResponse response = new ChargeResponse();
            response.setSuccess(true);
            response.setMessage("Payment Successful! Charge ID : " + charge.getId());
            response.setChargeId(charge.getId());

            return ResponseEntity.ok(response);

        } catch(Exception e){

            ChargeResponse response = new ChargeResponse();
            response.setSuccess(false);
            response.setMessage("Payment failed : " + e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}