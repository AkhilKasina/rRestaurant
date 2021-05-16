package com.softwarestudiogroup1.uts.eRestaurant.models.entities;

import java.security.Timestamp;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "rewards")
public class Reward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "reward_name")
    private String rewardName;

    @Column(name = "discount")
    private double discount;

	@Column(name = "date_acquired")
	private String dateAcquired;

	@Column(name = "expiry_date")
	private String expiryDate;

	@ManyToOne
	@JoinColumn(name = "customer_id", nullable = false)
	private Customer customer;

	public Reward() {

	}

	public Reward(String name, double discount, String dateExchanged, String couponExp, Customer customer) {
		this.rewardName = name;
		this.discount = discount;
		this.dateAcquired = dateExchanged;
		this.expiryDate = couponExp;
		this.customer = customer;
	}


    public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

    public String getRewardName() {
		return this.rewardName;
	}

	public void setRewardName(String rewardName) {
		this.rewardName = rewardName;
	}

    public double getDiscount() {
		return this.discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public String getDateAcquired() {
		return this.dateAcquired;
	}

	public void setDateAcquired(String dateAcquired) {
		this.dateAcquired = dateAcquired;
	}

	public String getExpiryDate() {
		return this.expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Customer getCustomers() {
		return this.customer;
	}

	public void setCustomers(Customer customer) {
		this.customer = customer;
	}
    
}
