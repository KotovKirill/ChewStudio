package com.example.kirill.chewstudio.FoodActivity.ChooseMenuActivity;

class Dish implements Cloneable{
    private int drawableAvatar;
    private int name;
    private String weight;
    private String energy;
    private int drawable;

    Dish(int drawableAvatar, int name, String weight, String energy, int drawable) {
        this.drawableAvatar = drawableAvatar;
        this.name = name;
        this.weight = weight;
        this.energy = energy;
        this.drawable = drawable;
    }

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

    @Override
    protected Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
