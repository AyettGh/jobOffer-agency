package org.example.recrutement.Service;

import jakarta.persistence.EntityNotFoundException;
import org.example.recrutement.Entity.Agency;
import org.example.recrutement.Repository.AgencyRepository;
import org.example.recrutement.Service.ServiceImpl.AgencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgencyServiceImpl implements AgencyService {
    @Autowired
    private AgencyRepository agencyRepository;
    @Override
    public List<Agency> all() {
        return agencyRepository.findAll();
    }

    @Override
    public Agency add(Agency agency) {
        return agencyRepository.save(agency);
    }

    @Override
    public Agency getById(Long id) {
        return agencyRepository.findById(id).orElseThrow(
                ()->new EntityNotFoundException("not found")
        );
    }

    @Override
    public void delete(Long id) {
          agencyRepository.deleteById(id);
    }

    @Override
    public Boolean existById(Long id) {
        return agencyRepository.existsById(id);
    }

    @Override
    public Object count() {
        return agencyRepository.count();
    }

    @Override
    public void deleteAll() {
        agencyRepository.deleteAll();
    }
}
