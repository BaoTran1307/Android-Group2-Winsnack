package com.baotran.winsnack_group2.models;

import java.io.Serializable;

public class Product implements Serializable {
    private String CategoryID;
    private String Description;
    private String Image;
    private double OriginalPrice;
    private double Price;
    private int ProductID;
    private String ProductName;
    private int Quantity;
    private double Rate; // Thêm trường Rate
    private String Label; // Thêm trường Label

    public Product() {
    }

    // Getters và setters
    public String getCategoryID() { return CategoryID; }
    public void setCategoryID(String CategoryID) { this.CategoryID = CategoryID; }
    public String getDescription() { return Description; }
    public void setDescription(String description) { Description = description; }
    public String getImage() { return Image; }
    public void setImage(String image) { Image = image; }
    public double getOriginalPrice() { return OriginalPrice; }
    public void setOriginalPrice(double originalPrice) { OriginalPrice = originalPrice; }
    public double getPrice() { return Price; }
    public void setPrice(double price) { Price = price; }
    public int getProductID() { return ProductID; }
    public void setProductID(int productID) { ProductID = productID; }
    public String getProductName() { return ProductName; }
    public void setProductName(String productName) { ProductName = productName; }
    public int getQuantity() { return Quantity; }
    public void setQuantity(int quantity) { Quantity = quantity; }
    public double getRate() { return Rate; } // Thêm getter cho Rate
    public void setRate(double rate) { this.Rate = rate; } // Thêm setter cho Rate
    public String getLabel() { return Label; } // Thêm getter cho Label
    public void setLabel(String label) { this.Label = label; } // Thêm setter cho Label
}