package com.dvlp.news.ui.views;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.dvlp.news.R;
import com.qianyilc.library.util.DeviceInfo;

import org.xutils.common.util.DensityUtil;

/**
 * TODO: document your custom view class.
 */
public class QianyiTitleBar extends RelativeLayout implements View.OnClickListener {

    private Context mContext;

    private ColorStateList mTextColors = ColorStateList.valueOf(Color.WHITE);
    private Drawable mBackDrawable;

    protected TextView btnLeft, btnRight1, btnRight2;

    private OnTitleBarListener mOnTitleBarListener;

    public static final int BUTTON_LEFT = 0;
    public static final int BUTTON_RIGHT1 = 2;
    public static final int BUTTON_RIGHT2 = 1;

    public QianyiTitleBar(Context context) {
        super(context);
        init(null, 0);
    }

    public QianyiTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public QianyiTitleBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        mContext = getContext();
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.QianyiTitleBar, defStyle, 0);

        if (a.hasValue(R.styleable.QianyiTitleBar_barTextColor)) {
            mTextColors = a.getColorStateList(R.styleable.QianyiTitleBar_barTextColor);
        }

        if (a.hasValue(R.styleable.QianyiTitleBar_backDrawable)) {
            mBackDrawable = a.getDrawable(
                    R.styleable.QianyiTitleBar_backDrawable);
            mBackDrawable.setCallback(this);
        } else {
            mBackDrawable = getResources().getDrawable(R.mipmap.btn_back_white);
        }

        a.recycle();

        LayoutInflater.from(mContext).inflate(R.layout.layout_qianyi_title_bar, this);

        btnLeft = (TextView) findViewById(R.id.btnLeft);
        btnRight1 = (TextView) findViewById(R.id.btnRight1);
        btnRight2 = (TextView) findViewById(R.id.btnRight2);

        btnLeft.setTextColor(mTextColors);
        btnLeft.setCompoundDrawablesWithIntrinsicBounds(mBackDrawable, null, null, null);
        btnLeft.setOnClickListener(this);

        btnRight1.setTextColor(mTextColors);
        btnRight1.setOnClickListener(this);

        btnRight2.setTextColor(mTextColors);
        btnRight2.setOnClickListener(this);
    }

    public void setOnTitleBarListener(OnTitleBarListener listener) {
        mOnTitleBarListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (mOnTitleBarListener == null)
            return;
        switch (v.getId()) {
            case R.id.btnLeft:
                mOnTitleBarListener.onClickLeft(v);
                break;
            case R.id.btnRight1:
                mOnTitleBarListener.onClickRight1(v);
                break;
            case R.id.btnRight2:
                mOnTitleBarListener.onClickRight2(v);
                break;
        }
    }

    public void setTitle(CharSequence title) {
        String tag = "title";
        TextView textView = (TextView) findViewWithTag(tag);
        if (textView == null) {
            textView = new TextView(mContext);
            textView.setTag(tag);
            textView.setTextSize(17);
            textView.setTextColor(mTextColors);
            textView.setGravity(Gravity.CENTER);

            LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            layoutParams.width= DeviceInfo.getScreenSize(mContext).x*2/3;

            addView(textView, layoutParams);
        }
        if (!title.toString().contains("\n")) {
            textView.setSingleLine(true);
            textView.setEllipsize(TextUtils.TruncateAt.MIDDLE);
        } else {
            textView.setSingleLine(false);
        }
        textView.setText(title);
    }

    public void setTitle(CharSequence title, CharSequence subTitle) {
        setTitle(title, subTitle, 0.5f);
    }

    public void setTitle(CharSequence title, CharSequence subTitle, float proportion) {
        String titleStr = title + "\n" + subTitle;
        SpannableString sp = new SpannableString(titleStr);
        sp.setSpan(new RelativeSizeSpan(proportion), sp.length() - subTitle.length(), sp.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        setTitle(sp);
    }

    public void setTitle(int titleId) {
        setTitle(getResources().getText(titleId));
    }

    public void setTitle(View view) {
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);

        addView(view, layoutParams);
    }

    public void setTitle(View view, LayoutParams layoutParams) {
        addView(view, layoutParams);
    }

    public void setButtonText(int index, int resid) {
        setButtonText(index, getResources().getText(resid), null);
    }

    public void setButtonText(int index, CharSequence text) {
        setButtonText(index, text, null);
    }

    public void setButtonText(int index, CharSequence text, OnClickListener listener) {
        TextView button = getButton(index);
        button.setText(text);
        if (listener != null) {
            button.setOnClickListener(listener);
        }
        button.setVisibility(View.VISIBLE);
    }

    public void setButtonDrawable(int index, int resId) {
        TextView button = getButton(index);
        button.setCompoundDrawablesWithIntrinsicBounds(resId, 0, 0, 0);
        button.setVisibility(View.VISIBLE);
    }

    public void setButtonDrawable(int index, Drawable drawable, OnClickListener listener) {
        TextView button = getButton(index);

        drawable.setBounds(0, 0, DensityUtil.dip2px(22.5F), DensityUtil.dip2px(22.5F));

        button.setCompoundDrawables(drawable, null, null, null);
        button.setOnClickListener(listener);
        button.setVisibility(View.VISIBLE);
    }

    public void setButtonVisible(int index, boolean visible) {
        TextView button = getButton(index);
        if (visible) {
            button.setVisibility(View.VISIBLE);
        } else {
            button.setVisibility(View.GONE);
        }
    }

    public TextView getButton(int index) {
        TextView button = null;
        switch (index) {
            case BUTTON_LEFT:
                button = btnLeft;
                break;
            case BUTTON_RIGHT2:
                button = btnRight2;
                break;
            case BUTTON_RIGHT1:
                button = btnRight1;
                break;
        }
        return button;
    }

    public interface OnTitleBarListener {
        void onClickLeft(View view);
        void onClickRight1(View view);
        void onClickRight2(View view);
    }
}
