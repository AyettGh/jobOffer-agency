package org.example.recrutement.Service.ServiceImpl;

import org.example.recrutement.Entity.Agency;
import org.example.recrutement.Entity.Application;
import org.example.recrutement.Entity.Enum.StatusApplicationEnum;
import org.example.recrutement.Entity.Offer;
import org.example.recrutement.Entity.User;

import java.util.List;

public interface ApplicationService {
    public List<Application> all();
    public Application add(Application application);
    public Application getById(Long id);
    public void delete(Long id);
    public Boolean existById(Long id);
    public Application assignOfferToApplication(Long idoffer,Long idapplication);
    public Application assignClientToApplication(Long idclient,Long idapplication);


    Application updateApplicationStatus(Long id, StatusApplicationEnum newStatus);

    Object count();

}
