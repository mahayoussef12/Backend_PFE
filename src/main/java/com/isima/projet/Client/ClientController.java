package com.isima.projet.Client;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isima.projet.Rendez_vous.ResourceNotFoundException;
import com.isima.projet.User.UserRepository;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static java.nio.file.Files.copy;
import static java.nio.file.Paths.get;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

@CrossOrigin(origins = "http://localhost:4200")
@Controller
@RestController
@RequestMapping("/api/v1")
public class ClientController {
    public static final String DIRECTORY = System.getProperty("user.home") + "/Downloads/uploads/";
    @Autowired
    private ServiceClient serviceClient;
    @Autowired
    private ClientRepository repositoryclient;

    @Autowired
    public JavaMailSender emailSender;
    public static final String ACCOUNT_SID = "AC4ef54b6c78aceb1e328e5e753de97c45";
    public static final String AUTH_TOKEN = "fc77906e886a27514d7ac38c5e6fbff8";
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private UserRepository Repository;


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
      /*  StringBuilder fileNames = new StringBuilder();
        String filename=client.getId() + file.getOriginalFilename().substring(file.getOriginalFilename().length()-4);
        Path fileNameAndPath = Paths.get( DIRECTORY ,filename);
        try {
            Files.write(fileNameAndPath, file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        client.setImages(filename);*/
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
       return  serviceClient.save(client1);
        //sendSMS();
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
        @GetMapping("/manel/{cv}")
        public String mdp (@PathVariable String cv){
            int strength = 10; // work factor of bcrypt
            BCryptPasswordEncoder bCryptPasswordEncoder =
                    new BCryptPasswordEncoder(strength, new SecureRandom());
            String encodedPassword;
            return encodedPassword = bCryptPasswordEncoder.encode(cv);

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
/*    @PostMapping("/add")
    public Client uploadImage(
            @RequestParam ("imageFile") MultipartFile file,
            @RequestParam("images") String images,
            @RequestParam ("nom") String nom,
            @RequestParam("prenom") String prenom,
            @RequestParam(" email") String email,
            @RequestParam("mdp") String mdp,
            @RequestParam("ville") String ville,
            @RequestParam("pays") String pays ,
            @RequestParam("genre") String genre,
            @RequestParam("adress") String adress
            )throws IOException {

        String newImageName=getSaltString().concat(file.getOriginalFilename());
        try {
            Files.copy(file.getInputStream(), this.root.resolve(newImageName));
        }catch (Exception e) {
            throw new RuntimeException("could not store the file. Error: " +e.getMessage());
        }
        Client client = new Client(nom,prenom,email,pays,ville,adress,genre,mdp,newImageName);

     serviceClient.save(client);
        return client;
    }*/
    public static String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }/*from  w  w  w .ja v a2 s . c om*/
        String saltStr = salt.toString();
        return saltStr;

    }
    }

