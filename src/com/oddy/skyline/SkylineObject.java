/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oddy.skyline;

/**
 *
 * @author ASUS
 */
public class SkylineObject {

    private double cost = 0;
    private double availability = 0;
    private String name = "";

    /**
     * @return the cost
     */
    public double getCost() {
        return cost;
    }

    /**
     * @param cost the cost to set
     */
    public void setCost(double cost) {
        this.cost = cost;
    }

    /**
     * @return the availability
     */
    public double getAvailability() {
        return availability;
    }

    /**
     * @param availability the availability to set
     */
    public void setAvailability(double availability) {
        this.availability = availability;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return "Name : " + this.name + " Cost: " + this.cost + " Availability : " + this.availability;
    }
}
