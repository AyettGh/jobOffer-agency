package org.example.recrutement.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;

@Entity
public class Category implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String text;

    @OneToMany(mappedBy="category",cascade = CascadeType.ALL)
    @JsonBackReference("category-offer")
    private List<Offer> offer;

    public Category(String name, String text, List<Offer> offer) {
        this.name = name;
        this.text = text;
        this.offer = offer;
    }

    public Category() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Offer> getOffer() {
        return offer;
    }

    public void setOffer(List<Offer> offer) {
        this.offer = offer;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", text='" + text + '\'' +
                ", offer=" + offer +
                '}';
    }
}
