package com.dvlp.news.ui.views;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.KeyEvent;

import com.dvlp.news.R;


/**
 * @author 刘伟 E-mail:liuwei1@leju.com
 * @version 创建时间：2014年7月18日 下午12:12:40
 * @类说明 diaolog
 */

public class CommonDialogFragment extends DialogFragment {
    DialogClickListener listener;

    DialogInterface.OnClickListener onItemClickListener;

    public static final int MODE_SINGLE = 1;
    public static final int MODE_DOUBLE = 2;
    private int mode = MODE_DOUBLE;

    private boolean isMessageCenter;
    private boolean isCancelable = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setStyle(CommonDialogFragment.STYLE_NORMAL, R.style.AppBaseTheme);
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    @Override
    public void setCancelable(boolean cancelable) {
        isCancelable = cancelable;
    }

    @SuppressLint("NewApi")
    public static CommonDialogFragment getInstance(String title, String message, DialogClickListener listener) {
        CommonDialogFragment frag = new CommonDialogFragment();
        frag.listener = listener;
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("message", message);

        frag.setArguments(args);
        return frag;
    }

    @SuppressLint("NewApi")
    public static CommonDialogFragment getInstance(String title, int layoutid, DialogClickListener listener) {
        CommonDialogFragment frag = new CommonDialogFragment();
        frag.listener = listener;
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putInt("layoutid", layoutid);

        frag.setArguments(args);
        return frag;
    }

    @SuppressLint("NewApi")
    public static CommonDialogFragment getInstance(CharSequence title, CharSequence message, CharSequence btnSureText, CharSequence btnCancelText, DialogClickListener listener) {

        CommonDialogFragment frag = new CommonDialogFragment();
        frag.listener = listener;
        Bundle args = new Bundle();
        args.putCharSequence("title", title);
        args.putCharSequence("message", message);
        args.putCharSequence("cancelText", btnCancelText);
        args.putCharSequence("sureText", btnSureText);
        frag.setArguments(args);

        return frag;
    }


    @SuppressLint("NewApi")
    public static CommonDialogFragment getInstance(String title, CharSequence message, CharSequence btnSureText, boolean isCancelable, DialogClickListener listener) {

        CommonDialogFragment frag = new CommonDialogFragment();
        frag.listener = listener;
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putCharSequence("message", message);
        args.putBoolean("isCancelable", isCancelable);
        frag.setArguments(args);
        return frag;
    }

    @SuppressLint("NewApi")
    public static CommonDialogFragment getInstance(String title, String[] datas, int checkedItem, DialogInterface.OnClickListener listener) {

        CommonDialogFragment frag = new CommonDialogFragment();
        frag.setMode(MODE_SINGLE);
        frag.onItemClickListener = listener;
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putInt("checkedItem", checkedItem);
        args.putStringArray("datas", datas);

        frag.setArguments(args);

        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog=null;
        final CharSequence message = getArguments().getCharSequence("message");
        final CharSequence title = getArguments().getCharSequence("title");
        final CharSequence btnSureText = TextUtils.isEmpty(getArguments().getCharSequence("sureText")) ? "确定" : getArguments().getCharSequence("sureText");
        final CharSequence btncancelText = TextUtils.isEmpty(getArguments().getCharSequence("cancelText")) ? "取消" : getArguments().getCharSequence("cancelText");
        final int layoutid = getArguments().getInt("layoutid", -1);
        String[] datas = getArguments().getStringArray("datas");
        boolean isCancelable = getArguments().getBoolean("isCancelable");
        if (isCancelable) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setCancelable(isCancelable);

            builder.setTitle(title);
            builder.setMessage(message);
            builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                        return true;
                    } else {
                        return false;
                    }
                }
            });

            if (layoutid != -1) {
                builder.setView(getActivity().getLayoutInflater().inflate(layoutid, null));
            }


            builder.setPositiveButton(btnSureText, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                    if (listener != null) {
                        listener.confirm();
                    }
                }
            });
            dialog=builder.create();
            dialog.setCanceledOnTouchOutside(false);
            return dialog;
        }

        if (datas == null || datas.length == 0) {
            UIAlertDialog.Builder builder = new UIAlertDialog.Builder(getActivity());

            builder.setTitle(title);
            builder.setMessage(message);

            if (layoutid != -1) {
                builder.setContentView(getActivity().getLayoutInflater().inflate(layoutid, null));
            }

            builder.setPositiveButton(btnSureText, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    dismiss();
                    if (listener != null) {
                        listener.confirm();
                    }
                }
            });
            if (mode == MODE_DOUBLE) {
                builder.setNegativeButton(btncancelText, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dismiss();
                        if (listener != null) {
                            listener.cancel();
                        }
                    }
                });
            }
            dialog=builder.create();

            return  dialog;
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(title);
            builder.setMessage(message);

            if (layoutid != -1) {
                builder.setView(getActivity().getLayoutInflater().inflate(layoutid, null));
            }

            if (datas != null && datas.length > 0) {
                switch (mode) {
                    case MODE_SINGLE:
                        builder.setItems(datas, onItemClickListener);
                        break;
                    default:
                        builder.setSingleChoiceItems(datas, 0, null);
                        break;
                }
            }

            builder.setPositiveButton(btnSureText, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                    if (listener != null) {
                        listener.confirm();
                    }
                }
            });
            if (mode == MODE_DOUBLE) {
                builder.setNegativeButton(btncancelText, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (listener != null) {
                            listener.cancel();
                        }
                    }
                });
            }

            dialog=builder.create();

            return  dialog;
        }

    }

    public void setMessageCenter(boolean isCenter) {
        isMessageCenter = isCenter;
    }

    public interface DialogClickListener {
        void confirm();

        void cancel();
    }

}
