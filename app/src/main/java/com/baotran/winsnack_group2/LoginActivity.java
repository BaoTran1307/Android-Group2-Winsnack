package com.baotran.winsnack_group2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.baotran.winsnack_group2.models.Customer;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmailPhone, etPassword;
    private Button btnLogin;
    private ImageView btnBack, btnTogglePassword, btnGoogle, btnApple, btnFingerprint;
    private TextView tvForgotPassword, tvSignUp;
    private boolean isPasswordVisible = false;
    private FirebaseFirestore db;
    private static final String TAG = "LoginActivity";
    private static final String PREFS_NAME = "UserPrefs";
    private static final String KEY_CUSTOMER_ID = "CustomerID";
    private static final String KEY_USERNAME = "Username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = FirebaseFirestore.getInstance();
        FirebaseFirestore.setLoggingEnabled(true);

        // Print all documents for debug
        db.collection("CUSTOMER").get().addOnSuccessListener(snapshot -> {
            for (QueryDocumentSnapshot doc : snapshot) {
                Log.d(TAG, "Document ID: " + doc.getId() + ", Data: " + doc.getData().toString());
            }
        }).addOnFailureListener(e -> Log.e(TAG, "Query error: ", e));

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
        btnFingerprint.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(LoginActivity.this, FingerprintSetupActivity.class);
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(this, "Error opening Fingerprint Setup: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

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
        String emailPhone = etEmailPhone.getText().toString().trim(); // No .toLowerCase()
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

        btnLogin.setEnabled(false);
        Toast.makeText(this, "Logging in...", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Input email/phone: " + emailPhone + ", Password: " + password);

        db.collection("CUSTOMER")
                .whereEqualTo("Email", emailPhone)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Email query successful, number of documents: " + task.getResult().size());
                        if (!task.getResult().isEmpty()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Customer customer = document.toObject(Customer.class);
                                Log.d(TAG, "Found email: " + customer.getEmail() + ", Password: " + customer.getPassword());
                                if (customer.getPassword().equals(password)) {
                                    loginSuccess(customer);
                                    return;
                                } else {
                                    Log.d(TAG, "Password mismatch: Input=" + password + ", DB=" + customer.getPassword());
                                    loginFailed("Incorrect password");
                                    return;
                                }
                            }
                        } else {
                            Log.d(TAG, "Email not found, trying phone...");
                            db.collection("CUSTOMER")
                                    .whereEqualTo("Phone", emailPhone)
                                    .get()
                                    .addOnCompleteListener(phoneTask -> {
                                        if (phoneTask.isSuccessful()) {
                                            Log.d(TAG, "Phone query successful, number of documents: " + phoneTask.getResult().size());
                                            if (!phoneTask.getResult().isEmpty()) {
                                                for (QueryDocumentSnapshot document : phoneTask.getResult()) {
                                                    Customer customer = document.toObject(Customer.class);
                                                    Log.d(TAG, "Found phone: " + customer.getPhone() + ", Password: " + customer.getPassword());
                                                    if (customer.getPassword().equals(password)) {
                                                        loginSuccess(customer);
                                                        return;
                                                    } else {
                                                        Log.d(TAG, "Password mismatch: Input=" + password + ", DB=" + customer.getPassword());
                                                        loginFailed("Incorrect password");
                                                        return;
                                                    }
                                                }
                                            } else {
                                                Log.d(TAG, "Phone not found");
                                                loginFailed("Email or phone number does not exist");
                                            }
                                        } else {
                                            Log.e(TAG, "Phone query failed: ", phoneTask.getException());
                                            loginFailed("Query error: " + phoneTask.getException().getMessage());
                                        }
                                    });
                        }
                    } else {
                        Log.e(TAG, "Email query failed: ", task.getException());
                        loginFailed("Query error: " + task.getException().getMessage());
                    }
                });
    }

    private void loginSuccess(Customer customer) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_CUSTOMER_ID, customer.getCustomerID());
        editor.putString(KEY_USERNAME, customer.getUsername());
        editor.apply();

        Log.d(TAG, "Login successful for user: " + customer.getUsername());
        Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("USERNAME", customer.getUsername());
        startActivity(intent);
        finish();
        btnLogin.setEnabled(true);
    }

    private void loginFailed(String message) {
        btnLogin.setEnabled(true);
        Toast.makeText(this, "Login failed: " + message, Toast.LENGTH_LONG).show();
    }
}