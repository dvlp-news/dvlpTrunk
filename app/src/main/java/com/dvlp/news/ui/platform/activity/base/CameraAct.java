package com.dvlp.news.ui.platform.activity.base;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.qianyilc.library.util.FileUtils;
import com.qianyilc.library.util.ImageUtil;
import com.qianyilc.library.util.Logger;

import java.io.File;
import java.io.IOException;

/**
 * 类说明 拍照或者选取图片并返回剪裁后的图片地址
 */

public class CameraAct extends BaseSecurityActivity {
    public static final int PHOTO_TAKEPHOTO = 11;// 拍照
    public static final int PHOTO_GALLERY = 2;// 从相册中选择
    public static final int PHOTO_CUT = 3;// 从相册中选择
    TakePhotoListener takePhotoListener;
    public static Uri photoUri = null;
    public static String cutPath = new FileUtils().getSDPATH() + "tmp.jpg";
    private static String photoTempFile = null;
    private static File tmpeFile = null;

    private int picWidth = 150, picHeight = 150;

    private String dialogTitle = "修改头像";

    private boolean cutEnable = true;// 是否需要剪裁图片

    /**
     * 选择获取图片的方式，拍照或者从相册中选取
     */

    public void takePhoto() {

        boolean sdState = false;

        String state = android.os.Environment.getExternalStorageState();
        // 判断SdCard是否存在并且是可用的
        if (android.os.Environment.MEDIA_MOUNTED.equals(state)) {
            if (android.os.Environment.getExternalStorageDirectory().canWrite()) {
                sdState = true;
            }
        }

        if (!sdState) {
            showToast("sd卡不可读,修改头像功能暂不能使用.");
            return;
        }

        Builder builder = new Builder(this);
        builder.setTitle(dialogTitle);
        builder.setItems(new String[]{"拍照", "从相册获取"}, onClickListener);
        Dialog dialog = builder.create();
        dialog.show();
    }

    OnClickListener onClickListener = new OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {

            if (which == 0) {

                doTakePhotoAction();

            } else {
                gotoGallery();
            }

        }
    };

    /**
     * 打开相册
     */
    private void gotoGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, PHOTO_GALLERY);
    }

    /**
     * 打开照相机
     */
    private void doTakePhotoAction() {

        photoTempFile = new FileUtils().getSDPATH() + "tmpPhoto.jpg";
        tmpeFile = new File(photoTempFile);
        if (tmpeFile.exists()) {
            tmpeFile.delete();
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tmpeFile));

        startActivityForResult(intent, PHOTO_TAKEPHOTO);

    }

    private void startPhotoZoom(String photpPath) {
        if (TextUtils.isEmpty(photpPath)) {
            showToast("获取图片失败，请重试！");
            return;
        }
        if (!cutEnable && takePhotoListener != null) {

            try {
                DisplayMetrics displayMetrics;
                displayMetrics = getResources().getDisplayMetrics();

                Bitmap bitmap = ImageUtil.readBitmap(photpPath, (int) (displayMetrics.widthPixels * 1.2f));
                ExifInterface exif;

                exif = new ExifInterface(photpPath);

                ImageUtil.saveBitmap(bitmap, cutPath);
                ExifInterface cutExif = new ExifInterface(cutPath);
                cutExif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE) == null ? "" : exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE));
                cutExif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE) == null ? "" : exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE));
                cutExif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF) == null ? "" : exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF));
                cutExif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF) == null ? "" : exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF));
                cutExif.saveAttributes();
                Logger.e("path1===="+cutPath);
                takePhotoListener.onTakePhotoResult(cutPath, bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            Intent intent = new Intent(this, ClipImageAct.class);
            intent.putExtra("path", photpPath);
            intent.putExtra("scale", (float) picHeight / (float) picWidth);
            startActivityForResult(intent, PHOTO_CUT);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {

                case PHOTO_TAKEPHOTO:

                    startPhotoZoom(photoTempFile);

                    break;
                case PHOTO_GALLERY:

                    if (data != null) {
                        startPhotoZoom(ImageUtil.getImageAbsolutePath(getApplicationContext(), data.getData()));
                    }
                    break;
                case PHOTO_CUT:

                    if (data != null) {
                        Bitmap bitmap = data.getParcelableExtra("bitmap");
                        String path = data.getStringExtra("path");
                        Logger.e("path===="+path);
                        if (takePhotoListener != null) {
                            takePhotoListener.onTakePhotoResult(path, bitmap);
                        }

                    }
                    break;

                default:
                    break;
            }
        }
    }

    public interface TakePhotoListener {
        void onTakePhotoResult(String imagePaht, Bitmap bitmap);
    }

    public void setTakePhotoListener(TakePhotoListener takePhotoListener) {
        this.takePhotoListener = takePhotoListener;
    }

    public void setCutEnable(boolean cutEnable) {
        this.cutEnable = cutEnable;
    }

}
