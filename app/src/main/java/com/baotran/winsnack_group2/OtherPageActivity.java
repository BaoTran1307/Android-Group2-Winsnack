package com.baotran.winsnack_group2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class OtherPageActivity extends FooterActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orther_page);
        setupFooter();
        // Ánh xạ các nút
        Button btnOrders = findViewById(R.id.btnOrders);
        Button btnProfile = findViewById(R.id.btnProfile);
        Button btnDeliveryAddress = findViewById(R.id.btnDeliveryAddress);
        Button btnPaymentMethod = findViewById(R.id.btnPaymentMethod);
        Button btnBlog = findViewById(R.id.btnBlog);
        Button btnContactUs = findViewById(R.id.btnContactUs);
        Button btnFAQs = findViewById(R.id.btnFAQs);
        Button btnPolicy = findViewById(R.id.btnPolicy);
        Button btnSettings = findViewById(R.id.btnSettings);
        Button btnLogOut = findViewById(R.id.btnLogOut);

        // Gán sự kiện chuyển trang
        btnOrders.setOnClickListener(v -> startActivity(new Intent(this, OrderHistoryActivity.class)));
        btnProfile.setOnClickListener(v -> startActivity(new Intent(this, MyProfileActivity.class)));
        btnDeliveryAddress.setOnClickListener(v -> startActivity(new Intent(this, DeliveryAddressActivity.class)));
        btnPaymentMethod.setOnClickListener(v -> startActivity(new Intent(this, PaymentMethodActivity.class)));
        btnBlog.setOnClickListener(v -> startActivity(new Intent(this, BlogActivity.class)));
        btnContactUs.setOnClickListener(v -> startActivity(new Intent(this, ContactUsActivity.class)));
        btnFAQs.setOnClickListener(v -> startActivity(new Intent(this, FAQsActivity.class)));
        btnPolicy.setOnClickListener(v -> startActivity(new Intent(this, PolicyActivity.class)));
//        btnSettings.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));

        // Đăng xuất
        btnLogOut.setOnClickListener(v -> {
            // Nếu có sử dụng Firebase Authentication:
            // FirebaseAuth.getInstance().signOut();

            Intent intent = new Intent(this, LaunchWelcomeScreenActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Xóa lịch sử
            startActivity(intent);
            finish();
        });
    }
}
