package org.example.recrutement.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.recrutement.Entity.Agency;
import org.example.recrutement.Entity.Application;
import org.example.recrutement.Entity.Category;
import org.example.recrutement.Entity.Offer;
import org.example.recrutement.Service.ServiceImpl.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/recrutement")
public class OfferController {
    @Autowired
    private OfferService offerService;
    @GetMapping("/alloffer")
    public List<Offer> all()
    {
        return offerService.all();
    }
    @GetMapping("/getoffer/{id}")
    public Offer getById(@PathVariable Long id)
    {
        return offerService.getById(id);
    }
    @PostMapping("/addoffer")
    public Offer save(@RequestBody Offer offer)
    {
        return offerService.add(offer);
    }

    @PutMapping("/updateoffer/{id}")
    public ResponseEntity<?> update(@RequestBody Offer offer, @PathVariable Long id)
    {
        if( offerService.existById(id))
        {
            Offer offer1=offerService.getById(id);
            offer1.setAgency(offer.getAgency());
            offer1.setApplication(offer.getApplication());
            offer1.setCategory(offer.getCategory());
            offer1.setCompanyName(offer.getCompanyName());
            offer1.setDescription(offer.getDescription());
            offer1.setCreatedAt(offer.getCreatedAt());
            offer1.setExpiryDate(offer.getExpiryDate());
            offer1.setImage(offer.getImage());
            offer1.setLocation(offer.getLocation());
            offer1.setSalary(offer.getSalary());
            offer1.setStatus(offer.getStatus());
            offer1.setTitle(offer.getTitle());
            offerService.add(offer1);
            return ResponseEntity.ok().body(offer1);
        }
        else {
            HashMap<String,String> map=new HashMap<>();
            map.put("error","error");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
        }
    }

    @DeleteMapping("/deleteoffer/{id}")
    public ResponseEntity<?> delete(@RequestBody Offer offer,@PathVariable Long id) {
        if (offerService.existById(id))
        {
            offerService.delete(id);
            HashMap<Offer,String> map=new HashMap<>();
            map.put(offer,"deleted with success");
            return
                    ResponseEntity.ok().body(map);
        }
        else{
            HashMap<String,String>map=new HashMap<>();
            map.put("error","in deleting");
            return    ResponseEntity.ok().body(map);

        }
    }
    @PutMapping("/assignAgencyToOffer/{idagency}/{idoffer}")
    public ResponseEntity<Offer> assignAgencyToOffer( @PathVariable Long idagency,@PathVariable Long idoffer)
    {
        Offer offer=offerService.assignAgencyToOffer(idagency,idoffer);
        return ResponseEntity.ok(offer);
    }
    @PutMapping("/assignCategoryToOffer/{idcategory}/{idoffer}")
    public ResponseEntity<Offer> assignCategoryToOffer( @PathVariable Long idcategory,@PathVariable Long idoffer)
    {
        Offer offer=offerService.assignCategoryToOffer(idcategory,idoffer);
        return ResponseEntity.ok(offer);
    }

    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addOfferWithImage(@RequestPart("offer") String offerJson,
                                                 @RequestPart("image") MultipartFile imageFile) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Offer offer = objectMapper.readValue(offerJson, Offer.class);
            offer.setImage(imageFile.getBytes());

            Offer savedOffer = offerService.add(offer);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedOffer);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading image: " + e.getMessage());
        }
    }

    @GetMapping("/offertimage/{id}")
    public ResponseEntity<?> getOfferImage(@PathVariable Long id) {
        Optional<Offer> optionalOffer = Optional.ofNullable(offerService.getById(id));
        if (optionalOffer.isPresent()) {
            byte[] imageBytes = optionalOffer.get().getImage();
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
            return ResponseEntity.ok().body(base64Image);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Offer not found");
        }
    }
    @GetMapping("/offers/agency/{agencyId}")
    public ResponseEntity<List<Offer>> getOffersByAgency(@PathVariable Long agencyId) {
        List<Offer> offers = offerService.findByAgencyId(agencyId);
        return ResponseEntity.ok(offers);
    }
}
