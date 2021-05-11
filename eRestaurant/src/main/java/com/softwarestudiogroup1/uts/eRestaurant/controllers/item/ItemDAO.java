package com.softwarestudiogroup1.uts.eRestaurant.controllers.item;

public class ItemDAO {
    private int id;
    private String name;
    private double price;
    private String menuType;
    private String description; 

    public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

    public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

    public double getPrice() {
		return this.price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

    public String getMenuType() {
		return this.menuType;
	}

	public void setMenuType(String menuType) {
		this.menuType = menuType;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
