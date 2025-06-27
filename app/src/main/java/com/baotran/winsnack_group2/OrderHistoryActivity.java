package com.baotran.winsnack_group2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baotran.winsnack_group2.adapter.OrderHistoryAdapter;
import com.baotran.winsnack_group2.models.Order;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OrderHistoryActivity extends AppCompatActivity {

    private RecyclerView rvOrderHistory;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private String userId;
    private OrderHistoryAdapter adapter;
    private List<Order> orderList;
    private final ExecutorService executorService = Executors.newFixedThreadPool(2);
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        // Khởi tạo Firebase
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        userId = user != null ? user.getUid() : "1";

        // Khởi tạo views
        rvOrderHistory = findViewById(R.id.rv_order_history);
        ImageView backButton = findViewById(R.id.btn_back);

        // Khởi tạo RecyclerView
        orderList = new ArrayList<>();
        rvOrderHistory.setLayoutManager(new LinearLayoutManager(this));
        adapter = new OrderHistoryAdapter(this, orderList);
        rvOrderHistory.setAdapter(adapter);

        // Thiết lập click listener cho nút back
        backButton.setOnClickListener(v -> finish());

        // Tải danh sách đơn hàng
        loadOrderHistory();
    }

    private void loadOrderHistory() {
        executorService.execute(() -> {
            // Lưu ý: Truy vấn này yêu cầu chỉ mục composite trên CustomerID và Date
            // Vui lòng tạo chỉ mục trong Firebase Console theo liên kết trong lỗi
            db.collection("ORDER")
                    .whereEqualTo("CustomerID", userId)
                    .get()
                    .addOnSuccessListener(querySnapshot -> {
                        orderList.clear();
                        for (QueryDocumentSnapshot document : querySnapshot) {
                            Order order = new Order(
                                    document.getString("OrderID"),
                                    document.getString("Date"),
                                    document.getDouble("Total") != null ? document.getDouble("Total") : 0.0,
                                    document.getString("Status"),
                                    document.getString("Address"),
                                    document.getString("PaymentMethod"),
                                    document.getString("Image")
                            );
                            orderList.add(order);
                        }
                        mainHandler.post(() -> adapter.notifyDataSetChanged());
                    })
                    .addOnFailureListener(e -> {
                        Log.e("OrderHistoryActivity", "Error loading order history", e);
                        mainHandler.post(() -> Toast.makeText(this, "Error loading order history: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    });
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}