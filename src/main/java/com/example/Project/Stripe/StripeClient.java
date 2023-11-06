//package com.example.Project.StripePayment;
//
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.Map;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import com.stripe.Stripe;
//import com.stripe.model.Charge;
//import com.stripe.model.Customer;
//
//@Component
//public class StripeClient {
//
//    private static final Logger logger = LoggerFactory.getLogger(StripeClient.class);
//
//    @Autowired
//    StripeClient(@Value("${STRIPE_SECRET_KEY}") String stripeSecretKey) {
//        Stripe.apiKey = stripeSecretKey;
//        logger.info("StripeClient initialized with API key: " + stripeSecretKey);
//    }
//
//    public Customer createCustomer(String token, String email) throws Exception {
//        Map<String, Object> customerParams = new HashMap<String, Object>();
//        customerParams.put("email", email);
//        customerParams.put("source", token);
//        return Customer.create(customerParams);
//    }
//
//    private Customer getCustomer(String id) throws Exception {
//        return Customer.retrieve(id);
//    }
//
//    public Charge chargeNewCard(String token, double amount) throws Exception {
//        Map<String, Object> chargeParams = new HashMap<String, Object>();
//        chargeParams.put("amount", (int)(amount * 100));
//        chargeParams.put("currency", "USD");
//        chargeParams.put("source", Collections.singletonMap("token", token));
//        Charge charge = Charge.create(chargeParams);
//        return charge;
//    }
//
//    public Charge chargeCustomerCard(String customerId, int amount) throws Exception {
//        String sourceCard = getCustomer(customerId).getDefaultSource();
//        Map<String, Object> chargeParams = new HashMap<String, Object>();
//        chargeParams.put("amount", amount);
//        chargeParams.put("currency", "USD");
//        chargeParams.put("customer", customerId);
//        chargeParams.put("source", sourceCard);
//        Charge charge = Charge.create(chargeParams);
//        return charge;
//    }
//}


package com.example.Project.Stripe;

import com.stripe.Stripe;
        import com.stripe.model.Charge;
        import com.stripe.model.Customer;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Component;

        import java.util.HashMap;
        import java.util.Map;

@Component
public class StripeClient {

    @Autowired
    StripeClient() {
        Stripe.apiKey = "sk_test_51O8PA9HVyHFmFtSnwQ9yLPpG7FJ3Oe5QSKhVE4bksS9W3NpgVcyZ2yEOHUPVzc918tnPhhoVIT8NWiGvxjnOsFzz00pO3xKO4Z";
    }

    public Customer createCustomer(String token, String email) throws Exception {
        Map<String, Object> customerParams = new HashMap<String, Object>();
        customerParams.put("email", email);
        customerParams.put("source", token);
        return Customer.create(customerParams);
    }

    private Customer getCustomer(String id) throws Exception {
        return Customer.retrieve(id);
    }

    public Charge chargeNewCard(String token, double amount) throws Exception {
        Map<String, Object> chargeParams = new HashMap<String, Object>();
        chargeParams.put("amount", (int)(amount * 100));
        chargeParams.put("currency", "SGD");
        chargeParams.put("source", token);
        Charge charge = Charge.create(chargeParams);

        return charge;
    }

    public Charge chargeCustomerCard(String customerId, int amount) throws Exception {
        String sourceCard = getCustomer(customerId).getDefaultSource();
        Map<String, Object> chargeParams = new HashMap<String, Object>();
        chargeParams.put("amount", amount);
        chargeParams.put("currency", "SGD");
        chargeParams.put("customer", customerId);
        chargeParams.put("source", sourceCard);
        Charge charge = Charge.create(chargeParams);
        return charge;
    }
}
