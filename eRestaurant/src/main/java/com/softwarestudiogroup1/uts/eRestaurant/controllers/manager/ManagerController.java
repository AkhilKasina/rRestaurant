package com.softwarestudiogroup1.uts.eRestaurant.controllers.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.softwarestudiogroup1.uts.eRestaurant.ViewManager;
import com.softwarestudiogroup1.uts.eRestaurant.controllers.item.ItemDAO;
import com.softwarestudiogroup1.uts.eRestaurant.models.BookingRepository;
import com.softwarestudiogroup1.uts.eRestaurant.models.CustomerRepository;
import com.softwarestudiogroup1.uts.eRestaurant.models.ItemRepository;
import com.softwarestudiogroup1.uts.eRestaurant.models.ManagerRepository;
import com.softwarestudiogroup1.uts.eRestaurant.models.StaffRepository;
import com.softwarestudiogroup1.uts.eRestaurant.models.entities.Booking;
import com.softwarestudiogroup1.uts.eRestaurant.models.entities.Customer;
import com.softwarestudiogroup1.uts.eRestaurant.models.entities.Item;
import com.softwarestudiogroup1.uts.eRestaurant.models.entities.Manager;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class ManagerController {

    private int currentID = 0;

    private final ManagerRepository managerRepository;
    private final StaffRepository staffRepository;
    private final BookingRepository bookingRepository;
    private final CustomerRepository customerRepository;
    private final ItemRepository itemRepository;

    public ManagerController(ManagerRepository managerRepository, StaffRepository staffRepository, BookingRepository bookingRepository, CustomerRepository customerRepository, ItemRepository itemRepository) {
        this.managerRepository = managerRepository;
        this.staffRepository = staffRepository;
        this.bookingRepository = bookingRepository;
        this.customerRepository = customerRepository;
        this.itemRepository = itemRepository;
    }

    //@GetMapping("/restrurant")

    @GetMapping("/staffmanager")
    public String staffmanager(){
        return ViewManager.MNG_STAFF;
    }

    @GetMapping("/menumanager")
    public String menumanager(Model model){
        List<Item> Itemlist = itemRepository.findAll();
        model.addAttribute("Items", Itemlist);
        model.addAttribute("newItem", new ItemDAO());
        return ViewManager.MNG_MENU;
    }
    
    @PostMapping("/menumanager")
    public String menumanager(@ModelAttribute("newItem") ItemDAO itemDAO, final RedirectAttributes redirectAttributes){
        System.out.println("name: " + itemDAO.getName() + "price: " + itemDAO.getPrice() + "description: " + itemDAO.getDescription() + "id: " + itemDAO.getId() + "Menu: " + itemDAO.getMenuType());
        Item item = new Item();
        item.isNew();
        item.setName(itemDAO.getName());
        item.setMenuType(itemDAO.getMenuType());
        item.setPrice(itemDAO.getPrice());
        item.setDescription(itemDAO.getDescription());
        itemRepository.save(item);
        return redirectToManagerPortal(redirectAttributes);
    }

    @GetMapping("/manager")
    public String managerPortal(Model model){//@ModelAttribute("managerID") int managerID, Model model) {
        //this.currentID = managerID;
        //Optional<Manager> currentManager = managerRepository.findById(currentID);
        //if (currentManager.isPresent()) {
            //Manager manager = currentManager.get();   ##### IS THIS NEEDED
            //model.addAttribute("manager", manager);

        List<Booking> bookinglist = bookingRepository.findAll();
        ArrayList<String> names = new ArrayList<>();

        for (Booking booking : bookinglist){
            Customer customer = booking.getCustomer();
            names.add(customer.getFirstName() + " " + customer.getLastName());
        }
        model.addAttribute("names", names);
        model.addAttribute("bookings", bookinglist);
        return ViewManager.MNG_PORTAL;
    }

    private String redirectToManagerPortal(final RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("managerID", this.currentID);
        return "redirect:/manager";
    }
}
