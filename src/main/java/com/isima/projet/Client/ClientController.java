package com.isima.projet.Client;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isima.projet.Rendez_vous.ResourceNotFoundException;
import com.isima.projet.User.UserRepository;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.nio.file.Files.copy;
import static java.nio.file.Paths.get;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

@CrossOrigin(origins = "http://localhost:4200")
@Controller
@RestController
@RequestMapping("/api/v1")
public class ClientController {
    public static final String DIRECTORY = "C:/Users/HP/Desktop/pfe/src/assets/img/";
    @Autowired
    private ServiceClient serviceClient;
    @Autowired
    private ClientRepository repositoryclient;

    @Autowired
    public JavaMailSender emailSender;
    public static final String ACCOUNT_SID = "AC30d125e7034600076cc44a3c286a0f19";
    public static final String AUTH_TOKEN = "5b3dfa49062cc4b736639b1a6e115bb5";
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private UserRepository Repository;
    @Autowired
    ServletContext context;

    @GetMapping("/client")
    public List<Client> getAllCliens() {
        return serviceClient.getAll();
    }

    @GetMapping("/client/{id}")
    public Client getClientById(@PathVariable int id) {

      

        return serviceClient.getById(id);
    }

    @GetMapping("/clientEmail/{email}")
    public Client getAllEmail(@PathVariable String email) {
        return serviceClient.GetEmail(email);
    }

    @PostMapping(value = "/client/ajouter",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)

    public Client createClient( @RequestParam("client") String client,@RequestParam("file") MultipartFile file) throws JsonParseException, JsonMappingException, Exception {

        Client client1 = new ObjectMapper().readValue(client, Client.class);
         String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        Path fileStorage = get(DIRECTORY, fileName).toAbsolutePath().normalize();
        copy(file.getInputStream(), fileStorage, REPLACE_EXISTING);
        String filename = file.getOriginalFilename();
        client1.setImages(fileName);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(client1.getEmail());
        message.setSubject("Confirmation d'inscri");
        message.setText("vous etes inscrie dans notre platform !! ");
        this.emailSender.send(message);
        client1.setMdp(encoder.encode(client1.getMdp()));

        Smscode smsCode = createSMSCode();
        {

            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
            String t="+216"+client1.getTel();
            Message message1 = Message.creator(
                            new PhoneNumber(t),//The phone number you are sending text to
                            new PhoneNumber("+12342064232"),//The Twilio phone number
                            "Your login verification code is:" + smsCode.getCode()+ "，Valid for 2 minutes")
                    .create();

            SimpleMailMessage messa = new SimpleMailMessage();
            messa.setTo(client1.getEmail());
            messa.setSubject("Code");
            messa.setText(smsCode.getCode());
            this.emailSender.send(messa);

        }
        client1.setTest(smsCode.getCode());
        client1.setTime((LocalDateTime) smsCode.getExpireTime());
       return  serviceClient.save(client1);
        //sendSMS();
    }

    @GetMapping(path="/Imgarticles/{id}")
    public byte[] getPhoto(@PathVariable("id") int id) throws Exception{
       Client Article   = repositoryclient.findById(id).get();
        return Files.readAllBytes(Paths.get(context.getRealPath("/Images/")+Article.getImages()));
    }
    @PostMapping("/client/modifier/{id}")
    public String update (@PathVariable(value = "id") long id,@RequestBody String mdp ) throws ResourceNotFoundException {
       Client avis=repositoryclient.findById((int) id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + id));
        avis.setMdp(encoder.encode(String.valueOf(mdp)));
        repositoryclient.save(avis);
        return "true";
    }

    @PutMapping("/client/{id}")
        public ResponseEntity<Client> updateClient ( @PathVariable(value = "id") int id,
        @RequestBody Client client1) throws ResourceNotFoundException {
            Client client = repositoryclient.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + id));
            client.setAdress(client1.getAdress());
            client.setNom(client1.getNom());
            client.setPrenom(client1.getPrenom());
            client.setTel(client1.getTel());
            client.setEmail(client1.getEmail());
            client.setAdress(client1.getAdress());
            client.setMdp(client1.getMdp());
            client.setVille(client1.getVille());
            client.setMdp(client1.getMdp());
            client.setImages(client1.getImages());
            final Client updatedEntreprise = serviceClient.save(client);
            return ResponseEntity.ok(updatedEntreprise);
        }


        @DeleteMapping("/client/delete/{id}")
        public void deleteClient ( @PathVariable int id){
            serviceClient.delete(id);

        }

        Client client;
        public Message sendSMS () {
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
            Message message = Message.creator(
                            new com.twilio.type.PhoneNumber(client.getTel()),//The phone number you are sending text to
                            new com.twilio.type.PhoneNumber("+12396030036"),//The Twilio phone number
                            "hi .....")
                    .create();

            return message;
        }
/*	@PostMapping("/upload")
	public ResponseEntity <String> uploadFiles(@RequestParam("files")MultipartFile multipartFiles) throws IOException {
		String filenames ;

			String filename = StringUtils.cleanPath(multipartFiles.getOriginalFilename());

		filenames=filename;

		return ResponseEntity.ok().body(filenames);
	}*/


        @GetMapping("download/{filename}")
        public ResponseEntity<Resource> downloadFiles (@PathVariable("filename") String filename) throws IOException {
            Path filePath = get(DIRECTORY).toAbsolutePath().normalize().resolve(filename);
            if (!Files.exists(filePath)) {
                throw new FileNotFoundException(filename + " was not found on the server");
            }
            Resource resource = new UrlResource(filePath.toUri());
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("File-Name", filename);
            httpHeaders.add(CONTENT_DISPOSITION, "attachment;File-Name=" + resource.getFilename());
            return ResponseEntity.ok().contentType(MediaType.parseMediaType(Files.probeContentType(filePath)))
                    .headers(httpHeaders).body(resource);
        }

        @PostMapping("/upload")
        public ResponseEntity<List<String>> uploadFiles (@RequestParam("files") List < MultipartFile > multipartFiles) throws
        IOException {
            List<String> filenames = new ArrayList<>();
            for (MultipartFile file : multipartFiles) {
                String filename = StringUtils.cleanPath(file.getOriginalFilename());
                Path fileStorage = get(DIRECTORY, filename).toAbsolutePath().normalize();
                copy(file.getInputStream(), fileStorage, REPLACE_EXISTING);
                filenames.add(filename);
            }
            return ResponseEntity.ok().body(filenames);
        }
    @PostMapping("veriff/{id}")
    public String veriff(@PathVariable int id, @RequestBody String code ) {
        Client client = serviceClient.getById(id);
        if ((code.equals(client.getTest()))&&(LocalDateTime.now().isBefore(client.getTime()))){
            SimpleMailMessage messa = new SimpleMailMessage();
            messa.setTo(client.getEmail());
            messa.setSubject("Confirmation d'inscri");
            messa.setText("vous etes inscrie dans notre platform !! ");
            this.emailSender.send(messa);
            return "true";
        }
        else{
            return"false";
        }
    }
    private Smscode createSMSCode() {
        String code = RandomStringUtils.randomNumeric(5);
        return new Smscode(code, 1800);
    }
    @GetMapping("/renvoi/{idclient}")
    public Client renvoiCode(@PathVariable int idclient){
        Client ent=serviceClient.getById(idclient);
        Smscode smsCode = createSMSCode();
        {
            SimpleMailMessage messa = new SimpleMailMessage();
            messa.setTo(ent.getEmail());
            messa.setSubject("Code");
            messa.setText(smsCode.getCode());
            this.emailSender.send(messa);
            ent.setTest(smsCode.getCode());
            ent.setTime((LocalDateTime) smsCode.getExpireTime());
            return serviceClient.save(ent);


        }}
    }

