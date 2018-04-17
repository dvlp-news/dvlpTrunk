package com.dvlp.news.ui.views;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import com.dvlp.news.R;


/**
 * 无边框的弹出框
 * Created by lw on 2015/2/4.
 */
public class NoFrameDialog extends Dialog {


    public NoFrameDialog(Context context, View view) {
        super(context, R.style.CustomProgressDialog);
        setContentView(view);
    }
    public NoFrameDialog(Context context, int layoutId) {
        super(context, R.style.CustomProgressDialog);
        setContentView(layoutId);
    }


}
