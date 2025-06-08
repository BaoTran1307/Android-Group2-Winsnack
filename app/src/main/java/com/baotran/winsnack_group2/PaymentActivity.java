package com.baotran.winsnack_group2;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;


public class PaymentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // Hardcoded data
        TextView shippingAddress = findViewById(R.id.shipping_address);
        shippingAddress.setText("Anh Nguyen | (+84) 123456789\n778 Locust View Drive Oakland, CA");

        TextView strawberryShake = findViewById(R.id.strawberry_shake);
        strawberryShake.setText("Strawberry Shake\n29 Nov, 15:20 pm\n$20.00\n2 items");

        TextView broccoliLasagna = findViewById(R.id.broccoli_lasagna);
        broccoliLasagna.setText("Broccoli Lasagna\n29 Nov, 12:00 pm\n$12.99\n1 item");

        TextView paymentMethod = findViewById(R.id.payment_method);
        paymentMethod.setText("Credit Card **** **** **** 4300 / 0000");

        TextView deliveryTime = findViewById(R.id.delivery_time);
        deliveryTime.setText("Estimated Delivery\n2 days");

        TextView subtotal = findViewById(R.id.subtotal);
        subtotal.setText("$32.00");

        TextView taxFees = findViewById(R.id.tax_fees);
        taxFees.setText("$5.00");

        TextView delivery = findViewById(R.id.delivery);
        delivery.setText("$3.00");

        TextView total = findViewById(R.id.total);
        total.setText("$40.00");

        // Database-driven data (commented out)
        /*
        // Assume we have a DatabaseHelper class to fetch data
        DatabaseHelper db = new DatabaseHelper(this);
        String address = db.getShippingAddress();
        shippingAddress.setText(address);

        String strawberryData = db.getItemDetails("strawberry_shake");
        strawberryShake.setText(strawberryData);

        String broccoliData = db.getItemDetails("broccoli_lasagna");
        broccoliLasagna.setText(broccoliData);

        String payment = db.getPaymentMethod();
        paymentMethod.setText(payment);

        String deliveryInfo = db.getDeliveryTime();
        deliveryTime.setText(deliveryInfo);

        String sub = db.getSubtotal();
        subtotal.setText(sub);

        String tax = db.getTaxFees();
        taxFees.setText(tax);

        String del = db.getDeliveryCost();
        delivery.setText(del);

        String tot = db.getTotal();
        total.setText(tot);
        */
    }
}