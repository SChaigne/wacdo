package com.gdu.wacdo.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

@Entity
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String name;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL)
    private List<Affectation> affectations;

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

    public List<Affectation> getAffectations() {
        return affectations;
    }

    public void setAffectations(List<Affectation> affectations) {
        this.affectations = affectations;
    }
}