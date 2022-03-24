package com.lilly.lubenova.vptsrest.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.firestore.annotation.PropertyName;
import org.springframework.cloud.gcp.data.firestore.Document;

import java.util.Date;

@Document(collectionName = "gps_data")
public class GPSRecord {

    @DocumentId
    private String id;

    private String deviceId;

    private GeoPoint location;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date recordTimestamp = new Date();

    public GPSRecord() {
    }

    public GPSRecord(String deviceId, GeoPoint location) {
        this.deviceId = deviceId;
        this.location = location;
    }

    @PropertyName("_id")
    @JsonProperty("_id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @PropertyName("device_id")
    @JsonProperty("device_id")
    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @PropertyName("location")
    @JsonProperty("location")
    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }

    @PropertyName("record_timestamp")
    @JsonProperty("record_timestamp")
    public Date getRecordTimestamp() {
        return recordTimestamp;
    }

    public void setRecordTimestamp(Date recordTimestamp) {
        this.recordTimestamp = recordTimestamp;
    }
}
