package com.gdu.wacdo.dtos;

import jakarta.validation.constraints.NotEmpty;

public class CollaboratorDto {
	@NotEmpty(message = "The first is required")
	private String firstName;

	@NotEmpty(message = "The lastName is required")
	private String lastName;

	@NotEmpty(message = "The email is required")
	private String email;


	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
