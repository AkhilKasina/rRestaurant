package com.softwarestudiogroup1.uts.eRestaurant.controllers.item;

import com.softwarestudiogroup1.uts.eRestaurant.ViewManager;
import com.softwarestudiogroup1.uts.eRestaurant.models.ItemRepository;
import com.softwarestudiogroup1.uts.eRestaurant.models.entities.Item;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;


@Controller
public class ItemController {


    private final ItemRepository itemRepository;

    public ItemController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    /**
    * This function is used for a purpose of database testing. 
    * Has to remove when publishing website
    */
    @GetMapping("/allitem")
    @ResponseBody 
    public List<Item> getHomePage(Model model) {
        List<Item> itemLists = itemRepository.findAll();

        model.addAttribute("itemLists", itemLists);

        return itemLists;
        //return "item/allitem";
        //revert back to original
        
    }
}


