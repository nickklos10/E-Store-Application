/*
Name: Nicholas Klos
Course: CNT 4714 – Fall 2024
Assignment title: Project 1 – An Event-driven Enterprise Simulation
Date: Sunday September 8, 2024
*/
package com.pro1;

public class CartItem {

    private String itemId;
    private String description;
    private double priceEach;
    private int quantity;
    private double discount;
    private double totalPrice;

    public CartItem(String itemId, String description, double priceEach, int quantity, double discount, double totalPrice) {
        this.itemId = itemId;
        this.description = description;
        this.priceEach = priceEach;
        this.quantity = quantity;
        this.discount = discount;
        this.totalPrice = totalPrice;
    }

    public String getItemId() {
        return itemId;
    }

    public String getDescription() {
        return description;
    }

    public double getPriceEach() {
        return priceEach;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getDiscount() {
        return discount;
    }

    public double getTotalPrice() {
        return priceEach * quantity * (1 - discount);
    }

    @Override
    public String toString() {
        return String.format("SKU: %s, Desc: \"%s\", Price Each: $%.2f, Qty: %d, Discount: %.0f%%, Total: $%.2f",
                itemId, description, priceEach, quantity, discount * 100, totalPrice);
    }
}

