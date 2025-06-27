package com.baotran.winsnack_group2.models;

public class Order {
    private String orderId;
    private String date;
    private double total;
    private String status;
    private String address;
    private String paymentMethod;
    private String image;

    public Order(String orderId, String date, double total, String status, String address, String paymentMethod, String image) {
        this.orderId = orderId;
        this.date = date;
        this.total = total;
        this.status = status;
        this.address = address;
        this.paymentMethod = paymentMethod;
        this.image = image;
    }

    public String getOrderId() { return orderId; }
    public String getDate() { return date; }
    public double getTotal() { return total; }
    public String getStatus() { return status; }
    public String getAddress() { return address; }
    public String getPaymentMethod() { return paymentMethod; }
    public String getImage() { return image; }
}