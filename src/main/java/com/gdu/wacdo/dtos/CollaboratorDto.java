package com.gdu.wacdo.dtos;

import jakarta.validation.constraints.NotEmpty;

import java.util.Date;

public class CollaboratorDto {
	@NotEmpty(message = "The first is required")
	private String firstName;

	@NotEmpty(message = "The lastName is required")
	private String lastName;

	@NotEmpty(message = "The email is required")
	private String email;

    private Date hireDate;


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

    public Date getHireDate() { return hireDate; }

    public void setHireDate(Date hireDate) { this.hireDate = hireDate; }
}
