package com.lilly.lubenova.vptsrest.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.firestore.annotation.PropertyName;
import org.springframework.cloud.gcp.data.firestore.Document;

import java.util.Date;

@Document(collectionName = "devices")

public class Device {

    @DocumentId
    private String deviceId;

    private Date dateRegistered = new Date();

    private String userId;

    private String certificateNo;

    private String vehicleRegistrationNumber;

    public Device() {
    }

    public Device(String userId, String certificateNo) {
        this.userId = userId;
        this.certificateNo = certificateNo;
    }

    @PropertyName("_id")
    @JsonProperty("_id")
    public String getDeviceId() {
        return deviceId;
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

    @PropertyName("user_id")
    @JsonProperty("user_id")
    public String getUserId() {
        return userId;
    }

    @PropertyName("user_id")
    @JsonProperty("user_id")
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @PropertyName("device_certificate_no")
    @JsonProperty("device_certificate_no")
    public String getCertificateNo() {
        return certificateNo;
    }

    @PropertyName("device_certificate_no")
    @JsonProperty("device_certificate_no")
    public void setCertificateNo(String certificateNo) {
        this.certificateNo = certificateNo;
    }

    @PropertyName("vehicle_registration_number")
    @JsonProperty("vehicle_registration_number")
    public String getVehicleRegistrationNumber() {
        return vehicleRegistrationNumber;
    }

    @PropertyName("vehicle_registration_number")
    @JsonProperty("vehicle_registration_number")
    public void setVehicleRegistrationNumber(String vehicleRegistrationNumber) {
        this.vehicleRegistrationNumber = vehicleRegistrationNumber;
    }
}
