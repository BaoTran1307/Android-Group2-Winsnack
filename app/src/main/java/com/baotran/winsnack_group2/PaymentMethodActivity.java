package com.baotran.winsnack_group2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class PaymentMethodActivity extends AppCompatActivity {
    private CardView cashCard, visaCard, mastercardCard;
    private ImageView cashCheck, visaCheck, mastercardCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);

        ImageView backButton = findViewById(R.id.btn_back);
        TextView titleText = findViewById(R.id.title_text);
        cashCard = findViewById(R.id.cash_card);
        visaCard = findViewById(R.id.visa_card);
        mastercardCard = findViewById(R.id.mastercard_card);
        cashCheck = findViewById(R.id.cash_check);
        visaCheck = findViewById(R.id.visa_check);
        mastercardCheck = findViewById(R.id.mastercard_check);
        TextView selectedMethod = findViewById(R.id.selected_method);
        CardView addNewCard = findViewById(R.id.add_new_card);

        titleText.setText("Payment Method");

        backButton.setOnClickListener(v -> onBackPressed());

        cashCard.setOnClickListener(v -> {
            resetSelections();
            cashCard.setCardBackgroundColor(getResources().getColor(android.R.color.holo_orange_light));
            cashCheck.setVisibility(View.VISIBLE);
            selectedMethod.setText("Cash");
            returnResult("Cash", "");
        });
        visaCard.setOnClickListener(v -> {
            resetSelections();
            visaCard.setCardBackgroundColor(getResources().getColor(android.R.color.holo_orange_light));
            visaCheck.setVisibility(View.VISIBLE);
            selectedMethod.setText("Visa");
            returnResult("Visa", "**** **** **** 436");
        });
        mastercardCard.setOnClickListener(v -> {
            resetSelections();
            mastercardCard.setCardBackgroundColor(getResources().getColor(android.R.color.holo_orange_light));
            mastercardCheck.setVisibility(View.VISIBLE);
            selectedMethod.setText("Mastercard");
            returnResult("Mastercard", "**** **** **** 436");
        });
        addNewCard.setOnClickListener(v -> {
            // Handle add new payment method
        });

        // Default selection
        mastercardCard.setCardBackgroundColor(getResources().getColor(android.R.color.holo_orange_light));
        mastercardCheck.setVisibility(View.VISIBLE);
        selectedMethod.setText("Mastercard");
    }

    private void resetSelections() {
        cashCard.setCardBackgroundColor(getResources().getColor(android.R.color.white));
        visaCard.setCardBackgroundColor(getResources().getColor(android.R.color.white));
        mastercardCard.setCardBackgroundColor(getResources().getColor(android.R.color.white));
        cashCheck.setVisibility(View.GONE);
        visaCheck.setVisibility(View.GONE);
        mastercardCheck.setVisibility(View.GONE);
    }

    private void returnResult(String method, String code) {
        Intent intent = new Intent();
        intent.putExtra("payment_method", method);
        intent.putExtra("payment_code", code);
        setResult(RESULT_OK, intent);
        finish();
    }
}