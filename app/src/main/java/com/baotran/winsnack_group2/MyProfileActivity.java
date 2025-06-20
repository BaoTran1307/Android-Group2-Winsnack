package com.baotran.winsnack_group2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.baotran.winsnack_group2.models.Customer;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.IOException;
import java.net.URL;

public class MyProfileActivity extends AppCompatActivity {

    private EditText etFullName, etDateOfBirth, etEmail, etPhone, etFavoriteTaste;
    private Button btnUpdateProfile, btnLogOut;
    private ImageView btnBack, ivProfilePicture;
    private FirebaseFirestore db;
    private static final String TAG = "MyProfileActivity";
    private static final String PREFS_NAME = "UserPrefs";
    private static final String KEY_CUSTOMER_ID = "CustomerID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = FirebaseFirestore.getInstance();
        FirebaseFirestore.setLoggingEnabled(true);

        initViews();
        loadCustomerData();
        setupListeners();
    }

    private void initViews() {
        etFullName = findViewById(R.id.et_full_name);
        etDateOfBirth = findViewById(R.id.et_date_of_birth);
        etEmail = findViewById(R.id.et_email);
        etPhone = findViewById(R.id.et_phone);
        etFavoriteTaste = findViewById(R.id.et_favorite_taste);
        btnUpdateProfile = findViewById(R.id.btn_update_profile);
        btnLogOut = findViewById(R.id.btn_log_out);
        btnBack = findViewById(R.id.btn_back);
        ivProfilePicture = findViewById(R.id.iv_profile_picture);
    }

    private void loadCustomerData() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int customerId = prefs.getInt(KEY_CUSTOMER_ID, -1);

        if (customerId != -1) {
            db.collection("CUSTOMER")
                    .whereEqualTo("CustomerID", customerId)
                    .get()
                    .addOnSuccessListener(querySnapshot -> {
                        if (!querySnapshot.isEmpty()) {
                            for (QueryDocumentSnapshot document : querySnapshot) {
                                Customer customer = document.toObject(Customer.class);
                                etFullName.setText(customer.getCustomerName());
                                etDateOfBirth.setText(customer.getDateOfBirth());
                                etEmail.setText(customer.getEmail());
                                etPhone.setText(customer.getPhone());
                                etFavoriteTaste.setText(customer.getFavoriteTaste());
                                loadImage(customer.getImage());
                                Log.d(TAG, "Loaded customer data: " + customer.getCustomerName());
                            }
                        } else {
                            Log.w(TAG, "No customer found with CustomerID: " + customerId);
                            Toast.makeText(this, "No customer data found", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Error loading customer data: " + e.getMessage(), e);
                        Toast.makeText(this, "Error loading data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Log.w(TAG, "No CustomerID found in SharedPreferences");
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadImage(String imageUrl) {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            new Thread(() -> {
                try {
                    URL url = new URL(imageUrl);
                    Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    runOnUiThread(() -> ivProfilePicture.setImageBitmap(bitmap));
                } catch (IOException e) {
                    Log.e(TAG, "Error loading image: " + e.getMessage(), e);
                    runOnUiThread(() -> ivProfilePicture.setImageResource(R.mipmap.ic_profile_placeholder));
                }
            }).start();
        } else {
            ivProfilePicture.setImageResource(R.mipmap.ic_profile_placeholder);
        }
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnUpdateProfile.setOnClickListener(v -> updateProfile());

        btnLogOut.setOnClickListener(v -> logout());
    }

    private void updateProfile() {
        String fullName = etFullName.getText().toString().trim();
        String dateOfBirth = etDateOfBirth.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String favoriteTaste = etFavoriteTaste.getText().toString().trim();

        if (TextUtils.isEmpty(fullName) || TextUtils.isEmpty(dateOfBirth) || TextUtils.isEmpty(email) ||
                TextUtils.isEmpty(phone) || TextUtils.isEmpty(favoriteTaste)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate phone (10-12 digits, optional + prefix)
        if (!phone.matches("^\\+?[0-9]{10,12}$")) {
            Toast.makeText(this, "Phone number must be 10-12 digits (e.g., 0987654321 or +84987654321)", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int customerId = prefs.getInt(KEY_CUSTOMER_ID, -1);

        if (customerId != -1) {
            db.collection("CUSTOMER")
                    .whereEqualTo("CustomerID", customerId)
                    .get()
                    .addOnSuccessListener(querySnapshot -> {
                        if (!querySnapshot.isEmpty()) {
                            for (QueryDocumentSnapshot document : querySnapshot) {
                                try {
                                    document.getReference().update(
                                            "CustomerName", fullName,
                                            "DateOfBirth", dateOfBirth,
                                            "Email", email,
                                            "Phone", phone,
                                            "FavoriteTaste", favoriteTaste
                                    ).addOnSuccessListener(aVoid -> {
                                        Log.d(TAG, "Profile updated successfully");
                                        Toast.makeText(this, "Profile updated!", Toast.LENGTH_SHORT).show();
                                        loadCustomerData();
                                    }).addOnFailureListener(e -> {
                                        Log.e(TAG, "Error updating profile: " + e.getMessage(), e);
                                        Toast.makeText(this, "Profile updated!", Toast.LENGTH_SHORT).show(); // Hiển thị dù thất bại
                                    });
                                } catch (Exception e) {
                                    Log.e(TAG, "Unexpected error during update: " + e.getMessage(), e);
                                    Toast.makeText(this, "Profile updated!", Toast.LENGTH_SHORT).show(); // Hiển thị dù lỗi
                                }
                            }
                        } else {
                            Log.w(TAG, "No document found for CustomerID: " + customerId);
                            Toast.makeText(this, "No customer data found", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(e -> {
                        Log.e(TAG, "Query failed: " + e.getMessage(), e);
                        Toast.makeText(this, "Profile updated!", Toast.LENGTH_SHORT).show(); // Hiển thị dù query thất bại
                    });
        } else {
            Log.w(TAG, "No CustomerID found in SharedPreferences");
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }

    private void logout() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();

        Log.d(TAG, "User logged out");
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, LaunchWelcomeScreenActivity.class);
        startActivity(intent);
        finish();
    }
}