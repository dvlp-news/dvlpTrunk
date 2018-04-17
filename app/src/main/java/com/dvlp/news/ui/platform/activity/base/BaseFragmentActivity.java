package com.dvlp.news.ui.platform.activity.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewStub;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dvlp.news.R;
import com.dvlp.news.ui.platform.activity.impl.OnGuideListener;
import com.dvlp.news.ui.platform.activity.impl.TitleBarStyle;
import com.dvlp.news.ui.platform.activity.impl.UiIdentification;
import com.dvlp.news.ui.views.CommonDialogFragment;
import com.dvlp.news.ui.views.GifView;
import com.dvlp.news.ui.views.NoFrameDialog;
import com.dvlp.news.ui.views.QianyiTitleBar;
import com.qianyilc.library.http.client.HttpContext;

import org.xutils.x;

/**
 * Created by liubaba on 2017/1/11.
 */
public class BaseFragmentActivity extends FragmentActivity implements HttpContext,UiIdentification,QianyiTitleBar.OnTitleBarListener ,CommonDialogFragment.DialogClickListener {


    protected LinearLayout rootLayout = null;
    protected QianyiTitleBar mTitleBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            String FRAGMENTS_TAG = "android:support:fragments";

            savedInstanceState.remove(FRAGMENTS_TAG);

        }
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.act_base);
        rootLayout = (LinearLayout) findViewById(R.id.rootLayout);
        if (getClass().isAnnotationPresent(TitleBarStyle.class)) {
            boolean visible = getClass().getAnnotation(TitleBarStyle.class).show();
            if (visible) {
                initTitleBar();
            }
        } else {
            initTitleBar();
        }
    }

    private void initTitleBar() {
        if (mTitleBar != null) {
            return;
        }
        ViewStub viewStub = (ViewStub) rootLayout.findViewById(R.id.stub_titleBar);
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

    //httpContext
    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    public NoFrameDialog getLoadingDialog() {
        return loadingDialog;
    }

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

    //添加布局
    @Override
    public void addView(View view) {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        layoutParams.addRule(RelativeLayout.BELOW, R.id.titleBar);
        rootLayout.addView(view, layoutParams);

        x.view().inject(this, rootLayout);
    }

    @Override
    public void addView(int layoutId) {
        View view = View.inflate(this, layoutId, null);
        addView(view);
    }

    @Override
    public void showToast(String str) {

    }

    @Override
    public void showToastCenter(String str) {

    }

    private NoFrameDialog loadingDialog = null;
    @Override
    public void showLoadDialog() {
        closeLoadingDialog();
        View view = View.inflate(getContext(), R.layout.loading_dialog, null);
        GifView gifView = (GifView) view.findViewById(R.id.gifView);
        gifView.setMovieResource(R.raw.loading_big);
        loadingDialog = new NoFrameDialog(this, view);
        loadingDialog.show();
    }

    @Override
    public void showLoadDialog(String str) {
        try {
            closeLoadingDialog();
            View view = View.inflate(getContext(), R.layout.loading_dialog, null);
            GifView gifView = (GifView) view.findViewById(R.id.gifView);
            gifView.setMovieResource(R.raw.loading_big);

            TextView textView = (TextView) view.findViewWithTag("text");

            if (textView != null) textView.setText(str);

            loadingDialog = new NoFrameDialog(this, view);

            loadingDialog.show();
        } catch (Exception e) {

        }
    }

    @Override
    public void showLoadDialog(boolean isCancel) {
        showLoadDialog();
        loadingDialog.setCancelable(isCancel);
    }

    @Override
    public void showLoadDialog(String str, boolean isCancel) {
        showLoadDialog(str);
        loadingDialog.setCancelable(isCancel);
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



    //添加tabar监听

    @Override
    public void onClickLeft(View view) {

    }

    @Override
    public void onClickRight1(View view) {

    }

    @Override
    public void onClickRight2(View view) {

    }

    //dialog 确认 和取消监听
    @Override
    public void confirm() {

    }

    @Override
    public void cancel() {

    }
}
