package com.lilly.lubenova.vptsrest.authenticator;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.context.annotation.ApplicationScope;

@Component
@ApplicationScope
public class Authenticator {
    public String authentication(@RequestHeader(name = "Authorization", required = true) String authHeader) throws FirebaseAuthException {
        String token = StringUtils.substringAfter(authHeader, "Bearer ").trim();
        FirebaseToken decodedToken = null;
        decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
        return decodedToken.getUid();
    }
}
