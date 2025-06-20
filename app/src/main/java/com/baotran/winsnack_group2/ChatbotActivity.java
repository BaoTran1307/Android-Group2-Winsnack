package com.baotran.winsnack_group2;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ChatbotActivity extends FooterActivity {

    private EditText messageInput;
    private LinearLayout chatContainer;
    private ImageButton sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);
        setupFooter();

        // Khởi tạo các view từ layout
        messageInput = findViewById(R.id.chatInputBar).findViewById(R.id.messageInput);
        sendButton = findViewById(R.id.chatInputBar).findViewById(R.id.btnSend);
        chatContainer = findViewById(R.id.scrollChat).findViewById(R.id.chatContainer);

        // Xử lý sự kiện nút Send
        sendButton.setOnClickListener(v -> {
            String message = messageInput.getText().toString().trim();
            if (!message.isEmpty()) {
                addUserMessage(message);
                messageInput.setText(""); // Xóa input sau khi gửi
                // Gọi hàm xử lý phản hồi của bot (có thể là API hoặc logic AI)
                addBotResponse("Bot: Đã nhận \"" + message + "\". Đang xử lý...");
            }
        });

        // Xử lý các nút gợi ý
//        setupSuggestedButtons();
    }

    // Thêm tin nhắn của người dùng vào giao diện
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
        avatar.setLayoutParams(new LinearLayout.LayoutParams(32, 32));
        avatar.setImageResource(R.mipmap.ic_user_avatar);
        avatar.setPadding(8, 0, 0, 0);

        innerLayout.addView(messageText);
        innerLayout.addView(avatar);
        messageLayout.addView(innerLayout);
        chatContainer.addView(messageLayout);
    }

    // Thêm phản hồi của bot vào giao diện
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
    }

    // Xử lý các nút gợi ý
//    private void setupSuggestedButtons() {
//        LinearLayout suggestedContainer = chatContainer.findViewById(R.id.suggestedQuestions);
//        for (int i = 0; i < suggestedContainer.getChildCount(); i++) {
//            View button = suggestedContainer.getChildAt(i);
//            if (button instanceof Button) {
//                button.setOnClickListener(v -> {
//                    String suggestedText = ((Button) v).getText().toString();
//                    addUserMessage(suggestedText);
//                    addBotResponse("Bot: Đang trả lời cho \"" + suggestedText + "\"...");
//                });
//            }
//        }
//    }
}