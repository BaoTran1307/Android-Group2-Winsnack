package com.baotran.winsnack_group2.models;

import java.util.List;

public class Cart {
    private List<CartItem> items;

    public Cart() {}

    public Cart(List<CartItem> items) {
        this.items = items;
    }

    public List<CartItem> getItems() { return items; }
    public void setItems(List<CartItem> items) { this.items = items; }
}