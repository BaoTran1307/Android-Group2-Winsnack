package com.baotran.winsnack_group2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class FingerprintActiveActivity extends AppCompatActivity {

    private ImageView btnBack;
    private Button btnSkip, btnContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fingerprint_active); // Sử dụng layout đúng

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btn_back);
        btnSkip = findViewById(R.id.btn_skip);
        btnContinue = findViewById(R.id.btn_continue);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnSkip.setOnClickListener(v -> {
            finish();
        });

        btnContinue.setOnClickListener(v -> {
            // Chuyển sang HomeActivity
            Intent intent = new Intent(FingerprintActiveActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });
    }
}