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

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RestController
public class DeviceController {

    @Autowired
    protected DeviceRepository deviceRepository;

    @Autowired
    protected Authenticator authenticator;

    @PostMapping(value = "/device", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Device> registerDevice(@RequestHeader(name = "Authorization", required = false) String authHeader,
                                                 @RequestBody Device inputDevice) {
        String uid;
        try {
            uid = authenticator.authentication(authHeader);
        } catch (FirebaseAuthException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        String userId = inputDevice.getUserId();
        if (uid.equals(userId)) {
            Device device = deviceRepository.findByCertificateNo(inputDevice.getCertificateNo()).block();
            if (device == null) {
                Device savedDevice = deviceRepository.save(inputDevice).block();
                return ResponseEntity.status(HttpStatus.CREATED).body(savedDevice);
            } else {
                if (device.getUserId().equals(uid)) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
                } else {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
                }
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
            device = deviceRepository.save(device).block();
            return ResponseEntity.status(HttpStatus.OK).body(device);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    @GetMapping("/devices/user/{userId}")
    public ResponseEntity<List<Device>> getAllDevicesByUser(@RequestHeader(name = "Authorization", required = true) String authHeader,
                                                            @PathVariable("userId") String userId) {
        String uid;
        try {
            uid = authenticator.authentication(authHeader);
        } catch (FirebaseAuthException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        if (userId.equals(uid)) {
            List<Device> userDevices = deviceRepository.findAllByUserId(userId).collectList().block();
            if(userDevices == null){
                userDevices = new LinkedList<>();
            }
            return ResponseEntity.status(HttpStatus.OK).body(userDevices);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }
}
