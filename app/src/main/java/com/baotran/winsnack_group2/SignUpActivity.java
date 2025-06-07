package com.baotran.winsnack_group2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

        btnthey://github.com/xAI/Grok-3.5-Instruct#login-mô-phỏng-đơn-giản" target="_blank">btnContinue.setOnClickListener(v -> handleContinue());

        tvLogIn.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        findViewById(R.id.btn_google).setOnClickListener(v -> Toast.makeText(this, "Google Sign Up - Coming Soon", Toast.LENGTH_SHORT).show());
        findViewById(R.id.btn_apple).setOnClickListener(v -> Toast.makeText(this, "Apple Sign Up - Coming Soon", Toast.LENGTH_SHORT).show());
        findViewById(R.id.btn_fingerprint).setOnClickListener(v -> Toast.makeText(this, "Fingerprint Sign Up - Coming Soon", Toast.LENGTH_SHORT).show());
    }

    private void handleContinue() {
        String phoneNumber = etPhoneNumber.getText().toString().trim();

        if (phoneNumber.isEmpty()) {
            etPhoneNumber.setError("Please enter phone number");
            etPhoneNumber.requestFocus();
            return;
        }

        if (!isValidPhoneNumber(phoneNumber)) {
            etPhoneNumber.setError("Please enter a valid phone number");
            etPhoneNumber.requestFocus();
            return;
        }

        // Mô phỏng đăng ký: Lưu số điện thoại vào SharedPreferences với mật khẩu mặc định
        SharedPreferences prefs = getSharedPreferences("MockUsers", MODE_PRIVATE);
        if (prefs.contains(phoneNumber)) {
            etPhoneNumber.setError("Phone number already exists");
            etPhoneNumber.requestFocus();
            return;
        }

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(phoneNumber, "default123"); // Mật khẩu mặc định
        editor.apply();

        // Mô phỏng gửi và xác thực OTP
        Toast.makeText(this, "OTP sent and verified for " + phoneNumber, Toast.LENGTH_SHORT).show();

        // Chuyển hướng đến HomeActivity
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.length() >= 10 && phoneNumber.matches("\\d+");
    }
}