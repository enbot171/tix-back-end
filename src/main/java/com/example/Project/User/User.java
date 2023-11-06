package com.example.Project.User;

//User Entity

import lombok.Data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "users")
public class User {
    @Id
    private String id;

    @NotBlank @Size(max = 20)
    private String fullname;

    @Email @NotBlank @Size(max = 50)
    private String email;

    @NotBlank @Size (max = 20)
    private String mobile;

    @NotBlank @Size(min = 8, max = 30)
    private String password;

    @NotBlank 
    private String verificationCode; 

    private boolean isVerified;

    private boolean inBuySet;


    public User(String fullname, String email, String mobile, String password) {
        this.fullname = fullname;
        this.email = email;
        this.mobile = mobile;
        this.password = password;
        this.inBuySet = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public boolean getVerificationStatus() {
        return isVerified;
    }

    public void setVerificationStatus(boolean isVerified) {
        this.isVerified = isVerified;
    }

    public boolean getInBuySet() {
        return inBuySet;
    }
    public void setInBuySet(boolean x) {
        this.inBuySet = x;
    }

}
