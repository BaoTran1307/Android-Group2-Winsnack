package com.baotran.winsnack_group2.models;

public class CartItem {
    private String LineID;
    private Long ProductID;
    private int Quantity;
    private double Price;
    private String Image; // New field for image URL

    // Default constructor (required for Firestore)
    public CartItem() {}

    // Constructor with all fields
    public CartItem(String lineID, Long productID, int quantity, double price, String image) {
        this.LineID = lineID;
        this.ProductID = productID;
        this.Quantity = quantity;
        this.Price = price;
        this.Image = image;
    }

    // Getter and Setter for LineID
    public String getLineID() { return LineID; }
    public void setLineID(String lineID) { this.LineID = lineID; }

    // Getter and Setter for ProductID
    public Long getProductID() { return ProductID; }
    public void setProductID(Long productID) { this.ProductID = productID; }

    // Getter and Setter for Quantity
    public int getQuantity() { return Quantity; }
    public void setQuantity(int quantity) { this.Quantity = quantity; }

    // Getter and Setter for Price
    public double getPrice() { return Price; }
    public void setPrice(double price) { this.Price = price; }

    // Getter and Setter for Image
    public String getImage() { return Image; }
    public void setImage(String image) { this.Image = image; }
}