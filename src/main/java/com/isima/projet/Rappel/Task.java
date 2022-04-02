package com.isima.projet.Rappel;

import com.isima.projet.Client.Client;
import com.isima.projet.Client.ClientRepository;
import com.isima.projet.Rendez_vous.RDV;
import com.isima.projet.Rendez_vous.RDVRepository;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Component
public class Task {
    private static final Logger log = LoggerFactory.getLogger(Task.class);
    @Autowired
    private RDVRepository repo;
    @Autowired
    protected ClientRepository repoc;


    private int FixedDelay;
    SimpleDateFormat dateFormat = new
            SimpleDateFormat("yyyy-MM-dd");
    public static final String ACCOUNT_SID = "AC821c8f198083ba2bb831f0cb4d51dfbf";
    public static final String AUTH_TOKEN = "ddf43c5ea724ff620efbc06789d62c85";

     //@Scheduled(fixedRate =86400*1000 )
    public void reportCurrentTime() throws InterruptedException, ParseException {
        List<RDV> rdv = repo.findAll();
        for (int i = 0; i < rdv.size(); i++) {
            Date date1 = dateFormat.parse(String.valueOf(LocalDate.now()));
            Date date2 = dateFormat.parse(String.valueOf(rdv.get(i).getDate_rdv()));
            int id= (rdv.get(i).getClient().getId());

            if (date1.equals(date2)) {

                Client client =repoc.findById(id).get();
                String tel=client.getTel();
                Twilio.init(ACCOUNT_SID, AUTH_TOKEN);


               Message message = Message.creator(
                       new PhoneNumber(tel),//The phone number you are sending text to
                           new PhoneNumber("+15078586431"),//The Twilio phone number
                                "hi test ")
                        .create();


                 log.info("test");
            }}
        // sleep(FixedDelay =200*1000 );
    }
}