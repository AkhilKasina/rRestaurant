package com.softwarestudiogroup1.uts.eRestaurant.models;

import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import com.softwarestudiogroup1.uts.eRestaurant.models.entities.Booking;
import com.softwarestudiogroup1.uts.eRestaurant.models.entities.BookingItem;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingItemRepository extends JpaRepository<BookingItem, Integer> {
    public BookingItem findByBookingId(int ID);
}
