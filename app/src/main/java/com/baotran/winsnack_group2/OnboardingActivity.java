package com.baotran.winsnack_group2;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.baotran.winsnack_group2.adapter.OnboardingAdapter;
import com.baotran.winsnack_group2.models.OnboardingItem;

import java.util.ArrayList;
import java.util.List;

public class OnboardingActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private OnboardingAdapter adapter;
    private LinearLayout layoutDots;
    private TextView[] dots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        viewPager = findViewById(R.id.viewPager);
        layoutDots = findViewById(R.id.layoutDots);

        // Danh sách trang
        List<OnboardingItem> items = new ArrayList<>();
        items.add(new OnboardingItem(R.drawable.img_order, "Order for Food", "Explore a world of irresistible snacks..."));
        items.add(new OnboardingItem(R.drawable.img_payment, "Easy Payment", "Choose from multiple secure methods..."));
        items.add(new OnboardingItem(R.drawable.img_delivery, "Fast Delivery", "From our shop to your door..."));

        adapter = new OnboardingAdapter(this, items);
        viewPager.setAdapter(adapter);

        // Gọi hàm tạo dot indicator
        addDotsIndicator(0);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                addDotsIndicator(position);
            }
        });
    }

    // ✅ Hàm tạo dot indicator
    private void addDotsIndicator(int position) {
        int totalDots = adapter.getItemCount();
        dots = new TextView[totalDots];
        layoutDots.removeAllViews();

        for (int i = 0; i < totalDots; i++) {
            dots[i] = new TextView(this);
            dots[i].setText("●");
            dots[i].setTextSize(18);
            dots[i].setTextColor(i == position ? Color.parseColor("#FF9413") : Color.parseColor("#FFD7A0"));
            layoutDots.addView(dots[i]);
        }
    }

    // ✅ Hàm chuyển trang – phải tách ra bên ngoài
    public void goToNextPage(int currentPosition) {
        if (currentPosition < adapter.getItemCount() - 1) {
            viewPager.setCurrentItem(currentPosition + 1);
        } else {
            // TODO: Chuyển đến màn hình tiếp theo, ví dụ WelcomeActivity
            // startActivity(new Intent(this, WelcomeActivity.class));
            finish(); // Tạm thời đóng nếu chưa có màn hình tiếp
        }
    }
}
