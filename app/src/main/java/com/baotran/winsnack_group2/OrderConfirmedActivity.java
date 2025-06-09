package com.baotran.winsnack_group2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class OrderConfirmedActivity extends AppCompatActivity {

    private ImageView btnBack;
    private Button btnReturnHome;
    private Button btnTrackOrder;
    private ImageView characterAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmed);

        initViews();
        setupClickListeners();
        animateCharacter();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btn_back);
        btnReturnHome = findViewById(R.id.btn_return_home);
        btnTrackOrder = findViewById(R.id.btn_track_order);
        characterAnimation = findViewById(R.id.character_animation);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnReturnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to main activity
                Intent intent = new Intent(OrderConfirmedActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        btnTrackOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to delivery tracking activity
                Intent intent = new Intent(OrderConfirmedActivity.this, DeliveryTrackingActivity.class);
                startActivity(intent);
            }
        });
    }

    private void animateCharacter() {
        // Simple bounce animation for the character
        characterAnimation.animate()
                .scaleX(1.1f)
                .scaleY(1.1f)
                .setDuration(500)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        characterAnimation.animate()
                                .scaleX(1.0f)
                                .scaleY(1.0f)
                                .setDuration(500)
                                .withEndAction(new Runnable() {
                                    @Override
                                    public void run() {
                                        animateCharacter(); // Repeat animation
                                    }
                                });
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}