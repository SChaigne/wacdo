package com.gdu.wacdo.models;

import jakarta.validation.constraints.NotEmpty;

public class RestaurantDto {
	@NotEmpty(message = "The name is required")
	private String name;

	@NotEmpty(message = "The adress is required")
	private String adress;

	@NotEmpty(message = "The zipCode is required")
	private String zipCode;
	
	@NotEmpty(message= "The city is required")
	private String city;

	public String getName() {
		return name;
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
}
