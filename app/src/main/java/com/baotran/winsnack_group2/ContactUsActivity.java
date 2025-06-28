package com.baotran.winsnack_group2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class ContactUsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        // Back về trang khác (OtherPageActivity)
        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(view -> {
            Intent intent = new Intent(ContactUsActivity.this, OtherPageActivity.class);
            startActivity(intent);
            finish();
        });

        // Nút Contact Us (hiện tại đang ở trang này, không cần xử lý gì)

        // Nút FAQs
        Button btnFaqs = findViewById(R.id.btnFaqs);
        btnFaqs.setOnClickListener(view -> {
            Intent intent = new Intent(ContactUsActivity.this, FAQsActivity.class);
            startActivity(intent);
        });
    }
}
