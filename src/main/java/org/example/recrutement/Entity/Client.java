package org.example.recrutement.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;

@Entity
public class Client implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String bio;
    private String skills ;
    private String experience;
    private String education;
    private String phoneNumber;


    @OneToOne(mappedBy = "client", cascade = CascadeType.ALL)
    private User user;


    @OneToMany(mappedBy = "client",cascade = CascadeType.ALL)
    @JsonBackReference("client-application")
    private List<Application> applications;

    public Client() {
    }

    public Client(String bio, String skills, String experience, String education, String phoneNumber, User user, List<Application> applications) {
        this.bio = bio;
        this.skills = skills;
        this.experience = experience;
        this.education = education;
        this.phoneNumber = phoneNumber;
        this.user = user;
        this.applications = applications;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Application> getApplications() {
        return applications;
    }

    public void setApplications(List<Application> applications) {
        this.applications = applications;
    }



    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", bio='" + bio + '\'' +
                ", skills='" + skills + '\'' +
                ", experience='" + experience + '\'' +
                ", education='" + education + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", user=" + user +
                ", applications=" + applications +
                '}';
    }
}
