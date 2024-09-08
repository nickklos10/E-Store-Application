/*
Name: Nicholas Klos
Course: CNT 4714 – Fall 2024
Assignment title: Project 1 – An Event-driven Enterprise Simulation
Date: Sunday September 8, 2024
*/

package com.pro1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class InventoryManager {

    private Map<String, InventoryItem> inventory = new HashMap<>();

    public InventoryManager(String filename) {
        loadInventory(filename);
    }

    private void loadInventory(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {

                String[] data = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                String itemId = data[0].trim();
                String description = data[1].trim().replace("\"", "");
                boolean inStock = Boolean.parseBoolean(data[2].trim());
                int quantity = Integer.parseInt(data[3].trim());
                double price = Double.parseDouble(data[4].trim());

                System.out.println("Parsed Item: " + itemId + ", " + description + ", " + inStock + ", " + quantity + ", " + price);

                inventory.put(itemId, new InventoryItem(itemId, description, inStock, quantity, price));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public InventoryItem getItem(String itemId) {
        return inventory.get(itemId);
    }
}

