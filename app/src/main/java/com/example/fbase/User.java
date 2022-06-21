package com.example.fbase;

import java.util.HashMap;

public class User {
    public String fullName;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String email;
    public String uid;



    public User(){
    }

    public User(String fullName, String email, String uid) {
        this.fullName = fullName.toLowerCase();
        this.email = email;
        this.uid= uid;


    }

}
