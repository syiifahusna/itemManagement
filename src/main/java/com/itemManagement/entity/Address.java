package com.itemManagement.entity;

import com.itemManagement.audit.Auditable;
import jakarta.persistence.*;

@Entity
@Table(name = "addresses")
public class Address extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @ManyToOne
//    private User user;
//    private String street;
//    private  String postcode;
//    private String city;
//    private String state;
//
    public Address(){}
//
//    public Address(Long id, User user, String street, String postcode, String city, String state) {
//        this.id = id;
//        this.user = user;
//        this.street = street;
//        this.postcode = postcode;
//        this.city = city;
//        this.state = state;
//    }
//
//    public Address(User user, String street, String postcode, String city, String state) {
//        this.user = user;
//        this.street = street;
//        this.postcode = postcode;
//        this.city = city;
//        this.state = state;
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public User getUser() {
//        return user;
//    }
//
//    public void setUser(User user) {
//        this.user = user;
//    }
//
//    public String getStreet() {
//        return street;
//    }
//
//    public void setStreet(String street) {
//        this.street = street;
//    }
//
//    public String getPostcode() {
//        return postcode;
//    }
//
//    public void setPostcode(String postcode) {
//        this.postcode = postcode;
//    }
//
//    public String getCity() {
//        return city;
//    }
//
//    public void setCity(String city) {
//        this.city = city;
//    }
//
//    public String getState() {
//        return state;
//    }
//
//    public void setState(String state) {
//        this.state = state;
//    }
}
