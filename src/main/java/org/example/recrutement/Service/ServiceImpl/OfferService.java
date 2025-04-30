package org.example.recrutement.Service.ServiceImpl;

import org.example.recrutement.Entity.Agency;
import org.example.recrutement.Entity.Client;
import org.example.recrutement.Entity.Offer;

import java.util.List;

public interface OfferService {
    public List<Offer> all();
    public Offer add(Offer offer);
    public Offer getById(Long id);
    public void delete(Long id);
    public Boolean existById(Long id);
    public Offer assignAgencyToOffer(Long idagency, Long idoffer);
    public Offer assignCategoryToOffer(Long idcategory,Long idoffer);

    Object count();

    List<Offer> findByAgency(Agency agency);

    List<Offer> findByAgencyId(Long agencyId);
}
