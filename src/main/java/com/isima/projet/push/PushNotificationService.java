package com.isima.projet.push;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class PushNotificationService {

    private FCMService fcmService;
    private FCMService_supprimer fcm;
    @Autowired
    private FCMService_update fcmService_update;

    public PushNotificationService(FCMService fcmService,FCMService_supprimer fcm) {
        this.fcmService = fcmService;
        this.fcm=fcm;
    }



    public void sendPushNotificationToToken() {
        try {
            fcmService.sendMessageToToken();
        } catch (InterruptedException | ExecutionException e) {

        }
    }


    public void Suppression() {
        try {
            fcm.sendMessageToToken();
        } catch (InterruptedException | ExecutionException e) {

        }
    }
    public void update() {
        try {
            fcmService_update.sendMessageToToken();
        } catch (InterruptedException | ExecutionException e) {

        }
    }

}
