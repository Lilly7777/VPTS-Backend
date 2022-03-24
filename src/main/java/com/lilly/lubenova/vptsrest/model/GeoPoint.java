package com.lilly.lubenova.vptsrest.model;

public class GeoPoint extends com.google.cloud.firestore.GeoPoint {

    public GeoPoint() {
        super(0, 0);
    }

    public GeoPoint(double latitude, double longitude) {
        super(latitude, longitude);
    }
}
