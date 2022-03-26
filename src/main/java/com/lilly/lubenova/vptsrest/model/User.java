package com.lilly.lubenova.vptsrest.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.firestore.annotation.PropertyName;
import org.springframework.cloud.gcp.data.firestore.Document;

import java.util.Date;

@Document(collectionName = "users")
public class User {

    @DocumentId
    private String userId;

    private String fullName = null;

    private String phoneNumber = null;

    private String location = null;

    private Date dateRegistered = new Date();

    public User() {
    }

    public User(String userId, String fullName, String phoneNumber, String location) {
        this.userId = userId;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.location = location;
    }

    public User(String userId, String fullName, String phoneNumber) {
        this.userId = userId;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
    }

    @PropertyName("_id")
    @JsonProperty("_id")
    public String getUserId() {
        return userId;
    }

    @PropertyName("full_name")
    @JsonProperty("full_name")
    public String getFullName() {
        return fullName;
    }

    @PropertyName("full_name")
    @JsonProperty("full_name")
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @PropertyName("phone_number")
    @JsonProperty("phone_number")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @PropertyName("phone_number")
    @JsonProperty("phone_number")
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @PropertyName("location")
    @JsonProperty("location")
    public String getLocation() {
        return location;
    }

    @PropertyName("location")
    @JsonProperty("location")
    public void setLocation(String location) {
        this.location = location;
    }

    @PropertyName("date_registered")
    @JsonProperty("date_registered")
    public Date getDateRegistered() {
        return dateRegistered;
    }

    @PropertyName("date_registered")
    @JsonProperty("date_registered")
    public void setDateRegistered(Date dateRegistered) {
        this.dateRegistered = dateRegistered;
    }
}
