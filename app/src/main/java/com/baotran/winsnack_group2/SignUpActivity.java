package com.baotran.winsnack_group2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    private EditText etPhoneNumber;
    private Button btnContinue;
    private TextView tvLogIn;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        etPhoneNumber = findViewById(R.id.et_phone_number);
        btnContinue = findViewById(R.id.btn_continue);
        tvLogIn = findViewById(R.id.tv_log_in);
        btnBack = findViewById(R.id.btn_back);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnContinue.setOnClickListener(v -> handleContinue());

        tvLogIn.setOnClickListener(v -> navigateToLogin());

        // Social login buttons
        findViewById(R.id.btn_google).setOnClickListener(v -> handleSocialLogin("Google"));
        findViewById(R.id.btn_apple).setOnClickListener(v -> handleSocialLogin("Apple"));
        findViewById(R.id.btn_fingerprint).setOnClickListener(v -> handleSocialLogin("Fingerprint"));
    }

    private void handleContinue() {
        String phoneNumber = etPhoneNumber.getText().toString().trim();

        if (phoneNumber.isEmpty()) {
            Toast.makeText(this, "Please enter phone number", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidPhoneNumber(phoneNumber)) {
            Toast.makeText(this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
            return;
        }

        // Send OTP logic here
        Toast.makeText(this, "OTP sent to " + phoneNumber, Toast.LENGTH_SHORT).show();

        // Navigate to OTP verification
        Intent intent = new Intent(this, OtpVerificationActivity.class);
        intent.putExtra("phone_number", phoneNumber);
        startActivity(intent);
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.length() >= 10 && phoneNumber.matches("\\d+");
    }

    private void navigateToLogin() {
        Intent intent = new Intent(this, ForgetPasswordActivity.class);
        startActivity(intent);
    }

    private void handleSocialLogin(String provider) {
        Toast.makeText(this, "Sign up with " + provider, Toast.LENGTH_SHORT).show();
        // Implement social login logic here
    }
}