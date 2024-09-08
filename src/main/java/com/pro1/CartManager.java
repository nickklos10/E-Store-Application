/*
Name: Nicholas Klos
Course: CNT 4714 – Fall 2024
Assignment title: Project 1 – An Event-driven Enterprise Simulation
Date: Sunday September 8, 2024
*/

package com.pro1;

import java.util.ArrayList;
import java.util.List;

public class CartManager {

    private List<CartItem> cartItems = new ArrayList<>();
    private double subtotal = 0.0;

    public void addItem(InventoryItem item, int quantity) {
        double discount = getDiscount(quantity);
        double priceWithDiscount = item.getPrice() * (1 - discount);
        double totalPrice = priceWithDiscount * quantity;

        CartItem cartItem = new CartItem(item.getItemId(), item.getDescription(), item.getPrice(), quantity, discount, totalPrice);
        cartItems.add(cartItem);

        subtotal += totalPrice;

        // Reduce the quantity in the inventory
        item.reduceQuantity(quantity);
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void clearCart() {
        cartItems.clear();
        subtotal = 0.0;
    }

    private double getDiscount(int quantity) {
        if (quantity >= 15) {
            return 0.20;
        } else if (quantity >= 10) {
            return 0.15;
        } else if (quantity >= 5) {
            return 0.10;
        } else {
            return 0.0;
        }
    }
}

