package com.example.Project.User;

public class UserInfoResponse {
    private String id;
    private String fullname;
    private String email;
    private String mobile;

    public UserInfoResponse(String id, String fullname, String email, String mobile) {
        this.id = id;
        this.fullname = fullname;
        this.email = email;
        this.mobile = mobile;
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
}
