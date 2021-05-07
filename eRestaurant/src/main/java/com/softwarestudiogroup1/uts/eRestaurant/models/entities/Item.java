package com.softwarestudiogroup1.uts.eRestaurant.models.entities;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;


@Entity
@Table(name= "items")
public class Item{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    @NotEmpty
    private String name;

    @Column(name = "price")
    @NotNull
    private double price;

    @Column(name = "menu_type")
    private String menuType;

	@Column(name = "description")
	private String description;

	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "item", fetch = FetchType.EAGER)
	private Set<BookingItem> bookings;

    public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public boolean isNew() {
		return this.id == null;
	}

    public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

    public double getPrice() {
		return this.price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

    public String getMenuType() {
		return this.menuType;
	}

	public void setMenuType(String menuType) {
		this.menuType = menuType;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	protected Set<BookingItem> getBookingItemsInternal() {
		if (this.bookings == null) {
			this.bookings = new HashSet<>();
		}
		return this.bookings;
	}

	protected void setBookingItemsInteral(Set<BookingItem> bookings) {
		this.bookings = bookings;
	}

	public List<BookingItem> getBookingItems() {
		List<BookingItem> sortedBookingItemss = new ArrayList<>(getBookingItemsInternal());
		PropertyComparator.sort(sortedBookingItemss, new MutableSortDefinition("id", true, true));

		return Collections.unmodifiableList(sortedBookingItemss);
	}



	public void addBookingItem(BookingItem bookingItem) {
		if (bookingItem.getId() == null) {
			getBookingItemsInternal().add(bookingItem);
		}
		
		bookingItem.setItem(this);
	}
}
