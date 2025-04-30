package org.example.recrutement.Controller;

import org.example.recrutement.Entity.Category;
import org.example.recrutement.Entity.Client;
import org.example.recrutement.Service.ServiceImpl.ApplicationService;
import org.example.recrutement.Service.ServiceImpl.CategoryService;
import org.example.recrutement.Service.ServiceImpl.ClientService;
import org.example.recrutement.Service.ServiceImpl.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/recrutement")
public class ClientController {
    @Autowired
    private ClientService clientService;
    @GetMapping("/allclient")
    public List<Client> all()
    {
        return clientService.all();
    }
    @GetMapping("/getclient/{id}")
    public Client getById(@PathVariable Long id)
    {
        return clientService.getById(id);
    }
    @PostMapping("/addclient")
    public Client save(@RequestBody Client client)
    {
        return clientService.add(client);
    }

    @PutMapping("/updateclient/{id}")
    public ResponseEntity<?> update(@RequestBody Client client, @PathVariable Long id)
    {
        if( clientService.existById(id))
        {
            Client client1=clientService.getById(id);
            client1.setApplications(client.getApplications());
            client1.setBio(client.getBio());
            client1.setEducation(client.getEducation());
            client1.setExperience(client.getExperience());
            client1.setPhoneNumber(client.getPhoneNumber());
            client1.setSkills(client.getSkills());
            client1.setUser(client.getUser());
            clientService.add(client1);
            return ResponseEntity.ok().body(client1);
        }
        else {
            HashMap<String,String> map=new HashMap<>();
            map.put("error","error");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
        }
    }

    @DeleteMapping("/deleteclient/{id}")
    public ResponseEntity<?> delete(@RequestBody Client client,@PathVariable Long id) {
        if (clientService.existById(id))
        {
            clientService.delete(id);
            HashMap<Client,String> map=new HashMap<>();
            map.put(client,"deleted with success");
            return
                    ResponseEntity.ok().body(map);
        }
        else{
            HashMap<String,String>map=new HashMap<>();
            map.put("error","in deleting");
            return    ResponseEntity.ok().body(map);

        }
    }



}
