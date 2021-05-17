package com.softwarestudiogroup1.uts.eRestaurant.controllers.manager;

public class RecentBookingDAO {

    private String id;
    private String firstName;
    private String lastName;

    private String dateTimeString;

    public RecentBookingDAO(String id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setDateTimeString(String dateTimeString) {
        this.dateTimeString = dateTimeString;
    }

    public String getDateTimeString() {
        return dateTimeString;
    }
    

    
    
}
