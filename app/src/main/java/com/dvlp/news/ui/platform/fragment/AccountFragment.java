package com.dvlp.news.ui.platform.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.dvlp.news.R;
import com.dvlp.news.ui.platform.activity.UserInfoActivity;

import org.xutils.view.annotation.Event;

/**
 * Created by liubaba on 2017/2/19.
 */

public class AccountFragment extends BaseFragment {
    @Override
    protected void init() {
        addView(R.layout.fragment_account);
    }
    public static AccountFragment newInstance() {
        Bundle args = new Bundle();

        AccountFragment fragment = new AccountFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Event(R.id.photo)
    private void getPictureEvent(View view) {
        startActivity(new Intent(getContext(), UserInfoActivity.class));
    }
}
