package com.softwarestudiogroup1.uts.eRestaurant.controllers.customer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.softwarestudiogroup1.uts.eRestaurant.models.entities.Item;

public class BookingDAO {
    private int id;
    private String bookingDate;
    private String bookingTime;
    private String tablePosition;

    private ArrayList<BookingItemDAO> bookingItems;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(String bookingTime) {
        this.bookingTime = bookingTime;
    }

    public String getTablePosition(){
        return tablePosition;
    }

    public void setTablePosition(String tablePosition){
        this.tablePosition = tablePosition;
    }

    private final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public void setDateAndTime(Date timeStamp) {
        String datesString = timeStamp.toString().split(" ")[0];
        String timeString = timeStamp.toString().split(" ")[1];

        setBookingDate(datesString);
        setBookingTime(timeString);
    }

    public Date getBookingTimeStamp() {
        try {
            return DATE_TIME_FORMAT.parse(bookingDate + " " + bookingTime);
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public void setBookingItems(ArrayList<BookingItemDAO> bookingItems) {
        this.bookingItems = bookingItems;
    }

    public void setBookingItemsFrom(List<Item> itemsList, MenuType type) {
        ArrayList<BookingItemDAO> bookingItemDAOs = new ArrayList<>();

        if (type == MenuType.LUNCH) {
             for(Item item : itemsList){
                if (item.getMenuType().equals("lunch")) {
                    BookingItemDAO bookingItemDAO = new BookingItemDAO();

                    bookingItemDAO.setItemID(item.getId());
                    bookingItemDAO.setName(item.getName());
                    bookingItemDAO.setPrice(item.getPrice());
                    bookingItemDAO.setDescription(item.getDescription());
                    bookingItemDAO.setQuantity("0");

                    bookingItemDAOs.add(bookingItemDAO);
                }
            } 
        }
        else if (type == MenuType.DINNER) {
            for (Item item : itemsList) {
                if(item.getMenuType().equals("dinner")){
                    BookingItemDAO bookingItemDAO = new BookingItemDAO();

                    bookingItemDAO.setItemID(item.getId());
                    bookingItemDAO.setName(item.getName());
                    bookingItemDAO.setPrice(item.getPrice());
                    bookingItemDAO.setDescription(item.getDescription());
                    bookingItemDAO.setQuantity("0");

                    bookingItemDAOs.add(bookingItemDAO);
                }
            }
        }

        this.setBookingItems(bookingItemDAOs);
    }

    public ArrayList<BookingItemDAO> getBookingItems() {
        return bookingItems;
    }

}