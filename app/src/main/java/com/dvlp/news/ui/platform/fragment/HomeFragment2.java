package com.dvlp.news.ui.platform.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.dvlp.news.R;
import com.dvlp.news.ui.platform.activity.impl.TitleBarStyle;

/**
 * Created by liubaba on 2017/2/19.
 */
@TitleBarStyle(show = false)

public class HomeFragment2 extends BaseFragment {


    public static HomeFragment2 newInstance() {
        Bundle args = new Bundle();

        HomeFragment2 fragment = new HomeFragment2();
        fragment.setArguments(args);
        return fragment;
    }

    private View mViewStatusBar;
    private LinearLayout mRootView;
    @Override
    protected void init() {

       mRootView= (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.fragment_home,null);
        mViewStatusBar = new View(getContext());
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(-1, 30);
        mViewStatusBar.setLayoutParams(layoutParams);
        mViewStatusBar.setBackgroundResource(R.color.text_color_red);
        mRootView.addView(mViewStatusBar, 0);
        addView(mRootView);
    }
}
