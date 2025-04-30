package org.example.recrutement.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import org.example.recrutement.Entity.Enum.StatusEnum;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Entity
public class Offer implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Title is required")

    private String title;
    private String description;
    private String companyName;
    private String location;

    private Float salary;
    private StatusEnum status;
   private LocalDateTime createdAt;

    private LocalDateTime expiryDate;
    @Lob
    private byte[] image;

    @ManyToOne
    @JsonBackReference("agency-offer")
    private Agency agency;

    @OneToMany(mappedBy = "offer",cascade = CascadeType.ALL)
    @JsonBackReference("offer-application")
    private List<Application> application;

    @ManyToOne
    @JsonBackReference("category-offer")
    private Category category;

    public Offer(String title, String description, String companyName, String location, Float salary, StatusEnum status, LocalDateTime createdAt, LocalDateTime expiryDate, byte[] image, Agency agency, List<Application> application, Category category) {
        this.title = title;
        this.description = description;
        this.companyName = companyName;
        this.location = location;
        this.salary = salary;
        this.status = status;
        this.createdAt = createdAt;
        this.expiryDate = expiryDate;
        this.image = image;
        this.agency = agency;
        this.application = application;
        this.category = category;
    }

    public Offer() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Float getSalary() {
        return salary;
    }

    public void setSalary(Float salary) {
        this.salary = salary;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Agency getAgency() {
        return agency;
    }

    public void setAgency(Agency agency) {
        this.agency = agency;
    }

    public List<Application> getApplication() {
        return application;
    }

    public void setApplication(List<Application> application) {
        this.application = application;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Offer{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", companyName='" + companyName + '\'' +
                ", location='" + location + '\'' +
                ", salary=" + salary +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", expiryDate=" + expiryDate +
                ", image=" + Arrays.toString(image) +
                ", agency=" + agency +
                ", application=" + application +
                ", category=" + category +
                '}';
    }
}
