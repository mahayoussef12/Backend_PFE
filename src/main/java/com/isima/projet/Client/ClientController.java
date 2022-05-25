package com.isima.projet.Client;

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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.util.List;

import static java.nio.file.Paths.get;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

@CrossOrigin(origins = "http://localhost:4200")
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
	public Client getAllEmail(@PathVariable String email){return  serviceClient.GetEmail(email);}

	@PostMapping(value = "/client/ajouter",consumes = { "multipart/mixed" })
	public String createClient(@RequestBody Client client,@RequestPart("files")MultipartFile multipartFiles ) throws IOException {
		String fileName = StringUtils.cleanPath(multipartFiles.getOriginalFilename());
			client.setImages(fileName);
					SimpleMailMessage message = new SimpleMailMessage();
					message.setTo(client.getEmail());
					message.setSubject("Confirmation d'inscri");
					message.setText("vous etes inscrie dans notre platform !! ");
					this.emailSender.send(message);
					client.setMdp(encoder.encode(client.getMdp()));
					 serviceClient.save(client);
				return"ajouter";

		//sendSMS();
//		return "Ajoutée avec succes !!";
	}
	@PutMapping("/client/{id}")
	public ResponseEntity<Client> updateClient(@PathVariable(value = "id") int id,
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
		final Client updatedEntreprise =serviceClient.save(client);
		return ResponseEntity.ok(updatedEntreprise);
	}
/*
	@PutMapping("/employees/{id}")
	Optional<Client> replaceEmployee(@RequestBody Client newEmployee, @PathVariable int id) {

		return repository.findById(id)
				.map(employee -> {
					employee.setEmail(newEmployee.getEmail());
					employee.setAdress(newEmployee.getAdress());

					return repository.save(employee);
				});
	}*/

	@DeleteMapping("/client/delete/{id}")
	public void deleteClient(@PathVariable int id){
	   serviceClient.delete(id);

	}
	@GetMapping("/manel/{cv}")
			public String mdp (@PathVariable String cv){
		int strength = 10; // work factor of bcrypt
		BCryptPasswordEncoder bCryptPasswordEncoder =
				new BCryptPasswordEncoder(strength, new SecureRandom());
		String encodedPassword;
		return  encodedPassword = bCryptPasswordEncoder.encode(cv);

	}
Client client;
	public Message sendSMS() {
		Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
		Message message = Message.creator(
						new com.twilio.type.PhoneNumber(client.getTel()),//The phone number you are sending text to
						new com.twilio.type.PhoneNumber("+12396030036"),//The Twilio phone number
						"hi .....")
				.create();

		return message;
	}
	@PostMapping("/upload")
	public ResponseEntity <String> uploadFiles(@RequestParam("files")MultipartFile multipartFiles) throws IOException {
		String filenames ;

			String filename = StringUtils.cleanPath(multipartFiles.getOriginalFilename());

		filenames=filename;

		return ResponseEntity.ok().body(filenames);
	}


	@GetMapping("download/{filename}")
	public ResponseEntity<Resource> downloadFiles(@PathVariable("filename") String filename) throws IOException {
		Path filePath = get(DIRECTORY).toAbsolutePath().normalize().resolve(filename);
		if(!Files.exists(filePath)) {
			throw new FileNotFoundException(filename + " was not found on the server");
		}
		Resource resource = new UrlResource(filePath.toUri());
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("File-Name", filename);
		httpHeaders.add(CONTENT_DISPOSITION, "attachment;File-Name=" + resource.getFilename());
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(Files.probeContentType(filePath)))
				.headers(httpHeaders).body(resource);
	}
}

