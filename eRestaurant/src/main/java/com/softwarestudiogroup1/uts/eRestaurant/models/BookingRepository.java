package com.softwarestudiogroup1.uts.eRestaurant.models;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import com.softwarestudiogroup1.uts.eRestaurant.models.entities.Booking;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    
    public List<Booking> findAllByOrderByBookingDateTimeAsc();
}
