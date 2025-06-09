package com.baotran.winsnack_group2.models;

public class CartItem {
    private String name;
    private String price;
    private String dateTime;
    private int quantity;

    public CartItem(String name, String price, String dateTime, int quantity) {
        this.name = name;
        this.price = price;
        this.dateTime = dateTime;
        this.quantity = quantity;
    }

    public String getName() { return name; }
    public String getPrice() { return price; }
    public String getDateTime() { return dateTime; }
    public int getQuantity() { return quantity; }
}
