package com.dvlp.news.ui.platform.activity.base;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.dvlp.news.ui.platform.activity.impl.OnGuideListener;
import com.dvlp.news.ui.platform.activity.impl.UiIdentification;

/**
 * Created by liubaba on 2017/1/11.
 */

public class BaseSecurityActivity extends BaseFragmentActivity implements UiIdentification {

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void addView(View view) {

    }

    @Override
    public void addView(int layoutId) {

    }

    @Override
    public void showToast(String str) {

    }

    @Override
    public void showToastCenter(String str) {

    }

    @Override
    public void showLoadDialog() {

    }

    @Override
    public void showLoadDialog(String str) {

    }

    @Override
    public void showLoadDialog(boolean isCancel) {

    }

    @Override
    public void showLoadDialog(String str, boolean isCancel) {

    }

    @Override
    public void closeLoadingDialog() {

    }

    @Override
    public void setTitle(CharSequence title, CharSequence subTitle) {

    }

    @Override
    public void setTitle(View view) {

    }

    @Override
    public void setTitle(View view, RelativeLayout.LayoutParams layoutParams) {

    }

    @Override
    public boolean isGuide() {
        return false;
    }

    @Override
    public void addGuide(int layoutResource, boolean blowTitlebar, int confirmId, OnGuideListener listener) {

    }

    @Override
    public void removeGuide() {

    }
}
