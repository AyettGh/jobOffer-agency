package org.example.recrutement.Entity;

import jakarta.persistence.*;
import org.example.recrutement.Entity.Enum.RoleEnum;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "\"user\"")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private String username;
    @Enumerated(EnumType.STRING)
    private RoleEnum role;
    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime lastLogin;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "agency_id")
    private Agency agency;


    public User(String email, String password, String username, RoleEnum role, LocalDateTime createdAt, LocalDateTime lastLogin, Client client) {
        this.email = email;
        this.password = password;
        this.username=username;
        this.role = role;
        this.createdAt = createdAt;
        this.lastLogin = lastLogin;
        this.client = client;
    }

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public RoleEnum getRole() {
        return role;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Agency getAgency() {
        return agency;
    }

    public void setAgency(Agency agency) {
        this.agency = agency;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", username='" + username + '\'' +
                ", role=" + role +
                ", createdAt=" + createdAt +
                ", lastLogin=" + lastLogin +
                ", client=" + client +
                '}';
    }
}
