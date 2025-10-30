package com.gdu.wacdo.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.util.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

@Entity
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String name;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL)
    private List<Affectation> affectations;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
	private Date createdAt ;
	
    @Column(name = "updated_at")
	private Date updatedAt;


    public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
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

    public List<Affectation> getAffectations() {
        return affectations;
    }

    public void setAffectations(List<Affectation> affectations) {
        this.affectations = affectations;
    }
}