package com.softwarestudiogroup1.uts.eRestaurant.controllers.customer;

import com.softwarestudiogroup1.uts.eRestaurant.models.entities.Customer;
import com.softwarestudiogroup1.uts.eRestaurant.models.entities.Item;
import com.softwarestudiogroup1.uts.eRestaurant.models.entities.Patron;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

import javax.swing.text.View;

import com.softwarestudiogroup1.uts.eRestaurant.ViewManager;
import com.softwarestudiogroup1.uts.eRestaurant.models.CustomerRepository;
import com.softwarestudiogroup1.uts.eRestaurant.models.ItemRepository;

import org.springframework.ui.Model;

@Controller
public class OrderController {

    private final ItemRepository itemRepository;

    public OrderController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @GetMapping("/orderPage")
    public String orderPage(Model model) {
        List<Item> itemLists = itemRepository.findAll();
        List<Item> lunchList = new ArrayList<>();

        //only puts lunch items into lunchList
        for(Item item : itemLists){
            if(item.getMenuType().equals("lunch")){
                lunchList.add(item);
            }
        }

        model.addAttribute("Items", lunchList);
        return "customers/orderPage";
    }
}