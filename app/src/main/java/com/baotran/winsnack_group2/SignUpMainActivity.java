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

public class SignUpMainActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_sign_up_main);

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        etUsername = findViewById(R.id.et_username);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        btnSignUp = findViewById(R.id.btn_sign_up);
        btnBack = findViewById(R.id.btn_back);
        ivPasswordToggle = findViewById(R.id.iv_password_toggle);
        ivConfirmPasswordToggle = findViewById(R.id.iv_confirm_password_toggle);
        btnGoogle = findViewById(R.id.btn_google);
        btnApple = findViewById(R.id.btn_apple);
        btnFingerprint = findViewById(R.id.btn_fingerprint);
        tvLogin = findViewById(R.id.tv_login);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnSignUp.setOnClickListener(v -> handleSignUp());

        ivPasswordToggle.setOnClickListener(v -> togglePasswordVisibility());
        ivConfirmPasswordToggle.setOnClickListener(v -> toggleConfirmPasswordVisibility());

        btnGoogle.setOnClickListener(v -> Toast.makeText(this, "Google Sign Up - Coming Soon", Toast.LENGTH_SHORT).show());
        btnApple.setOnClickListener(v -> Toast.makeText(this, "Apple Sign Up - Coming Soon", Toast.LENGTH_SHORT).show());
        btnFingerprint.setOnClickListener(v -> Toast.makeText(this, "Fingerprint Sign Up - Coming Soon", Toast.LENGTH_SHORT).show());

        tvLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
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

        if (!isValidPassword(password)) {
            etPassword.setError("Password must be at least 8 characters, including letters and numbers");
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

        // Check if email exists
        SharedPreferences prefs = getSharedPreferences("MockUsers", MODE_PRIVATE);
        if (prefs.contains(email)) {
            etEmail.setError("Email already exists");
            etEmail.requestFocus();
            return;
        }

        // Save user data
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(email, password);
        editor.putString(email + "_username", username);
        editor.apply();

        Toast.makeText(this, "Sign up successful!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("USERNAME", username);
        startActivity(intent);
        finish();
    }

    private void togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible;
        etPassword.setTransformationMethod(isPasswordVisible ? null : PasswordTransformationMethod.getInstance());
        ivPasswordToggle.setImageResource(isPasswordVisible ? android.R.drawable.ic_menu_close_clear_cancel : android.R.drawable.ic_menu_view);
        etPassword.setSelection(etPassword.getText().length());
    }

    private void toggleConfirmPasswordVisibility() {
        isConfirmPasswordVisible = !isConfirmPasswordVisible;
        etConfirmPassword.setTransformationMethod(isConfirmPasswordVisible ? null : PasswordTransformationMethod.getInstance());
        ivConfirmPasswordToggle.setImageResource(isConfirmPasswordVisible ? android.R.drawable.ic_menu_close_clear_cancel : android.R.drawable.ic_menu_view);
        etConfirmPassword.setSelection(etConfirmPassword.getText().length());
    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 8 && password.matches(".*[a-zA-Z].*") && password.matches(".*\\d.*");
    }
}