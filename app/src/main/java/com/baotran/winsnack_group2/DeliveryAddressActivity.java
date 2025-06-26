package com.baotran.winsnack_group2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class DeliveryAddressActivity extends AppCompatActivity {

    private static final int ADD_ADDRESS_REQUEST_CODE = 1001;
    private List<Address> addressList;

    // Address class (Parcelable)
    public static class Address implements Parcelable {
        private String name;
        private String details;
        private boolean selected;

        public Address(String name, String details, boolean selected) {
            this.name = name;
            this.details = details;
            this.selected = selected;
        }

        protected Address(Parcel in) {
            name = in.readString();
            details = in.readString();
            selected = in.readByte() != 0;
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

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(name);
            dest.writeString(details);
            dest.writeByte((byte) (selected ? 1 : 0));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_address);

        // Initialize address list
        addressList = new ArrayList<>();
        addressList.add(new Address("My Home", "778 Locust View Drive Oakland, CA", true));
        addressList.add(new Address("AnkTranDan's House", "778 Locust View Drive Oakland, CA", false));
        addressList.add(new Address("TyParent's House", "778 Locust View Drive Oakland, CA", false));

        // Find views
        ImageView btnBack = findViewById(R.id.btn_back);
        Button btnAddNewAddress = findViewById(R.id.btn_add_new_address);
        LinearLayout addressContainer = (LinearLayout) ((ScrollView) findViewById(R.id.scrollView)).getChildAt(0);
        // Handle back button click
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Handle add new address button click
        btnAddNewAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeliveryAddressActivity.this, AddNewAddressActivity.class);
                startActivityForResult(intent, ADD_ADDRESS_REQUEST_CODE);
            }
        });

        // Handle address selection
        setupAddressSelection(addressContainer);
    }

    private void setupAddressSelection(LinearLayout addressContainer) {
        // Set click listeners for each address item
        for (int i = 0; i < addressContainer.getChildCount() - 1; i++) { // Exclude the button
            final int index = i;
            LinearLayout addressLayout = (LinearLayout) addressContainer.getChildAt(i);
            addressLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Update selection state
                    for (int j = 0; j < addressList.size(); j++) {
                        addressList.get(j).setSelected(j == index);
                    }

                    // Update radio button visuals
                    updateRadioButtons(addressContainer);

                    // Return the selected address to the calling activity
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("SELECTED_ADDRESS", addressList.get(index));
                    setResult(RESULT_OK, resultIntent);
                }
            });
        }

        // Initial radio button state
        updateRadioButtons(addressContainer);
    }

    private void updateRadioButtons(LinearLayout addressContainer) {
        for (int i = 0; i < addressContainer.getChildCount() - 1; i++) {
            LinearLayout addressLayout = (LinearLayout) addressContainer.getChildAt(i);
            ImageView radioButton = (ImageView) addressLayout.getChildAt(2); // Radio button is the 3rd child
            radioButton.setImageResource(
                    addressList.get(i).isSelected() ? R.drawable.ic_radio_button_checked : R.drawable.ic_radio_button
            );
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_ADDRESS_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Address newAddress = data.getParcelableExtra("NEW_ADDRESS");
            if (newAddress != null) {
                addressList.add(newAddress);
                // Refresh the address list UI
                recreate();
            }
        }
    }
}