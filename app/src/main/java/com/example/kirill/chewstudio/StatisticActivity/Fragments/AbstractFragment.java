package com.example.kirill.chewstudio.StatisticActivity.Fragments;

import android.support.v4.app.Fragment;


public abstract class AbstractFragment extends Fragment {
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
