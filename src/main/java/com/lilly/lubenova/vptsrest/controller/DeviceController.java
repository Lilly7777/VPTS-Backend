package com.lilly.lubenova.vptsrest.controller;

import com.google.firebase.auth.FirebaseAuthException;
import com.lilly.lubenova.vptsrest.authenticator.Authenticator;
import com.lilly.lubenova.vptsrest.model.Device;
import com.lilly.lubenova.vptsrest.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class DeviceController {

    @Autowired
    protected DeviceRepository deviceRepository;

    @Autowired
    protected Authenticator authenticator;

    @PostMapping(value = "/device", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Device> registerDevice(@RequestHeader(name = "Authorization", required = false) String authHeader, @RequestBody Device device) {
        String uid;
        try {
            uid = authenticator.authentication(authHeader);
        } catch (FirebaseAuthException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        String userId = device.getUserId();
        if (uid.equals(userId)) {
            if (deviceRepository.findById(uid).block() != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
            } else {
                return ResponseEntity.status(HttpStatus.CREATED).body(deviceRepository.save(device).block());
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    @GetMapping("/device/{deviceId}")
    public ResponseEntity<Device> getDevice(@RequestHeader(name = "Authorization", required = true) String authHeader, @PathVariable("deviceId") String deviceId) {
        try {
            authenticator.authentication(authHeader);
        } catch (FirebaseAuthException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        Device device = deviceRepository.findById(deviceId).block();
        if (device == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(device);
    }

    @DeleteMapping("/device/{deviceId}")
    public ResponseEntity<Device> deleteDevice(@RequestHeader(name = "Authorization", required = true) String authHeader, @PathVariable("deviceId") String deviceId) {
        try {
            authenticator.authentication(authHeader);
        } catch (FirebaseAuthException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        Device device = deviceRepository.findById(deviceId).block();
        if (device == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        deviceRepository.deleteById(deviceId).block();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @PutMapping("/device/{deviceId}")
    public ResponseEntity<Device> updateDevice(@RequestHeader(name = "Authorization", required = true) String authHeader,
                                               @PathVariable("deviceId") String deviceId,
                                               @RequestBody Map<String, Object> body) {
        String uid;
        try {
            uid = authenticator.authentication(authHeader);
        } catch (FirebaseAuthException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Device device = deviceRepository.findById(deviceId).block();
        if (device == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        boolean isUserOwnerOfDevice = uid.equals(device.getUserId());
        if (body.containsKey("user_id") && isUserOwnerOfDevice) {
            device.setUserId(String.valueOf(body.get("user_id")));
            deviceRepository.save(device).block();
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }
}
