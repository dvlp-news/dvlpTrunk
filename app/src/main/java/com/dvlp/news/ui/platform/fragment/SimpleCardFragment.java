package com.dvlp.news.ui.platform.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import com.dvlp.news.R;


@SuppressLint("ValidFragment")
public class SimpleCardFragment extends BaseFragment {
    private String mTitle;

    public static SimpleCardFragment getInstance(String title) {
        SimpleCardFragment sf = new SimpleCardFragment();
        sf.mTitle = title;
        Log.d("liu","title========"+title);
        return sf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    protected void init() {
addView(R.layout.fragment_home);
    }
}