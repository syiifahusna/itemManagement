package com.itemManagement.entity.dto;

import java.util.List;

public class City {

    private String name;
    private List<String> postcode;

    public City(){}

    public City(String name, List<String> postcode) {
        this.name = name;
        this.postcode = postcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getPostcode() {
        return postcode;
    }

    public void setPostcode(List<String> postcode) {
        this.postcode = postcode;
    }

	@Override
	public String toString() {
		return "City [name=" + name + ", postcode=" + postcode + "]";
	}
    
    
}
