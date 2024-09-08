/*
Name: Nicholas Klos
Course: CNT 4714 – Fall 2024
Assignment title: Project 1 – An Event-driven Enterprise Simulation
Date: Sunday September 8, 2024
*/


package com.pro1;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.ZonedDateTime;

public class NileDotCom extends Application {

    private TextField itemIdField;
    private TextField quantityField;
    private Label itemIdLabel;
    private Label quantityLabel;
    private Label itemDetailsLabel;
    private Label subtotalLabel;
    private Label cartStatusLabel;
    private TextArea cartArea;
    private Button searchButton, addButton, viewCartButton, checkOutButton, emptyCartButton, exitButton;

    private InventoryManager inventoryManager;
    private CartManager cartManager;

    @Override
    public void start(Stage primaryStage) {
        // Initialize Components
        itemIdField = new TextField();
        quantityField = new TextField();
        itemIdLabel = new Label("Enter item ID for Item #1:");
        quantityLabel = new Label("Enter quantity for Item #1:");
        itemDetailsLabel = new Label("Details for Item #1:");
        subtotalLabel = new Label("Current Subtotal for 0 item(s): $0.00");
        cartStatusLabel = new Label("Your shopping cart is currently empty");

        // DropShadow effect with white color
        DropShadow textBorderEffect = new DropShadow();
        textBorderEffect.setOffsetX(0);
        textBorderEffect.setOffsetY(0);
        textBorderEffect.setColor(Color.WHITE);
        textBorderEffect.setRadius(15);

        itemIdLabel.setStyle("-fx-text-fill: blue;");
        itemIdLabel.setEffect(textBorderEffect);

        quantityLabel.setStyle("-fx-text-fill: darkblue;");
        quantityLabel.setEffect(textBorderEffect);

        itemDetailsLabel.setStyle("-fx-text-fill: black;");
        itemDetailsLabel.setEffect(textBorderEffect);

        subtotalLabel.setStyle("-fx-text-fill: #003300;;");
        subtotalLabel.setEffect(textBorderEffect);

        cartStatusLabel.setStyle("-fx-text-fill: black;");
        cartStatusLabel.setEffect(textBorderEffect);


        cartArea = new TextArea();
        cartArea.setEditable(false);

        searchButton = new Button("Search for Item #1");
        addButton = new Button("Add Item #1 to Cart");
        viewCartButton = new Button("View Cart");
        checkOutButton = new Button("Check Out");
        emptyCartButton = new Button("Empty Cart");
        exitButton = new Button("Exit");

        // Disabling buttons that shouldn't be accessible at start
        addButton.setDisable(true);
        viewCartButton.setDisable(true);
        checkOutButton.setDisable(true);

        // Initializing Managers
        inventoryManager = new InventoryManager("inventory.csv");
        cartManager = new CartManager();



        // Layout setup using VBox
        VBox topSection = new VBox(10, itemIdLabel, itemIdField, quantityLabel, quantityField, itemDetailsLabel, subtotalLabel);
        topSection.setPadding(new Insets(10));

        VBox middleSection = new VBox(10, cartStatusLabel, cartArea);
        middleSection.setPadding(new Insets(10));

        HBox bottomSection = new HBox(10, searchButton, addButton, viewCartButton, checkOutButton, emptyCartButton, exitButton);
        bottomSection.setPadding(new Insets(10));

        VBox root = new VBox(10, topSection, middleSection, bottomSection);

        String imageUrl = getClass().getResource("/images/hi.jpg").toExternalForm();

        if (imageUrl == null) {
            System.err.println("Background image not found!");
        } else {
            root.setStyle("-fx-background-image: url('" + imageUrl + "'); " +
                    "-fx-background-size: cover; " +
                    "-fx-background-repeat: no-repeat;");
        }

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Nile Dot Com");
        primaryStage.show();

        // Event Handlers
        searchButton.setOnAction(e -> searchItem());
        addButton.setOnAction(event -> {
            // Logic for adding the item to the cart
            addItemToCart();

            // Re-enabling the "Search Item" button
            if (cartManager.getCartItems().size() >= 5) {
                searchButton.setDisable(true);
            }
            else {
                searchButton.setDisable(false);
            }
        });
        viewCartButton.setOnAction(e -> viewCart());
        checkOutButton.setOnAction(event -> {
            // Logic for checking out
            boolean checkoutSuccess = checkOut();

            if (checkoutSuccess) {
                // Assuming checkOut() returns true if the checkout was successful

                // Show a confirmation dialog
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Order Confirmation");
                alert.setHeaderText("Thank you for your order!");
                alert.setContentText("Your order has been processed successfully.");

                // Wait for user to press OK
                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        // Disable the itemIdField and quantityField
                        itemIdField.setDisable(true);
                        quantityField.setDisable(true);

                        // Optionally, disable other buttons if needed
                        searchButton.setDisable(true);
                        addButton.setDisable(true);
                    }
                });
            } else {
                // Checkout failure
            }
        });
        emptyCartButton.setOnAction(e -> emptyCart());
        exitButton.setOnAction(e -> primaryStage.close());
    }


    private void searchItem() {
        String itemId = itemIdField.getText();
        InventoryItem item = inventoryManager.getItem(itemId);

        if (item == null) {
            showError("Item ID '" + itemId + "' not in file");
            itemDetailsLabel.setText("Details for Item #1: Item not found.");
        } else if (!item.isInStock() || item.getQuantity() <= 0) {
            showError("Sorry... that item is out of stock, please try another item.");
            itemDetailsLabel.setText("Details for Item #1: Item out of stock.");
        } else {
            int requestedQuantity = Integer.parseInt(quantityField.getText());
            if (requestedQuantity > item.getQuantity()) {
                showError("Insufficient stock, only " + item.getQuantity() + " on hand. Please reduce the quantity.");
                itemDetailsLabel.setText("Details for Item #1: Insufficient stock.");
                quantityField.setText("");
            } else {
                double discountPercentage = calculateDiscountPercentage(requestedQuantity);
                double totalPriceBeforeDiscount = item.getPrice() * requestedQuantity;
                double discountAmount = totalPriceBeforeDiscount * discountPercentage / 100;
                double totalPriceAfterDiscount = totalPriceBeforeDiscount - discountAmount;
                // Get the next item number (1-based index)
                int nextItemNumber = cartManager.getCartItems().size() + 1;

                // Update item details label with the item number, item ID, and item details
                itemDetailsLabel.setText("Details for Item #" + nextItemNumber + ": " + item.getItemId() + " - " + "\"" + item.getDescription() + "\"" + " $" + item.getPrice() + " " + (int)discountPercentage + "%" + " $" + String.format("%.2f", totalPriceAfterDiscount));

                addButton.setDisable(false);
                subtotalLabel.setText("Current Subtotal for " + cartManager.getCartItems().size() + " item(s): $" + String.format("%.2f", cartManager.getSubtotal()));
                // Disable the search button after a successful search
                searchButton.setDisable(true);
            }
        }
    }

    private double calculateDiscountPercentage(int quantity) {
        if (quantity >= 15) {
            return 20.0;
        } else if (quantity >= 10) {
            return 15.0;
        } else if (quantity >= 5) {
            return 10.0;
        } else {
            return 0.0;
        }
    }


    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Nile Dot Com - Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void addItemToCart() {
        String itemId = itemIdField.getText();
        int quantity = Integer.parseInt(quantityField.getText());

        InventoryItem item = inventoryManager.getItem(itemId);
        cartManager.addItem(item, quantity);

        updateCartDisplay();
        updateSubtotalDisplay();

        // Clear input fields and prepare for next item
        itemIdField.clear();
        quantityField.clear();
        addButton.setDisable(true);

        // Update labels for the next item
        int nextItemNumber = cartManager.getCartItems().size() + 1;
        itemIdLabel.setText("Enter item ID for Item #" + nextItemNumber + ":");
        quantityLabel.setText("Enter quantity for Item #" + nextItemNumber + ":");
        searchButton.setText("Search for Item #" + nextItemNumber);
        addButton.setText("Add Item #" + nextItemNumber + " to Cart");

        // Update cart status label
        cartStatusLabel.setText("Your shopping cart currently contains " + cartManager.getCartItems().size() + " item(s)");

        // Enable relevant buttons
        viewCartButton.setDisable(false);
        checkOutButton.setDisable(false);

        if (cartManager.getCartItems().size() >= 5) {
            searchButton.setDisable(true);
            itemIdField.setDisable(true);
            quantityField.setDisable(true);
        }
    }

    private void updateCartDisplay() {
        cartArea.clear();
        int itemNumber = 1;  // Start the item number counter

        for (CartItem cartItem : cartManager.getCartItems()) {
            // Create a string with the item number, SKU, and other details
            String itemDetails = String.format("%d. %s\n",
                    itemNumber++,
                    cartItem.toString());
            cartArea.appendText(itemDetails);
        }
    }

    private void updateSubtotalDisplay() {
        subtotalLabel.setText("Current Subtotal for " + cartManager.getCartItems().size() + " item(s): $" + String.format("%.2f", cartManager.getSubtotal()));
    }

    private void viewCart() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Nile Dot Com - Current Shopping Cart Status");
        alert.setHeaderText("Your Cart Items");

        // TextArea to display cart contents
        TextArea cartContent = new TextArea();
        cartContent.setEditable(false);
        cartContent.setWrapText(true);

        // Building the cart content with item numbers on the left
        StringBuilder cartText = new StringBuilder();
        int itemNumber = 1;
        for (CartItem cartItem : cartManager.getCartItems()) {
            cartText.append(itemNumber++).append(". ").append(cartItem.toString()).append("\n");
        }

        cartContent.setText(cartText.toString());
        cartContent.setPrefSize(710, 300);

        alert.getDialogPane().setContent(cartContent);

        alert.showAndWait();
    }



    private boolean checkOut() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Nile Dot Com - FINAL INVOICE");
        alert.setHeaderText(null);

        // TextArea to display checkout details
        TextArea invoiceContent = new TextArea();
        invoiceContent.setEditable(false);
        invoiceContent.setWrapText(true);

        // Date and Time
        String dateTime = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String formattedDateTime = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy, hh:mm:ss a z"));

        // Invoice content
        StringBuilder invoiceText = new StringBuilder();
        invoiceText.append("Date: ").append(formattedDateTime).append("\n");
        invoiceText.append("Number of line items: ").append(cartManager.getCartItems().size()).append("\n\n");
        invoiceText.append("Item# / ID / Title / Price / Qty / Disc % / Subtotal:\n");

        int itemNumber = 1;
        double subtotal = 0.0;
        for (CartItem cartItem : cartManager.getCartItems()) {
            invoiceText.append(itemNumber++).append(". ")
                    .append(cartItem.toString()).append("\n");
            subtotal += cartItem.getTotalPrice();
        }

        // Order subtotal
        invoiceText.append("\nOrder subtotal: $").append(String.format("%.2f", subtotal)).append("\n");

        // Tax calculation (assuming 6% tax rate)
        double taxRate = 0.06;
        double taxAmount = subtotal * taxRate;
        invoiceText.append("Tax rate: ").append(String.format("%.0f", taxRate * 100)).append("%\n");
        invoiceText.append("Tax amount: $").append(String.format("%.2f", taxAmount)).append("\n");

        // Total amount
        double total = subtotal + taxAmount;
        invoiceText.append("ORDER TOTAL: $").append(String.format("%.2f", total)).append("\n\n");
        invoiceText.append("Thanks for shopping at Nile Dot Com!");

        // Text content to the TextArea
        invoiceContent.setText(invoiceText.toString());

        invoiceContent.setPrefSize(750, 380);

        alert.getDialogPane().setContent(invoiceContent);

        // Show the dialog and wait for user response
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Write to transactions.csv
                try (FileWriter writer = new FileWriter("transactions.csv", true)) {
                    for (CartItem cartItem : cartManager.getCartItems()) {
                        writer.append(dateTime).append(", ")
                                .append(cartItem.getItemId()).append(", ")
                                .append("\"").append(cartItem.getDescription()).append("\", ")
                                .append(String.format("%.2f", cartItem.getPriceEach())).append(", ")
                                .append(String.valueOf(cartItem.getQuantity())).append(", ")
                                .append(String.format("%.2f", cartItem.getDiscount())).append(", ")
                                .append("$").append((String.format("%.2f", cartItem.getTotalPrice()))).append(", ")
                                .append(formattedDateTime).append("\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Clearing the cart after writing to the file
                cartManager.clearCart();
                updateCartDisplay();
                updateSubtotalDisplay();

                // Resetting GUI
                searchButton.setDisable(false);
                itemIdField.setDisable(true);
                quantityField.setDisable(true);
                viewCartButton.setDisable(true);
                checkOutButton.setDisable(true);
            }
        });
        return false;
    }

    private void writeTransactionToFile() {
        // File logic
    }

    private void emptyCart() {
        // Clear the cart
        cartManager.clearCart();

        // Clear the cart display area
        cartArea.clear();

        // Reset the item details and subtotal labels to their initial state
        itemDetailsLabel.setText("Details for Item #1:");
        subtotalLabel.setText("Current Subtotal for 0 item(s): $0.00");

        // Reset the cart status label to indicate the cart is empty
        cartStatusLabel.setText("Your shopping cart is currently empty");

        // Reset the input labels and search button text to item #1
        itemIdLabel.setText("Enter item ID for Item #1:");
        quantityLabel.setText("Enter quantity for Item #1:");
        searchButton.setText("Search for Item #1");
        addButton.setText("Add Item #1 to Cart");

        // Reset the input fields for item ID and quantity
        itemIdField.clear();
        quantityField.clear();

        // Disable buttons that shouldn't be accessible after emptying the cart
        addButton.setDisable(true);
        viewCartButton.setDisable(true);
        checkOutButton.setDisable(true);
        searchButton.setDisable(false);
        itemIdField.setDisable(false);
        quantityField.setDisable(false);
    }



    public static void main(String[] args) {
        launch(args);
    }
}
