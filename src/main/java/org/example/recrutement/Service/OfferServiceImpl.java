package org.example.recrutement.Service;

import jakarta.persistence.EntityNotFoundException;
import org.example.recrutement.Entity.Agency;
import org.example.recrutement.Entity.Category;
import org.example.recrutement.Entity.Offer;
import org.example.recrutement.Repository.AgencyRepository;
import org.example.recrutement.Repository.CategoryRepository;
import org.example.recrutement.Repository.OfferRepositroy;
import org.example.recrutement.Service.ServiceImpl.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OfferServiceImpl implements OfferService {
    @Autowired
    private OfferRepositroy offerRepositroy;
    @Override
    public List<Offer> all() {
        return offerRepositroy.findAll();
    }

    @Override
    public Offer add(Offer offer) {
        return offerRepositroy.save(offer);
    }

    @Override
    public Offer getById(Long id) {
        return offerRepositroy.findById(id).orElseThrow(
                ()->new EntityNotFoundException("not found")
        );
    }

    @Override
    public void delete(Long id) {
            offerRepositroy.deleteById(id);
    }

    @Override
    public Boolean existById(Long id) {
        return offerRepositroy.existsById(id);
    }


    @Autowired
    private AgencyRepository agencyRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Override
    public Offer assignAgencyToOffer(Long idagency, Long idoffer) {
        Offer offer=offerRepositroy.findById(idoffer).orElseThrow(
                ()->new EntityNotFoundException("not found")
        );
        Agency agency=agencyRepository.findById(idagency).orElseThrow(
                ()->new EntityNotFoundException("not found")
        );
        offer.setAgency(agency);
        return offerRepositroy.save(offer);
    }

    @Override
    public Offer assignCategoryToOffer(Long idcategory, Long idoffer) {

        Category category=categoryRepository.findById(idcategory).orElseThrow(
                ()->new EntityNotFoundException("not found")

        );
        Offer offer=offerRepositroy.findById(idoffer).orElseThrow(
                ()->new EntityNotFoundException("not found")
        );
        offer.setCategory(category);
        return offerRepositroy.save(offer);
    }

    @Override
    public Object count() {
        return offerRepositroy.count();
    }

    @Override
    public List<Offer> findByAgency(Agency agency) {
        return offerRepositroy.findByAgency(agency);
    }

    @Override
    public List<Offer> findByAgencyId(Long agencyId){
        return offerRepositroy.findByAgencyId(agencyId);
    }


}
