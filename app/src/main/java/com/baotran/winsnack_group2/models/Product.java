package com.baotran.winsnack_group2.models;

import java.io.Serializable;

public class Product implements Serializable {
    private Long ProductID;
    private String CategoryID;
    private String ProductName;
    private double Price;
    private double OriginalPrice;
    private String Description;
    private String Image;
    private int Quantity;
    private int Rate;
    private String Label;

    public Product() {}

    public Product(Long productID, String categoryID, String productName, double price, double originalPrice, String description, String image, int quantity, int rate, String label) {
        this.ProductID = productID;
        this.CategoryID = categoryID;
        this.ProductName = productName;
        this.Price = price;
        this.OriginalPrice = originalPrice;
        this.Description = description;
        this.Image = image;
        this.Quantity = quantity;
        this.Rate = rate;
        this.Label = label;
    }

    public Long getProductID() { return ProductID; }
    public void setProductID(Long productID) { this.ProductID = productID; }
    public String getCategoryID() { return CategoryID; }
    public void setCategoryID(String categoryID) { this.CategoryID = categoryID; }
    public String getProductName() { return ProductName; }
    public void setProductName(String productName) { this.ProductName = productName; }
    public double getPrice() { return Price; }
    public void setPrice(double price) { this.Price = price; }
    public double getOriginalPrice() { return OriginalPrice; }
    public void setOriginalPrice(double originalPrice) { this.OriginalPrice = originalPrice; }
    public String getDescription() { return Description; }
    public void setDescription(String description) { this.Description = description; }
    public String getImage() { return Image; }
    public void setImage(String image) { this.Image = image; }
    public int getQuantity() { return Quantity; }
    public void setQuantity(int quantity) { this.Quantity = quantity; }
    public int getRate() { return Rate; }
    public void setRate(int rate) { this.Rate = rate; }
    public String getLabel() { return Label; }
    public void setLabel(String label) { this.Label = label; }
}