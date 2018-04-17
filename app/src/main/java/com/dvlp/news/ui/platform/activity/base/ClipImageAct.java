package com.dvlp.news.ui.platform.activity.base;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.dvlp.news.R;
import com.dvlp.news.ui.views.ClipImageLayout;
import com.dvlp.news.ui.views.QianyiTitleBar;
import com.qianyilc.library.util.FileUtils;
import com.qianyilc.library.util.ImageUtil;


import org.xutils.view.annotation.ViewInject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * 类说明 :剪裁图片类
 *
 * @author 刘伟 E-mail:liuwei1@leju.com
 * @version 创建时间：2014年9月4日 下午3:35:15
 */
public class ClipImageAct extends BaseSecurityActivity {

    public String cutPath = new FileUtils().getSDPATH() + "tmp.jpg";

   @ViewInject(R.id.cropImageView)
    public ClipImageLayout cropImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
//        addView(R.layout.act_base_clip);
        setContentView(R.layout.act_base_clip);
        cropImageView= (ClipImageLayout) findViewById(R.id.cropImageView);
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss", Locale.CHINESE);
        cutPath = new FileUtils().getSDPATH() + dateFormat.format(date) + ".jpg";

        String imagePath = getIntent().getStringExtra("path");
        if (!TextUtils.isEmpty(imagePath)) {
            Toast.makeText(ClipImageAct.this,"dianjishijian"+imagePath,Toast.LENGTH_SHORT).show();
            cropImageView.setLocalImagePath(imagePath);
        } else {
            showToast("所选图片路径有误");
        }
        setTitle("剪裁图片");

        mTitleBar.setButtonText(QianyiTitleBar.BUTTON_RIGHT1, "剪裁");
        mTitleBar.setButtonText(QianyiTitleBar.BUTTON_RIGHT2, "取消");
    }

    @Override
    public void onClickRight1(View view) {
        Bitmap bitmap = cropImageView.clip();
        if (bitmap != null) {
            ImageUtil.saveBitmap(bitmap, cutPath);
            Intent intent = new Intent();
//            intent.putExtra("bitmap", bitmap);
//            intent.pute
            intent.putExtra("path", cutPath);
            setResult(RESULT_OK, intent);
        } else {
            setResult(RESULT_CANCELED);
        }
        finish();
    }

    @Override
    public void onClickLeft(View view) {
        finish();
    }


    @Override
    public void onClickRight2(View view) {
        finish();
    }

}
