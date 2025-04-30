package org.example.recrutement.Repository;

import org.example.recrutement.Entity.Agency;
import org.example.recrutement.Entity.Enum.StatusEnum;
import org.example.recrutement.Entity.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OfferRepositroy extends JpaRepository<Offer,Long> {
     List<Offer> findByExpiryDateBeforeAndStatusNot(LocalDateTime expiryDate, StatusEnum Enum);
    List<Offer> findByAgencyId(Long agencyId);
    List<Offer> findByAgency(Agency agency);

}
