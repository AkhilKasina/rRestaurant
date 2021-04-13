package com.softwarestudiogroup1.uts.eRestaurant.models;

import com.softwarestudiogroup1.uts.eRestaurant.models.entities.Customer;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

}
