package org.example.recrutement.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;

@Entity
public class Agency implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String companyName;
    private String contactPerson;
    private String email;
    private String number;
    private String address;
    private Boolean isVerified;
    @OneToMany(mappedBy = "agency",cascade = CascadeType.ALL)
    @JsonBackReference("agency-offer")
    private List<Offer> offer;

    @OneToOne(mappedBy = "agency", cascade = CascadeType.ALL)
    private User user;

    public Agency() {
    }

    public Agency(String companyName, String contactPerson, String email, String number, String address, Boolean isVerified, List<Offer> offer) {
        this.companyName = companyName;
        this.contactPerson = contactPerson;
        this.email = email;
        this.number = number;
        this.address = address;
        this.isVerified = isVerified;
        this.offer = offer;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        number = number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getVerified() {
        return isVerified;
    }

    public void setVerified(Boolean verified) {
        isVerified = verified;
    }

    public List<Offer> getOffer() {
        return offer;
    }

    public void setOffer(List<Offer> offer) {
        this.offer = offer;
    }

    @Override
    public String toString() {
        return "Agency{" +
                "id=" + id +
                ", companyName='" + companyName + '\'' +
                ", contactPerson='" + contactPerson + '\'' +
                ", email='" + email + '\'' +
                ", Number='" + number + '\'' +
                ", address='" + address + '\'' +
                ", isVerified=" + isVerified +
                ", offer=" + offer +
                '}';
    }
}
