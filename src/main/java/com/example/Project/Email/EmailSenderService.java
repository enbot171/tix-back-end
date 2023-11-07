package com.example.Project.Email;

import com.example.Project.User.User;

import java.io.UnsupportedEncodingException;
import jakarta.mail.MessagingException;

public interface EmailSenderService {
    void sendVerificationEmail(User user) throws UnsupportedEncodingException, MessagingException;
    void sendPurchaseEmail(User user, String purchaseId) throws UnsupportedEncodingException, MessagingException;
}
