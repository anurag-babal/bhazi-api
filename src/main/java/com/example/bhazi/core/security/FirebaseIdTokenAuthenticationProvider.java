package com.example.bhazi.core.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;

import lombok.extern.slf4j.Slf4j;

// @Component
@Slf4j
public class FirebaseIdTokenAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(
        Authentication authentication
    ) throws AuthenticationException {
        String idToken = ((FirebaseAuthenticationToken) authentication).getIdToken();
        try {
            FirebaseToken firebaseToken = FirebaseAuth.getInstance()
                    .verifyIdToken(idToken, true);
            String uid = firebaseToken.getUid();
            UserRecord userRecord = FirebaseAuth.getInstance().getUser(uid);
            log.info("User id = {}", userRecord.getUid());
            User user = new User(userRecord);
            return user;
        } catch (FirebaseAuthException e) {
            if (e.getErrorCode().equals("id-token-revoked")) {
                throw new SecurityException("User token has been revoked, please sign in again!");
            } else {
                throw new SecurityException("Authentication failed!");
            }
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(FirebaseAuthenticationToken.class);
    }
    
}
