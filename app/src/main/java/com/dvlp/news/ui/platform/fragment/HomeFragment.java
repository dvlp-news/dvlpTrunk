package com.dvlp.news.ui.platform.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.dvlp.news.R;
import com.dvlp.news.empHttp.MainActivityHttp;
import com.dvlp.news.ui.platform.activity.impl.TitleBarStyle;
import com.dvlp.news.wxpay.PayActivity;


/**
 * Created by liubaba on 2017/2/19.
 */
@TitleBarStyle(show = false)
public class HomeFragment extends BaseFragment {
    /**
     * 本类布局根布局
     */
    private View mRootView;
    public static HomeFragment newInstance() {
        Bundle args = new Bundle();

        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void init() {
        mRootView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_home, null);
        TextView textView= (TextView) mRootView.findViewById(R.id.toPays);
        addView(mRootView);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(getContext(), PayActivity.class));
                startActivity(new Intent(getContext(), MainActivityHttp.class));

            }
        });
    }
}
