package com.baotran.winsnack_group2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class OrderConfirmedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmed);

        // Tìm các view trong layout
        ImageView btnBack = findViewById(R.id.btn_back);
        Button btnReturnHome = findViewById(R.id.btn_return_home);
        Button btnTrackOrder = findViewById(R.id.btn_track_order);

        // Lấy orderId từ Intent
        String orderId = getIntent().getStringExtra("orderId");
        if (orderId != null) {
            // Có thể sử dụng orderId để hiển thị thêm thông tin nếu cần
            // Ví dụ: deliveryInfoText.setText("Delivery by June, 29th, 4:00 PM for Order #" + orderId);
        }

        // Đặt ngày giao hàng động (có thể lấy từ Firestore hoặc tính toán)
        String deliveryDate = "Delivery by June, 29th, 4:00 PM"; // Giá trị mặc định, có thể thay bằng logic động

        // Xử lý sự kiện click cho nút Back
        btnBack.setOnClickListener(v -> finish());

        // Xử lý sự kiện click cho nút Return Home
        btnReturnHome.setOnClickListener(v -> {
            Intent intent = new Intent(OrderConfirmedActivity.this, HomeActivity.class); // Thay MainActivity bằng activity chính của bạn
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        // Xử lý sự kiện click cho nút Track Order
        btnTrackOrder.setOnClickListener(v -> {
            Intent intent = new Intent(OrderConfirmedActivity.this, OrderDetailsActivity.class); // Thay TrackOrderActivity bằng activity theo dõi đơn hàng
            intent.putExtra("orderId", orderId);
            startActivity(intent);
        });
    }
}
