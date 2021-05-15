package com.softwarestudiogroup1.uts.eRestaurant.models.entities;

import javax.persistence.MappedSuperclass;

import javax.persistence.Column;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;

/**
 * Super Class for System User: Manager, Staff
 */
@MappedSuperclass
public class SystemUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username")
    @NotEmpty
    private String username;

    @Column(name = "password")
    @NotEmpty
    private String password;

    @Column(name = "email")
    @NotEmpty 
    private String email;
    
    @Column(name = "first_name")
	@NotEmpty
	private String firstName;

	@Column(name = "last_name")
	@NotEmpty
	private String lastName;

    @Column(name = "telephone")
	@NotEmpty
	@Digits(fraction = 0, integer = 10)
	private String telephone;
    
    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getLastName() {
        return lastName;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    
    public String getTelephone() {
        return telephone;
    }

    public boolean isNew() {
        return this.id == null;
    }
}
