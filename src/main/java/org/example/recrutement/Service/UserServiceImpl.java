package org.example.recrutement.Service;

import jakarta.persistence.EntityNotFoundException;
import org.example.recrutement.DTO.RegisterRequest;
import org.example.recrutement.DTO.UserRegisteredEvent;
import org.example.recrutement.Entity.Agency;
import org.example.recrutement.Entity.Client;
import org.example.recrutement.Entity.Enum.RoleEnum;
import org.example.recrutement.Entity.User;
import org.example.recrutement.RabbitMQ.UserEventPublisher;
import org.example.recrutement.Repository.AgencyRepository;
import org.example.recrutement.Repository.ClientRepository;
import org.example.recrutement.Repository.UserRepository;
import org.example.recrutement.Service.ServiceImpl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AgencyRepository agencyRepository;
    private final ClientRepository clientRepository;
    private final UserEventPublisher userEventPublisher;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           AgencyRepository agencyRepository,
                           ClientRepository clientRepository,
                           UserEventPublisher userEventPublisher) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.agencyRepository = agencyRepository;
        this.clientRepository = clientRepository;
        this.userEventPublisher = userEventPublisher;
    }


    @Override
    public List<User> all() {
        return userRepository.findAll();
    }

    @Override
    public User add(User user) {
        return userRepository.save(user);
    }

    @Override
    public User getById(Long id) {
        return userRepository.findById(id).orElseThrow(
                ()->new EntityNotFoundException("not found")
        );
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public Boolean existById(Long id) {
        return userRepository.existsById(id);
    }

    @Override
    public User assignClientToUser(Long idclient, Long iduser) {

        Client client=clientRepository.findById(idclient).orElseThrow(
                ()->new EntityNotFoundException("not found")
        );
        User user=userRepository.findById(iduser).orElseThrow(
                ()->new EntityNotFoundException("not found")
        );
        user.setClient(client);
        return userRepository.save(user);
    }
    @Override
    public User assignAgencyToUser(Long idagency, Long iduser) {
        Agency agency=agencyRepository.findById(idagency).orElseThrow(
                ()->new EntityNotFoundException("not found")
        );
        User user=userRepository.findById(iduser).orElseThrow(
                ()->new EntityNotFoundException("not found")
        );
        user.setAgency(agency);
        return userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }


    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }


    @Override
    public User registerUser(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user.setCreatedAt(LocalDateTime.now());

        if (request.getRole() == RoleEnum.CLIENT) {
            Client client = new Client();
            client.setBio(request.getBio());
            client.setSkills(request.getSkills());
            client.setExperience(request.getExperience());
            client.setEducation(request.getEducation());
            client.setPhoneNumber(request.getPhoneNumber());
            client.setUser(user);
            user.setClient(client);
        }
        else if (request.getRole() == RoleEnum.AGENCY) {
            Agency agency = new Agency();
            agency.setCompanyName(request.getCompanyName());
            agency.setContactPerson(request.getContactPerson());
            agency.setAddress(request.getAddress());
            agency.setUser(user);
            user.setAgency(agency);
        }


        User savedUser =  userRepository.save(user);
        userEventPublisher.publishUserRegistration(
                new UserRegisteredEvent(
                        savedUser.getEmail(),
                        savedUser.getUsername()
                )
        );

        return savedUser;
    }



    @Override
    public Object count() {
        return userRepository.count();
    }

}
