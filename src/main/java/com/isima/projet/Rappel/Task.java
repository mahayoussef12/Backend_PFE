package com.isima.projet.Rappel;

import com.isima.projet.Client.Client;
import com.isima.projet.Client.ClientRepository;
import com.isima.projet.Entreprise.Entreprise;
import com.isima.projet.Entreprise.EntrepriseRepo;
import com.isima.projet.Rendez_vous.RDV;
import com.isima.projet.Rendez_vous.RDVRepository;
import com.isima.projet.push.PushNotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static java.lang.Thread.sleep;

@Component
public class Task {
    private static final Logger log = LoggerFactory.getLogger(Task.class);
    private final RDVRepository repo;
    protected final ClientRepository repoc;
   @Autowired
    private final PushNotificationService pushNotificationService;
    private final EntrepriseRepo repoen;
    @Autowired
    public JavaMailSender emailSender;
    private int FixedDelay;
    SimpleDateFormat dateFormat = new
            SimpleDateFormat("yyyy-MM-dd");
    public static final String ACCOUNT_SID = "AC30d125e7034600076cc44a3c286a0f19";
    public static final String AUTH_TOKEN = "5b3dfa49062cc4b736639b1a6e115bb5";

    public Task(RDVRepository repo, ClientRepository repoc, PushNotificationService pushNotificationService, EntrepriseRepo repoen) {
        this.repo = repo;
        this.repoc = repoc;
        this.pushNotificationService = pushNotificationService;
        this.repoen = repoen;
    }

    @Scheduled(cron = "0 0 * * * ?")
    public void reportCurrentTime() throws InterruptedException, ParseException {
        List<RDV> rdv = repo.findAllByClientNotNullAndEntrepriseNotNull();
         for (RDV value : rdv) {
             Date date1 = dateFormat.parse(String.valueOf(LocalDate.now()));
             Date date2 = dateFormat.parse(String.valueOf(value.getDate_rdv()));
             int id = (value.getClient().getId());
             long in = (value.getEntreprise().getId());
             if (date1.equals(date2)) {
                 Client client = repoc.findById(id).get();
                 Entreprise entreprise = repoen.findById(in).get();
                 pushNotificationService.sendPushNotificationToToken();
                 String tel = client.getTel();
                 String tell = entreprise.getTel();
             /*    Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
                 Message message = Message.creator(
                                 new PhoneNumber(tel),//The phone number you are sending text to
                                 new PhoneNumber("+12342064232"),//The Twilio phone number
                                 "il exciste un rendez vous de Nom" + client.getNom() + client.getPrenom() + date2 + "avec " + entreprise.getNomSociete())
                         .create();
                 Message messagee = Message.creator(
                                 new PhoneNumber(tell),//The phone number you are sending text to
                                 new PhoneNumber("+12342064232"),//The Twilio phone number
                                 "il exciste un rendez vous de Nom" + client.getNom() + client.getPrenom() + date2 + "avec " + entreprise.getNomSociete() + value.getDate_rdv())
                         .create();
*/
                 SimpleMailMessage message1 = new SimpleMailMessage();
                 message1.setTo(client.getEmail());
                 message1.setSubject("Rappel rendez-vous");
                 message1.setText( "il exciste un rendez vous de Nom" + client.getNom() + client.getPrenom() + date2 + "avec " + entreprise.getNomSociete() + value.getDate_rdv());
                 this.emailSender.send(message1);
                 log.info("test");
             }
         }
        sleep(FixedDelay =200);
    }
}
