package com.softwarestudiogroup1.uts.eRestaurant.models.entities;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;

@Entity // This tells Hibernate to make a table out of this class
@Table(name= "customers")
public class Customer {

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

    @Column(name = "username")
    @NotEmpty
    private String username;

    @Column(name = "password")
    @NotEmpty
    private String password;

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

    @Column(name = "address")
	private String address;

	@Column(name = "points")
	private Integer points;

	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "customer", fetch = FetchType.EAGER)
	private Set<Booking> bookings;

	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "customer", fetch = FetchType.EAGER)
	private Set<Reward> rewards;




	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public boolean isNew() {
		return this.id == null;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

    public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

    public String getTelephone() {
		return this.telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
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

	public Integer getPoints() {
		return this.points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	protected Set<Booking> getBookingsInternal() {
		if (this.bookings == null) {
			this.bookings = new HashSet<>();
		}
		return this.bookings;
	}

	protected void setBookingsInteral(Set<Booking> bookings) {
		this.bookings = bookings;
	}

	public List<Booking> getBookings() {
		List<Booking> sortedBookings = new ArrayList<>(getBookingsInternal());
		PropertyComparator.sort(sortedBookings, new MutableSortDefinition("id", true, true));

		return Collections.unmodifiableList(sortedBookings);
	}

	public void addBooking(Booking booking) {
		if (booking.getId() == null) {
			getBookingsInternal().add(booking);
		}
		
		booking.setCustomer(this);
	}

	public void set(String firstName, String lastName, String telephone, String address, String username, String password ) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.telephone = telephone;
		this.address = address;
		this.username = username;
		this.password = password;
	}

	// protected Set<Rewards> getRewardsInternal() {
	// 	if (this.rewards == null) {
	// 		this.rewards = new HashSet<>();
	// 	}
	// 	return this.rewards;
	// }

	// protected void setRewardsInteral(Set<Rewards> rewards) {
	// 	this.rewards = rewards;
	// }

	// public List<Rewards> getRewards() {
	// 	List<Rewards> sortedRewards = new ArrayList<>(getRewardsInternal());
	// 	PropertyComparator.sort(sortedRewards, new MutableSortDefinition("id",1 true, true));

	// 	return Collections.unmodifiableList(sortedRewards);
	// }

	public Set<Reward> getRewards() {
		return this.rewards;
	}

	public void setRewards(Set<Reward> rewards) {
		this.rewards = rewards;
	}

}