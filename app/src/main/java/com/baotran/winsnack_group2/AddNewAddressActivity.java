package com.baotran.winsnack_group2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class AddNewAddressActivity extends AppCompatActivity {

    private static class Address implements Parcelable {
        private String name;
        private String details;

        public Address(String name, String details) {
            this.name = name;
            this.details = details;
        }

        protected Address(Parcel in) {
            name = in.readString();
            details = in.readString();
        }

        public static final Creator<Address> CREATOR = new Creator<Address>() {
            @Override
            public Address createFromParcel(Parcel in) {
                return new Address(in);
            }

            @Override
            public Address[] newArray(int size) {
                return new Address[size];
            }
        };

        public String getName() {
            return name;
        }

        public String getDetails() {
            return details;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(name);
            dest.writeString(details);
        }
    }

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
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Handle apply button click
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString().trim();
                String address = editTextAddress.getText().toString().trim();

                if (!name.isEmpty() && !address.isEmpty()) {
                    Address newAddress = new Address(name, address);
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("NEW_ADDRESS", newAddress);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
            }
        });
    }
}