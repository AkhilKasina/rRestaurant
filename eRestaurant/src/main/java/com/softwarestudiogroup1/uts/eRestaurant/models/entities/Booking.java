package com.softwarestudiogroup1.uts.eRestaurant.models.entities;

import java.security.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;


@Entity
@Table(name = "bookings")
public class Booking {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "booking_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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
        this.bookingDate= bookingDateTime.toString().split(" ")[0];
        this.bookingTime = bookingDateTime.toString().split(" ")[1];
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

}
