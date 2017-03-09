package com.example.kirill.chewstudio.StatisticActivity.Fragments;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

/**
 * Created by kirill on 08.02.2017.
 */

public class Food implements Parcelable{
    private int[] calories;
    private int[] proteins;
    private int[] fats;
    private int[] carbohydrates;

    public Food(int[] calories, int[] proteins, int[] fats, int[] carbohydrates) {
        this.calories = calories;
        this.proteins = proteins;
        this.fats = fats;
        this.carbohydrates = carbohydrates;
    }

    public int[] getCalories() {
        return calories;
    }
    public void setCalories(int[] calories) {
        this.calories = calories;
    }

    public int[] getProteins() {
        return proteins;
    }
    public void setProteins(int[] proteins) {
        this.proteins = proteins;
    }

    public int[] getFats() {
        return fats;
    }
    public void setFats(int[] fats) {
        this.fats = fats;
    }

    public int[] getCarbohydrates() {
        return carbohydrates;
    }
    public void setCarbohydrates(int[] carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    @Override
    public String toString() {
        return "Food{" +
                "calories=" + Arrays.toString(calories) +
                ", proteins=" + Arrays.toString(proteins) +
                ", fats=" + Arrays.toString(fats) +
                ", carbohydrates=" + Arrays.toString(carbohydrates) +
                '}';
    }
}
