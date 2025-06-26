package com.baotran.winsnack_group2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baotran.winsnack_group2.adapter.CartAdapter;
import com.baotran.winsnack_group2.models.CartItem;
import com.baotran.winsnack_group2.models.Product;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnItemInteractionListener {

    private RecyclerView rvCart;
    private TextView tvItemCount, tvTotal;
    private Button btnCheckout;
    private CartAdapter cartAdapter;
    private List<CartItem> cartItems = new ArrayList<>();
    private Map<Long, Product> productMap = new HashMap<>();
    private double totalPrice = 0.0;
    private FirebaseFirestore db;
    private static final String TAG = "CartActivity";
    private static final String PREFS_NAME = "UserPrefs";
    private static final String KEY_CUSTOMER_ID = "CustomerID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = FirebaseFirestore.getInstance();
        FirebaseFirestore.setLoggingEnabled(true);

        rvCart = findViewById(R.id.rvCart);
        tvItemCount = findViewById(R.id.tvItemCount);
        tvTotal = findViewById(R.id.tvTotal);
        btnCheckout = findViewById(R.id.btn_checkout);

        cartAdapter = new CartAdapter(this, cartItems, this);
        rvCart.setLayoutManager(new LinearLayoutManager(this));
        rvCart.setAdapter(cartAdapter);

        btnCheckout.setOnClickListener(v -> {
            List<CartItem> selectedItems = new ArrayList<>();
            for (CartItem item : cartItems) {
                if (item.isChecked()) {
                    selectedItems.add(item);
                }
            }
            if (selectedItems.isEmpty()) {
                Toast.makeText(this, "Vui lòng chọn ít nhất một sản phẩm!", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(CartActivity.this, PaymentActivity.class);
                intent.putExtra("selectedItems", new ArrayList<>(selectedItems));
                startActivity(intent);
            }
        });

        loadCartData();
    }

    private void loadCartData() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int customerId = prefs.getInt(KEY_CUSTOMER_ID, -1);

        if (customerId != -1) {
            db.collection("PRODUCT")
                    .get()
                    .addOnSuccessListener(productSnapshot -> {
                        productMap.clear();
                        for (QueryDocumentSnapshot doc : productSnapshot) {
                            Product product = doc.toObject(Product.class);
                            productMap.put(product.getProductID(), product);
                        }
                        Log.d(TAG, "Loaded " + productMap.size() + " products");

                        db.collection("CARTS")
                                .whereEqualTo("CustomerID", (long) customerId)
                                .get()
                                .addOnSuccessListener(cartSnapshot -> {
                                    cartItems.clear();
                                    totalPrice = 0.0; // Reset totalPrice khi load
                                    for (QueryDocumentSnapshot doc : cartSnapshot) {
                                        CartItem item = doc.toObject(CartItem.class);
                                        Product product = productMap.get(item.getProductID());
                                        if (product != null) {
                                            item.setPrice(product.getPrice());
                                            item.setImage(product.getImage());
                                            item.setProductName(product.getProductName());
                                        } else {
                                            Log.w(TAG, "Product not found for ProductID: " + item.getProductID());
                                            item.setProductName("Unknown");
                                        }
                                        cartItems.add(item);
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
                                    Toast.makeText(this, "Error loading cart: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    tvItemCount.setText("You have 0 items in the cart");
                                    tvTotal.setText("$0.00");
                                });
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Error loading products: " + e.getMessage());
                        Toast.makeText(this, "Error loading products: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Log.w(TAG, "No CustomerID found in SharedPreferences");
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void onItemChecked(int position, boolean isChecked) {
        CartItem item = cartItems.get(position);
        if (isChecked) {
            totalPrice += item.getPrice() * item.getQuantity();
        } else {
            totalPrice -= item.getPrice() * item.getQuantity();
        }
        tvTotal.setText(String.format("$%.2f", totalPrice));
    }

    @Override
    public void onQuantityChanged(int position, int newQuantity) {
        if (position >= 0 && position < cartItems.size()) {
            CartItem item = cartItems.get(position);
            double priceChange = (newQuantity - item.getQuantity()) * item.getPrice();
            item.setQuantity(newQuantity);
            if (item.isChecked()) { // Giả sử có trường isChecked, cần thêm vào CartItem
                totalPrice += priceChange;
            }
            tvTotal.setText(String.format("$%.2f", totalPrice));
            cartAdapter.notifyItemChanged(position);

            SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            int customerId = prefs.getInt(KEY_CUSTOMER_ID, -1);
            if (customerId != -1) {
                db.collection("CARTS")
                        .whereEqualTo("CustomerID", (long) customerId)
                        .whereEqualTo("LineID", item.getLineID())
                        .get()
                        .addOnSuccessListener(querySnapshot -> {
                            if (!querySnapshot.isEmpty()) {
                                querySnapshot.getDocuments().get(0).getReference()
                                        .update("Quantity", newQuantity)
                                        .addOnSuccessListener(aVoid -> Log.d(TAG, "Quantity updated"))
                                        .addOnFailureListener(e -> Log.e(TAG, "Error updating quantity: " + e.getMessage()));
                            }
                        });
            }
        }
    }

    @Override
    public void onCancelOrder(int position) {
        if (position >= 0 && position < cartItems.size()) {
            CartItem item = cartItems.get(position);
            double itemPrice = item.isChecked() ? item.getPrice() * item.getQuantity() : 0;
            totalPrice -= itemPrice; // Cập nhật tổng tiền nếu mục được chọn

            SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            int customerId = prefs.getInt(KEY_CUSTOMER_ID, -1);
            if (customerId != -1) {
                db.collection("CARTS")
                        .document(item.getLineID().toString())
                        .delete()
                        .addOnSuccessListener(aVoid -> {
                            cartItems.remove(position);
                            cartAdapter.notifyItemRemoved(position);
                            tvItemCount.setText("You have " + cartItems.size() + " items in the cart");
                            tvTotal.setText(String.format("$%.2f", totalPrice));
                            Log.d(TAG, "Cart item removed successfully");
                        })
                        .addOnFailureListener(e -> {
                            Log.e(TAG, "Error removing cart item: " + e.getMessage());
                            Toast.makeText(this, "Error removing item: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
        }
    }
}