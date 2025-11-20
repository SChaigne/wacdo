package com.gdu.wacdo.models;

import java.util.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;

@Entity
@Table(name = "restaurants")
public class Restaurant {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;
	private String adress;
	private String zipCode;
	private String city;

	@CreationTimestamp
	@Column(name = "created_at", updatable = false)
	private Date createdAt;

	@OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
	private List<Affectation> affectations;

	public Restaurant() {}

	public Restaurant(String name, String adress, String zipCode, String city) {
		this.name = name;
		this.adress = adress;
		this.zipCode = zipCode;
		this.city = city;
	}

	public String getName() {
		return name;
	}

	public Long getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAdress() {
		return adress;
	}

	public void setAdress(String adress) {
		this.adress = adress;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public List<Affectation> getAffectations() {
		return affectations;
	}

	public void setAffectations(List<Affectation> affectations) {
		this.affectations = affectations;
	}

	@Override
	public String toString() {
		return "Restaurant{" + "nom='" + name + '\'' + ", adresse='" + adress + '\'' + ", codePostal='" + zipCode + '\''
				+ ", ville='" + city + '\'' + '}';
	}

	public void setId(Long id) {
		this.id = id;
	}
}
