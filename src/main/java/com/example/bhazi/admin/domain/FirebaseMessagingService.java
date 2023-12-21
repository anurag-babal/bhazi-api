package com.example.bhazi.admin.domain;

import org.springframework.stereotype.Service;

import com.example.bhazi.admin.domain.model.BhaziMessage;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FirebaseMessagingService {
    // private final FirebaseMessaging firebaseMessaging;

    public String sendNotification(
        BhaziMessage bhaziMessage, 
        String token
    ) throws FirebaseMessagingException {

        Notification notification = Notification
                .builder()
                .setTitle(bhaziMessage.getTitle())
                .setBody(bhaziMessage.getBody())
                .build();

        Message message = Message
                .builder()
                .setNotification(notification)
                .setToken(token)
                .build();

        return FirebaseMessaging.getInstance().send(message);
    }
}
