package org.example.recrutement.Controller;

import org.example.recrutement.Entity.Agency;
import org.example.recrutement.Entity.Offer;
import org.example.recrutement.Service.ServiceImpl.AgencyService;
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
public class AgencyController {
    @Autowired
    private AgencyService agencyService;

    @GetMapping("/allagency")
    public List<Agency> all()
    {
        return agencyService.all();
    }
    @GetMapping("/getagency/{id}")
   public Agency getById(@PathVariable Long id)
    {
        return agencyService.getById(id);
    }
    @PostMapping("/addagency")
    public Agency save(@RequestBody Agency agency)
    {
        return agencyService.add(agency);
    }

    @PutMapping("/updateagency/{id}")
    public ResponseEntity<?> update(@RequestBody Agency agency,@PathVariable Long id)
    {
        if( agencyService.existById(id))
        {
            Agency agency1=agencyService.getById(id);
            agency1.setAddress(agency.getAddress());
            agency1.setCompanyName(agency.getCompanyName());
            agency1.setEmail(agency.getEmail());
            agency1.setNumber(agency.getNumber());
            agency1.setContactPerson(agency.getContactPerson());
            agency1.setOffer(agency.getOffer());
            agency1.setVerified(agency.getVerified());
            agencyService.add(agency1);
            return ResponseEntity.ok().body(agency1);
        }
        else {
            HashMap<String,String> map=new HashMap<>();
            map.put("error","error");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
        }
    }

    @DeleteMapping("/deleteagency/{id}")
    public ResponseEntity<?> delete(@RequestBody Agency agency,@PathVariable Long id) {
        if (agencyService.existById(id))
        {
            agencyService.delete(id);
            HashMap<Agency,String> map=new HashMap<>();
            map.put(agency,"deleted with success");
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
