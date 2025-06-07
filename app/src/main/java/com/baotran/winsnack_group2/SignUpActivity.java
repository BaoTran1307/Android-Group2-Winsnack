package com.baotran.winsnack_group2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
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
    private ImageView btnBack, btnGoogle, btnApple, btnFingerprint;

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
        btnGoogle = findViewById(R.id.btn_google);
        btnApple = findViewById(R.id.btn_apple);
        btnFingerprint = findViewById(R.id.btn_fingerprint);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnContinue.setOnClickListener(v -> handleContinue());

        btnGoogle.setOnClickListener(v -> Toast.makeText(this, "Google Sign Up - Coming Soon", Toast.LENGTH_SHORT).show());
        btnApple.setOnClickListener(v -> Toast.makeText(this, "Apple Sign Up - Coming Soon", Toast.LENGTH_SHORT).show());
        btnFingerprint.setOnClickListener(v -> Toast.makeText(this, "Fingerprint Sign Up - Coming Soon", Toast.LENGTH_SHORT).show());

        tvLogIn.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    private void handleContinue() {
        String phoneNumber = etPhoneNumber.getText().toString().trim();

        if (TextUtils.isEmpty(phoneNumber)) {
            etPhoneNumber.setError("Please enter phone number");
            etPhoneNumber.requestFocus();
            return;
        }

        if (!isValidPhoneNumber(phoneNumber)) {
            etPhoneNumber.setError("Please enter a valid phone number (10+ digits)");
            etPhoneNumber.requestFocus();
            return;
        }

        SharedPreferences prefs = getSharedPreferences("MockUsers", MODE_PRIVATE);
        if (prefs.contains(phoneNumber)) {
            etPhoneNumber.setError("Phone number already exists");
            etPhoneNumber.requestFocus();
            return;
        }

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(phoneNumber, "default123");
        editor.putString(phoneNumber + "_username", "PhoneUser_" + phoneNumber);
        editor.apply();

        Toast.makeText(this, "Sign up successful! OTP verified.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("USERNAME", "PhoneUser_" + phoneNumber);
        startActivity(intent);
        finish();
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.length() >= 10 && phoneNumber.matches("\\d+");
    }
}