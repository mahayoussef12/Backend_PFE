package com.isima.projet.Client;

import com.isima.projet.Rendez_vous.ResourceNotFoundException;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1")
public class ClientController {
	@Autowired
	private ServiceClient serviceClient;
	@Autowired ClientRepository repository;

@Autowired
public JavaMailSender emailSender;
	public static final String ACCOUNT_SID = "AC4ef54b6c78aceb1e328e5e753de97c45";
	public static final String AUTH_TOKEN = "fc77906e886a27514d7ac38c5e6fbff8";
	@Autowired
	private PasswordEncoder encoder;

	@GetMapping("/client")
	public List<Client> getAllCliens() {
		return serviceClient.getAll();
	}

	@GetMapping("/client/{id}")
	public Client getClientById(@PathVariable int id) {
		return serviceClient.getById(id);
	}

	@GetMapping("/clientEmail/{email}")
	public List<Client> getAllEmail(@PathVariable String email){return  serviceClient.GetEmail(email);}

	@PostMapping("/client/ajouter")
	public Client  createClient(@RequestBody Client client ) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(client.getEmail());
		message.setSubject("Confirmation d'inscri");
		message.setText("vous etes inscrie dans notre platform !! ");
		this.emailSender.send(message);
		//client.setMdp(encoder.encode(client.getMdp()));
		 return serviceClient.save(client);
		//sendSMS();
//		return "Ajoutée avec succes !!";
	}
	@PutMapping("/client/{id}")
	public ResponseEntity<Client> updateClient(@PathVariable(value = "id") int id,
													   @RequestBody Client client1) throws ResourceNotFoundException {
		Client client = repository.findById(id)
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

}

