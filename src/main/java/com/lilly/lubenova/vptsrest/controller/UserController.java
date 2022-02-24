package com.lilly.lubenova.vptsrest.controller;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.lilly.lubenova.vptsrest.model.User;
import com.lilly.lubenova.vptsrest.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    protected UserRepository userRepository;

    @PostMapping(value = "/user", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<User> createUser(@RequestHeader(name = "Authorization", required = true) String authHeader, @RequestBody User user) {
        String token = StringUtils.substringAfter(authHeader, "Bearer ").trim();
        FirebaseToken decodedToken = null;
        try {
            decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
        } catch (FirebaseAuthException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        String uid = decodedToken.getUid();
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
    public ResponseEntity<User> getUser(@PathVariable("userId") String userId, @RequestHeader(name = "Authorization", required = true) String authHeader) {
        String token = StringUtils.substringAfter(authHeader, "Bearer ").trim();
        FirebaseToken decodedToken = null;
        try {
            decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
        } catch (FirebaseAuthException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        String uid = decodedToken.getUid();

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
}
