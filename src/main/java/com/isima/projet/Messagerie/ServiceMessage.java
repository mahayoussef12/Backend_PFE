package com.isima.projet.Messagerie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceMessage {
@Autowired private messagerieRepository messagerieRepository;
    public List<messagerie> getAll()

    {
        List<messagerie> messageries = new ArrayList<messagerie>();
        messagerieRepository.findAll().forEach(message -> messageries.add(message));
        return messageries;
    }


    public messagerie getById(Long id_mess)
    {

        return messagerieRepository.findById(id_mess).get();
    }

    public messagerie save(messagerie message)
    {
        messagerieRepository.save(message); return message;
    }
}
