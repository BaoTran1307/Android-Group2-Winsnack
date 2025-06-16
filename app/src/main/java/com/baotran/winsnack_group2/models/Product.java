package com.baotran.winsnack_group2.models;

public class Product {
    private String CategoryID;
    private String Description;
    private String Image;
    private double OriginalPrice;
    private double Price;
    private int ProductID;
    private String ProductName;
    private int Quantity;

    public Product() {
    }

    // Getters và setters
    public String getCategoryID() { return CategoryID; }
    public void setCategoryID(String categoryID) { CategoryID = categoryID; }
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
}
