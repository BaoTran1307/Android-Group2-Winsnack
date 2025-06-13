package com.baotran.winsnack_group2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class FingerprintSetupActivity extends AppCompatActivity {

    private ImageView btnBack, ivFingerprint;
    private Button btnSkip, btnContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fingerprint_setup);

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
            Toast.makeText(this, "Fingerprint setup skipped", Toast.LENGTH_SHORT).show();
            finish();
        });

        btnContinue.setOnClickListener(v -> {
            // Chuyển sang FingerprintActiveActivity
            Intent intent = new Intent(FingerprintSetupActivity.this, FingerprintActiveActivity.class);
            startActivity(intent);
        });
    }
}