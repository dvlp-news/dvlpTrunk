package com.dvlp.news.ui.platform.fragment.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.dvlp.news.R;
import com.dvlp.news.empHttp.MainActivityHttp;
import com.dvlp.news.ui.platform.activity.impl.TitleBarStyle;
import com.dvlp.news.ui.platform.fragment.BaseFragment;


/**
 * Created by liubaba on 2017/2/19.
 * 文章精选
 */
@TitleBarStyle(show = false)
public class ReadFragment extends BaseFragment {
    /**
     * 本类布局根布局
     */
    private View mRootView;
    public static ReadFragment newInstance() {
        Bundle args = new Bundle();

        ReadFragment fragment = new ReadFragment();
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
