//package com.baotran.winsnack_group2;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.CountDownTimer;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//import androidx.appcompat.app.AppCompatActivity;
//
//public class OtpVerificationActivity extends AppCompatActivity {
//
//    private EditText etOtpCode;
//    private Button btnContinue;
//    private TextView tvLogIn, tvResend;
//    private ImageView btnBack;
//    private String phoneNumber;
//    private CountDownTimer resendTimer;
//    private boolean canResend = true;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_otp_verification);
//
//        phoneNumber = getIntent().getStringExtra("phone_number");
//
//        initViews();
//        setupClickListeners();
//        startResendTimer();
//    }
//
//    private void initViews() {
//        etOtpCode = findViewById(R.id.et_otp_code);
//        btnContinue = findViewById(R.id.btn_continue);
//        tvLogIn = findViewById(R.id.tv_log_in);
//        tvResend = findViewById(R.id.tv_resend);
//        btnBack = findViewById(R.id.btn_back);
//    }
//
//    private void setupClickListeners() {
//        btnBack.setOnClickListener(v -> finish());
//
//        btnContinue.setOnClickListener(v -> handleContinue());
//
//        tvLogIn.setOnClickListener(v -> navigateToLogin());
//
//        tvResend.setOnClickListener(v -> handleResend());
//
//        // Social login buttons
//        findViewById(R.id.btn_google).setOnClickListener(v -> handleSocialLogin("Google"));
//        findViewById(R.id.btn_apple).setOnClickListener(v -> handleSocialLogin("Apple"));
//        findViewById(R.id.btn_fingerprint).setOnClickListener(v -> handleSocialLogin("Fingerprint"));
//    }
//
//    private void handleContinue() {
//        String otpCode = etOtpCode.getText().toString().trim();
//
//        if (otpCode.isEmpty()) {
//            Toast.makeText(this, "Please enter OTP code", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        if (otpCode.length() < 4) {
//            Toast.makeText(this, "Please enter valid OTP code", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // Verify OTP logic here
//        if (verifyOTP(otpCode)) {
//            Toast.makeText(this, "OTP verified successfully", Toast.LENGTH_SHORT).show();
//            // Navigate to main activity or complete registration
//            // Intent intent = new Intent(this, MainActivity.class);
//            // startActivity(intent);
//            // finish();
//        } else {
//            Toast.makeText(this, "Invalid OTP code", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private boolean verifyOTP(String otpCode) {
//        // Implement OTP verification logic here
//        // For demo purpose, accept any 4-digit code
//        return otpCode.length() == 4;
//    }
//
//    private void handleResend() {
//        if (!canResend) {
//            Toast.makeText(this, "Please wait before requesting new OTP", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // Resend OTP logic here
//        Toast.makeText(this, "New OTP sent to " + phoneNumber, Toast.LENGTH_SHORT).show();
//        startResendTimer();
//    }
//
//    private void startResendTimer() {
//        canResend = false;
//        tvResend.setEnabled(false);
//        tvResend.setTextColor(getResources().getColor(R.color.text_secondary));
//
//        resendTimer = new CountDownTimer(60000, 1000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                tvResend.setText("RESEND (" + millisUntilFinished / 1000 + "s)");
//            }
//
//            @Override
//            public void onFinish() {
//                canResend = true;
//                tvResend.setEnabled(true);
//                tvResend.setText("RESEND");
//                tvResend.setTextColor(getResources().getColor(R.color.orange_primary));
//            }
//        };
//        resendTimer.start();
//    }
//
//    private void navigateToLogin() {
//        Intent intent = new Intent(this, ForgetPasswordActivity.class);
//        startActivity(intent);
//    }
//
//    private void handleSocialLogin(String provider) {
//        Toast.makeText(this, "Sign up with " + provider, Toast.LENGTH_SHORT).show();
//        // Implement social login logic here
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (resendTimer != null) {
//            resendTimer.cancel();
//        }
//    }
//}