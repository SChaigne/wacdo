package com.gdu.wacdo.models;

import java.util.Date;

import jakarta.persistence.*;

@Entity
public class Affectation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "collaborator_id")
    private Collaborator collaborator;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

    private Date startDate;

    private Date endDate; // nullable if assignment is active

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Collaborator getCollaborator() { return collaborator; }
    public void setCollaborator(Collaborator collaborator) { this.collaborator = collaborator; }

    public Restaurant getRestaurant() { return restaurant; }
    public void setRestaurant(Restaurant restaurant) { this.restaurant = restaurant; }

    public Job getFunction() { return job; }
    public void setFunction(Job function) { this.job = function; }

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }
}