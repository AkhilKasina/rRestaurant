package com.softwarestudiogroup1.uts.eRestaurant.controllers.Reciept;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.ArrayList;
import java.util.List;

import com.softwarestudiogroup1.uts.eRestaurant.ViewManager;
import com.softwarestudiogroup1.uts.eRestaurant.models.BookingItemRepository;
import com.softwarestudiogroup1.uts.eRestaurant.models.entities.BookingItem;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

public class recieptController {
    private final int bookingID;
    private final BookingItemRepository bookingItemRepository;

    public recieptController(@ModelAttribute("bookingID") int bookingID, BookingItemRepository bookingItemRepository) {
        this.bookingItemRepository = bookingItemRepository;
        this.bookingID = bookingID;
    }

    @GetMapping("/reciept")
    public String recieptScreen(Model model){
        ArrayList<String> names= new ArrayList();
        ArrayList<Integer> quantities= new ArrayList();
        ArrayList<Double> Price= new ArrayList();
        List<BookingItem> Bitems = bookingItemRepository.findbyBookingID(bookingID);
        Double total = 0.0;

        for (BookingItem Bitem:Bitems){
            names.add(Bitem.getItem().getName());
            int currentquan = Bitem.getQuantity();
            quantities.add(currentquan);
            double currentprice = Bitem.getItem().getPrice()*currentquan;
            Price.add(currentprice);
            total+=currentprice;
        }

        model.addAttribute("names",names);
        model.addAttribute("quantities",quantities);
        model.addAttribute("price",Price);
        model.addAttribute("total",total);

        return ViewManager.RECIEPT;
    }
}
