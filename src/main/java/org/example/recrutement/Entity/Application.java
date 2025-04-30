package org.example.recrutement.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import org.example.recrutement.Entity.Enum.StatusApplicationEnum;

import java.io.Serializable;
import java.net.URL;
import java.time.LocalDateTime;

@Entity
public class Application implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Lob
    @Column(name = "cv_pdf")
    private byte[] cvPdf;

    @Column(name = "cv_file_name")
    private String cvFileName;
    private StatusApplicationEnum status;
    @Column(updatable = false)
    private LocalDateTime submittedAt;

    private LocalDateTime updatedAt;

    @ManyToOne
    @JsonBackReference("agency-offer")
    private Offer offer;

    @ManyToOne
    @JsonBackReference("client-application")
    private Client client;

    public Application() {
    }

    public Application(byte[] cvPdf, String cvFileName,  StatusApplicationEnum status, LocalDateTime submittedAt, LocalDateTime updatedAt, Offer offer, Client client) {
        this.cvPdf = cvPdf;
        this.cvFileName = cvFileName;
        this.status = status;
        this.submittedAt = submittedAt;
        this.updatedAt = updatedAt;
        this.offer = offer;
        this.client = client;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public byte[] getCvPdf() {
        return cvPdf;
    }

    public void setCvPdf(byte[] cvPdf) {
        this.cvPdf = cvPdf;
    }

    public String getCvFileName() {
        return cvFileName;
    }

    public void setCvFileName(String cvFileName) {
        this.cvFileName = cvFileName;
    }



    public StatusApplicationEnum getStatus() {
        return status;
    }

    public void setStatus(StatusApplicationEnum status) {
        this.status = status;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public String toString() {
        return "Application{" +
                "id=" + id +
                ", cvURl=" + cvPdf + '\'' +
                ", status=" + status +
                ", submittedAt=" + submittedAt +
                ", updatedAt=" + updatedAt +
                ", offer=" + offer +
                ", client=" + client +
                '}';
    }
}
