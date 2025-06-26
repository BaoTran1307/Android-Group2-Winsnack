package com.baotran.winsnack_group2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baotran.winsnack_group2.adapter.CartAdapter;
import com.baotran.winsnack_group2.models.Address;
import com.baotran.winsnack_group2.models.CartItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Transaction;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    private String selectedAddress = "Anh Nguyen | (+84) 123456789\n778 Locust View Drive Oakland, CA"; // Địa chỉ mặc định
    private double discount = 0.0;
    private String couponCode = "";
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

        // Khởi tạo Firebase
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        userId = user != null ? user.getUid() : "1"; // Chuyển thành string để khớp với CustomerID

        // Khởi tạo views
        shippingAddressTextView = findViewById(R.id.shipping_address_text);
        ImageView addressEditIcon = findViewById(R.id.imageView);
        ImageView paymentMethodEditIcon = findViewById(R.id.imageView2);
        couponEditText = findViewById(R.id.coupon_edit_text);
        placeOrderButton = findViewById(R.id.place_order_button);
        subtotalTextView = findViewById(R.id.subtotal_text);
        taxTextView = findViewById(R.id.tax_text);
        deliveryTextView = findViewById(R.id.delivery_text);
        totalTextView = findViewById(R.id.total_text);
        cartRecyclerView = findViewById(R.id.cart_recycler_view);
        progressBar = findViewById(R.id.progress_bar);
        ImageView backButton = findViewById(R.id.btn_back);

        // Đặt địa chỉ mặc định
        shippingAddressTextView.setText(selectedAddress);

        // Khởi tạo RecyclerView
        cartItems = new ArrayList<>();
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CartAdapter(this, cartItems, new CartAdapter.OnItemInteractionListener() {
            @Override
            public void onItemChecked(int position, boolean isChecked) {
                // Không xử lý check trong PaymentActivity
            }

            @Override
            public void onQuantityChanged(int position, int newQuantity) {
                CartItem item = cartItems.get(position);
                item.setQuantity(newQuantity);
                updateCartItem(item);
            }

            @Override
            public void onCancelOrder(int position) {
                CartItem item = cartItems.get(position);
                removeCartItem(item);
            }
        });
        cartRecyclerView.setAdapter(adapter);

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        ArrayList<CartItem> selectedItems = (ArrayList<CartItem>) intent.getSerializableExtra("selectedItems");
        if (selectedItems != null && !selectedItems.isEmpty()) {
            cartItems.addAll(selectedItems);
            adapter.notifyDataSetChanged();
            updateSummary();
        } else {
            Toast.makeText(this, "Không có sản phẩm nào được chọn!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Thiết lập click listeners
        backButton.setOnClickListener(v -> finish());

        addressEditIcon.setOnClickListener(v -> {
            Intent intentAddress = new Intent(PaymentActivity.this, DeliveryAddressActivity.class);
            startActivityForResult(intentAddress, REQUEST_ADDRESS);
        });

        paymentMethodEditIcon.setOnClickListener(v -> {
            Intent intentPayment = new Intent(PaymentActivity.this, PaymentMethodActivity.class);
            startActivityForResult(intentPayment, REQUEST_PAYMENT_METHOD);
        });

        couponEditText.setOnEditorActionListener((v, actionId, event) -> {
            validateAndApplyCoupon(couponEditText.getText().toString().trim());
            return true;
        });

        placeOrderButton.setOnClickListener(v -> processOrder());
    }

    private void updateCartItem(CartItem item) {
        DocumentReference cartRef = db.collection("CARTS").document(item.getLineID().toString());
        Map<String, Object> updates = new HashMap<>();
        updates.put("Quantity", item.getQuantity());

        cartRef.update(updates)
                .addOnSuccessListener(aVoid -> {
                    adapter.notifyDataSetChanged();
                    updateSummary();
                })
                .addOnFailureListener(e -> {
                    Log.e("PaymentActivity", "Error updating cart item", e);
                    Toast.makeText(this, "Lỗi khi cập nhật giỏ hàng: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void removeCartItem(CartItem item) {
        DocumentReference cartRef = db.collection("CARTS").document(item.getLineID().toString());
        cartRef.delete()
                .addOnSuccessListener(aVoid -> {
                    cartItems.remove(item);
                    adapter.notifyDataSetChanged();
                    updateSummary();
                    if (cartItems.isEmpty()) {
                        Toast.makeText(this, "Giỏ hàng trống!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("PaymentActivity", "Error removing cart item", e);
                    Toast.makeText(this, "Lỗi khi xóa sản phẩm: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void updateSummary() {
        mainHandler.post(() -> {
            double subtotal = 0.0;
            for (CartItem item : cartItems) {
                subtotal += item.getPrice() * item.getQuantity();
            }
            double total = subtotal + TAX_FEE + DELIVERY_FEE - discount;
            subtotalTextView.setText(formatPrice(subtotal));
            taxTextView.setText(formatPrice(TAX_FEE));
            deliveryTextView.setText(formatPrice(DELIVERY_FEE));
            totalTextView.setText(formatPrice(total));
        });
    }

    private void validateAndApplyCoupon(String code) {
        if (code.isEmpty()) {
            discount = 0.0;
            couponCode = "";
            updateSummary();
            return;
        }
        executorService.execute(() -> {
            try {
                DocumentSnapshot couponDoc = db.collection("COUPON").document(code).get().getResult();
                if (couponDoc.exists()) {
                    String couponType = couponDoc.getString("CouponType");
                    Number couponValue = couponDoc.getLong("CouponValue");
                    Number minNetPrice = couponDoc.getLong("MinimunNetPrice");
                    Number maxNetPrice = couponDoc.getLong("MaximumNetPrice");
                    Number expireDate = couponDoc.getLong("ExpireDate");
                    Number validDate = couponDoc.getLong("ValidDate");

                    double subtotal = getSubtotal();
                    long currentDate = Long.parseLong(new SimpleDateFormat("yyyyMMdd").format(new Date()));

                    if (expireDate.longValue() >= currentDate && validDate.longValue() <= currentDate &&
                            subtotal >= minNetPrice.longValue() && (maxNetPrice.longValue() == 0 || subtotal <= maxNetPrice.longValue())) {
                        double discountAmount = couponType.equals("Percentage") ?
                                subtotal * (couponValue.doubleValue() / 100) : couponValue.doubleValue();
                        mainHandler.post(() -> {
                            discount = discountAmount;
                            couponCode = code;
                            Toast.makeText(this, "Áp dụng mã giảm giá thành công!", Toast.LENGTH_SHORT).show();
                            updateSummary();
                        });
                    } else {
                        mainHandler.post(() -> {
                            discount = 0.0;
                            couponCode = "";
                            Toast.makeText(this, "Mã giảm giá không hợp lệ hoặc không áp dụng được", Toast.LENGTH_SHORT).show();
                            updateSummary();
                        });
                    }
                } else {
                    mainHandler.post(() -> {
                        discount = 0.0;
                        couponCode = "";
                        Toast.makeText(this, "Mã giảm giá không tồn tại", Toast.LENGTH_SHORT).show();
                        updateSummary();
                    });
                }
            } catch (Exception e) {
                Log.e("PaymentActivity", "Error validating coupon", e);
                mainHandler.post(() -> Toast.makeText(this, "Lỗi khi kiểm tra mã giảm giá: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void processOrder() {
        if (cartItems.isEmpty()) {
            Toast.makeText(this, "Giỏ hàng trống", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedAddress == null || selectedAddress.trim().isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn địa chỉ giao hàng", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        placeOrderButton.setEnabled(false);

        // Tạo orderId dưới dạng String
        String orderId;
        try {
            orderId = UUID.randomUUID().toString(); // Giữ nguyên UUID dạng chuỗi
            if (orderId.isEmpty()) {
                throw new IllegalStateException("Invalid order ID generated");
            }
        } catch (Exception e) {
            Log.e("PaymentActivity", "Error generating orderId", e);
            mainHandler.post(() -> {
                progressBar.setVisibility(View.GONE);
                placeOrderButton.setEnabled(true);
                Toast.makeText(this, "Lỗi khi tạo mã đơn hàng", Toast.LENGTH_SHORT).show();
            });
            return;
        }

        String paymentId = String.valueOf(System.currentTimeMillis());
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        Map<String, Object> order = new HashMap<>();
        order.put("OrderID", orderId); // Lưu dưới dạng String
        order.put("CustomerID", userId); // userId đã là String
        order.put("Date", date);
        order.put("Image", cartItems.isEmpty() ? "" : cartItems.get(0).getImage());
        order.put("Note", "Deliver before 6 PM");
        order.put("PaymentID", paymentId); // Lưu dưới dạng String
        order.put("PaymentMethod", "Credit Card");
        order.put("Status", "Completed");
        order.put("Total", getSubtotal() + TAX_FEE + DELIVERY_FEE - discount);
        order.put("Address", selectedAddress);
        if (!couponCode.isEmpty()) {
            order.put("CouponCode", couponCode);
        }

        List<Map<String, Object>> orderLines = new ArrayList<>();
        for (CartItem item : cartItems) {
            Map<String, Object> line = new HashMap<>();
            line.put("LineID", item.getLineID());
            line.put("OrderID", orderId); // Lưu dưới dạng String
            line.put("ProductID", item.getProductID());
            line.put("Quantity", item.getQuantity());
            line.put("SalePrice", item.getPrice());
            line.put("Image", item.getImage() != null ? item.getImage() : "");
            orderLines.add(line);
        }

        executorService.execute(() -> {
            try {
                db.runTransaction((Transaction.Function<Void>) transaction -> {
                    DocumentReference orderRef = db.collection("ORDER").document(orderId);
                    transaction.set(orderRef, order);

                    for (Map<String, Object> line : orderLines) {
                        DocumentReference lineRef = db.collection("ORDERLINE").document(line.get("LineID").toString());
                        transaction.set(lineRef, line);
                    }

                    for (CartItem item : cartItems) {
                        DocumentReference cartRef = db.collection("CARTS").document(item.getLineID().toString());
                        transaction.delete(cartRef);
                    }

                    return null;
                }).addOnSuccessListener(aVoid -> mainHandler.post(() -> {
                    Log.d("PaymentActivity", "Order placed successfully, orderId: " + orderId);
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Đặt hàng thành công!", Toast.LENGTH_SHORT).show();
                    cartItems.clear();
                    adapter.notifyDataSetChanged();
                    Intent intent = new Intent(PaymentActivity.this, OrderConfirmedActivity.class);
                    intent.putExtra("orderId", orderId);
                    startActivity(intent);
                    finish(); // Đóng PaymentActivity
                })).addOnFailureListener(e -> mainHandler.post(() -> {
                    Log.e("PaymentActivity", "Error placing order", e);
                    progressBar.setVisibility(View.GONE);
                    placeOrderButton.setEnabled(true);
                    Toast.makeText(this, "Lỗi khi đặt hàng: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }));
            } catch (Exception e) {
                Log.e("PaymentActivity", "Transaction error", e);
                mainHandler.post(() -> {
                    progressBar.setVisibility(View.GONE);
                    placeOrderButton.setEnabled(true);
                    Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });

    }

    private double getSubtotal() {
        double subtotal = 0.0;
        for (CartItem item : cartItems) {
            subtotal += item.getPrice() * item.getQuantity();
        }
        return subtotal;
    }

    private String formatPrice(double price) {
        return new DecimalFormat("$#.##").format(price);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ADDRESS && resultCode == RESULT_OK && data != null) {
            Address selected = data.getParcelableExtra("SELECTED_ADDRESS");
            if (selected != null) {
                selectedAddress = selected.toString();
                shippingAddressTextView.setText(selectedAddress);
                Log.d("PaymentActivity", "Received address: " + selectedAddress);
            }
        } else if (requestCode == REQUEST_PAYMENT_METHOD && resultCode == RESULT_OK && data != null) {
            // Xử lý lựa chọn phương thức thanh toán nếu cần
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}
