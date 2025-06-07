package com.baotran.winsnack_group2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmailPhone, etPassword;
    private Button btnLogin;
    private ImageView btnBack, btnTogglePassword, btnGoogle, btnApple, btnFingerprint;
    private TextView tvForgotPassword, tvSignUp;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        setupListeners();
    }

    private void initViews() {
        etEmailPhone = findViewById(R.id.et_email_phone);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        btnBack = findViewById(R.id.btn_back);
        btnTogglePassword = findViewById(R.id.btn_toggle_password);
        btnGoogle = findViewById(R.id.btn_google);
        btnApple = findViewById(R.id.btn_apple);
        btnFingerprint = findViewById(R.id.btn_fingerprint);
        tvForgotPassword = findViewById(R.id.tv_forgot_password);
        tvSignUp = findViewById(R.id.tv_sign_up);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnTogglePassword.setOnClickListener(v -> togglePasswordVisibility());

        btnLogin.setOnClickListener(v -> handleLogin());

        tvForgotPassword.setOnClickListener(v -> Toast.makeText(this, "Forgot password not implemented", Toast.LENGTH_SHORT).show());

        tvSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(this, SignUpMainActivity.class);
            startActivity(intent);
        });

        btnGoogle.setOnClickListener(v -> Toast.makeText(this, "Google login not implemented", Toast.LENGTH_SHORT).show());

        btnApple.setOnClickListener(v -> Toast.makeText(this, "Apple login not implemented", Toast.LENGTH_SHORT).show());

        btnFingerprint.setOnClickListener(v -> Toast.makeText(this, "Fingerprint login not implemented", Toast.LENGTH_SHORT).show());
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            btnTogglePassword.setImageResource(R.mipmap.ic_eye);
            isPasswordVisible = false;
        } else {
            etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            btnTogglePassword.setImageResource(R.mipmap.ic_eye_off);
            isPasswordVisible = true;
        }
        etPassword.setSelection(etPassword.getText().length());
    }

    private void handleLogin() {
        String emailPhone = etEmailPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (emailPhone.isEmpty()) {
            etEmailPhone.setError("Please enter email or phone number");
            etEmailPhone.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            etPassword.setError("Please enter password");
            etPassword.requestFocus();
            return;
        }

        // Kiểm tra thông tin đăng nhập với SharedPreferences
        SharedPreferences prefs = getSharedPreferences("MockUsers", MODE_PRIVATE);
        String storedPassword = prefs.getString(emailPhone, null);

        if (storedPassword != null && storedPassword.equals(password)) {
            Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Invalid email/phone or password", Toast.LENGTH_SHORT).show();
        }
    }
}