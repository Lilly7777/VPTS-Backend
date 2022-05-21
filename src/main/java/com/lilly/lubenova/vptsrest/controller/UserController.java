package com.lilly.lubenova.vptsrest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.firebase.auth.FirebaseAuthException;
import com.lilly.lubenova.vptsrest.authenticator.Authenticator;
import com.lilly.lubenova.vptsrest.model.Device;
import com.lilly.lubenova.vptsrest.model.User;
import com.lilly.lubenova.vptsrest.repository.DeviceRepository;
import com.lilly.lubenova.vptsrest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
public class UserController {

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected DeviceRepository deviceRepository;

    @Autowired
    protected Authenticator authenticator;

    @PostMapping(value = "/user", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<User> createUser(@RequestHeader(name = "Authorization", required = false) String authHeader, @RequestBody User user) {
        String uid;
        try {
            uid = authenticator.authentication(authHeader);
        } catch (FirebaseAuthException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        if (uid.equals(user.getUserId())) {
            if (userRepository.findById(uid).block() != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
            } else {
                return ResponseEntity.status(HttpStatus.CREATED).body(userRepository.save(user).block());
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<User> getUser(@RequestHeader(name = "Authorization", required = true) String authHeader, @PathVariable("userId") String userId) {
        String uid;
        try {
            uid = authenticator.authentication(authHeader);
        } catch (FirebaseAuthException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        if (uid.equals(userId)) {
            User user = userRepository.findById(uid).block();
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<User> deleteUser(@RequestHeader(name = "Authorization", required = true) String authHeader, @PathVariable("userId") String userId) {
        String uid;
        try {
            uid = authenticator.authentication(authHeader);
        } catch (FirebaseAuthException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        if (uid.equals(userId)) {
            User user = userRepository.findById(uid).block();
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            userRepository.deleteById(uid).block();
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    @PutMapping("/user/{userId}")
    public ResponseEntity<User> editUser(@RequestHeader(name = "Authorization", required = true) String authHeader, @PathVariable("userId") String userId, @RequestBody User editedUser) {
        String uid;
        try {
            uid = authenticator.authentication(authHeader);
        } catch (FirebaseAuthException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        if (uid.equals(userId)) {
            User user = userRepository.findById(uid).block();
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            if (Objects.nonNull(editedUser.getFullName())) {
                user.setFullName(editedUser.getFullName());
            }
            if (Objects.nonNull(editedUser.getLocation())) {
                user.setLocation(editedUser.getLocation());
            }
            if (Objects.nonNull(editedUser.getPhoneNumber())) {
                user.setPhoneNumber(editedUser.getPhoneNumber());
            }
            return ResponseEntity.status(HttpStatus.OK).body(userRepository.save(user).block());
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    @GetMapping("/user/{userId}/devices")
    public ResponseEntity<List<Device>> getUserDevices(@RequestHeader(name = "Authorization", required = true) String authHeader, @PathVariable("userId") String userId){
        String uid;
        try {
            uid = authenticator.authentication(authHeader);
        } catch (FirebaseAuthException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        if (uid.equals(userId)) {
            return ResponseEntity.status(HttpStatus.OK).body(deviceRepository.findAllByUserId(userId).collectList().block());
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }
}
