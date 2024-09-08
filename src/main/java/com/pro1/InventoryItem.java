/*
Name: Nicholas Klos
Course: CNT 4714 – Fall 2024
Assignment title: Project 1 – An Event-driven Enterprise Simulation
Date: Sunday September 8, 2024
*/

package com.pro1;
public class InventoryItem {

    private String itemId;
    private String description;
    private boolean inStock;
    private int quantity;
    private double price;

    public InventoryItem(String itemId, String description, boolean inStock, int quantity, double price) {
        this.itemId = itemId;
        this.description = description;
        this.inStock = inStock;
        this.quantity = quantity;
        this.price = price;
    }

    public String getItemId() {
        return itemId;
    }

    public String getDescription() {
        return description;
    }

    public boolean isInStock() {
        return inStock;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public void reduceQuantity(int amount) {
        this.quantity -= amount;
    }
}
