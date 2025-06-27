package com.baotran.winsnack_group2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.baotran.winsnack_group2.models.Address;

public class AddNewAddressActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_address);

        // Find views
        ImageView btnBack = findViewById(R.id.btn_back);
        EditText editTextName = findViewById(R.id.edit_text_name);
        EditText editTextAddress = findViewById(R.id.edit_text_address);
        Button btnApply = findViewById(R.id.btn_apply);

        // Set initial values from XML
        editTextName.setText("Anna House");
        editTextAddress.setText("778 Locust View Drive Oakland, CA");

        // Handle back button click
        btnBack.setOnClickListener(v -> finish());

        // Handle apply button click
        btnApply.setOnClickListener(v -> {
            String name = editTextName.getText().toString().trim();
            String address = editTextAddress.getText().toString().trim();

            if (!name.isEmpty() && !address.isEmpty()) {
                Address newAddress = new Address(name, address, true); // Đặt selected = true cho địa chỉ mới
                Intent resultIntent = new Intent();
                resultIntent.putExtra("NEW_ADDRESS", newAddress);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}
