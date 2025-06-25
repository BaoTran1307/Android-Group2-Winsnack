package com.baotran.winsnack_group2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baotran.winsnack_group2.adapter.CartAdapter;
import com.baotran.winsnack_group2.models.CartItem;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnItemActionListener {

    private static final String TAG = "CartActivity";
    private static final int HARDCODED_CUSTOMER_ID = 1; // Hardcode CustomerID = 1 (Naruto)

    private RecyclerView recyclerViewCart;
    private CartAdapter cartAdapter;
    private TextView tvItemCount, tvTotal;
    private Button btnCheckout;
    private FirebaseFirestore db;
    private List<CartItem> cartItems = new ArrayList<>();
    private double totalPrice = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();

        // Initialize UI elements
        recyclerViewCart = findViewById(R.id.recyclerViewCart);
        tvItemCount = findViewById(R.id.tvItemCount);
        tvTotal = findViewById(R.id.tvTotal);
        btnCheckout = findViewById(R.id.btnCheckout);

        // Set up RecyclerView
        cartAdapter = new CartAdapter(cartItems, this, this);
        recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCart.setAdapter(cartAdapter);

        // Load cart data for hardcoded CustomerID = 1
        loadCartData(HARDCODED_CUSTOMER_ID);

        // Checkout button click
        btnCheckout.setOnClickListener(v -> {
            Intent intent = new Intent(CartActivity.this, HomeActivity.class); // Thay bằng PaymentActivity nếu cần
            intent.putExtra("totalAmount", totalPrice);
            startActivity(intent);
        });
    }

    private void loadCartData(int customerId) {
        Log.d(TAG, "Loading cart data for CustomerID: " + customerId);
        db.collection("CARTS") // Sử dụng collection CARTS
                .whereEqualTo("CustomerID", customerId)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    Log.d(TAG, "Query returned " + querySnapshot.size() + " documents");
                    cartItems.clear();
                    totalPrice = 0.0;
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        CartItem item = document.toObject(CartItem.class);
                        cartItems.add(item);
                        totalPrice += (item.getPrice() != 0 ? item.getPrice() : 0) * item.getQuantity();
                        Log.d(TAG, "Added item: LineID=" + item.getLineID() + ", ProductID=" + item.getProductID());
                    }
                    if (cartItems.isEmpty()) {
                        Log.w(TAG, "No items found for CustomerID: " + customerId);
                        tvItemCount.setText("You have 0 items in the cart");
                        tvTotal.setText("$0.00");
                    } else {
                        tvItemCount.setText("You have " + cartItems.size() + " items in the cart");
                        tvTotal.setText(String.format("$%.2f", totalPrice));
                    }
                    cartAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error loading cart data: " + e.getMessage());
                    tvItemCount.setText("You have 0 items in the cart");
                    tvTotal.setText("$0.00");
                    cartItems.clear();
                    cartAdapter.notifyDataSetChanged();
                });
    }

    @Override
    public void onQuantityChanged(int position, int newQuantity) {
        if (position >= 0 && position < cartItems.size()) {
            CartItem item = cartItems.get(position);
            item.setQuantity(newQuantity);
            totalPrice = 0.0;
            for (CartItem cartItem : cartItems) {
                totalPrice += (cartItem.getPrice() != 0 ? cartItem.getPrice() : 0) * cartItem.getQuantity();
            }
            tvTotal.setText(String.format("$%.2f", totalPrice));
            cartAdapter.notifyItemChanged(position);

            // Cập nhật Firestore với CustomerID = 1
            updateCartItem(item, newQuantity);
        }
    }

    @Override
    public void onItemChecked(int position, boolean isChecked) {
        Log.d(TAG, "Item checked at position " + position + ": " + isChecked);
    }

    private void updateCartItem(CartItem item, int newQuantity) {
        db.collection("CARTS")
                .whereEqualTo("CustomerID", HARDCODED_CUSTOMER_ID)
                .whereEqualTo("LineID", item.getLineID())
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        document.getReference()
                                .update("Quantity", newQuantity)
                                .addOnSuccessListener(aVoid -> Log.d(TAG, "Cart item updated"))
                                .addOnFailureListener(e -> Log.e(TAG, "Error updating cart item: " + e.getMessage()));
                    }
                });
    }
}