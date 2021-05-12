package com.softwarestudiogroup1.uts.eRestaurant.controllers.Reciept;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

import com.softwarestudiogroup1.uts.eRestaurant.ViewManager;
import com.softwarestudiogroup1.uts.eRestaurant.models.BookingItemRepository;
import com.softwarestudiogroup1.uts.eRestaurant.models.entities.BookingItem;
import com.softwarestudiogroup1.uts.eRestaurant.models.entities.Customer;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class recieptController {
    private final BookingItemRepository bookingItemRepository;
    public recieptController(BookingItemRepository bookingItemRepository) {
        this.bookingItemRepository = bookingItemRepository;
    }

    @GetMapping("/showreciept/{BookingID}")
    public String recieptScreen(@PathVariable("BookingID") int BookingID, Model model){
        System.out.println("hi");
        ArrayList<String> names= new ArrayList();
        ArrayList<Integer> quantities= new ArrayList();
        ArrayList<Double> Price= new ArrayList();
        List<BookingItem> Bitems = bookingItemRepository.findAll();
        Double total = 0.0;
        int ID = BookingID;
        Customer customer = new Customer();

        for (BookingItem Bitem:Bitems){
            if (Bitem.getBooking().getId() == ID && Bitem.getQuantity() != 0){
                customer = Bitem.getBooking().getCustomer();
                names.add(Bitem.getItem().getName());
                int currentquan = Bitem.getQuantity();
                quantities.add(currentquan);
                double currentprice = Bitem.getItem().getPrice()*currentquan;
                double roundDbl = Math.round(currentprice*100.0)/100.0;
                Price.add(roundDbl);
                total+=roundDbl;
            }
        }

        model.addAttribute("names",names);
        model.addAttribute("quantities",quantities);
        model.addAttribute("price",Price);
        model.addAttribute("total",total);
        model.addAttribute("ID", BookingID);
        model.addAttribute("cusID", customer.getId());
        model.addAttribute("cusfirst", customer.getFirstName());
        model.addAttribute("cuslast", customer.getLastName());

        return ViewManager.RECIEPT;
    }
}
