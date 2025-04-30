package org.example.recrutement.Service.ServiceImpl;

import org.example.recrutement.Entity.Agency;

import java.util.List;

public interface AgencyService {
    public List<Agency> all();
    public Agency add(Agency agency);
    public Agency getById(Long id);
    public void delete(Long id);
    public Boolean existById(Long id);

    Object count();

    void deleteAll();
}
