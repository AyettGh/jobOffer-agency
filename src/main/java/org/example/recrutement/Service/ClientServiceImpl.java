package org.example.recrutement.Service;

import jakarta.persistence.EntityNotFoundException;
import org.example.recrutement.Entity.Client;
import org.example.recrutement.Repository.ClientRepository;
import org.example.recrutement.Service.ServiceImpl.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {
    @Autowired
    private ClientRepository clientRepository;
    @Override
    public List<Client> all() {
        return clientRepository.findAll();
    }

    @Override
    public Client add(Client client) {
        return clientRepository.save(client);
    }

    @Override
    public Client getById(Long id) {
        return clientRepository.findById(id).orElseThrow(
                ()->new EntityNotFoundException("not found")
        );
    }

    @Override
    public void delete(Long id) {
            clientRepository.deleteById(id);
    }

    @Override
    public Boolean existById(Long id) {
        return clientRepository.existsById(id);
    }





}
