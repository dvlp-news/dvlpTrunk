package com.dvlp.news.ui.platform.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Toast;

import com.dvlp.news.R;
import com.dvlp.news.ui.AppContext;
import com.dvlp.news.ui.platform.activity.base.BaseFragmentActivity;
import com.dvlp.news.ui.platform.activity.base.CameraAct;
import com.dvlp.news.ui.platform.http.HttpRequestClient;
import com.dvlp.news.ui.utils.Constant;
import com.dvlp.news.ui.views.CommonDialogFragment;
import com.dvlp.news.ui.views.RoundImageView;
import com.qianyilc.library.http.client.HttpContext;
import com.qianyilc.library.http.client.RequestCallBack;
import com.qianyilc.library.util.Logger;

import org.xutils.common.Callback;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;

/**
 * Created by liubaba on 2017/6/6.
 */

public class UserInfoActivity extends CameraAct implements CameraAct.TakePhotoListener,CommonDialogFragment.DialogClickListener{

    @ViewInject(R.id.photo)
    RoundImageView photoView;

    public interface OnUploadHeadListener {
        void onUploadHead(String portrait);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_accounttest);
        x.Ext.init(getApplication());
        x.Ext.setDebug(Logger.enabled);
        getPictureEvent();
    }
    private void getPictureEvent() {
        showToast("点击事件");
        Toast.makeText(UserInfoActivity.this,"dianjishijian",Toast.LENGTH_SHORT).show();
        takePhoto();
    }
    @Override
    public void onTakePhotoResult(String imagePaht, Bitmap bitmap) {
        uploadHead(this, imagePaht, new OnUploadHeadListener() {
            @Override
            public void onUploadHead(String portrait) {
                try {
                    AppContext.user.portrait = portrait;
//                    UserStateManager.getInstance().saveUser(UserInfoActivity.this, AppContext.user);
                } catch (Exception e) {
                    Logger.i("Exception:" + e.toString());
                }

                updateUserPhoto(AppContext.user.portrait);
            }
        });

    }


    private void updateUserPhoto(String portrait) {
        synchronized (photoView) {
            ImageOptions imageOptions = new ImageOptions.Builder().setConfig(Bitmap.Config.ARGB_8888).build();
            x.image().bind(photoView, portrait, imageOptions, new Callback.CommonCallback<Drawable>() {
                @Override
                public void onSuccess(Drawable result) {
//                    photoLable.setVisibility(View.GONE);
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
//                    photoLable.setVisibility(View.VISIBLE);
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {

                }
            });
        }
    }

    public static void uploadHead(final BaseFragmentActivity activity, String imagePaht, final OnUploadHeadListener listener) {
        activity.showLoadDialog();
        HttpRequestClient client = new HttpRequestClient(new HttpContext() {
            @Override
            public Context getContext() {
                return activity;
            }

            @Override
            public void onRequestStart(int requestId) {
            }

            @Override
            public void onRequestFinish(int requestId) {
                activity.closeLoadingDialog();
            }

            @Override
            public void onError(int requestId, String info) {
                activity.showToast(info);
            }

            @Override
            public void onUnExpectResult(int requestId, int resultCode, String info) {
                activity.showToast(info);
            }
        }, new RequestCallBack<String>() {

            @Override
            public void onResult(String s, Object... objects) {
                listener.onUploadHead(s);

                try {

                } catch (Exception e) {
                    Logger.i("Exception:" + e.toString());
                }
            }
        }, String.class, "user.upload");
        client.setUrlServer(Constant.HTTP_SERVER);
        client.addFile("photo", new File(imagePaht));
        client.addNode("portrait", String.class);
        client.setLogTag("photo");
        client.sendPostRequest();
    }
}
