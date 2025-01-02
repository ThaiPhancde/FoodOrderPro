package com.pro.foodorder.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.pro.foodorder.model.Feedback;
import com.pro.foodorder.model.Food;

import java.util.List;

public class FeedBackViewModel extends AndroidViewModel {
    public FeedBackViewModel(@NonNull Application application) {
        super(application);
    }
    private MutableLiveData<List<Food>> foods = new MutableLiveData<>();
    public final MutableLiveData<List<Food>> getLiveDataFoods() {
        return foods;
    }
    public void setDataFoods(List<Food> foods) {
        this.foods.setValue(foods);
    }
}