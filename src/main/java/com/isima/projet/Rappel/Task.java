package com.isima.projet.Rappel;

import com.isima.projet.Client.Client;
import com.isima.projet.Client.ClientRepository;
import com.isima.projet.Entreprise.Entreprise;
import com.isima.projet.Entreprise.EntrepriseRepo;
import com.isima.projet.Rendez_vous.RDV;
import com.isima.projet.Rendez_vous.RDVRepository;
import com.isima.projet.push.PushNotificationService;
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
    @Autowired
    private PushNotificationService pushNotificationService;
    @Autowired
    private EntrepriseRepo repoen;

    private int FixedDelay;
    SimpleDateFormat dateFormat = new
            SimpleDateFormat("yyyy-MM-dd");
    public static final String ACCOUNT_SID = "AC4ef54b6c78aceb1e328e5e753de97c45";
    public static final String AUTH_TOKEN = "fc77906e886a27514d7ac38c5e6fbff8";

    // @Scheduled(fixedRate =86400*1000 )
    public void reportCurrentTime() throws InterruptedException, ParseException {
        List<RDV> rdv = repo.findAll();
        for (int i = 0; i < rdv.size(); i++) {
            Date date1 = dateFormat.parse(String.valueOf(LocalDate.now()));
            Date date2 = dateFormat.parse(String.valueOf(rdv.get(i).getDate_rdv()));
            int id= (rdv.get(i).getClient().getId());
            long in=(rdv.get(i).getEntreprise().getId());
            if (date1.equals(date2)) {
                Client client =repoc.findById(id).get();
                Entreprise entreprise=repoen.findById(in).get();
                pushNotificationService.sendPushNotificationToToken();
                String tel=client.getTel();
                String tell=entreprise.getTel();
                Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
               Message message = Message.creator(
                       new PhoneNumber(tel),//The phone number you are sending text to
                           new PhoneNumber("+12396030036"),//The Twilio phone number
                                "il exciste un rendez vous de Nom"+client.getNom()+client.getPrenom()+date2+"avec "+entreprise.getNomSociete())
                        .create();
                Message messagee = Message.creator(
                                new PhoneNumber(tell),//The phone number you are sending text to
                                new PhoneNumber("+12396030036"),//The Twilio phone number
                                "il exciste un rendez vous de Nom"+client.getNom()+client.getPrenom()+date2+"avec "+entreprise.getNomSociete()+rdv.get(i).getDate_rdv())
                        .create();


                 log.info("test");
            }}
        //sleep(FixedDelay =20000);
    }
}
