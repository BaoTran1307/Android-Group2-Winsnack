package com.baotran.winsnack_group2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class OtpVerificationActivity extends AppCompatActivity {

    private EditText etOtpCode;
    private Button btnContinue;
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        phoneNumber = getIntent().getStringExtra("phone_number");

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        etOtpCode = findViewById(R.id.et_otp_code);
        btnContinue = findViewById(R.id.btn_continue);
    }

    private void setupClickListeners() {
        btnContinue.setOnClickListener(v -> handleContinue());
    }

    private void handleContinue() {
        Intent intent = new Intent(this, SignUpMainActivity.class);
        intent.putExtra("phone_number", phoneNumber);
        startActivity(intent);
        finish();
    }
}