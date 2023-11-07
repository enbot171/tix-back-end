// package com.example.Project.StripePayment;

// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;

// @RestController
// public class PaymentController {
    
//     @Value("$(STRIPE_PUBLIC_KEY)")
//     private String stripePublicKey;


//     @GetMapping("/checkout")
//     public String checkout(Model model, @RequestParam(required = true ) String userId, @RequestParam(required = true) Integer amount){
//         model.addAttribute("amount", amount*100);
//         model.addAttribute("stripePublicKey", stripePublicKey);
//         model.addAttribute("Currency", "USD");
//         model.addAttribute("userId", userId);

//         return "checkout";
//     }
// }
