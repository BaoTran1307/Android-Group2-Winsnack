package com.baotran.winsnack_group2;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    private EditText etUsername, etEmail, etPassword, etConfirmPassword;
    private Button btnSignUp;
    private ImageView btnBack, ivPasswordToggle, ivConfirmPasswordToggle;
    private ImageView btnGoogle, btnApple, btnFingerprint;
    private TextView tvLogin;

    private boolean isPasswordVisible = false;
    private boolean isConfirmPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        // Input fields
        etUsername = findViewById(R.id.et_username);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);

        // Buttons
        btnSignUp = findViewById(R.id.btn_sign_up);
        btnBack = findViewById(R.id.btn_back);

        // Password toggle icons
        ivPasswordToggle = findViewById(R.id.iv_password_toggle);
        ivConfirmPasswordToggle = findViewById(R.id.iv_confirm_password_toggle);

        // Social login buttons
        btnGoogle = findViewById(R.id.btn_google);
        btnApple = findViewById(R.id.btn_apple);
        btnFingerprint = findViewById(R.id.btn_fingerprint);

        // Login link
        tvLogin = findViewById(R.id.tv_login);
    }

    private void setupClickListeners() {
        // Back button
        btnBack.setOnClickListener(v -> finish());

        // Sign up button
        btnSignUp.setOnClickListener(v -> handleSignUp());

        // Password visibility toggles
        ivPasswordToggle.setOnClickListener(v -> togglePasswordVisibility());
        ivConfirmPasswordToggle.setOnClickListener(v -> toggleConfirmPasswordVisibility());

        // Social login buttons
        btnGoogle.setOnClickListener(v -> handleGoogleSignUp());
        btnApple.setOnClickListener(v -> handleAppleSignUp());
        btnFingerprint.setOnClickListener(v -> navigateToFingerprintSetup());

        // Login link
        tvLogin.setOnClickListener(v -> navigateToLogin());
    }

    private void handleSignUp() {
        String username = etUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // Validation
        if (TextUtils.isEmpty(username)) {
            etUsername.setError("Username is required");
            etUsername.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return;
        }

        if (!isValidEmail(email)) {
            etEmail.setError("Please enter a valid email");
            etEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            etPassword.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            etConfirmPassword.setError("Please confirm your password");
            etConfirmPassword.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match");
            etConfirmPassword.requestFocus();
            return;
        }

        // Perform sign up
        performSignUp(username, email, password);
    }

    private void performSignUp(String username, String email, String password) {
        // Show loading state
        btnSignUp.setEnabled(false);
        btnSignUp.setText("Signing Up...");

        // TODO: Implement actual sign up logic here
        // This could include API calls, Firebase authentication, etc.

        // Simulate sign up process
        new android.os.Handler().postDelayed(() -> {
            // Reset button state
            btnSignUp.setEnabled(true);
            btnSignUp.setText(R.string.sign_up);

            // Show success message
            Toast.makeText(this, "Sign up successful!", Toast.LENGTH_SHORT).show();

            // Navigate to fingerprint setup or main activity
            navigateToFingerprintSetup();
        }, 2000);
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            ivPasswordToggle.setImageResource(android.R.drawable.ic_menu_view);
        } else {
            etPassword.setTransformationMethod(null);
            ivPasswordToggle.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
        }
        isPasswordVisible = !isPasswordVisible;
        etPassword.setSelection(etPassword.getText().length());
    }

    private void toggleConfirmPasswordVisibility() {
        if (isConfirmPasswordVisible) {
            etConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            ivConfirmPasswordToggle.setImageResource(android.R.drawable.ic_menu_view);
        } else {
            etConfirmPassword.setTransformationMethod(null);
            ivConfirmPasswordToggle.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
        }
        isConfirmPasswordVisible = !isConfirmPasswordVisible;
        etConfirmPassword.setSelection(etConfirmPassword.getText().length());
    }

    private void handleGoogleSignUp() {
        // TODO: Implement Google Sign-In
        Toast.makeText(this, "Google Sign Up - Coming Soon", Toast.LENGTH_SHORT).show();
    }

    private void handleAppleSignUp() {
        // TODO: Implement Apple Sign-In
        Toast.makeText(this, "Apple Sign Up - Coming Soon", Toast.LENGTH_SHORT).show();
    }

    private void navigateToFingerprintSetup() {
        Intent intent = new Intent(this, FingerprintSetupActivity.class);
        startActivity(intent);
    }

    private void navigateToLogin() {
        // Navigate to login activity
        // Intent intent = new Intent(this, LoginActivity.class);
        // startActivity(intent);
        finish();
    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}