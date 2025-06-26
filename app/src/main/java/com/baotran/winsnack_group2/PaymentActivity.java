package com.baotran.winsnack_group2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baotran.winsnack_group2.adapter.CartAdapter;
import com.baotran.winsnack_group2.models.CartItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Transaction;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PaymentActivity extends AppCompatActivity {

    private TextView shippingAddressTextView, subtotalTextView, taxTextView, deliveryTextView, totalTextView;
    private EditText couponEditText;
    private Button placeOrderButton;
    private ProgressBar progressBar;
    private RecyclerView cartRecyclerView;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private List<CartItem> cartItems;
    private CartAdapter adapter;
    private String selectedAddress = "";
    private double discount = 0.0;
    private final double TAX_FEE = 5.0;
    private final double DELIVERY_FEE = 3.0;
    private final ExecutorService executorService = Executors.newFixedThreadPool(2);
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private static final int REQUEST_ADDRESS = 100;
    private static final int REQUEST_PAYMENT_METHOD = 101;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        userId = user != null ? user.getUid() : "user_1";

        // Initialize views
        shippingAddressTextView = findViewById(R.id.shipping_address_text);
        ImageView addressEditIcon = findViewById(R.id.imageView);
        ImageView paymentMethodEditIcon = findViewById(R.id.imageView2);
        couponEditText = findViewById(R.id.coupon_edit_text);
        placeOrderButton = findViewById(R.id.place_order_button);
        subtotalTextView = findViewById(R.id.subtotal_text);
        taxTextView = findViewById(R.id.tax_text);
        deliveryTextView = findViewById(R.id.delivery_text);
        totalTextView = findViewById(R.id.total_text);
        cartRecyclerView = findViewById(R.id.cart_recycler_view); // Thay cart_items_container bằng RecyclerView
        progressBar = findViewById(R.id.progress_bar);
        ImageView backButton = findViewById(R.id.btn_back);

        // Initialize RecyclerView
        cartItems = new ArrayList<>();
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CartAdapter(cartItems, new CartAdapter.OnItemActionListener() {
            @Override
            public void onQuantityChanged(int position, int newQuantity) {
                CartItem item = cartItems.get(position);
                item.setQuantity(newQuantity);
                updateCartItem(item);
            }

            @Override
            public void onItemChecked(int position, boolean isChecked) {
                // Nếu cần logic chọn sản phẩm để xóa, xử lý ở đây
                if (isChecked) {
                    removeCartItem(cartItems.get(position));
                }
            }
        });
        cartRecyclerView.setAdapter(adapter);

        // Load cart items
        loadCartItemsFromFirestore();

        // Setup click listeners
        backButton.setOnClickListener(v -> finish());

        addressEditIcon.setOnClickListener(v -> {
            Intent intent = new Intent(PaymentActivity.this, DeliveryAddressActivity.class);
            startActivityForResult(intent, REQUEST_ADDRESS);
        });

        paymentMethodEditIcon.setOnClickListener(v -> {
            // TODO: Implement navigation to PaymentMethodActivity
            // Intent intent = new Intent(PaymentActivity.this, PaymentMethodActivity.class);
            // startActivityForResult(intent, REQUEST_PAYMENT_METHOD);
        });

        couponEditText.setOnEditorActionListener((v, actionId, event) -> {
            validateAndApplyCoupon(couponEditText.getText().toString().trim());
            return true;
        });

        placeOrderButton.setOnClickListener(v -> processOrder());
    }

    private void loadCartItemsFromFirestore() {
        db.collection("CART").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    cartItems.clear();
                    if (documentSnapshot.exists()) {
                        List<Map<String, Object>> items = (List<Map<String, Object>>) documentSnapshot.get("items");
                        if (items != null) {
                            for (Map<String, Object> item : items) {
                                String productName = (String) item.get("productName");
                                String price = String.valueOf(((Number) item.get("price")).doubleValue());
                                String dateTime = item.get("dateTime") != null ? (String) item.get("dateTime") : "";
                                int quantity = ((Number) item.get("quantity")).intValue();
                                cartItems.add(new CartItem(productName, price, dateTime, quantity));
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                    updateSummary();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Lỗi khi tải giỏ hàng: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void updateCartItem(CartItem item) {
        DocumentReference cartRef = db.collection("CART").document(userId);
        db.runTransaction((Transaction.Function<Void>) transaction -> {
            DocumentSnapshot snapshot = transaction.get(cartRef);
            List<Map<String, Object>> items = (List<Map<String, Object>>) snapshot.get("items");
            if (items != null) {
                for (Map<String, Object> map : items) {
                    if (map.get("productName").equals(item.getName())) {
                        map.put("quantity", item.getQuantity());
                        break;
                    }
                }
                transaction.update(cartRef, "items", items);
            }
            return null;
        }).addOnSuccessListener(aVoid -> {
            adapter.notifyDataSetChanged();
            updateSummary();
        }).addOnFailureListener(e -> Toast.makeText(this, "Lỗi khi cập nhật giỏ hàng: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void removeCartItem(CartItem item) {
        DocumentReference cartRef = db.collection("CART").document(userId);
        db.runTransaction((Transaction.Function<Void>) transaction -> {
            DocumentSnapshot snapshot = transaction.get(cartRef);
            List<Map<String, Object>> items = (List<Map<String, Object>>) snapshot.get("items");
            if (items != null) {
                items.removeIf(map -> map.get("productName").equals(item.getName()));
                transaction.update(cartRef, "items", items);
            }
            return null;
        }).addOnSuccessListener(aVoid -> {
            cartItems.remove(item);
            adapter.notifyDataSetChanged();
            updateSummary();
        }).addOnFailureListener(e -> Toast.makeText(this, "Lỗi khi xóa sản phẩm: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void updateSummary() {
        double subtotal = 0.0;
        for (CartItem item : cartItems) {
            try {
                double price = Double.parseDouble(item.getPrice());
                subtotal += price * item.getQuantity();
            } catch (NumberFormatException e) {
                // Handle invalid price format if needed
            }
        }
        double total = subtotal + TAX_FEE + DELIVERY_FEE - discount;

        subtotalTextView.setText(formatPrice(subtotal));
        taxTextView.setText(formatPrice(TAX_FEE));
        deliveryTextView.setText(formatPrice(DELIVERY_FEE));
        totalTextView.setText(formatPrice(total));
    }

    private void validateAndApplyCoupon(String code) {
        if (code.isEmpty()) {
            discount = 0.0;
            updateSummary();
            return;
        }
        executorService.execute(() -> {
            try {
                DocumentSnapshot couponDoc = db.collection("COUPON").document(code).get().get();
                if (couponDoc.exists()) {
                    Double discountAmount = couponDoc.getDouble("discount");
                    mainHandler.post(() -> {
                        discount = discountAmount != null ? discountAmount : 0.0;
                        Toast.makeText(this, "Áp dụng mã giảm giá thành công!", Toast.LENGTH_SHORT).show();
                        updateSummary();
                    });
                } else {
                    mainHandler.post(() -> {
                        discount = 0.0;
                        Toast.makeText(this, "Mã giảm giá không hợp lệ", Toast.LENGTH_SHORT).show();
                        updateSummary();
                    });
                }
            } catch (Exception e) {
                mainHandler.post(() -> Toast.makeText(this, "Lỗi khi kiểm tra mã giảm giá: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void processOrder() {
        if (cartItems.isEmpty()) {
            Toast.makeText(this, "Giỏ hàng trống", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedAddress.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn địa chỉ giao hàng", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        placeOrderButton.setEnabled(false);

        String orderId = UUID.randomUUID().toString();
        Map<String, Object> order = new HashMap<>();
        order.put("orderId", orderId);
        order.put("userId", userId);
        order.put("address", selectedAddress);
        order.put("items", getItemsForFirestore());
        order.put("subtotal", getSubtotal());
        order.put("tax", TAX_FEE);
        order.put("delivery", DELIVERY_FEE);
        order.put("discount", discount);
        order.put("total", getSubtotal() + TAX_FEE + DELIVERY_FEE - discount);
        order.put("timestamp", System.currentTimeMillis());

        executorService.execute(() -> {
            try {
                db.runTransaction((Transaction.Function<Void>) transaction -> {
                    DocumentReference orderRef = db.collection("ORDER").document(orderId);
                    transaction.set(orderRef, order);

                    DocumentReference cartRef = db.collection("CART").document(userId);
                    transaction.update(cartRef, "items", new ArrayList<>());
                    return null;
                }).addOnSuccessListener(aVoid -> mainHandler.post(() -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Đặt hàng thành công!", Toast.LENGTH_SHORT).show();
                    cartItems.clear();
                    adapter.notifyDataSetChanged();
                    Intent intent = new Intent(PaymentActivity.this, OrderConfirmedActivity.class);
                    intent.putExtra("orderId", orderId);
                    startActivity(intent);
                    finish();
                })).addOnFailureListener(e -> mainHandler.post(() -> {
                    progressBar.setVisibility(View.GONE);
                    placeOrderButton.setEnabled(true);
                    Toast.makeText(this, "Lỗi khi đặt hàng: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }));
            } catch (Exception e) {
                mainHandler.post(() -> {
                    progressBar.setVisibility(View.GONE);
                    placeOrderButton.setEnabled(true);
                    Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private List<Map<String, Object>> getItemsForFirestore() {
        List<Map<String, Object>> items = new ArrayList<>();
        for (CartItem item : cartItems) {
            Map<String, Object> map = new HashMap<>();
            map.put("productName", item.getName());
            map.put("price", Double.parseDouble(item.getPrice()));
            map.put("quantity", item.getQuantity());
            map.put("dateTime", item.getDateTime());
            items.add(map);
        }
        return items;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ADDRESS && resultCode == RESULT_OK && data != null) {
            selectedAddress = data.getStringExtra("selected_address");
            shippingAddressTextView.setText(selectedAddress);
        }
    }

    private double getSubtotal() {
        double subtotal = 0.0;
        for (CartItem item : cartItems) {
            try {
                subtotal += Double.parseDouble(item.getPrice()) * item.getQuantity();
            } catch (NumberFormatException e) {
                // Handle invalid price format if needed
            }
        }
        return subtotal;
    }

    private String formatPrice(double price) {
        return new DecimalFormat("$#.##").format(price);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}