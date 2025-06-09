package com.baotran.winsnack_group2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
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

        btnGoogle.setOnClickListener(v -> Toast.makeText(this, "Google Login - Coming Soon", Toast.LENGTH_SHORT).show());
        btnApple.setOnClickListener(v -> Toast.makeText(this, "Apple Login - Coming Soon", Toast.LENGTH_SHORT).show());
        btnFingerprint.setOnClickListener(v -> Toast.makeText(this, "Fingerprint Login - Coming Soon", Toast.LENGTH_SHORT).show());

        tvForgotPassword.setOnClickListener(v -> Toast.makeText(this, "Forgot password not implemented", Toast.LENGTH_SHORT).show());

        tvSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(this, SignUpMainActivity.class);
            startActivity(intent);
        });
    }

    private void togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible;
        etPassword.setTransformationMethod(isPasswordVisible ? null : PasswordTransformationMethod.getInstance());
        btnTogglePassword.setImageResource(isPasswordVisible ? android.R.drawable.ic_menu_close_clear_cancel : android.R.drawable.ic_menu_view);
        etPassword.setSelection(etPassword.getText().length());
    }

    private void handleLogin() {
        String emailPhone = etEmailPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(emailPhone)) {
            etEmailPhone.setError("Please enter email or phone number");
            etEmailPhone.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Please enter password");
            etPassword.requestFocus();
            return;
        }

        SharedPreferences prefs = getSharedPreferences("MockUsers", MODE_PRIVATE);
        String storedPassword = prefs.getString(emailPhone, null);
        String username = prefs.getString(emailPhone + "_username", "User");

        if (storedPassword != null && storedPassword.equals(password)) {
            Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra("USERNAME", username);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Invalid email/phone or password", Toast.LENGTH_SHORT).show();
        }
    }
}