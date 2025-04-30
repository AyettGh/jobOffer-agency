package org.example.recrutement.Service.ServiceImpl;

import org.example.recrutement.Entity.Application;
import org.example.recrutement.Entity.Client;

import java.util.List;

public interface ClientService {
    public List<Client> all();
    public Client add(Client client);
    public Client getById(Long id);
    public void delete(Long id);
    public Boolean existById(Long id);

}
