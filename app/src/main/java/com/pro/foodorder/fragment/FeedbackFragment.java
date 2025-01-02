package com.pro.foodorder.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.pro.foodorder.ControllerApplication;
import com.pro.foodorder.R;
import com.pro.foodorder.activity.MainActivity;
import com.pro.foodorder.constant.GlobalFunction;
import com.pro.foodorder.databinding.FragmentFeedbackBinding;
import com.pro.foodorder.model.Feedback;
import com.pro.foodorder.model.Food;
import com.pro.foodorder.prefs.DataStoreManager;
import com.pro.foodorder.utils.StringUtil;
import com.pro.foodorder.viewmodel.FeedBackViewModel;
import com.pro.foodorder.viewmodel.FoodDetailViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FeedbackFragment extends BaseFragment {
    private List<Food> foods;
    private FragmentFeedbackBinding mFragmentFeedbackBinding;
    private FeedBackViewModel feedBackViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentFeedbackBinding = FragmentFeedbackBinding.inflate(inflater, container, false);

        mFragmentFeedbackBinding.edtEmail.setText(DataStoreManager.getUser().getEmail());
        mFragmentFeedbackBinding.tvSendFeedback.setOnClickListener(v -> onClickSendFeedback());
        feedBackViewModel = new ViewModelProvider(getActivity()).get(FeedBackViewModel.class);
        feedBackViewModel.getLiveDataFoods().observeForever(data -> {
            if (data == null) return;
            this.foods = data;
            String[] items = new String[data.size()];
            for (int i = 0; i < data.size(); i++)
                items[i] = data.get(i).getName();
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.spinner_item,
                items
            );
            mFragmentFeedbackBinding.edtChoseFood.setAdapter(adapter);
        });
        getListFoodsFromFirebase();
        return mFragmentFeedbackBinding.getRoot();
    }

    private void getListFoodsFromFirebase() {
        ControllerApplication.get(getActivity()).getFoodDatabaseReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Food> foods = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Food food = dataSnapshot.getValue(Food.class);
                    if (food == null) {
                        continue;
                    }
                    foods.add(food);
                }
                feedBackViewModel.setDataFoods(foods);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                GlobalFunction.showToastMessage(getContext(), getString(R.string.msg_get_date_error));
            }
        });
    }

    private void onClickSendFeedback() {
        if (getActivity() == null) {
            return;
        }
        MainActivity activity = (MainActivity) getActivity();

        String strName = mFragmentFeedbackBinding.edtName.getText().toString();
        String strPhone = mFragmentFeedbackBinding.edtPhone.getText().toString();
        String strEmail = mFragmentFeedbackBinding.edtEmail.getText().toString();
        String strComment = mFragmentFeedbackBinding.edtComment.getText().toString();
        long foodId = foods.get(mFragmentFeedbackBinding.edtChoseFood.getSelectedItemPosition()).getId();

        if (StringUtil.isEmpty(strName)) {
            GlobalFunction.showToastMessage(activity, getString(R.string.name_require));
        } else if (StringUtil.isEmpty(strComment)) {
            GlobalFunction.showToastMessage(activity, getString(R.string.comment_require));
        } else {
            activity.showProgressDialog(true);
            Feedback feedback = new Feedback(strName, strPhone, strEmail, strComment, foodId);
            ControllerApplication.get(getActivity()).getFeedbackDatabaseReference()
                    .child(String.valueOf(System.currentTimeMillis()))
                    .setValue(feedback, (databaseError, databaseReference) -> {
                activity.showProgressDialog(false);
                sendFeedbackSuccess();
            });
        }
    }

    public void sendFeedbackSuccess() {
        GlobalFunction.hideSoftKeyboard(getActivity());
        GlobalFunction.showToastMessage(getActivity(), getString(R.string.send_feedback_success));
        mFragmentFeedbackBinding.edtName.setText("");
        mFragmentFeedbackBinding.edtPhone.setText("");
        mFragmentFeedbackBinding.edtComment.setText("");
        mFragmentFeedbackBinding.edtChoseFood.setSelection(0);
    }

    @Override
    protected void initToolbar() {
        if (getActivity() != null) {
            ((MainActivity) getActivity()).setToolBar(false, getString(R.string.feedback));
        }
    }
}
