package com.baotran.winsnack_group2;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ForgetPasswordActivity extends AppCompatActivity {

    private EditText etPassword, etConfirmPassword;
    private ImageView ivPasswordToggle, ivConfirmPasswordToggle;
    private Button btnLogin;
    private TextView tvSignUp;
    private ImageView btnBack;
    private boolean isPasswordVisible = false;
    private boolean isConfirmPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        ivPasswordToggle = findViewById(R.id.iv_password_toggle);
        ivConfirmPasswordToggle = findViewById(R.id.iv_confirm_password_toggle);
        btnLogin = findViewById(R.id.btn_login);
        tvSignUp = findViewById(R.id.tv_sign_up);
        btnBack = findViewById(R.id.btn_back);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());

        ivPasswordToggle.setOnClickListener(v -> togglePasswordVisibility(etPassword, ivPasswordToggle, isPasswordVisible));

        ivConfirmPasswordToggle.setOnClickListener(v -> togglePasswordVisibility(etConfirmPassword, ivConfirmPasswordToggle, isConfirmPasswordVisible));

        btnLogin.setOnClickListener(v -> handleLogin());

        tvSignUp.setOnClickListener(v -> navigateToSignUp());

        // Social login buttons
        findViewById(R.id.btn_google).setOnClickListener(v -> handleSocialLogin("Google"));
        findViewById(R.id.btn_apple).setOnClickListener(v -> handleSocialLogin("Apple"));
        findViewById(R.id.btn_fingerprint).setOnClickListener(v -> handleSocialLogin("Fingerprint"));
    }

    private void togglePasswordVisibility(EditText editText, ImageView toggleButton, boolean isVisible) {
        if (isVisible) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            toggleButton.setImageResource(R.mipmap.ic_eye);
        } else {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            toggleButton.setImageResource(R.mipmap.ic_eye_off);
        }
        editText.setSelection(editText.getText().length());
        isVisible = !isVisible;
    }

    private void handleLogin() {
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (password.isEmpty()) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        if (confirmPassword.isEmpty()) {
            Toast.makeText(this, "Please confirm password", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // Handle password reset logic here
        Toast.makeText(this, "Password reset successfully", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void navigateToSignUp() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    private void handleSocialLogin(String provider) {
        Toast.makeText(this, "Login with " + provider, Toast.LENGTH_SHORT).show();
        // Implement social login logic here
    }
}