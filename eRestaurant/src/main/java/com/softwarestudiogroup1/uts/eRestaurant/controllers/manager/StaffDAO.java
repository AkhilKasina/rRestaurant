package com.softwarestudiogroup1.uts.eRestaurant.controllers.manager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StaffDAO {
    private int id;
    private String telephone;
    private String password;
    private String email;
    private String username;
    private String firstName;
    private String lastName;
    private String description;
    private double hourlyWage;
    // private String dateOfBirth;
    private String shiftType;

    private ArrayList<StaffDAO> staff;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDescription() {
        return description;
    }

    public void setdescription(String description) {
        this.description = description;
    }

    public double getHourlyWage(){
        return hourlyWage;
    }

    public void setHourlyWage(double hourlyWage){
        this.hourlyWage = hourlyWage;
    }

    // private final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    // public void setDate(String datesString) {
    //     setDate(datesString);
    // }

    // public Date getDateOfBirth() {
    //     try {
    //         return DATE_TIME_FORMAT.parse(dateOfBirth);
    //     } catch (ParseException e) {
    //         throw new IllegalArgumentException(e);
    //     }
    // }

    public String getShiftType(){
        return shiftType;
    }

    public void setShiftType(String shiftType) {
        this.shiftType = shiftType;
    }

    public void setStaff(ArrayList<StaffDAO> staff) {
        this.staff = staff;
    }

    public ArrayList<StaffDAO> getStaff() {
        return staff;
    }

}