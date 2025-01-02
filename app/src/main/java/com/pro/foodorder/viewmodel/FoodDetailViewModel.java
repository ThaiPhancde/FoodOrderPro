package com.pro.foodorder.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.pro.foodorder.model.Feedback;

import java.util.List;

public class FoodDetailViewModel extends AndroidViewModel {
    public FoodDetailViewModel(@NonNull Application application) {
        super(application);
    }
    private MutableLiveData<List<Feedback>> feedBacks = new MutableLiveData<>();
    public final MutableLiveData<List<Feedback>> getLiveDataFeedBacks() {
        return feedBacks;
    }
    public void setDataFeedBacks(List<Feedback> feedBacks) {
        this.feedBacks.setValue(feedBacks);
    }
}
