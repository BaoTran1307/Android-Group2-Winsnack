package com.baotran.winsnack_group2;


import android.content.Intent;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;

public class FingerprintSetupActivity extends AppCompatActivity {

    private ImageView btnBack, ivFingerprint;
    private Button btnSkip, btnContinue;
    private boolean isFingerprintSupported = false;
    private boolean isFingerprintEnrolled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fingerprint_setup);

        initViews();
        checkFingerprintSupport();
        setupClickListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btn_back);
        ivFingerprint = findViewById(R.id.iv_fingerprint);
        btnSkip = findViewById(R.id.btn_skip);
        btnContinue = findViewById(R.id.btn_continue);
    }

    private void checkFingerprintSupport() {
        FingerprintManagerCompat fingerprintManager = FingerprintManagerCompat.from(this);

        if (!fingerprintManager.isHardwareDetected()) {
            // Device doesn't support fingerprint authentication
            isFingerprintSupported = false;
            showFingerprintNotSupported();
        } else if (!fingerprintManager.hasEnrolledFingerprints()) {
            // User hasn't enrolled any fingerprints
            isFingerprintSupported = true;
            isFingerprintEnrolled = false;
            showNoFingerprintsEnrolled();
        } else {
            // Fingerprint authentication is available
            isFingerprintSupported = true;
            isFingerprintEnrolled = true;
        }
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnSkip.setOnClickListener(v -> {
            // Skip fingerprint setup and go to main activity
            navigateToMainActivity();
        });

        btnContinue.setOnClickListener(v -> {
            if (isFingerprintSupported && isFingerprintEnrolled) {
                // Start fingerprint authentication
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    showBiometricPrompt();
                } else {
                    showFingerprintPrompt();
                }
            } else if (isFingerprintSupported && !isFingerprintEnrolled) {
                // Redirect to system settings to enroll fingerprints
                openFingerprintSettings();
            } else {
                // Fingerprint not supported, continue without it
                navigateToMainActivity();
            }
        });

        // Fingerprint icon click to start authentication
        ivFingerprint.setOnClickListener(v -> {
            if (isFingerprintSupported && isFingerprintEnrolled) {
                btnContinue.performClick();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private void showBiometricPrompt() {
        BiometricPrompt biometricPrompt = new BiometricPrompt.Builder(this)
                .setTitle("Fingerprint Authentication")
                .setSubtitle("Place your finger on the sensor to verify your identity")
                .setDescription("Use your fingerprint to quickly and securely access your account")
                .setNegativeButton("Cancel", getMainExecutor(), (dialog, which) -> {
                    // User cancelled
                    Toast.makeText(this, "Authentication cancelled", Toast.LENGTH_SHORT).show();
                })
                .build();

        CancellationSignal cancellationSignal = new CancellationSignal();

        biometricPrompt.authenticate(cancellationSignal, getMainExecutor(),
                new BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationError(int errorCode, CharSequence errString) {
                        super.onAuthenticationError(errorCode, errString);
                        Toast.makeText(FingerprintSetupActivity.this,
                                "Authentication error: " + errString, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        // Fingerprint authentication successful
                        onFingerprintAuthenticationSuccess();
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                        Toast.makeText(FingerprintSetupActivity.this,
                                "Authentication failed. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showFingerprintPrompt() {
        // For older Android versions, show the active fingerprint state
        Intent intent = new Intent(this, FingerprintActiveActivity.class);
        startActivityForResult(intent, 100);
    }

    private void onFingerprintAuthenticationSuccess() {
        Toast.makeText(this, "Fingerprint setup successful!", Toast.LENGTH_SHORT).show();

        // Save fingerprint preference
        saveFingerprintPreference(true);

        // Navigate to main activity
        navigateToMainActivity();
    }

    private void showFingerprintNotSupported() {
        Toast.makeText(this, "Fingerprint authentication is not supported on this device",
                Toast.LENGTH_LONG).show();
        btnContinue.setText("Continue without Fingerprint");
    }

    private void showNoFingerprintsEnrolled() {
        Toast.makeText(this, "No fingerprints enrolled. Please add fingerprints in Settings",
                Toast.LENGTH_LONG).show();
        btnContinue.setText("Add Fingerprints");
    }

    private void openFingerprintSettings() {
        try {
            Intent intent = new Intent(android.provider.Settings.ACTION_SECURITY_SETTINGS);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Unable to open fingerprint settings", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveFingerprintPreference(boolean enabled) {
        getSharedPreferences("app_prefs", MODE_PRIVATE)
                .edit()
                .putBoolean("fingerprint_enabled", enabled)
                .apply();
    }

    private void navigateToMainActivity() {
        // Navigate to main activity
        // Intent intent = new Intent(this, MainActivity.class);
        // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // startActivity(intent);

        // For now, just finish this activity
        Toast.makeText(this, "Setup completed! Welcome to Win Snack!", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                onFingerprintAuthenticationSuccess();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Re-check fingerprint status when returning from settings
        checkFingerprintSupport();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}