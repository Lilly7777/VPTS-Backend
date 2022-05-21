package com.lilly.lubenova.vptsrest.controller;

import com.google.firebase.auth.FirebaseAuthException;
import com.lilly.lubenova.vptsrest.authenticator.Authenticator;
import com.lilly.lubenova.vptsrest.model.GPSRecord;
import com.lilly.lubenova.vptsrest.repository.GPSRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GPSController {

    @Autowired
    protected GPSRepository gpsRepository;

    @Autowired
    protected Authenticator authenticator;

    @GetMapping("/gps/{gpsRecordId}")
    public ResponseEntity<GPSRecord> getGPSRecord(@RequestHeader(name = "Authorization", required = true) String authHeader, @PathVariable("gpsRecordId") String gpsRecordId){
        try {
            authenticator.authentication(authHeader);
        } catch (FirebaseAuthException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        GPSRecord gpsRecord = gpsRepository.findById(gpsRecordId).block();
        if (gpsRecord == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(gpsRecord);
    }
}
