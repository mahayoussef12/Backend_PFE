package com.isima.projet.Client;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isima.projet.Rendez_vous.ResourceNotFoundException;
import com.isima.projet.User.UserRepository;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
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
    @Autowired
    ServletContext context;
    @GetMapping ("/getAll")
    public ResponseEntity<List<String>> getAll()
    {
        List<String> listArt = new ArrayList<String>();
        String filesPath = context.getRealPath("/Images");
        File filefolder = new File(filesPath);
        for (File file : Objects.requireNonNull(filefolder.listFiles()))
        {
            if(!file.isDirectory())
            {
                String encodeBase64 = null;
                try {
                    String extension = FilenameUtils.getExtension(file.getName());
                    FileInputStream fileInputStream = new FileInputStream(file);
                    byte[] bytes = new byte[(int)file.length()];
                    fileInputStream.read(bytes);
                    encodeBase64 = Base64.getEncoder().encodeToString(bytes);
                    listArt.add("data:image/"+extension+";base64,"+encodeBase64);
                    fileInputStream.close();


                }catch (Exception ignored){

                }
            }
        }
        return new ResponseEntity<>(listArt, HttpStatus.OK);
    }
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
        boolean isExit = new File(context.getRealPath("/Images/")).exists();
        if (!isExit)
        {
            new File (context.getRealPath("/Images/")).mkdir();
            System.out.println("mk dir.............");
        }
        Client client1 = new ObjectMapper().readValue(client, Client.class);
        /*String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        Path fileStorage = get(DIRECTORY, fileName).toAbsolutePath().normalize();
        copy(file.getInputStream(), fileStorage, REPLACE_EXISTING);
        String filename = file.getOriginalFilename();
        client1.setImages(fileName);*/
        String filename = file.getOriginalFilename();
        String newFileName = FilenameUtils.getBaseName(filename)+"."+FilenameUtils.getExtension(filename);
        File serverFile = new File (context.getRealPath("/Images/"+File.separator+newFileName));
        try
        {
            System.out.println("Image");
            FileUtils.writeByteArrayToFile(serverFile,file.getBytes());

        }catch(Exception e) {
            e.printStackTrace();
        }

       client1.setImages(newFileName);
      /*  SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(client1.getEmail());
        message.setSubject("Confirmation d'inscri");
        message.setText("vous etes inscrie dans notre platform !! ");
        this.emailSender.send(message);*/
        client1.setMdp(encoder.encode(client1.getMdp()));

        Smscode smsCode = createSMSCode();
        {

//            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
//            String t="+216"+entreprise.getTel();
//
//
//            Message message = Message.creator(
//                            new PhoneNumber(t),//The phone number you are sending text to
//                            new PhoneNumber("+12396030036"),//The Twilio phone number
//                            "Your login verification code is:" + smsCode.getCode()+ "，Valid for 2 minutes")
//                    .create();

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

