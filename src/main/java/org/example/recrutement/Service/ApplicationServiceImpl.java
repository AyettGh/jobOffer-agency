package org.example.recrutement.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.example.recrutement.Entity.Application;
import org.example.recrutement.Entity.Client;
import org.example.recrutement.Entity.Enum.StatusApplicationEnum;
import org.example.recrutement.Entity.Offer;
import org.example.recrutement.Entity.User;
import org.example.recrutement.Repository.ApplicationRepository;
import org.example.recrutement.Repository.ClientRepository;
import org.example.recrutement.Repository.OfferRepositroy;
import org.example.recrutement.Service.ServiceImpl.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ApplicationServiceImpl implements ApplicationService {
    @Autowired
    private ApplicationRepository applicationRepository;
    @Override
    public List<Application> all() {
        return applicationRepository.findAll();
    }

    @Override
    public Application add(Application application) {
        return applicationRepository.save(application);
    }

    @Override
    public Application getById(Long id) {
        return applicationRepository.findById(id).orElseThrow(
                ()->new EntityNotFoundException("not found")
        );
    }

    @Override
    public void delete(Long id) {
                applicationRepository.deleteById(id);
    }

    @Override
    public Boolean existById(Long id) {
        return applicationRepository.existsById(id);
    }


    @Autowired
    private OfferRepositroy offerRepositroy;
    @Autowired
    private ClientRepository clientRepository;
    @Override
    public Application assignOfferToApplication(Long idoffer, Long idapplication) {
        Offer offer=offerRepositroy.findById(idoffer).orElseThrow(
                ()->new EntityNotFoundException("not found")
        );
        Application application=applicationRepository.findById(idapplication).orElseThrow(
                ()->new EntityNotFoundException("not found")
        );
        application.setOffer(offer);
        return applicationRepository.save(application);
    }

    @Override
    public Application assignClientToApplication(Long idclient, Long idapplication) {

        Client client=clientRepository.findById(idclient).orElseThrow(
                ()->new EntityNotFoundException("not found")
        );
        Application application=applicationRepository.findById(idapplication).orElseThrow(
                ()->new EntityNotFoundException("not found")
        );
        application.setClient(client);
        return applicationRepository.save(application);

    }

    @Transactional
    public Application updateApplicationStatus(Long id, StatusApplicationEnum newStatus) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Application not found"));

        if (!application.getStatus().isValidTransition(newStatus)) {
            throw new IllegalStateException(
                    String.format("Cannot change status from %s to %s",
                            application.getStatus(),
                            newStatus)
            );
        }

        application.setStatus(newStatus);
        application.setUpdatedAt(LocalDateTime.now());
        return applicationRepository.save(application);
    }

    @Override
    public Object count() {
        return applicationRepository.count();
    }




}
