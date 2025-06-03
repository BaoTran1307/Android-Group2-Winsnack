package com.baotran.winsnack_group2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.baotran.winsnack_group2.R;
import com.baotran.winsnack_group2.models.OnboardingItem;
import com.baotran.winsnack_group2.OnboardingActivity; // Nhớ import nếu chưa

import java.util.List;

public class OnboardingAdapter extends RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder> {
    private List<OnboardingItem> itemList;
    private Context context;

    public OnboardingAdapter(Context context, List<OnboardingItem> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public OnboardingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_onboarding, parent, false);
        return new OnboardingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OnboardingViewHolder holder, int position) {
        OnboardingItem item = itemList.get(position);
        holder.image.setImageResource(item.getImageRes());
        holder.title.setText(item.getTitle());
        holder.description.setText(item.getDescription());

        // Đổi nút Next thành Get Started ở trang cuối
        if (position == itemList.size() - 1) {
            holder.nextBtn.setText("Get Started");
        } else {
            holder.nextBtn.setText("Next");
        }

        holder.nextBtn.setOnClickListener(v -> {
            if (context instanceof OnboardingActivity) {
                ((OnboardingActivity) context).goToNextPage(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class OnboardingViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title, description;
        Button nextBtn;

        public OnboardingViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imageOnboarding);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            nextBtn = itemView.findViewById(R.id.btnNext);
        }
    }
}
