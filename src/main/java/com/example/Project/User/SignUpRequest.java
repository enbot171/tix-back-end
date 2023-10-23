package com.example.Project.User;

// Sign Up input from user

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SignUpRequest {
    @NotBlank
    @Size(max = 20)
    private String fullname;

    @Email
    @NotBlank @Size(max = 50)
    private String email;

    @NotBlank @Size (max = 20)
    private String mobile;

    @NotBlank @Size(min = 6, max = 30)
    private String password;

    public SignUpRequest(String fullname, String email, String mobile, String password) {
        this.fullname = fullname;
        this.email = email;
        this.mobile = mobile;
        this.password = password;
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
}
