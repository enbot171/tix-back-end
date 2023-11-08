package com.example.Project.Email;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.example.Project.User.User;

import java.io.UnsupportedEncodingException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailSenderServiceImpl implements EmailSenderService{

    private final JavaMailSender mailSender;

    private String from = "tix.cs203@gmail.com";
    private String senderName = "TIX Team";
    private String subject = "";

    public EmailSenderServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    } 

    // send email with link to verify registered email
    public void sendVerificationEmail(User user) throws UnsupportedEncodingException, MessagingException {

        MimeMessage message = mailSender.createMimeMessage();

        String verificationCode = user.getId();
        user.setVerificationCode(verificationCode);

        String verifyURL = "https://cs203back.azurewebsites.net/api/v1/auth/verify?code=" + verificationCode; // not sure if need change after deployment
        subject = "Verify your Email";

        String body = "<p>Dear " + user.getFullname() + ",</p>";
        body += "<p>Welcome to TIX! </p>";
        body += "<p>Please click on the link below to verify your registration:</p>";
        body += "<h3><a href=\"" + verifyURL + "\">Click HERE to verify</a></h3>";
        body += "<p>See you soon!</p>";
        
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(from, senderName);
        helper.setTo(user.getEmail());
        helper.setSubject(subject);
        helper.setText(body, true);

        this.mailSender.send(message);
    }

    // send email with link containing pdf of the purchased ticket, sends 1 ticket in 1 email
    public void sendPurchaseEmail(User user, String purchaseID) throws UnsupportedEncodingException, MessagingException {

        MimeMessage message = mailSender.createMimeMessage();

        String downloadURL = "https://cs203back.azurewebsites.net/api/v1/purchases/" + purchaseID + "/pdf"; 
        subject = "Thank You for your Purchase";

        String body = "<p>Dear " + user.getFullname() + ",</p>";
        body += "<p>Thank you for your purchase! Here is your ticket:</p>";
        body += "<h3><a href=\'" + downloadURL + "\'>Click HERE to download</a></h3>";

        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(from, senderName);
        helper.setTo(user.getEmail());
        helper.setSubject(subject);
        helper.setText(body, true);

        this.mailSender.send(message);
    }
}
