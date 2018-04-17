package com.qianyilc.library.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageUtil {

    public static Bitmap byteToBitmap(byte[] byteArray) {
        if (byteArray.length != 0) {
            return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        } else {
            return null;
        }
    }

    // 将Bitmap转换成InputStream
    public static InputStream Bitmap2InputStream(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(CompressFormat.JPEG, 100, baos);
        InputStream is = new ByteArrayInputStream(baos.toByteArray());
        return is;
    }

    // 将Bitmap转换成InputStream
    public static InputStream Bitmap2InputStream(Bitmap bm, int quality) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(CompressFormat.PNG, quality, baos);
        InputStream is = new ByteArrayInputStream(baos.toByteArray());
        return is;
    }

    public static InputStream scaleBitmapByHeight(String path, int height, int degree) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int optionsWidth = options.outWidth;
        int optionsHeight = options.outHeight;

        options.inJustDecodeBounds = false;
        float sampleSize = ((float) optionsHeight / (float) height);
        sampleSize = sampleSize % 1.0f > 0.4f ? sampleSize + 1 : sampleSize;
        options.inSampleSize = (int) sampleSize;
        options.outHeight = height;
        float scale = (float) height / (float) optionsHeight;
        options.outWidth = (int) (optionsWidth * scale);
//		options.inPreferredConfig = Bitmap.Config.ARGB_4444; // 默认是Bitmap.Config.ARGB_8888
        /* 下面两个字段需要组合使用 */
        options.inPurgeable = true;
        options.inInputShareable = true;
        Bitmap bmp = BitmapFactory.decodeFile(path, options);
        if (degree > 0) {
            if (bmp != null && degree != 0) {
                Matrix m = new Matrix();
                m.setRotate(degree, bmp.getWidth() / 2, bmp.getHeight() / 2);
                bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), m, true);

            }
        }

        return Bitmap2InputStream(bmp);

    }

    public static InputStream scaleBitmapByWidth(String path, int width) {


        return Bitmap2InputStream(readBitmap(path, width));

    }

    public static Bitmap readBitmap(String path, int width) {
        int angle = readPictureDegree(path);

        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int optionsWidth = angle == 0 ? options.outWidth : options.outHeight;

        options.inJustDecodeBounds = false;
//        if (optionsWidth > width) {
//
//            float sampleSize = ((float) optionsWidth / (float) width);
//            sampleSize = sampleSize % 1.0f > 0.6f ? sampleSize + 1 : sampleSize;
//            options.inSampleSize = (int) sampleSize;
//        }
//        float sampleSize = 0;
        if (optionsWidth > width * 3) {
            options.inSampleSize = 3;
        } else if (optionsWidth > width * 15 / 10) {
            options.inSampleSize = 2;
        } else {
            options.inSampleSize = 1;
        }

        options.inPreferredConfig = Config.ARGB_8888; // 默认是Bitmap.Config.ARGB_8888
		/* 下面两个字段需要组合使用 */
        options.inPurgeable = true;
        options.inInputShareable = true;
        Bitmap bmp = BitmapFactory.decodeFile(path, options);
        if (angle != 0) {
            Matrix matrix = new Matrix();

            matrix.postRotate(angle);
            Bitmap resizedBitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
            return resizedBitmap;

        } else {
            return bmp;
        }

    }

    public static InputStream BitmapFromLocationToInputStream(String path, int width) {
        if (TextUtils.isEmpty(path) || width <= 0) {
            return null;
        }

        int degree = readPictureDegree(path);
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        if (options.outWidth > options.outHeight) {
            int height = width;
            if (options.outHeight > height) {
                options.inSampleSize = options.outHeight / width;
                float scale = (float) (height) / (float) (options.outHeight);
                int bitmapHeight = (int) (options.outWidth * scale);
                options.outHeight = width;

                options.outWidth = bitmapHeight;

            }
        } else if (options.outWidth < options.outHeight) {
            if (options.outWidth > width) {
                options.inSampleSize = options.outWidth / width;
                float scale = (float) (width) / (float) (options.outWidth);
                float height = options.outHeight * scale;
                options.outWidth = width;

                options.outHeight = (int) height;

            }
        }
        options.inJustDecodeBounds = false;

        options.inPreferredConfig = Config.ARGB_4444; // 默认是Bitmap.Config.ARGB_8888
		/* 下面两个字段需要组合使用 */
        options.inPurgeable = true;
        options.inInputShareable = true;
        Bitmap bmp = BitmapFactory.decodeFile(path, options);
        FileUtils fileUtils = new FileUtils();
        fileUtils.getSDPATH();
        try {
            File file = fileUtils.creatSDFile("11111.jpg");
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);
            bmp.compress(CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (bmp != null && degree != 0) {
            Matrix m = new Matrix();
            m.setRotate(degree, bmp.getHeight() / 2, bmp.getWidth() / 2);
            bmp = Bitmap.createBitmap(bmp, 0, 0, width, bmp.getHeight(), m, true);
			/* m.setRotate(bmp, bmp.getWidth()/2, bmp.getHeight()/2); */
        }

        try {
            File file = fileUtils.creatSDFile("2222.jpg");
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);
            bmp.compress(CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return Bitmap2InputStream(bmp);

    }

    public static Drawable byteToDrawable(byte[] byteArray) {
        ByteArrayInputStream ins = new ByteArrayInputStream(byteArray);
        return Drawable.createFromStream(ins, null);
    }

    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888 : Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * 图片去色,返回灰度图片
     *
     * @param bmpOriginal 传入的图片
     * @return 去色后的图片
     */
    public static Bitmap toGrayscale(Bitmap bmpOriginal) {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Config.RGB_565);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }

    /**
     * 去色同时加圆角
     *
     * @param bmpOriginal 原图
     * @param pixels      圆角弧度
     * @return 修改后的图片
     */
    public static Bitmap toGrayscale(Bitmap bmpOriginal, int pixels) {
        return toRoundCorner(toGrayscale(bmpOriginal), pixels);
    }

    /**
     * 等比例缩放到指定宽度
     *
     * @param bitmap
     * @param width
     * @return
     */
    public static Bitmap scaleBitmap(Bitmap bitmap, int width) {
        float scaleWidth = (float) width / bitmap.getWidth();

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleWidth);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return newbm;

    }

    /**
     * 把图片变成圆角
     *
     * @param bitmap 需要修改的图片
     * @param pixels 圆角的弧度
     * @return 圆角图片
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    /**
     * 使圆角功能支持BitampDrawable
     *
     * @param bitmapDrawable
     * @param pixels
     * @return
     */
    public static BitmapDrawable toRoundCorner(BitmapDrawable bitmapDrawable, int pixels) {
        Bitmap bitmap = bitmapDrawable.getBitmap();
        bitmapDrawable = new BitmapDrawable(toRoundCorner(bitmap, pixels));
        return bitmapDrawable;
    }

    /**
     * 获取图片的旋转角度
     *
     * @param path
     * @return int
     * @Title: readPictureDegree
     * @date 2012-12-4 上午9:22:33
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);

            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
                default:
                    degree = 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 将图片的旋转角度置为0
     *
     * @param path
     * @return void
     * @Title: setPictureDegreeZero
     * @date 2012-12-10 上午10:54:46
     */
    public static void setPictureDegreeZero(String path) {
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            // 修正图片的旋转角度，设置其不旋转。这里也可以设置其旋转的角度，可以传值过去，
            // 例如旋转90度，传值ExifInterface.ORIENTATION_ROTATE_90，需要将这个值转换为String类型的
            exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, String.valueOf(ExifInterface.ORIENTATION_NORMAL));
            exifInterface.saveAttributes();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * 根据Uri获取图片在内存卡中的位置
     *
     * @param mContext
     * @param uri
     * @return
     */

    public static String getImagePath(Activity mContext, Uri uri) {
        // String[] projection = { MediaStore.Images.Media.DATA };
        // Cursor cursor = mContext.managedQuery(uri, projection, null, null,
        // null);
        // int column_index =
        // cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        // cursor.moveToFirst();
        // return cursor.getString(column_index);
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = mContext.getContentResolver().query(uri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    /**
     * 保存图片到本地
     *
     * @param bm
     * @param filaPath
     */
    public static void saveBitmap(Bitmap bm, String filaPath) {
        Bitmap rotaBitmap = bm;

        File f = new File(filaPath);
        if (f.exists()) {
            f.delete();
        }
        try {

            FileOutputStream out = new FileOutputStream(f);
            rotaBitmap.compress(CompressFormat.JPEG, 60, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * 根据Uri获取图片绝对路径，解决Android4.4以上版本Uri转换
     * @param context
     * @param imageUri
     * @author yaoxing
     * @date 2014-10-12
     */
    @TargetApi(19)
    public static String getImageAbsolutePath(Context context, Uri imageUri) {
        if (context == null || imageUri == null)
            return null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, imageUri)) {
            if (isExternalStorageDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(imageUri)) {
                String id = DocumentsContract.getDocumentId(imageUri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = new String[] { split[1] };
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } // MediaStore (and general)
        else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(imageUri))
                return imageUri.getLastPathSegment();
            return getDataColumn(context, imageUri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            return imageUri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = { column };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

}