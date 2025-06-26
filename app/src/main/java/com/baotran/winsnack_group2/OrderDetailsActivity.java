package com.baotran.winsnack_group2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class OrderDetailsActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private TextView orderNumberText, orderDateText, subtotalText, taxFeesText, deliveryText, totalText;
    private RecyclerView orderItemsRecyclerView;
    private OrderItemAdapter adapter;
    private List<OrderItem> orderItems = new ArrayList<>();
    private Map<Long, String> productNames = new HashMap<>(); // Lưu tên sản phẩm theo ProductID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        // Khởi tạo Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Tìm các view trong layout
        ImageView btnBack = findViewById(R.id.btn_back);
        orderNumberText = findViewById(R.id.order_number_text);
        orderDateText = findViewById(R.id.order_date_text);
        orderItemsRecyclerView = findViewById(R.id.order_items_recycler_view);
        subtotalText = findViewById(R.id.subtotal_text);
        taxFeesText = findViewById(R.id.tax_fees_text);
        deliveryText = findViewById(R.id.delivery_text);
        totalText = findViewById(R.id.total_text);
        Button btnOrderAgain = findViewById(R.id.btn_order_again);

        // Cấu hình RecyclerView
        adapter = new OrderItemAdapter();
        orderItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderItemsRecyclerView.setAdapter(adapter);

        // Lấy orderId từ Intent
        String orderId = getIntent().getStringExtra("orderId");
        if (orderId != null) {
            loadOrderDetails(orderId);
        } else {
            orderNumberText.setText("Order No. 0054752");
            orderDateText.setText("29 Nov, 01:20 pm");
        }

        // Xử lý sự kiện click cho nút Back
        btnBack.setOnClickListener(v -> finish());

        // Xử lý sự kiện click cho nút Order Again
        btnOrderAgain.setOnClickListener(v -> {
            Intent intent = new Intent(OrderDetailsActivity.this, CartActivity.class); // Thay CartActivity bằng activity giỏ hàng
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }

    private void loadOrderDetails(String orderId) {
        db.collection("ORDER").document(orderId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String date = documentSnapshot.getString("Date");
                        Number total = documentSnapshot.getLong("Total");

                        // Định dạng ngày
                        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());
                        try {
                            date = outputFormat.format(inputFormat.parse(date));
                        } catch (Exception e) {
                            Log.e("OrderDetailsActivity", "Error parsing date", e);
                            date = "Date not found";
                        }

                        // Cập nhật UI với dữ liệu từ ORDER
                        orderNumberText.setText("Order No. " + orderId);
                        orderDateText.setText(date);

                        // Lấy danh sách sản phẩm từ ORDERLINE và tải tên sản phẩm từ PRODUCT
                        loadProductNames();
                        db.collection("ORDERLINE")
                                .whereEqualTo("OrderID", orderId)
                                .get()
                                .addOnSuccessListener(queryDocumentSnapshots -> {
                                    orderItems.clear();
                                    double subtotal = 0.0;
                                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                        long productId = document.getLong("ProductID") != null ? document.getLong("ProductID") : 0;
                                        long quantity = document.getLong("Quantity") != null ? document.getLong("Quantity") : 0;
                                        double salePrice = document.getDouble("SalePrice") != null ? document.getDouble("SalePrice") : 0.0;
                                        String image = document.getString("Image");

                                        String productName = productNames.getOrDefault(productId, "Unknown Product");
                                        orderItems.add(new OrderItem(productName, quantity, salePrice, image));
                                        subtotal += salePrice * quantity;
                                    }
                                    adapter.notifyDataSetChanged();

                                    // Cập nhật summary
                                    double taxFees = 5.0; // Hardcode hoặc lấy từ ORDER nếu có
                                    double delivery = 3.0; // Hardcode hoặc lấy từ ORDER nếu có
                                    double totalAmount = subtotal + taxFees + delivery;

                                    DecimalFormat df = new DecimalFormat("$#.##");
                                    subtotalText.setText(df.format(subtotal));
                                    taxFeesText.setText(df.format(taxFees));
                                    deliveryText.setText(df.format(delivery));
                                    totalText.setText(df.format(totalAmount));
                                })
                                .addOnFailureListener(e -> Log.e("OrderDetailsActivity", "Error getting ORDERLINE", e));
                    } else {
                        Log.d("OrderDetailsActivity", "No such document for orderId: " + orderId);
                        orderNumberText.setText("Order No. " + orderId);
                        orderDateText.setText("Date not found");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("OrderDetailsActivity", "Error getting ORDER", e);
                    orderNumberText.setText("Order No. " + orderId);
                    orderDateText.setText("Error loading date");
                });
    }

    private void loadProductNames() {
        db.collection("PRODUCT")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        long productId = document.getLong("ProductID") != null ? document.getLong("ProductID") : 0;
                        String name = document.getString("ProductName");
                        if (name != null) {
                            productNames.put(productId, name);
                        }
                    }
                })
                .addOnFailureListener(e -> Log.e("OrderDetailsActivity", "Error loading PRODUCT", e));
    }

    // Model cho sản phẩm
    private static class OrderItem {
        String productName;
        long quantity;
        double salePrice;
        String image;

        OrderItem(String productName, long quantity, double salePrice, String image) {
            this.productName = productName;
            this.quantity = quantity;
            this.salePrice = salePrice;
            this.image = image;
        }
    }

    // Adapter cho RecyclerView
    private class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.ViewHolder> {
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_order_detail, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            OrderItem item = orderItems.get(position);
            Glide.with(holder.itemView.getContext()).load(item.image).into(holder.productImage);
            holder.productName.setText(item.productName);
            holder.productDate.setText("2025-06-26"); // Lấy từ ORDER Date
            holder.productPrice.setText(new DecimalFormat("$#.##").format(item.salePrice));
            holder.productQuantity.setText("- " + item.quantity);
        }

        @Override
        public int getItemCount() {
            return orderItems.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView productImage;
            TextView productName, productDate, productPrice, productQuantity;

            ViewHolder(View itemView) {
                super(itemView);
                productImage = itemView.findViewById(R.id.product_image);
                productName = itemView.findViewById(R.id.product_name);
                productDate = itemView.findViewById(R.id.product_date);
                productPrice = itemView.findViewById(R.id.product_price);
                productQuantity = itemView.findViewById(R.id.product_quantity);
            }
        }
    }
}
