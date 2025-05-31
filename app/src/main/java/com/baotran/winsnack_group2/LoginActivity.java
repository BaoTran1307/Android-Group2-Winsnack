package com.baotran.winsnack_group2;

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
        btnBack.setOnClickListener(v -> onBackPressed());

        btnTogglePassword.setOnClickListener(v -> togglePasswordVisibility());

        btnLogin.setOnClickListener(v -> handleLogin());

        tvForgotPassword.setOnClickListener(v -> handleForgotPassword());

        tvSignUp.setOnClickListener(v -> handleSignUp());

        btnGoogle.setOnClickListener(v -> handleGoogleLogin());

        btnApple.setOnClickListener(v -> handleAppleLogin());

        btnFingerprint.setOnClickListener(v -> handleFingerprintLogin());
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            // Hide password
            etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            btnTogglePassword.setImageResource(R.mipmap.ic_eye);
            isPasswordVisible = false;
        } else {
            // Show password
            etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            btnTogglePassword.setImageResource(R.mipmap.ic_eye_off);
            isPasswordVisible = true;
        }
        // Move cursor to end
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

        // TODO: Implement actual login logic
        Toast.makeText(this, "Login clicked", Toast.LENGTH_SHORT).show();
    }

    private void handleForgotPassword() {
        // TODO: Navigate to forgot password screen
        Toast.makeText(this, "Forgot password clicked", Toast.LENGTH_SHORT).show();
    }

    private void handleSignUp() {
        // TODO: Navigate to sign up screen
        Toast.makeText(this, "Sign up clicked", Toast.LENGTH_SHORT).show();
    }

    private void handleGoogleLogin() {
        // TODO: Implement Google login
        Toast.makeText(this, "Google login clicked", Toast.LENGTH_SHORT).show();
    }

    private void handleAppleLogin() {
        // TODO: Implement Apple login
        Toast.makeText(this, "Apple login clicked", Toast.LENGTH_SHORT).show();
    }

    private void handleFingerprintLogin() {
        // TODO: Implement fingerprint login
        Toast.makeText(this, "Fingerprint login clicked", Toast.LENGTH_SHORT).show();
    }
}