package org.example.recrutement.Service.ServiceImpl;

import org.example.recrutement.DTO.RegisterRequest;
import org.example.recrutement.Entity.Application;
import org.example.recrutement.Entity.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserService {
    public List<User> all();
    public User add(User user);
    public User getById(Long id);
    public void delete(Long id);
    public Boolean existById(Long id);
    public User assignClientToUser(Long idclient, Long iduser);

    User assignAgencyToUser(Long idagency, Long iduser);

    public User findByEmail(String email);
    public boolean existsByEmail(String email);


    UserDetails loadUserByUsername(String username);

    public User registerUser(RegisterRequest request);


    Object count();
}
