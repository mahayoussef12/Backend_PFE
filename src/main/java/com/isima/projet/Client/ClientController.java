package com.isima.projet.Client;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import org.springframework.beans.factory.annotation.Autowired;
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
	public String deleteEmployee(@PathVariable int id){
	   serviceClient.delete(id);
		return"Suppression avec success";
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

