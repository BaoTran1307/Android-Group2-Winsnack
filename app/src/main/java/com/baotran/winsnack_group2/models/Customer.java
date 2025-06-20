package com.baotran.winsnack_group2.models;

import java.io.Serializable;

public class Customer implements Serializable {
    private int CustomerID;
    private String CustomerName;
    private String Email;
    private String Password;
    private String Phone;
    private String Username;
    private int MembershipScore;
    private String DateOfBirth;
    private String FavoriteTaste;
    private String Image; // Thêm trường Image

    // Default constructor for Firestore
    public Customer() {}

    // Getters and setters
    public int getCustomerID() { return CustomerID; }
    public void setCustomerID(int customerID) { CustomerID = customerID; }
    public String getCustomerName() { return CustomerName; }
    public void setCustomerName(String customerName) { CustomerName = customerName; }
    public String getEmail() { return Email; }
    public void setEmail(String email) { Email = email; }
    public String getPassword() { return Password; }
    public void setPassword(String password) { Password = password; }
    public String getPhone() { return Phone; }
    public void setPhone(String phone) { Phone = phone; }
    public String getUsername() { return Username; }
    public void setUsername(String username) { Username = username; }
    public int getMembershipScore() { return MembershipScore; }
    public void setMembershipScore(int membershipScore) { MembershipScore = membershipScore; }
    public String getDateOfBirth() { return DateOfBirth; }
    public void setDateOfBirth(String dateOfBirth) { DateOfBirth = dateOfBirth; }
    public String getFavoriteTaste() { return FavoriteTaste; }
    public void setFavoriteTaste(String favoriteTaste) { FavoriteTaste = favoriteTaste; }
    public String getImage() { return Image; } // Getter cho Image
    public void setImage(String image) { Image = image; } // Setter cho Image
}