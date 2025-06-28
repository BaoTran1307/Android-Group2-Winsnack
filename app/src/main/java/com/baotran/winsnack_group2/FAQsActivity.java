package com.baotran.winsnack_group2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class FAQsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us_faqs);

        // Nút back → quay về trang khác (ví dụ OtherPageActivity)
        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(view -> {
            Intent intent = new Intent(FAQsActivity.this, OtherPageActivity.class);
            startActivity(intent);
            finish();
        });

        // Nút Contact Us → chuyển trang ContactUsActivity
        Button btnContactUs = findViewById(R.id.btnContactUs);
        btnContactUs.setOnClickListener(view -> {
            Intent intent = new Intent(FAQsActivity.this, ContactUsActivity.class);
            startActivity(intent);
            finish();
        });

        // Nút FAQs không xử lý gì (đang ở trang này rồi)
    }
}
