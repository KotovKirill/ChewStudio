package com.example.kirill.chewstudio.StatisticActivity.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kirill.chewstudio.R;

public class GraphicFragment extends AbstractFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmet_graphic_statistic, container, false);
        return view;
    }

    public static GraphicFragment getInstance(Context context){
        GraphicFragment fragment = new GraphicFragment();
        fragment.setTitle(context.getString(R.string.graphic_fragment_text_title));
        return fragment;
    }
}
