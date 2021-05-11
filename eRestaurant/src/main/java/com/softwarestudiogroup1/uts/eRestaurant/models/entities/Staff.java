package com.softwarestudiogroup1.uts.eRestaurant.models.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "staffs")
public class Staff extends SystemUser {
    
    @Column(name = "description")
    @NotEmpty
    private String description;

    @Column(name = "date_of_birth")
    @NotEmpty
    private Date dateOfBirth;

    @Column(name = "hourly_wage")
    @NotEmpty
    private double hourlyWage;


    public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

    public double getHourlyWage() {
		return this.hourlyWage;
	}

	public void setHourlyWage(double hourlyWage) {
		this.hourlyWage = hourlyWage;
	}


    public @NotEmpty Date getDateOfBirth() {
		return this.dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;

	}
}
