package com.dvlp.news.ui.platform.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.dvlp.news.R;
import com.dvlp.news.ui.AppContext;
import com.dvlp.news.ui.platform.activity.base.BaseFragmentActivity;
import com.dvlp.news.ui.platform.activity.impl.OnGuideListener;
import com.dvlp.news.ui.platform.activity.impl.TitleBarStyle;
import com.dvlp.news.ui.platform.activity.impl.UiIdentification;
import com.dvlp.news.ui.views.NoFrameDialog;
import com.dvlp.news.ui.views.QianyiTitleBar;
import com.qianyilc.library.http.client.HttpContext;
import com.qianyilc.library.util.Logger;

import org.xutils.x;

/**
 * Created by liubaba on 2017/1/13.
 */

public abstract class BaseFragment extends Fragment implements HttpContext,UiIdentification,QianyiTitleBar.OnTitleBarListener {


    protected BaseFragmentActivity mBaseFragmentActivity;
    protected RelativeLayout rootView;
    protected ViewGroup mViewContainer;
    //loading加载信息
    private String loadingText = null;
    boolean isShowLoadingDialog;
    protected QianyiTitleBar mTitleBar;

    private View mViewStatusBar;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mBaseFragmentActivity = (BaseFragmentActivity) activity;
    }
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        rootView = (RelativeLayout) inflater.inflate(R.layout.base_fragment, null);
        initStatusView();
        mViewContainer= (ViewGroup) rootView.findViewById(R.id.container);
        //是否显示TitleBar
        if (getClass().isAnnotationPresent(TitleBarStyle.class)) {
            boolean show = getClass().getAnnotation(TitleBarStyle.class).show();
            if (show)
                initTitleBar();
        } else {
            initTitleBar();
        }
        Log.i("istrue",getUserVisibleHint()+"======"+(mViewContainer.getChildCount() == 0));
        if (getUserVisibleHint() && mViewContainer.getChildCount() == 0) {
            init();
        }

        return rootView;
    }
    private void initStatusView() {
        this.mViewStatusBar = new View(getContext());
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(-1, 20);
        mViewStatusBar.setLayoutParams(layoutParams);
        mViewStatusBar.setBackgroundResource(R.color.colorAccent);
        rootView.addView(this.mViewStatusBar, 0);
        onPostCreate();
    }

    protected void onPostCreate() {

        Window window = getActivity().getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        if(Build.VERSION.SDK_INT >= 21) {
            View systemDecor = window.getDecorView();
            systemDecor.setSystemUiVisibility(1280);
            layoutParams.flags |= -2147483648;
            window.setStatusBarColor(0);
        } else {
            layoutParams.flags |= 67108864;
        }

        window.setAttributes(layoutParams);
    }

    private void initTitleBar() {
        if (mTitleBar != null) {
            return;
        }
        ViewStub viewStub = (ViewStub) rootView.findViewById(R.id.stub_titleBar);
        if (getClass().isAnnotationPresent(TitleBarStyle.class)) {
            TitleBarStyle.Style style = getClass().getAnnotation(TitleBarStyle.class).style();
            if (style == TitleBarStyle.Style.BLACK) {
                viewStub.setLayoutResource(R.layout.layout_titlebar_black);
            } else {
                viewStub.setLayoutResource(R.layout.layout_titlebar_white);
            }
        } else {
            viewStub.setLayoutResource(R.layout.layout_titlebar_black);
        }
        mTitleBar = (QianyiTitleBar) viewStub.inflate();
        mTitleBar.setOnTitleBarListener(this);
    }

    //   请求回调
    @Override
    public void onRequestStart(int requestId) {

    }

    @Override
    public void onRequestFinish(int requestId) {

    }

    @Override
    public void onError(int requestId, String info) {

    }

    @Override
    public void onUnExpectResult(int requestId, int resultCode, String info) {

    }
    public String getPageTitle() {
        return this.getClass().getName();
    }

//    UiIdentification 接口实现
    @Override
    public void addView(View view) {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);

        mViewContainer.addView(view, params);

        x.view().inject(this, rootView);
    }

    @Override
    public void addView(int layoutId) {
        View view=LayoutInflater.from(AppContext.getContext()).inflate(layoutId,null);
        addView(view);
    }

    public void removeView(View mView) {
        if (mViewContainer != null)
            mViewContainer.removeView(mView);
    }

    @Override
    public void showToast(String str) {
        if (!TextUtils.isEmpty(str)) {
            Toast.makeText(AppContext.getInstance(), str, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void showToastCenter(String str) {
        if (!TextUtils.isEmpty(str)) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View view = inflater.inflate(R.layout.toast_show_message_center, null);
            TextView message = (TextView) view.findViewById(R.id.toast_login_act_tvId);
            message.setText(str);
            Toast toast = new Toast(getContext());
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(view);
            toast.show();
        }
    }

    @Override
    public void showLoadDialog() {
        loadingText = null;
        getParentActivity().showLoadDialog();
        isShowLoadingDialog = true;
    }

    @Override
    public void showLoadDialog(String str) {
        loadingText = str;
        getParentActivity().showLoadDialog(str);
        isShowLoadingDialog = true;
    }

    @Override
    public void showLoadDialog(boolean isCancel) {
        Logger.i("showLoadDialog");
        loadingText = null;
        getParentActivity().showLoadDialog(isCancel);
        isShowLoadingDialog = true;
    }

    @Override
    public void showLoadDialog(String str, boolean isCancel) {
        getParentActivity().showLoadDialog(str, isCancel);
    }

    public NoFrameDialog getLoadingDialog() {
        return getParentActivity().getLoadingDialog();
    }
    @Override
    public void closeLoadingDialog() {
        getParentActivity().closeLoadingDialog();
    }

    @Override
    public void setTitle(CharSequence title) {
        if(mTitleBar != null) {
            mTitleBar.setTitle(title);
        }
    }

    @Override
    public void setTitle(int resId) {
        if(mTitleBar != null) {
            mTitleBar.setTitle(resId);
        }
    }

    @Override
    public void setTitle(CharSequence title, CharSequence subTitle) {
        if(mTitleBar != null) {
            mTitleBar.setTitle(title, subTitle);
        }
    }

    @Override
    public void setTitle(View view) {
        if(mTitleBar != null) {
            mTitleBar.setTitle(view);
        }
    }

    @Override
    public void setTitle(View view, RelativeLayout.LayoutParams layoutParams) {
        if(mTitleBar != null) {
            mTitleBar.setTitle(view, layoutParams);
        }
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


    //titlebar 点击事件
    @Override
    public void onClickLeft(View view) {

    }

    @Override
    public void onClickRight1(View view) {

    }

    @Override
    public void onClickRight2(View view) {

    }

    //本类方法
    public BaseFragmentActivity getParentActivity() {
        return (BaseFragmentActivity) getActivity();
    }
    protected abstract void init(); // 模块初始化方法 在onActivityCreated方法的最后调用

    @Override
    public Context getContext() {
        return getActivity();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (mViewContainer != null) {
            if (mViewContainer.getChildCount() == 0) {
                if (isVisibleToUser)
                    init();
            } else {
                if (isVisibleToUser) {
                    onShow();
                } else {
                    onHide();
                }
            }
        }
    }
    public void onShow() {
    }

    public void onHide() {
    }
}
