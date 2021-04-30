package com.softwarestudiogroup1.uts.eRestaurant.controllers.customer;

public class BookingItemDAO {
    private int id;
    private int itemID;
    private int bookingID;
    
    private String name;
    private double price;
    private String description;
    private String quantity;

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public void setBookingID(int bookingID) {
        this.bookingID = bookingID;
    }

    public void setName(String itemName) {
        this.name = itemName;
    }

    public void setPrice(double itemPrice) {
        this.price = itemPrice;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public int getItemID() {
        return itemID;
    }

    public int getBookingID() {
        return bookingID;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getQuantity() {
        return quantity;
    }
    
    public String getDescription() {
        return description;
    }
}
