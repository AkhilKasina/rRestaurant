package com.softwarestudiogroup1.uts.eRestaurant.models;

import com.softwarestudiogroup1.uts.eRestaurant.models.entities.Item;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Integer> {

}
