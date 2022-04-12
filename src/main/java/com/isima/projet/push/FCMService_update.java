package com.isima.projet.push;

import com.google.firebase.messaging.*;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class FCMService_update {

    private String token="dG5G0Zdig5D8Gb-1z3xMIb:APA91bGjERMct4eKKf5WhkIK4tR1qjJGzFTQBS_DsOxxAXQTF_due2hyqtCev54V0hOjqotFyS7l2QRFTsYkupuu7EXfVqqgxVkUjI2ParoxF2vXyAhAU0SHlO6VBpXF_BDAugeBmWir";

    private String title="Mise a jour les information";
    private String message="vous etes mise a jour les informations ";
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
