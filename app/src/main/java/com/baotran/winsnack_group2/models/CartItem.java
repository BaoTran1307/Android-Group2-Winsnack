package com.baotran.winsnack_group2.models;

import java.io.Serializable;

public class CartItem implements Serializable {
    private Long LineID;
    private Long CustomerID;
    private Long ProductID;
    private int Quantity;
    private double Price;
    private String Image;
    private String ProductName;
    private boolean isChecked; // Thêm trường này

    public CartItem() {
        this.isChecked = false; // Mặc định là false
    }

    public CartItem(Long lineID, Long customerID, Long productID, int quantity, double price, String image, String productName) {
        this.LineID = lineID;
        this.CustomerID = customerID;
        this.ProductID = productID;
        this.Quantity = quantity;
        this.Price = price;
        this.Image = image;
        this.ProductName = productName;
        this.isChecked = false; // Mặc định là false
    }

    public Long getLineID() { return LineID; }
    public void setLineID(Long lineID) { this.LineID = lineID; }
    public Long getCustomerID() { return CustomerID; }
    public void setCustomerID(Long customerID) { this.CustomerID = customerID; }
    public Long getProductID() { return ProductID; }
    public void setProductID(Long productID) { this.ProductID = productID; }
    public int getQuantity() { return Quantity; }
    public void setQuantity(int quantity) { this.Quantity = quantity; }
    public double getPrice() { return Price; }
    public void setPrice(double price) { this.Price = price; }
    public String getImage() { return Image; }
    public void setImage(String image) { this.Image = image; }
    public String getProductName() { return ProductName; }
    public void setProductName(String productName) { this.ProductName = productName; }
    public boolean isChecked() { return isChecked; } // Getter
    public void setChecked(boolean checked) { this.isChecked = checked; } // Setter
}