
package com.isima.projet.push;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PushNotificationController {

    private PushNotificationService pushNotificationService;

    public PushNotificationController(PushNotificationService pushNotificationService) {
        this.pushNotificationService = pushNotificationService;
    }


    @PostMapping("/notification/token")
    public String sendTokenNotification() {
        pushNotificationService.Suppression();
        return  "Notification has been sent.";
    }

}

