package com.baotran.winsnack_group2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.baotran.winsnack_group2.adapter.BannerAdapter;
import com.baotran.winsnack_group2.adapter.ProductAdapter;
import com.baotran.winsnack_group2.models.Banner;
import com.baotran.winsnack_group2.models.Product;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends FooterActivity {

    private RecyclerView bestSellerRecyclerView, recommendRecyclerView;
    private ProductAdapter bestSellerAdapter, recommendAdapter;
    private List<Product> productList;
    private FirebaseFirestore db;
    private EditText searchEditText;
    private LinearLayout searchContainer;
    private ViewPager2 bannerViewPager;
    private TabLayout bannerDots;
    private BannerAdapter bannerAdapter;
    private List<Banner> bannerList;
    private Handler autoScrollHandler;
    private Runnable autoScrollRunnable;
    private static final String TAG = "HomeActivity";
    private static final long AUTO_SCROLL_DELAY = 4000; // 4 giây

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setupFooter();

        // Kiểm tra Google Play Services
        int resultCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            Toast.makeText(this, "Google Play Services not available: " + resultCode, Toast.LENGTH_LONG).show();
            if (GoogleApiAvailability.getInstance().isUserResolvableError(resultCode)) {
                GoogleApiAvailability.getInstance().getErrorDialog(this, resultCode, 9000).show();
            }
            return;
        }

        // Khởi tạo Firestore
        db = FirebaseFirestore.getInstance();
        FirebaseFirestore.setLoggingEnabled(true);
        db.getFirestoreSettings().isPersistenceEnabled();

        // Kiểm tra kết nối mạng
        db.enableNetwork().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Toast.makeText(HomeActivity.this, "Cannot connect to Firestore. Running in offline mode.", Toast.LENGTH_SHORT).show();
            }
        });

        // Khởi tạo UI
        searchEditText = findViewById(R.id.search_edit_text);
        searchContainer = findViewById(R.id.search_container);
        bestSellerRecyclerView = findViewById(R.id.best_seller_recycler_view);
        recommendRecyclerView = findViewById(R.id.recommend_recycler_view);
        bannerViewPager = findViewById(R.id.banner_view_pager);
        bannerDots = findViewById(R.id.banner_dots);

        // Kiểm tra null cho bannerViewPager
        if (bannerViewPager == null || bannerDots == null) {
            Log.e(TAG, "Banner ViewPager or TabLayout is null. Check activity_home.xml for IDs.");
            Toast.makeText(this, "Banner interface error. Please check again.", Toast.LENGTH_LONG).show();
            return;
        }

        // Khởi tạo Handler
        initAutoScrollHandler();

        // Thiết lập RecyclerView
        productList = new ArrayList<>();
        bestSellerAdapter = new ProductAdapter(this, productList, R.layout.product_item);
        recommendAdapter = new ProductAdapter(this, productList, R.layout.product_item);

        bestSellerRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        bestSellerRecyclerView.setHasFixedSize(true);
        bestSellerRecyclerView.setAdapter(bestSellerAdapter);

        recommendRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recommendRecyclerView.setHasFixedSize(true);
        recommendRecyclerView.setAdapter(recommendAdapter);

        // Thiết lập banner sau khi UI render
        bannerViewPager.post(this::setupBanner);

        // Tải dữ liệu từ Firestore
        bestSellerRecyclerView.post(this::loadProducts);

        // Thiết lập tìm kiếm
        setupSearch();

        // Thiết lập nút danh mục
        setupCategoryButtons();

        // Thiết lập nút điều hướng
        setupNavigationButtons();

        // Thiết lập View All
        setupViewAllButtons();
    }

    private void initAutoScrollHandler() {
        if (autoScrollHandler == null) {
            autoScrollHandler = new Handler(Looper.getMainLooper());
        }
    }

    private void setupBanner() {
        // Khởi tạo danh sách banner với resource ID từ mipmap
        bannerList = new ArrayList<>();
        bannerList.add(new Banner(R.mipmap.ic_discount_banner));
        bannerList.add(new Banner(R.mipmap.ic_banner2)); // Tạm dùng ic_discount_banner
        bannerList.add(new Banner(R.mipmap.ic_banner3));

        // Thiết lập adapter
        bannerAdapter = new BannerAdapter(this, bannerList);
        bannerViewPager.setAdapter(bannerAdapter);

        // Đồng bộ chấm chỉ báo
        new TabLayoutMediator(bannerDots, bannerViewPager, (tab, position) -> {
            // Không cần nội dung cho tab, chỉ dùng chấm
        }).attach();

        // Thêm hiệu ứng chuyển mượt
        bannerViewPager.setPageTransformer((page, position) -> {
            float absPos = Math.abs(position);
            page.setAlpha(1 - absPos * 0.5f); // Hiệu ứng mờ
            page.setScaleX(1 - absPos * 0.1f); // Thu nhỏ nhẹ
            page.setScaleY(1 - absPos * 0.1f);
        });

        // Khởi tạo auto-scroll
        startAutoScroll();

        // Tạm dừng auto-scroll khi người dùng vuốt
        bannerViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                if (state == ViewPager2.SCROLL_STATE_DRAGGING) {
                    stopAutoScroll();
                } else if (state == ViewPager2.SCROLL_STATE_IDLE) {
                    startAutoScroll();
                }
            }
        });
    }

    private void startAutoScroll() {
        // Khởi tạo Handler nếu null
        initAutoScrollHandler();

        // Xóa các Runnable cũ
        stopAutoScroll();

        // Khởi tạo Runnable
        autoScrollRunnable = new Runnable() {
            @Override
            public void run() {
                if (bannerViewPager != null && bannerList != null && !bannerList.isEmpty() && !isFinishing()) {
                    int currentItem = bannerViewPager.getCurrentItem();
                    int nextItem = (currentItem + 1) % bannerList.size();
                    bannerViewPager.setCurrentItem(nextItem, true);
                    autoScrollHandler.postDelayed(this, AUTO_SCROLL_DELAY);
                    Log.d(TAG, "Auto-scroll to item: " + nextItem);
                }
            }
        };
        autoScrollHandler.postDelayed(autoScrollRunnable, AUTO_SCROLL_DELAY);
    }

    private void stopAutoScroll() {
        if (autoScrollHandler != null && autoScrollRunnable != null) {
            autoScrollHandler.removeCallbacks(autoScrollRunnable);
        }
    }

    private void loadProducts() {
        db.collection("PRODUCT")
                .limit(10)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            productList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Product product = document.toObject(Product.class);
                                Log.d(TAG, "Product: " + product.getProductName() + ", Image: " + product.getImage());
                                productList.add(product);
                            }
                            bestSellerAdapter.notifyDataSetChanged();
                            recommendAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(HomeActivity.this, "Failed to load products: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void setupSearch() {
        ImageButton searchButton = findViewById(R.id.filter_button);
        ImageButton voiceButton = findViewById(R.id.voice_button);
        if (searchButton != null) {
            searchButton.setOnClickListener(v -> {
                String query = searchEditText.getText().toString().trim();
                filterProducts(query);
            });
        }
        if (voiceButton != null) {
            voiceButton.setOnClickListener(v -> {
                Toast.makeText(this, "Voice search not implemented", Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void filterProducts(String query) {
        List<Product> filteredList = new ArrayList<>();
        for (Product product : productList) {
            if (product.getProductName().toLowerCase().contains(query.toLowerCase()) ||
                    product.getDescription().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(product);
            }
        }
        bestSellerAdapter.updateList(filteredList);
        recommendAdapter.updateList(filteredList);
    }

    private void setupCategoryButtons() {
        int[] categoryIds = {
                R.id.mixed_category,
                R.id.grilled_category,
                R.id.sweet_category,
                R.id.combo_category,
                R.id.ingredients_category
        };

        String[] categoryCodes = {"BT01", "BT02", "BT03", "BT04", "BT05"};
        String[] categoryNames = {"Mixed", "Grilled", "Sweet", "Combo", "Ingredients"};

        for (int i = 0; i < categoryIds.length; i++) {
            LinearLayout categoryLayout = findViewById(categoryIds[i]);
            if (categoryLayout != null) {
                final String code = categoryCodes[i];
                final String name = categoryNames[i];
                categoryLayout.setOnClickListener(v -> openCategory(code, name));
            }
        }
    }

    private void filterByCategory(String category) {
        List<Product> filteredList = new ArrayList<>();
        for (Product product : productList) {
            if (product.getCategoryID().equalsIgnoreCase(category)) {
                filteredList.add(product);
            }
        }
        bestSellerAdapter.updateList(filteredList);
        recommendAdapter.updateList(filteredList);
    }

    private void setupNavigationButtons() {
        ImageButton cartButton = findViewById(R.id.cart_button);
        ImageButton notificationButton = findViewById(R.id.notification_button);
        ImageButton profileButton = findViewById(R.id.profile_button);

        if (cartButton != null) {
            cartButton.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, CartActivity.class)));
        }

//        if (notificationButton != null) {
//            notificationButton.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, NotificationActivity.class)));
//        }

        if (profileButton != null) {
            profileButton.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, MyProfileActivity.class)));
        }
    }

    private void setupViewAllButtons() {
        TextView bestSellerViewAll = findViewById(R.id.best_seller_view_all);
        if (bestSellerViewAll != null) {
            bestSellerViewAll.setOnClickListener(v -> {
                Intent intent = new Intent(HomeActivity.this, BestSellerActivity.class);
                intent.putExtra("bestSellerList", new ArrayList<>(productList)); // truyền list
                startActivity(intent);
            });
        }

        TextView recommendViewAll = findViewById(R.id.recommend_view_all);
        if (recommendViewAll != null) {
            recommendViewAll.setOnClickListener(v -> {
                Intent intent = new Intent(HomeActivity.this, RecommendActivity.class);
                intent.putExtra("recommendList", new ArrayList<>(productList)); // truyền list đang hiển thị
                startActivity(intent);
            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Tạm dừng auto-scroll
        stopAutoScroll();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Tiếp tục auto-scroll
        startAutoScroll();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Dọn dẹp
        stopAutoScroll();
        // Không đặt null để tránh lỗi, chỉ xóa callbacks
    }

    private void openCategory(String code, String name) {
        Intent intent = new Intent(HomeActivity.this, CategoryActivity.class);
        intent.putExtra("category_code", code);
        intent.putExtra("category_name", name);
        startActivity(intent);
    }
}