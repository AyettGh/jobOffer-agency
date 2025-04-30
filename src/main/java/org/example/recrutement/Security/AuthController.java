package org.example.recrutement.Security;

import org.example.recrutement.DTO.*;
import org.example.recrutement.Entity.*;
import org.example.recrutement.Repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepo;
    private final AgencyRepository agencyRepo;
    private final ClientRepository clientRepo;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authManager, JwtUtil jwtUtil,
                          UserRepository userRepo, AgencyRepository agencyRepo,
                          ClientRepository clientRepo, PasswordEncoder passwordEncoder) {
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
        this.userRepo = userRepo;
        this.agencyRepo = agencyRepo;
        this.clientRepo = clientRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        User user = userRepo.findByEmail(request.email());
        user.setLastLogin(LocalDateTime.now());
        userRepo.save(user);

        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String token = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthResponse(token, user.getEmail(), user.getRole().name()));
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        if (userRepo.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body("Email already exists");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setUsername(request.getUsername());
        user.setRole(request.getRole());
        user.setCreatedAt(LocalDateTime.now());

        switch (request.getRole()) {
            case AGENCY -> {
                userRepo.save(user);
                Agency agency = new Agency();
                agency.setCompanyName(request.getCompanyName());
                agency.setContactPerson(request.getContactPerson());
                agency.setEmail(user.getEmail());
                agency.setNumber(request.getPhoneNumber());
                agency.setAddress(request.getAddress());
                agencyRepo.save(agency);
            }
            case CLIENT -> {
                Client client = new Client();
                client.setBio(request.getBio());
                client.setSkills(request.getSkills());
                client.setExperience(request.getExperience());
                client.setEducation(request.getEducation());
                client.setPhoneNumber(request.getPhoneNumber());
                client.setUser(user);
                user.setClient(client);
                userRepo.save(user);
            }
            default -> userRepo.save(user);
        }

        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok("Logged out successfully");
    }
}