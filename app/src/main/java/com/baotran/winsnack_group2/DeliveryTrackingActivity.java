package com.baotran.winsnack_group2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class DeliveryTrackingActivity extends AppCompatActivity {

    private ImageView btnBack;
    private Button btnReturnHome;
    private Button btnTrackOrder;
    private TextView deliveryTimeText;
    private View step1Dot, step2Dot, step3Dot, step4Dot;
    private TextView step1Time, step2Time, step3Time, step4Time;

    private Handler handler = new Handler();
    private int currentStep = 3; // Current active step
    private int remainingMinutes = 25;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_tracking);

        initViews();
        setupClickListeners();
        updateTrackingStatus();
        startTimer();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btn_back);
        btnReturnHome = findViewById(R.id.btn_return_home_tracking);
        btnTrackOrder = findViewById(R.id.btn_track_order_tracking);

        step1Dot = findViewById(R.id.step1_dot);
        step2Dot = findViewById(R.id.step2_dot);
        step3Dot = findViewById(R.id.step3_dot);
        step4Dot = findViewById(R.id.step4_dot);

        step1Time = findViewById(R.id.step1_time);
        step2Time = findViewById(R.id.step2_time);
        step3Time = findViewById(R.id.step3_time);
        step4Time = findViewById(R.id.step4_time);
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
                Intent intent = new Intent(DeliveryTrackingActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        btnTrackOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Refresh tracking or show more details
                updateTrackingStatus();
            }
        });
    }

    private void updateTrackingStatus() {
        // Reset all steps
        resetStepStyles();

        // Update steps based on current progress
        switch (currentStep) {
            case 1:
                setStepCompleted(step1Dot);
                break;
            case 2:
                setStepCompleted(step1Dot);
                setStepCompleted(step2Dot);
                break;
            case 3:
                setStepCompleted(step1Dot);
                setStepCompleted(step2Dot);
                setStepActive(step3Dot);
                break;
            case 4:
                setStepCompleted(step1Dot);
                setStepCompleted(step2Dot);
                setStepCompleted(step3Dot);
                setStepCompleted(step4Dot);
                break;
        }
    }

    private void resetStepStyles() {
        step1Dot.setBackgroundResource(R.drawable.step_pending_dot);
        step2Dot.setBackgroundResource(R.drawable.step_pending_dot);
        step3Dot.setBackgroundResource(R.drawable.step_pending_dot);
        step4Dot.setBackgroundResource(R.drawable.step_pending_dot);
    }

    private void setStepCompleted(View stepDot) {
        stepDot.setBackgroundResource(R.drawable.step_completed_dot);
    }

    private void setStepActive(View stepDot) {
        stepDot.setBackgroundResource(R.drawable.step_active_dot);
    }

    private void startTimer() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (remainingMinutes > 0) {
                    remainingMinutes--;

                    // Simulate progression through steps
                    if (remainingMinutes == 15 && currentStep == 3) {
                        currentStep = 4;
                        updateTrackingStatus();
                    }

                    startTimer(); // Continue timer
                }
            }
        }, 60000); // Update every minute
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}