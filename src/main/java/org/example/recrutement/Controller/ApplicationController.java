package org.example.recrutement.Controller;
import org.example.recrutement.Entity.Application;
import org.example.recrutement.Entity.Enum.StatusApplicationEnum;
import org.example.recrutement.Service.ServiceImpl.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/recrutement")
public class ApplicationController {
    @Autowired
    private ApplicationService applicationService;
    @GetMapping("/allapplication")
    public List<Application> all()
    {
        return applicationService.all();
    }
    @GetMapping("/getapplication/{id}")
    public Application getById(@PathVariable Long id)
    {
        return applicationService.getById(id);
    }
    @PostMapping("/addapplication")
    public Application save(@RequestBody Application application)
    {
        return applicationService.add(application);
    }

    @PutMapping("/updateapplication/{id}")
    public ResponseEntity<?> update(@RequestBody Application application, @PathVariable Long id)
    {
        if( applicationService.existById(id))
        {
            Application application1=applicationService.getById(id);
            application1.setClient(application.getClient());
            application1.setOffer(application.getOffer());
            application1.setUpdatedAt(application.getUpdatedAt());
            application1.setStatus(application.getStatus());
            application1.setCvPdf(application.getCvPdf());
            application1.setCvFileName(application.getCvFileName());
            application1.setSubmittedAt(application.getSubmittedAt());
            applicationService.add(application1);
            return ResponseEntity.ok().body(application1);
        }
        else {
            HashMap<String,String> map=new HashMap<>();
            map.put("error","error");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
        }
    }

    @DeleteMapping("/deleteapplication/{id}")
    public ResponseEntity<?> delete(@RequestBody Application application,@PathVariable Long id) {
        if (applicationService.existById(id))
        {
            applicationService.delete(id);
            HashMap<Application,String> map=new HashMap<>();
            map.put(application,"deleted with success");
            return
                    ResponseEntity.ok().body(map);
        }
        else{
            HashMap<String,String>map=new HashMap<>();
            map.put("error","in deleting");
            return    ResponseEntity.ok().body(map);

        }
    }
    @PutMapping("/assignOfferToApplication/{idoffer}/{idapplication}")
    public ResponseEntity<Application> assignOfferToApplication(@PathVariable Long idoffer,@PathVariable Long idapplication)
    {
        Application application=applicationService.assignOfferToApplication(idoffer, idapplication);
        return ResponseEntity.ok(application);
    }
    @PutMapping("assignClientToApplication/{idclient}/{idapplication}")
    public ResponseEntity<Application> assignClientToApplication(@PathVariable Long idclient,@PathVariable Long idapplication)
    {
        Application application=applicationService.assignClientToApplication(idclient, idapplication);
        return ResponseEntity.ok(application);
    }


    @PutMapping("/applications/{id}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long id,
            @RequestParam StatusApplicationEnum newStatus) {

        try {
            Application updated = applicationService.updateApplicationStatus(id, newStatus);
            return ResponseEntity.ok(updated);
        } catch (IllegalStateException e) {
            HashMap<String,String>map=new HashMap<>();
            map.put("error","in updating status");
            return    ResponseEntity.ok().body(map);
        }
    }
    @GetMapping("/applications/cv/{id}")
    public ResponseEntity<byte[]> downloadCv(@PathVariable Long id) {
        Application application = applicationService.getById(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", application.getCvFileName());
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        return new ResponseEntity<>(application.getCvPdf(), headers, HttpStatus.OK);
    }
}
