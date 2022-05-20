package com.isima.projet.push;

import com.google.firebase.messaging.*;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class FCMService_update {

    private String token="dG5G0Zdig5D8Gb-1z3xMIb:APA91bHORQbBZK0-hVxps78E8wW4gfk4T06GekaoaNh4oN0zd3R3NjTndAZPz5qu64RPogj706rmp_j_2FCwarO7H_feC1bPiRrkP_xFJIMsUION3yVBXxum3jLzUY3TQ7djuOPFLzv7";

    private String title="Mise a horaire les information";
    private String message="vous etes mise a horaire les informations ";
    private String topic="";


    public void sendMessageToToken()
            throws InterruptedException, ExecutionException {
        Message message = getPreconfiguredMessageToToken(token);
        String response = sendAndGetResponse(message);

    }

    private String sendAndGetResponse(Message message) throws InterruptedException, ExecutionException {
        return FirebaseMessaging.getInstance().sendAsync(message).get();
    }


    private ApnsConfig getApnsConfig(String topic) {
        return ApnsConfig.builder()
                .setAps(Aps.builder().setCategory(topic).setThreadId(topic).build()).build();
    }

    private Message getPreconfiguredMessageToToken(String request) {
        return getPreconfiguredMessageBuilder(request).setToken(token)
                .build();
    }


    private Message.Builder getPreconfiguredMessageBuilder(String request) {

        ApnsConfig apnsConfig = getApnsConfig(topic);
        return Message.builder()
                .setApnsConfig(apnsConfig).setNotification(
                        new Notification(title,message));
    }


}
