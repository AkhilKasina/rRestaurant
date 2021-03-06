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
import com.softwarestudiogroup1.uts.eRestaurant.models.entities.Staff;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;



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
    public String staffmanager(Model model){

        //  Validate if the current session has manager
        Optional<Manager> currentManager = managerRepository.findById(currentID);

        if (currentManager.isPresent()) {
            model.addAttribute("manager", currentManager.get());
        } else {
            return "redirect:/";
        }

        List<Staff> Stafflist = staffRepository.findAll();
        model.addAttribute("Staff", Stafflist);
        model.addAttribute("newStaff", new StaffDAO());
        return ViewManager.MNG_STAFF;
    }

    @PostMapping("/staffmanager")
    public String staffmanager(@ModelAttribute("newStaff") StaffDAO staffDAO, final RedirectAttributes redirectAttributes){
        System.out.println("id: " + staffDAO.getId() + "Firstname: " + staffDAO.getFirstName() + "Lastname: " + staffDAO.getLastName() + "description: " + staffDAO.getDescription() + "Hourly wage: " + staffDAO.getHourlyWage() + "Shift type " + staffDAO.getShiftType());
        Staff staff = new Staff();
        staff.isNew();
        staff.setTelephone(staffDAO.getTelephone());
        staff.setPassword(staffDAO.getPassword());
        staff.setEmail(staffDAO.getEmail());
        staff.setUsername(staffDAO.getUsername());
        staff.setFirstName(staffDAO.getFirstName());
        staff.setLastName(staffDAO.getLastName());
        staff.setDescription(staffDAO.getDescription());
        staff.setHourlyWage(staffDAO.getHourlyWage());
        // staff.setDateOfBirth(staffDAO.getDateOfBirth());
        staffRepository.save(staff);
        return "redirect:/staffmanager";
    }

    @RequestMapping(value = {"/staffManager/{staffID}"}, method = RequestMethod.POST, params = "delete") 
    public String deleteStaff(@PathVariable("staffID") int staffID, Model model) {
        Optional<Manager> currentManager = managerRepository.findById(currentID);

        if (currentManager.isPresent()) {
            model.addAttribute("manager", currentManager.get());
        } else {
            return "redirect:/";
        }

        if (staffRepository.existsById(staffID)) {
            staffRepository.deleteById(staffID);
        }
        
        return "redirect:/staffmanager";
    }

    @GetMapping("/menumanager")
    public String menumanager(Model model){

        //  Validate if the current session has manager
        Optional<Manager> currentManager = managerRepository.findById(currentID);

        if (currentManager.isPresent()) {
            model.addAttribute("manager", currentManager.get());
        } else {
            return "redirect:/";
        }

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
        item.setMenuType(itemDAO.getMenuType().toLowerCase());
        item.setPrice(itemDAO.getPrice());
        item.setDescription(itemDAO.getDescription());
        itemRepository.save(item);
        return "redirect:/menumanager";
    }

    @GetMapping("/manager")
    public String managerPortal(Model model){

        //  Validate if the current session has manager
        Optional<Manager> currentManager = managerRepository.findById(currentID);

        if (currentManager.isPresent()) {
            model.addAttribute("manager", currentManager.get());

            model.addAttribute("customersCount", customerRepository.findAll().size());
            model.addAttribute("bookingsCount", bookingRepository.findAll().size());

        } else {
            return "redirect:/";
        }

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

    
    @RequestMapping(value = {"/manager", "/staffmanager", "/menumanager"}, method = RequestMethod.POST, params = "logout")
    public String logout() {
        this.currentID = -1;
        return "redirect:/";
    }
    
    

    private String redirectToManagerPortal(final RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("managerID", this.currentID);
        return "redirect:/manager";
    }

    public void setID(int id) {
        this.currentID = id;
    }
}
