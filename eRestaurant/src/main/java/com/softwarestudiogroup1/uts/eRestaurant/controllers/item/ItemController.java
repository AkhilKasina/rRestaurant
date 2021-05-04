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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;


@Controller
public class ItemController {

    private int currentID = 0;

    private final ItemRepository itemRepository;

    public ItemController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @GetMapping("/lunch")
    public String getLunchMenu(Model model) {
        List<Item> itemLists = itemRepository.findAll();
        List<Item> lunchList = new ArrayList<>();

        //only puts lunch items into lunchList
        for(Item item : itemLists){
            if(item.getMenuType().equals("lunch")){
                lunchList.add(item);
            }
        }

        model.addAttribute("Items", lunchList);
        return "menu/lunchMenu";
    }

    @GetMapping("/dinner")
    public String getDinnerMenu(Model model) {
        List<Item> itemLists = itemRepository.findAll();
        List<Item> dinnerList = new ArrayList<>();

        for(Item item : itemLists){
            if(item.getMenuType().equals("dinner")){
                dinnerList.add(item);
            }
        }

        model.addAttribute("Items", dinnerList);
        return "menu/dinnerMenu";
    }

}


