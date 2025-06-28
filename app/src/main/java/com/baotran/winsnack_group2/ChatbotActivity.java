package com.baotran.winsnack_group2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;
import java.util.ArrayList;
import java.util.List;

// Data classes for Gemini API request and response
class GeminiRequest {
    public List<Content> contents;

    public GeminiRequest(String prompt) {
        Part part = new Part(prompt);
        Content content = new Content();
        content.parts = new ArrayList<>();
        content.parts.add(part);
        contents = new ArrayList<>();
        contents.add(content);
    }

    static class Content {
        public List<Part> parts;
    }

    static class Part {
        public String text;
        public Part(String text) {
            this.text = text;
        }
    }
}

class GeminiResponse {
    public List<Candidate> candidates;

    static class Candidate {
        public Content content;
    }

    static class Content {
        public List<Part> parts;
    }

    static class Part {
        public String text;
    }
}

interface GeminiApi {
    @POST("v1beta/models/gemini-1.5-flash:generateContent")
    Call<GeminiResponse> getResponse(
            @Query("key") String apiKey,
            @Body GeminiRequest request
    );
}

// Class to store Win Snack information
class WinSnackInfo {
    private String brandName = "Win Snack";
    private String slogan = "Ăn là ghiền!";
    private String brandStory = "Win Snack ra đời từ niềm đam mê với bánh tráng, mang đến trải nghiệm sáng tạo và thú vị. Chúng tôi không ngừng đổi mới với các hương vị như bánh tráng chà bông, rong biển, sốt bơ, để lan tỏa niềm vui ăn vặt.";
    private String vision = "Trở thành thương hiệu bánh tráng hàng đầu Việt Nam, mang đến trải nghiệm ẩm thực độc đáo và nâng tầm bánh tráng Việt trên thị trường quốc tế.";
    private String mission = "Cung cấp trải nghiệm ẩm thực đỉnh cao qua bánh tráng chất lượng, từ nguyên liệu tự nhiên, cam kết đổi mới và trách nhiệm.";
    private String[] products = {"Bánh tráng trộn sẵn", "Bánh tráng nướng", "Bánh tráng ngọt", "Combo bánh tráng mix vị", "Nguyên liệu lẻ"};
    private String targetCustomers = "Nhóm yêu thích ăn vặt, độ tuổi 16-35, tập trung ở TP.HCM, Hà Nội, thích khám phá ẩm thực qua mạng xã hội.";

    public String getBrandInfo() {
        return String.format("Thương hiệu: %s\nKhẩu hiệu: %s\nCâu chuyện: %s\nTầm nhìn: %s\nSứ mệnh: %s\nSản phẩm: %s\nKhách hàng mục tiêu: %s",
                brandName, slogan, brandStory, vision, mission, String.join(", ", products), targetCustomers);
    }

    public String getProductDetails(String product) {
        switch (product.toLowerCase()) {
            case "bánh tráng trộn sẵn":
                return "Bánh tráng trộn sẵn - món ăn đậm đà, tiện lợi, sẵn sàng thưởng thức mọi lúc mọi nơi.";
            case "bánh tráng nướng":
                return "Bánh tráng nướng - giòn rụm, thơm lừng, mang hương vị đường phố Việt Nam.";
            case "bánh tráng ngọt":
                return "Bánh tráng ngọt - sự kết hợp độc đáo giữa bánh tráng và vị ngọt mới lạ.";
            case "combo bánh tráng mix vị":
                return "Combo bánh tráng mix vị bao gồm nhiều loại bánh tráng kết hợp cùng nhau. Bạn có thể mua ăn thử để khám phá nhiều hương vị trong một bộ sưu tập tiện lợi nhé!.";
            case "nguyên liệu lẻ":
                return "Nguyên liệu lẻ - lựa chọn topping và gia vị để tự tay tạo nên món bánh tráng theo ý thích.";
            default:
                return "Sản phẩm không có trong danh sách. Vui lòng chọn: bánh tráng trộn sẵn, bánh tráng nướng, bánh tráng ngọt, combo bánh tráng mix vị, hoặc nguyên liệu lẻ.";
        }
    }

    public String suggestProduct(String preference) {
        String pref = preference.toLowerCase();
        Log.d("SuggestProduct", "Preference: " + pref);
        if (pref.contains("giòn") || pref.contains("thơm")) {
            Log.d("SuggestProduct", "Matched giòn/thơm");
            return "Bạn thích đồ giòn và thơm? Hãy thử Bánh tráng nướng bên mình nhé! Bánh tráng giòn rụm, mang hương vị đường phố Việt Nam, rất thích hợp để ăn vặt nè!";
        } else if (pref.contains("ngọt") || pref.contains("tráng miệng")) {
            Log.d("SuggestProduct", "Matched ngọt/tráng miệng");
            return "Bạn thích vị ngọt? Bánh tráng ngọt là lựa chọn hoàn hảo với sự kết hợp độc đáo và mới lạ!";
        } else if (pref.contains("tiện lợi") || pref.contains("nhanh")) {
            Log.d("SuggestProduct", "Matched tiện lợi/nhanh");
            return "Cần món ăn nhanh và tiện lợi? Bánh tráng trộn sẵn sẽ là lựa chọn tuyệt vời cho bạn!";
        } else if (pref.contains("đa dạng") || pref.contains("thử")) {
            Log.d("SuggestProduct", "Matched đa dạng/thử");
            return "Bạn muốn khám phá nhiều hương vị? Combo bánh tráng mix vị sẽ giúp bạn trải nghiệm đa dạng!";
        } else if (pref.contains("tự làm")) {
            Log.d("SuggestProduct", "Matched tự làm");
            return "Thích tự tay sáng tạo? Nguyên liệu lẻ sẽ giúp bạn làm món bánh tráng theo ý thích!";
        } else {
            Log.d("SuggestProduct", "No match");
            return "Hãy cho mình biết sở thích của bạn (ví dụ: giòn, ngọt, tiện lợi), mình sẽ tư vấn sản phẩm phù hợp nhé!";
        }
    }

    // Getter cho mảng products
    public String[] getProducts() {
        return products;
    }
}

public class ChatbotActivity extends FooterActivity {

    private EditText messageInput;
    private LinearLayout chatContainer;
    private ImageButton sendButton;
    private Retrofit retrofit;
    private GeminiApi geminiApi;
    private final String API_KEY = "AIzaSyBoQtNypCVF2hCGjuf0GHqYOs2eoOJruAs";
    ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);
        setupFooter();
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatbotActivity.this, HomeActivity.class);
                startActivity(intent);
                finish(); // Đóng BlogActivity để tránh quay lại bằng nút back
            }
        });

        messageInput = findViewById(R.id.messageInput);
        sendButton = findViewById(R.id.btnSend);
        chatContainer = findViewById(R.id.chatContainer);

        retrofit = new Retrofit.Builder()
                .baseUrl("https://generativelanguage.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        geminiApi = retrofit.create(GeminiApi.class);

        sendButton.setOnClickListener(v -> {
            String message = messageInput.getText().toString().trim();
            if (!message.isEmpty()) {
                addUserMessage(message);
                sendMessageToGemini(message);
                messageInput.setText("");
            }
        });

        setupSuggestedButtons();
    }

    private void addUserMessage(String message) {
        LinearLayout messageLayout = new LinearLayout(this);
        messageLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        messageLayout.setGravity(Gravity.END);
        messageLayout.setPadding(0, 0, 0, 16);

        LinearLayout innerLayout = new LinearLayout(this);
        innerLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        innerLayout.setOrientation(LinearLayout.HORIZONTAL);
        innerLayout.setGravity(Gravity.CENTER_VERTICAL);

        TextView messageText = new TextView(this);
        messageText.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        messageText.setText(message);
        messageText.setTextColor(getResources().getColor(android.R.color.black));
        messageText.setTextSize(14);
        messageText.setBackgroundResource(R.drawable.bg_user_bubble);
        messageText.setPadding(24, 24, 24, 24);

        ImageView avatar = new ImageView(this);
        avatar.setLayoutParams(new LinearLayout.LayoutParams(90, 90)); // Tăng kích thước lên 64x64 dp
        avatar.setImageResource(R.mipmap.ic_user_avatar);
        avatar.setPadding(8, 0, 0, 0);

        innerLayout.addView(messageText);
        innerLayout.addView(avatar);
        messageLayout.addView(innerLayout);
        chatContainer.addView(messageLayout);
        scrollToBottom();
    }

    private void addBotResponse(String response) {
        LinearLayout messageLayout = new LinearLayout(this);
        messageLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        messageLayout.setPadding(0, 0, 0, 16);

        TextView messageText = new TextView(this);
        messageText.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        messageText.setText(response);
        messageText.setTextColor(getResources().getColor(android.R.color.black));
        messageText.setTextSize(14);
        messageText.setBackgroundResource(R.drawable.bg_ai_bubble);
        messageText.setPadding(24, 24, 24, 24);

        messageLayout.addView(messageText);
        chatContainer.addView(messageLayout);
        scrollToBottom();
    }

    private void scrollToBottom() {
        final ScrollView scrollView = findViewById(R.id.scrollChat);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    private void sendMessageToGemini(String message) {
        WinSnackInfo winSnack = new WinSnackInfo();
        if (message.toLowerCase().contains("thông tin cửa hàng") || message.toLowerCase().contains("về thương hiệu")) {
            Log.d("Chatbot", "Brand info detected");
            runOnUiThread(() -> addBotResponse(winSnack.getBrandInfo()));
            return;
        }
        if (message.toLowerCase().contains("sản phẩm")) {
            Log.d("Chatbot", "Products detected");
            String products = "Sản phẩm của Win Snack: " + String.join(", ", winSnack.getProducts());
            runOnUiThread(() -> addBotResponse(products));
            return;
        }
        for (String product : winSnack.getProducts()) {
            if (message.toLowerCase().contains(product.toLowerCase())) {
                Log.d("Chatbot", "Product detail detected: " + product);
                runOnUiThread(() -> addBotResponse(winSnack.getProductDetails(product)));
                return;
            }
        }
        if (message.toLowerCase().contains("ý nghĩa thương hiệu")) {
            Log.d("Chatbot", "Brand meaning detected");
            runOnUiThread(() -> addBotResponse("Thương hiệu Win Snack mang ý nghĩa từ niềm đam mê bánh tráng, với khẩu hiệu 'Ăn là ghiền!' tạo ấn tượng mạnh mẽ. Logo với tông cam năng động và hình ảnh túi snack tạo nên sự trẻ trung, phù hợp với người yêu ăn vặt."));
            return;
        }
        if (message.toLowerCase().contains("giòn")) {
            Log.d("Chatbot", "Giòn preference detected");
            runOnUiThread(() -> addBotResponse(winSnack.suggestProduct(message)));
            return;
        }
        if (message.toLowerCase().contains("tư vấn") || message.toLowerCase().contains("gợi ý")) {
            Log.d("Chatbot", "Suggestion detected");
            runOnUiThread(() -> addBotResponse(winSnack.suggestProduct(message)));
            return;
        }

        Log.d("Chatbot", "Sending to Gemini API: " + message);
        GeminiRequest request = new GeminiRequest(message);
        geminiApi.getResponse(API_KEY, request).enqueue(new Callback<GeminiResponse>() {
            @Override
            public void onResponse(Call<GeminiResponse> call, Response<GeminiResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().candidates != null && !response.body().candidates.isEmpty()) {
                    String aiResponse = response.body().candidates.get(0).content.parts.get(0).text;
                    Log.d("Chatbot", "Gemini response: " + aiResponse);
                    runOnUiThread(() -> addBotResponse(aiResponse));
                } else {
                    final String errorMsgBase = "Lỗi: Mã trạng thái: " + response.code();
                    String errorMsg = errorMsgBase;
                    if (response.errorBody() != null) {
                        try {
                            errorMsg += ", Chi tiết: " + response.errorBody().string();
                        } catch (Exception e) {
                            errorMsg += ", Không đọc được chi tiết.";
                        }
                    }
                    final String finalErrorMsg = errorMsg;
                    Log.e("GeminiAPI", finalErrorMsg);
                    runOnUiThread(() -> addBotResponse(finalErrorMsg));
                }
            }

            @Override
            public void onFailure(Call<GeminiResponse> call, Throwable t) {
                final String errorMsg = "Lỗi mạng: " + t.getMessage();
                Log.e("GeminiAPI", errorMsg);
                runOnUiThread(() -> addBotResponse(errorMsg));
            }
        });
    }

    private void setupSuggestedButtons() {
        List<Button> suggestedButtons = new ArrayList<>();
        findButtonsInLayout(chatContainer, suggestedButtons);
        for (Button button : suggestedButtons) {
            button.setOnClickListener(v -> {
                String suggestedText = ((Button) v).getText().toString();
                addUserMessage(suggestedText);
                sendMessageToGemini(suggestedText);
            });
        }
    }

    private void findButtonsInLayout(LinearLayout layout, List<Button> buttons) {
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            if (child instanceof Button) {
                buttons.add((Button) child);
            } else if (child instanceof LinearLayout) {
                findButtonsInLayout((LinearLayout) child, buttons);
            }
        }
    }
}