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
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.firestore.WriteBatch;

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

    private TextView shippingAddressTextView, subtotalTextView, taxTextView, deliveryTextView, totalTextView, discountTextView, paymentMethodText, paymentMethodCode;
    private EditText couponEditText;
    private Button placeOrderButton, applyCouponButton;
    private ProgressBar progressBar;
    private RecyclerView cartRecyclerView;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private List<CartItem> cartItems;
    private CartAdapter adapter;
    private String selectedAddress = "Anh Nguyen | (+84) 123456789\n778 Locust View Drive Oakland";
    private double discount = 0.0;
    private String couponCode = "";
    private final double TAX_FEE = 5.0;
    private final double DELIVERY_FEE = 3.0;
    private final ExecutorService executorService = Executors.newFixedThreadPool(2);
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private static final int REQUEST_ADDRESS = 100;
    private static final int REQUEST_PAYMENT_METHOD = 101;
    private String userId;
    private String orderId; // Khai báo orderId như biến thành viên

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // Khởi tạo Firebase
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        userId = user != null ? user.getUid() : "1";

        // Khởi tạo views
        shippingAddressTextView = findViewById(R.id.shipping_address_text);
        ImageView addressEditIcon = findViewById(R.id.imageView);
        ImageView paymentMethodEditIcon = findViewById(R.id.imageView2);
        couponEditText = findViewById(R.id.coupon_edit_text);
        placeOrderButton = findViewById(R.id.place_order_button);
        applyCouponButton = findViewById(R.id.apply_coupon_button);
        subtotalTextView = findViewById(R.id.subtotal_text);
        taxTextView = findViewById(R.id.tax_text);
        deliveryTextView = findViewById(R.id.delivery_text);
        totalTextView = findViewById(R.id.total_text);
        discountTextView = findViewById(R.id.discount_text);
        paymentMethodText = findViewById(R.id.payment_method_text);
        paymentMethodCode = findViewById(R.id.payment_method_code);
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
            public void onItemChecked(int position, boolean isChecked) {}

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
            Toast.makeText(this, "No items selected!", Toast.LENGTH_SHORT).show();
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

        applyCouponButton.setOnClickListener(v -> {
            String code = couponEditText.getText().toString().trim().toUpperCase();
            validateAndApplyCoupon(code);
        });

        placeOrderButton.setOnClickListener(v -> processOrder());
    }

    private void updateCartItem(CartItem item) {
        db.collection("CARTS")
                .whereEqualTo("CustomerID", Integer.parseInt(userId))
                .whereEqualTo("ProductID", item.getProductID())
                .whereEqualTo("LineID", item.getLineID())
                .limit(1)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        DocumentReference cartRef = querySnapshot.getDocuments().get(0).getReference();
                        Map<String, Object> updates = new HashMap<>();
                        updates.put("Quantity", item.getQuantity());

                        cartRef.update(updates)
                                .addOnSuccessListener(aVoid -> {
                                    adapter.notifyDataSetChanged();
                                    updateSummary();
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("PaymentActivity", "Error updating cart item", e);
                                    Toast.makeText(this, "Error updating cart: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("PaymentActivity", "Error querying cart item for update", e);
                    Toast.makeText(this, "Error updating cart: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void removeCartItem(CartItem item) {
        db.collection("CARTS")
                .whereEqualTo("CustomerID", Integer.parseInt(userId))
                .whereEqualTo("ProductID", item.getProductID())
                .whereEqualTo("LineID", item.getLineID())
                .limit(1)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        DocumentReference cartRef = querySnapshot.getDocuments().get(0).getReference();
                        cartRef.delete()
                                .addOnSuccessListener(aVoid -> {
                                    cartItems.remove(item);
                                    adapter.notifyDataSetChanged();
                                    updateSummary();
                                    if (cartItems.isEmpty()) {
                                        Toast.makeText(this, "Cart is empty!", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("PaymentActivity", "Error removing cart item", e);
                                    Toast.makeText(this, "Error removing item: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("PaymentActivity", "Error querying cart item for removal", e);
                    Toast.makeText(this, "Error removing item: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
            discountTextView.setText(formatPrice(-discount));
            totalTextView.setText(formatPrice(total));
        });
    }

    private void validateAndApplyCoupon(String code) {
        if (code.isEmpty()) {
            discount = 0.0;
            couponCode = "";
            updateSummary();
            Toast.makeText(this, "Please enter a coupon code!", Toast.LENGTH_SHORT).show();
            return;
        }

        executorService.execute(() -> {
            db.collection("COUPON")
                    .whereEqualTo("CouponCode", code)
                    .limit(1)
                    .get()
                    .addOnSuccessListener(querySnapshot -> {
                        Log.d("CouponDebug", "Query result size: " + querySnapshot.size());
                        if (!querySnapshot.isEmpty()) {
                            DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                            String couponType = document.getString("CouponType");
                            Number couponValue = document.getLong("CouponValue");
                            double subtotal = getSubtotal();

                            Log.d("CouponDebug", "CouponType: " + couponType);
                            Log.d("CouponDebug", "CouponValue: " + couponValue);
                            Log.d("CouponDebug", "Subtotal: " + subtotal);

                            double discountAmount;
                            if ("Fixed".equals(couponType) && couponValue != null) {
                                discountAmount = Math.min(couponValue.doubleValue(), subtotal);
                            } else if ("Percentage".equals(couponType) && couponValue != null) {
                                discountAmount = subtotal * (couponValue.doubleValue() / 100);
                            } else {
                                discountAmount = 0.0;
                            }

                            if (discountAmount > 0) {
                                mainHandler.post(() -> {
                                    discount = discountAmount;
                                    couponCode = code;
                                    Toast.makeText(this, "Coupon applied successfully! Discount: $" + discountAmount, Toast.LENGTH_SHORT).show();
                                    updateSummary();
                                });
                            } else {
                                mainHandler.post(() -> {
                                    discount = 0.0;
                                    couponCode = "";
                                    Toast.makeText(this, "Coupon is invalid or cannot be applied", Toast.LENGTH_SHORT).show();
                                    updateSummary();
                                });
                            }
                        } else {
                            mainHandler.post(() -> {
                                discount = 0.0;
                                couponCode = "";
                                Toast.makeText(this, "Coupon code does not exist", Toast.LENGTH_SHORT).show();
                                updateSummary();
                            });
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e("PaymentActivity", "Error validating coupon: " + e.getMessage());
                        mainHandler.post(() -> Toast.makeText(this, "Error validating coupon: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    });
        });
    }

    private void processOrder() {
        if (cartItems.isEmpty()) {
            Toast.makeText(this, "Cart is empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedAddress == null || selectedAddress.trim().isEmpty()) {
            Toast.makeText(this, "Please select a delivery address", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        placeOrderButton.setEnabled(false);

        orderId = UUID.randomUUID().toString(); // Gán giá trị cho biến thành viên orderId
        String paymentId = String.valueOf(System.currentTimeMillis());
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        Map<String, Object> order = new HashMap<>();
        order.put("OrderID", orderId);
        order.put("CustomerID", userId);
        order.put("Date", date);
        order.put("Image", cartItems.isEmpty() ? "" : cartItems.get(0).getImage());
        order.put("Note", "Deliver before 6 PM");
        order.put("PaymentID", paymentId);
        order.put("PaymentMethod", paymentMethodText.getText().toString());
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
            line.put("OrderID", orderId);
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
                        DocumentReference lineRef = db.collection("ORDERLINE").document(UUID.randomUUID().toString()); // Tạo ID mới cho ORDERLINE
                        transaction.set(lineRef, line);
                    }

                    return null;
                }).addOnSuccessListener(aVoid -> {
                    mainHandler.post(() -> {
                        Log.d("PaymentActivity", "Order placed successfully, orderId: " + orderId);
                        progressBar.setVisibility(View.GONE);

                        // Thực hiện xóa cart items sau khi order thành công
                        deleteCartItems();
                    });
                }).addOnFailureListener(e -> mainHandler.post(() -> {
                    Log.e("PaymentActivity", "Error placing order", e);
                    progressBar.setVisibility(View.GONE);
                    placeOrderButton.setEnabled(true);
                    Toast.makeText(this, "Error placing order: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }));
            } catch (Exception e) {
                Log.e("PaymentActivity", "Transaction error", e);
                mainHandler.post(() -> {
                    progressBar.setVisibility(View.GONE);
                    placeOrderButton.setEnabled(true);
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void deleteCartItems() {
        List<Map<String, Object>> cartQueryConditions = new ArrayList<>();
        for (CartItem item : cartItems) {
            Map<String, Object> condition = new HashMap<>();
            condition.put("CustomerID", Integer.parseInt(userId));
            condition.put("ProductID", item.getProductID());
            condition.put("LineID", item.getLineID());
            cartQueryConditions.add(condition);
            Log.d("PaymentActivity", "Attempting to delete cart item with CustomerID: " + userId + ", ProductID: " + item.getProductID() + ", LineID: " + item.getLineID());
        }

        db.collection("CARTS")
                .whereEqualTo("CustomerID", Integer.parseInt(userId))
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<WriteBatch> batches = new ArrayList<>();
                    WriteBatch batch = db.batch();
                    int count = 0;

                    for (DocumentSnapshot document : querySnapshot) {
                        int docCustomerID = document.getLong("CustomerID") != null ? document.getLong("CustomerID").intValue() : 0;
                        int docProductID = document.getLong("ProductID") != null ? document.getLong("ProductID").intValue() : 0;
                        int docLineID = document.getLong("LineID") != null ? document.getLong("LineID").intValue() : 0;

                        for (CartItem item : cartItems) {
                            if (docCustomerID == Integer.parseInt(userId) && docProductID == item.getProductID() && docLineID == item.getLineID()) {
                                batch.delete(document.getReference());
                                count++;
                                if (count == 500) {
                                    batches.add(batch);
                                    batch = db.batch();
                                    count = 0;
                                }
                                break;
                            }
                        }
                    }

                    if (count > 0) {
                        batches.add(batch);
                    }

                    // Thực thi từng batch
                    for (int i = 0; i < batches.size(); i++) {
                        int finalI = i;
                        batches.get(i).commit().addOnSuccessListener(aVoid -> {
                            Log.d("PaymentActivity", "Batch " + finalI + " deleted successfully");
                            if (finalI == batches.size() - 1) {
                                mainHandler.post(() -> {
                                    cartItems.clear();
                                    adapter.notifyDataSetChanged();
                                    Toast.makeText(this, "Order placed successfully!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(PaymentActivity.this, OrderConfirmedActivity.class);
                                    intent.putExtra("orderId", orderId); // Sử dụng biến thành viên orderId
                                    startActivity(intent);
                                    finish();
                                });
                            }
                        }).addOnFailureListener(e -> {
                            Log.e("PaymentActivity", "Error deleting cart items in batch " + finalI, e);
                            mainHandler.post(() -> Toast.makeText(this, "Error deleting cart items: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                        });
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("PaymentActivity", "Error querying cart items for deletion", e);
                    mainHandler.post(() -> Toast.makeText(this, "Error deleting cart items: " + e.getMessage(), Toast.LENGTH_SHORT).show());
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
            String method = data.getStringExtra("payment_method");
            String code = data.getStringExtra("payment_code");
            if (method != null) {
                paymentMethodText.setText(method);
                paymentMethodCode.setText(code.isEmpty() ? "" : code);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}