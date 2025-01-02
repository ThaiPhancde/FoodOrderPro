package com.pro.foodorder.fragment.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.pro.foodorder.ControllerApplication;
import com.pro.foodorder.R;
import com.pro.foodorder.activity.AdminMainActivity;
import com.pro.foodorder.adapter.FeedbackAdapter;
import com.pro.foodorder.adapter.FoodFeedBackAdapter;
import com.pro.foodorder.adapter.MoreImageAdapter;
import com.pro.foodorder.constant.GlobalFunction;
import com.pro.foodorder.databinding.FragmentAdminFeedbackBinding;
import com.pro.foodorder.fragment.BaseFragment;
import com.pro.foodorder.model.Feedback;
import com.pro.foodorder.model.Food;
import com.pro.foodorder.utils.StringUtil;
import com.pro.foodorder.viewmodel.FeedBackViewModel;
import com.pro.foodorder.viewmodel.FoodDetailViewModel;

import java.util.ArrayList;
import java.util.List;

public class AdminFeedbackFragment extends BaseFragment {

    private FragmentAdminFeedbackBinding mFragmentAdminFeedbackBinding;
    private List<Food> foods;
    private FeedBackViewModel feedBackViewModel;
    private FoodDetailViewModel foodDetailViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentAdminFeedbackBinding = FragmentAdminFeedbackBinding.inflate(inflater, container, false);
        feedBackViewModel = new ViewModelProvider(getActivity()).get(FeedBackViewModel.class);
        foodDetailViewModel = new ViewModelProvider(getActivity()).get(FoodDetailViewModel.class);
        feedBackViewModel.getLiveDataFoods().observeForever(data -> {
            if (data == null || data.isEmpty()) return;
            this.foods = data;
            getFeedBacksFromFirebase();
        });
        foodDetailViewModel.getLiveDataFeedBacks().observeForever(feedbacks -> {
            if (foods == null || foods.isEmpty()) return;
            if (!(feedbacks == null || feedbacks.isEmpty())) {
                for (int i = 0; i < feedbacks.size(); i++) {
                    for (Food food : foods) {
                        if (food.getId() == feedbacks.get(i).getFoodId()) {
                            if (food.getFeedBacks() == null)
                                food.setFeedBacks(new ArrayList<>());
                            food.getFeedBacks().add(feedbacks.get(i));
                            break;
                        }
                    }
                }
            }
            displayFoods();
        });
        getFoodsFromFirebase();
        initView();
        return mFragmentAdminFeedbackBinding.getRoot();
    }

    private void displayFoods() {
        if (foods == null || foods.isEmpty()) {
            return;
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mFragmentAdminFeedbackBinding.rcvFeedback.setLayoutManager(linearLayoutManager);
        FoodFeedBackAdapter foodFeedBackAdapter = new FoodFeedBackAdapter(foods);
        mFragmentAdminFeedbackBinding.rcvFeedback.setAdapter(foodFeedBackAdapter);
    }

    private void getFoodsFromFirebase() {
        if (getActivity() == null) {
            return;
        }
        ControllerApplication.get(getActivity()).getFoodDatabaseReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Food> getFoods = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Food food = dataSnapshot.getValue(Food.class);
                    if (food == null) {
                        return;
                    }
                    getFoods.add(food);
                }
                feedBackViewModel.setDataFoods(getFoods);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                GlobalFunction.showToastMessage(getActivity(), getString(R.string.msg_get_date_error));
            }
        });
    }

    private void getFeedBacksFromFirebase() {
        if (getActivity() == null) {
            return;
        }
        ControllerApplication.get(getActivity()).getFeedbackDatabaseReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Feedback> getFeedBacks = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Feedback feedback = dataSnapshot.getValue(Feedback.class);
                    if (feedback == null) {
                        return;
                    }
                    getFeedBacks.add(feedback);
                }
                foodDetailViewModel.setDataFeedBacks(getFeedBacks);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                GlobalFunction.showToastMessage(getActivity(), getString(R.string.msg_get_date_error));
            }
        });
    }

    @Override
    protected void initToolbar() {
        if (getActivity() != null) {
            ((AdminMainActivity) getActivity()).setToolBar(getString(R.string.feedback));
        }
    }

    private void initView() {
        if (getActivity() == null) {
            return;
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mFragmentAdminFeedbackBinding.rcvFeedback.setLayoutManager(linearLayoutManager);
    }
}
