package com.ija.tacosproject;

/*
 * This  class was written for Challenge 1 and represents an INDIVIDUAL Json record such as those found
 * under "tacos" in db.json.
 * This was patterned on the constructor() method in models/Base.js in the tacos-master
 * NodeJS project
 */


public class TacoJsonRecord {
	
	public TacoJsonRecord() {}
	
	private String name;
	private String tortilla;
	private String toppings;
	private boolean vegetarian;
	private boolean soft;
	public String getName() {
		return name;
	}
	
	// getters and setters
	public void setName(String name) {
		this.name = name;
	}
	public String getTortilla() {
		return tortilla;
	}
	public void setTortilla(String tortilla) {
		this.tortilla = tortilla;
	}
	public String getToppings() {
		return toppings;
	}
	public void setToppings(String toppings) {
		this.toppings = toppings;
	}
	public boolean isVegetarian() {
		return vegetarian;
	}
	public void setVegetarian(boolean vegetarian) {
		this.vegetarian = vegetarian;
	}
	public boolean isSoft() {
		return soft;
	}
	public void setSoft(boolean soft) {
		this.soft = soft;
	}
	

}
