package com.example.kirill.chewstudio.FoodActivity.ChooseMenuActivity;

import android.os.Parcel;
import android.os.Parcelable;

public class Dish implements Cloneable, Parcelable{
    private int drawableAvatar;
    private int name;
    private String weight;
    private String energy;
    private int drawable;
    private int describeDrawable;
    private int describe;

    public Dish(int drawableAvatar, int name, String weight, String energy, int drawable, int describeDrawable, int describe) {
        this.drawableAvatar = drawableAvatar;
        this.name = name;
        this.weight = weight;
        this.energy = energy;
        this.drawable = drawable;
        this.describeDrawable = describeDrawable;
        this.describe = describe;
    }

    protected Dish(Parcel in) {
        in.readParcelable(Dish.class.getClassLoader());
        drawableAvatar = in.readInt();
        name = in.readInt();
        weight = in.readString();
        energy = in.readString();
        drawable = in.readInt();
        describeDrawable = in.readInt();
        describe = in.readInt();
    }

    public static final Creator<Dish> CREATOR = new Creator<Dish>() {
        @Override
        public Dish createFromParcel(Parcel in) {
            return new Dish(in);
        }

        @Override
        public Dish[] newArray(int size) {
            return new Dish[size];
        }
    };

    public int getName() {
        return name;
    }
    public void setName(int name) {
        this.name = name;
    }

    public String getWeight() {
        return weight;
    }
    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getEnergy() {
        return energy;
    }
    public void setEnergy(String energy) {
        this.energy = energy;
    }

    public int getDrawable() {
        return drawable;
    }
    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    public int getDrawableAvatar() {
        return drawableAvatar;
    }
    public void setDrawableAvatar(int drawableAvatar) {
        this.drawableAvatar = drawableAvatar;
    }

    public int getDescribeDrawable() {
        return describeDrawable;
    }
    public void setDescribeDrawable(int describeDrawable) {
        this.describeDrawable = describeDrawable;
    }

    public int getDescribe() {
        return describe;
    }
    public void setDescribe(int describe) {
        this.describe = describe;
    }

    @Override
    protected Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(drawableAvatar);
        dest.writeInt(name);
        dest.writeString(weight);
        dest.writeString(energy);
        dest.writeInt(drawable);
        dest.writeInt(describeDrawable);
        dest.writeInt(describe);
    }
}
