package com.softwarestudiogroup1.uts.eRestaurant.models.entities;

import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.format.annotation.DateTimeFormat;


@Entity
@Table(name = "bookings")
public class Booking {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "booking_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date bookingDateTime;

    @Column(name = "booking_time")
    private String bookingDate;

    @Column(name = "booking_date")
    private String bookingTime;

    @Column(name = "table_position")
    private String tablePosition;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "booking", fetch = FetchType.EAGER)
    private Set<BookingItem> items;

    @OneToOne
    @JoinColumn(name = "reward_id")
    private Reward reward;

    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }

    public Date getBookingDateTime() {
        return bookingDateTime;
    }

    public void setBookingDateTime(Date bookingDateTime) {
        this.bookingDateTime = bookingDateTime;
    }

    public void setBookingTime(String time){
        this.bookingTime = time;
    }

    public String getBookingTime(){
        return bookingTime;
    }

    public void setBookingDate(String date){
        this.bookingDate = date;
    }

    public String getBookingDate(){
        return bookingDate;
    }

    public String getTablePosition() {
        return tablePosition;
    }

    public void setTablePosition(String tablePosition) {
        this.tablePosition = tablePosition;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Customer getCustomer() {
        return customer;
    }

    protected Set<BookingItem> getBookingItemsInternal() {
		if (this.items == null) {
			this.items = new HashSet<>();
		}
		return this.items;
	}

	protected void setBookingItemsInteral(Set<BookingItem> items) {
		this.items = items;
	}

	public List<BookingItem> getBookingItems() {
		List<BookingItem> sortedBookingItemss = new ArrayList<>(getBookingItemsInternal());
		PropertyComparator.sort(sortedBookingItemss, new MutableSortDefinition("id", true, true));

		return Collections.unmodifiableList(sortedBookingItemss);
	}

	public void addBookingItems(List<BookingItem> bookingItems) {
        Set<BookingItem> sets = new HashSet<>();
        
        for (BookingItem bookingItem: bookingItems) {
            sets.add(bookingItem);
            bookingItem.setBooking(this);
        }
		
        setBookingItemsInteral(sets);
	}

    public Reward getReward() {
		return this.reward;
	}

	public void setReward(Reward reward) {
		this.reward = reward;
	}


}
