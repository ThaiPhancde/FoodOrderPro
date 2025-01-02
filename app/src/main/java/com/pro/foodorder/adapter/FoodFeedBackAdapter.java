package com.pro.foodorder.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pro.foodorder.R;
import com.pro.foodorder.databinding.ItemFeedbackBinding;
import com.pro.foodorder.model.Feedback;
import com.pro.foodorder.model.Food;

import java.util.List;

public class FoodFeedBackAdapter extends RecyclerView.Adapter<FoodFeedBackAdapter.FoodFeedBackViewHolder> {

    private final List<Food> foods;

    public FoodFeedBackAdapter(List<Food> foods) {
        this.foods = foods;
    }

    @NonNull
    @Override
    public FoodFeedBackAdapter.FoodFeedBackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_feed_back, parent, false);
        return new FoodFeedBackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodFeedBackAdapter.FoodFeedBackViewHolder holder, int position) {
        holder.setData(foods.get(position));
    }

    @Override
    public int getItemCount() {
        if (foods != null) {
            return foods.size();
        }
        return 0;
    }

    public static class FoodFeedBackViewHolder extends RecyclerView.ViewHolder {
        ImageView foodImage;
        TextView foodName;
        TextView dropdown;
        RecyclerView listFeedBacks;
        View container;
        public FoodFeedBackViewHolder(@NonNull View view) {
            super(view);
            container = view;
            foodImage = view.findViewById(R.id.food_item_image);
            foodName = view.findViewById(R.id.food_item_name);
            dropdown = view.findViewById(R.id.food_item_dropdown);
            listFeedBacks = view.findViewById(R.id.food_item_recycle);
            view.findViewById(R.id.food_item_container).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewGroup.LayoutParams layoutParams = listFeedBacks.getLayoutParams();
                    if (dropdown.getText().equals(">")) {
                        dropdown.setText("v");
                        listFeedBacks.setVisibility(View.VISIBLE);
                        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    } else {
                        dropdown.setText(">");
                        listFeedBacks.setVisibility(View.INVISIBLE);
                        layoutParams.height = 0;
                    }
                    listFeedBacks.setLayoutParams(layoutParams);
                }
            });
        }
        public void setData(Food food) {
            foodName.setText(food.getName());
            Glide.with(container)
                .load(food.getImage())
                .into(foodImage);
            listFeedBacks.setLayoutManager(new LinearLayoutManager(container.getContext()));
            listFeedBacks.setAdapter(new FeedbackAdapter(food.getFeedBacks()));
        }
    }
}
