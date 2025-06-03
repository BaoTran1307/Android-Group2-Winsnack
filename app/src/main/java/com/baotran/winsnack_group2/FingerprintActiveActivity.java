package com.baotran.winsnack_group2;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class FingerprintActiveActivity extends AppCompatActivity {

    private ImageView btnBack, ivFingerprintActive;
    private View fingerprintBackground, pulseRing1, pulseRing2;
    private Button btnSkip, btnContinue;

    private Handler animationHandler;
    private Runnable pulseAnimation;
    private boolean isScanning = false;
    private boolean authenticationSuccess = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fingerprint_active);

        initViews();
        setupClickListeners();
        startPulseAnimation();
        simulateFingerprintScanning();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btn_back);
        ivFingerprintActive = findViewById(R.id.iv_fingerprint_active);
        fingerprintBackground = findViewById(R.id.fingerprint_background);
        pulseRing1 = findViewById(R.id.pulse_ring_1);
        pulseRing2 = findViewById(R.id.pulse_ring_2);
        btnSkip = findViewById(R.id.btn_skip);
        btnContinue = findViewById(R.id.btn_continue);

        animationHandler = new Handler();
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> {
            stopAnimations();
            finish();
        });

        btnSkip.setOnClickListener(v -> {
            stopAnimations();
            setResult(RESULT_CANCELED);
            finish();
        });

        btnContinue.setOnClickListener(v -> {
            if (authenticationSuccess) {
                stopAnimations();
                setResult(RESULT_OK);
                finish();
            } else {
                // Restart scanning process
                restartScanning();
            }
        });

        // Allow tap on fingerprint to restart scanning
        ivFingerprintActive.setOnClickListener(v -> restartScanning());
    }

    private void startPulseAnimation() {
        pulseAnimation = new Runnable() {
            @Override
            public void run() {
                if (isScanning) {
                    animatePulse();
                    animationHandler.postDelayed(this, 1500); // Repeat every 1.5 seconds
                }
            }
        };

        isScanning = true;
        animationHandler.post(pulseAnimation);
    }

    private void animatePulse() {
        // Animate pulse ring 1
        ObjectAnimator scaleX1 = ObjectAnimator.ofFloat(pulseRing1, "scaleX", 1f, 1.2f, 1f);
        ObjectAnimator scaleY1 = ObjectAnimator.ofFloat(pulseRing1, "scaleY", 1f, 1.2f, 1f);
        ObjectAnimator alpha1 = ObjectAnimator.ofFloat(pulseRing1, "alpha", 0.3f, 0f, 0.3f);

        AnimatorSet animatorSet1 = new AnimatorSet();
        animatorSet1.playTogether(scaleX1, scaleY1, alpha1);
        animatorSet1.setDuration(1500);
        animatorSet1.setInterpolator(new AccelerateDecelerateInterpolator());

        // Animate pulse ring 2 with delay
        ObjectAnimator scaleX2 = ObjectAnimator.ofFloat(pulseRing2, "scaleX", 1f, 1.3f, 1f);
        ObjectAnimator scaleY2 = ObjectAnimator.ofFloat(pulseRing2, "scaleY", 1f, 1.3f, 1f);
        ObjectAnimator alpha2 = ObjectAnimator.ofFloat(pulseRing2, "alpha", 0.1f, 0f, 0.1f);

        AnimatorSet animatorSet2 = new AnimatorSet();
        animatorSet2.playTogether(scaleX2, scaleY2, alpha2);
        animatorSet2.setDuration(1500);
        animatorSet2.setStartDelay(750); // Start halfway through first animation
        animatorSet2.setInterpolator(new AccelerateDecelerateInterpolator());

        // Start animations
        animatorSet1.start();
        animatorSet2.start();

        // Add slight rotation to fingerprint icon
        ObjectAnimator rotation = ObjectAnimator.ofFloat(ivFingerprintActive, "rotation", 0f, 5f, -5f, 0f);
        rotation.setDuration(1500);
        rotation.setInterpolator(new AccelerateDecelerateInterpolator());
        rotation.start();
    }

    private void simulateFingerprintScanning() {
        // Simulate fingerprint scanning process
        animationHandler.postDelayed(() -> {
            if (isScanning) {
                // Random success/failure for demo purposes
                if (Math.random() > 0.3) { // 70% success rate
                    onScanningSuccess();
                } else {
                    onScanningFailed();
                }
            }
        }, 3000); // 3 seconds scanning time
    }

    private void onScanningSuccess() {
        authenticationSuccess = true;
        isScanning = false;

        // Change fingerprint color to indicate success
        ivFingerprintActive.setColorFilter(getResources().getColor(R.color.orange_secondary));

        // Stop pulse animation
        stopAnimations();

        // Show success animation
        ObjectAnimator successScale = ObjectAnimator.ofFloat(ivFingerprintActive, "scaleX", 1f, 1.2f, 1f);
        ObjectAnimator successScaleY = ObjectAnimator.ofFloat(ivFingerprintActive, "scaleY", 1f, 1.2f, 1f);

        AnimatorSet successAnimation = new AnimatorSet();
        successAnimation.playTogether(successScale, successScaleY);
        successAnimation.setDuration(500);
        successAnimation.start();

        // Update UI
        btnContinue.setText("Success! Continue");
        btnContinue.setBackgroundResource(R.drawable.button_primary);

        Toast.makeText(this, "Fingerprint authentication successful!", Toast.LENGTH_SHORT).show();

        // Auto continue after 2 seconds
        animationHandler.postDelayed(() -> {
            if (!isFinishing()) {
                btnContinue.performClick();
            }
        }, 2000);
    }

    private void onScanningFailed() {
        isScanning = false;

        // Change fingerprint color to indicate failure
        ivFingerprintActive.setColorFilter(getResources().getColor(android.R.color.holo_red_light));

        // Stop pulse animation
        stopAnimations();

        // Show failure animation (shake)
        ObjectAnimator shake = ObjectAnimator.ofFloat(ivFingerprintActive, "translationX", 0f, -10f, 10f, -10f, 10f, 0f);
        shake.setDuration(500);
        shake.start();

        // Update UI
        btnContinue.setText("Try Again");
        btnContinue.setBackgroundResource(R.drawable.input_background);

        Toast.makeText(this, "Fingerprint not recognized. Please try again.", Toast.LENGTH_SHORT).show();
    }

    private void restartScanning() {
        authenticationSuccess = false;
        isScanning = true;

        // Reset fingerprint color
        ivFingerprintActive.setColorFilter(getResources().getColor(R.color.orange_primary));

        // Reset UI
        btnContinue.setText("Continue");
        btnContinue.setBackgroundResource(R.drawable.button_primary);

        // Restart animations
        startPulseAnimation();
        simulateFingerprintScanning();

        Toast.makeText(this, "Place your finger on the sensor", Toast.LENGTH_SHORT).show();
    }

    private void stopAnimations() {
        isScanning = false;
        if (animationHandler != null && pulseAnimation != null) {
            animationHandler.removeCallbacks(pulseAnimation);
        }

        // Reset animation views
        pulseRing1.clearAnimation();
        pulseRing2.clearAnimation();
        ivFingerprintActive.clearAnimation();

        pulseRing1.setScaleX(1f);
        pulseRing1.setScaleY(1f);
        pulseRing1.setAlpha(0.3f);

        pulseRing2.setScaleX(1f);
        pulseRing2.setScaleY(1f);
        pulseRing2.setAlpha(0.1f);

        ivFingerprintActive.setRotation(0f);
        ivFingerprintActive.setScaleX(1f);
        ivFingerprintActive.setScaleY(1f);
        ivFingerprintActive.setTranslationX(0f);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopAnimations();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!authenticationSuccess) {
            restartScanning();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopAnimations();
        if (animationHandler != null) {
            animationHandler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    public void onBackPressed() {
        stopAnimations();
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }
}