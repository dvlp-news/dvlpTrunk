package com.dvlp.news.ui.platform.activity.impl;

import android.view.View;
import android.widget.RelativeLayout;

/**
 * UI的标识接口
 * Created by 刘伟 on 2015/2/3.
 */
public interface UiIdentification {
    void addView(View view);

    void addView(int layoutId);

    void showToast(String str);

    void showToastCenter(String str);

    void showLoadDialog();

    void showLoadDialog(String str);

    void showLoadDialog(boolean isCancel);

    void showLoadDialog(String str, boolean isCancel);

    void closeLoadingDialog();

    void setTitle(CharSequence title);

    void setTitle(int titleId);

    void setTitle(CharSequence title, CharSequence subTitle);

    void setTitle(View view);

    void setTitle(View view, RelativeLayout.LayoutParams layoutParams);

    /**
     * 是否 有引导浮层
     * @return
     */
    boolean isGuide();

    /**
     * 添加浮层
     *
     * @param layoutResource
     * @param blowTitlebar
     * @param confirmId
     * @param listener
     */
    void addGuide(int layoutResource, boolean blowTitlebar, int confirmId, OnGuideListener listener);

    /**
     * 移除浮层
     */
    void removeGuide();
}
