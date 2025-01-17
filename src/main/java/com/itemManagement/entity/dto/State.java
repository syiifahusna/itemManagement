package com.itemManagement.entity.dto;

import java.util.List;

public class State {
    private String name;
    private List<City> city;

    public State(){}

    public State(String name, List<City> city) {
        this.name = name;
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<City> getCity() {
        return city;
    }

    public void setCity(List<City> city) {
        this.city = city;
    }

	@Override
	public String toString() {
		return "State [name=" + name + ", city=" + city + "]";
	}
    
    
}
