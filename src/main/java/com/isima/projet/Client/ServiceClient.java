package com.isima.projet.Client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceClient {
    @Autowired
    private ClientRepository repo;
    public Client save (Client client)  {
        repo.save(client);

        return client;
    }
    public List<Client> getAll ()
    {
        List<Client> clients = new ArrayList<Client>();
        repo.findAll().forEach(clients1 -> clients.add(clients1));
        return clients;
    }
    public Client getById (int id)
    {
     return repo.findById(id).get();
    }


    public void delete(int id) {
        repo.deleteById(id);
    }

    public Client update(int id, Client client) {

        Client client1 = repo.getById(id);
        return repo.save(client1);
    }
  public Client GetEmail(String email){
      return repo.findByEmail(email);

  }
}
