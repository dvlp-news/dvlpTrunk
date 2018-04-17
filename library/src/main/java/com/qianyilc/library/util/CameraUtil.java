package com.qianyilc.library.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.text.TextUtils;

/**
 * 照相机拍照片或者从图库中选取照片并剪裁的工具类
 * 
 * @author 刘伟 E-mail:liuwei1@leju.com
 * @version 创建时间：2014年6月26日 下午2:37:08
 * 
 */
public class CameraUtil {
	/**
	 * 根据比例剪裁指定路径下的图片
	 * 
	 * @param activity
	 * @param uri
	 * @param width
	 * @param height
	 * @param SavePaht
	 * @return
	 */
	public static Intent getCutIntent(Activity activity, Uri uri, String filePath, int blX, int blY, float width, float height, String SavePaht) {
		String photpPath = null;
		if (TextUtils.isEmpty(filePath)) {
			photpPath = ImageUtil.getImagePath(activity, uri);
		} else {
			photpPath = filePath;
		}
		if (TextUtils.isEmpty(photpPath)) {
			Logger.e(uri.toString());

		}

		uri = CameraUtil.getRotaUri(activity, photpPath, SavePaht);
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// crop为true是设置在开启的intent中设置显示的view可以剪裁
		intent.putExtra("crop", "true");

		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", blX);
		intent.putExtra("aspectY", blY);

		// outputX,outputY 是剪裁图片的宽高
		intent.putExtra("outputX", width);
		intent.putExtra("outputY", height);
		intent.putExtra("return-data", true);
		intent.putExtra("noFaceDetection", true);
		return intent;
	}

	public static Uri getRotaUri(Activity activity, String filePath, String savePath) {
		int angle = ImageUtil.readPictureDegree(filePath);
		if (angle != 0) {
			return rotaingImageView(activity, angle, filePath, savePath);
		}
		return Uri.fromFile(new File(filePath));
	}

	/**
	 * 旋转图片
	 * 
	 * @param angle
	 * @param bitmap
	 * @return Bitmap
	 */
	public static Uri rotaingImageView(Activity activity, int angle, String filePath, String savePath) {
		if (ImageUtil.readPictureDegree(filePath) != 0) {

		}

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		options.inPreferredConfig = Bitmap.Config.ARGB_4444;
		Bitmap bmp = BitmapFactory.decodeFile(filePath, options);
		int bs = options.outHeight / activity.getWindowManager().getDefaultDisplay().getWidth();
		if (bs < 1) {
			bs = 1;
		}
		options.inSampleSize = bs;
		options.inPurgeable = true;
		options.inInputShareable = true;
		options.inJustDecodeBounds = false;
		bmp = BitmapFactory.decodeFile(filePath, options);
		// 旋转图片 动作
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);

		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
		File file = new File(savePath);
		try {
			BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(file));
			resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
			os.close();
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
		return Uri.fromFile(file);
	}

	/**
	 * 获取可以打开照相机的Intent
	 * 
	 * @param activity
	 * @param uri
	 *            照片保存的位置，可以为空，因为即使不为空也会被覆盖
	 * @return
	 */

	public static Intent doTakePhotoAction(Activity activity, Uri uri) {

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

		try {
			intent.putExtra("return-data", true);

		} catch (ActivityNotFoundException e) {
			// Do nothing for now
		}
		return intent;
	}

	public static Uri getPhotoSaveUri(Context activity) {

		SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
		String filename = timeStampFormat.format(new Date());
		ContentValues values = new ContentValues();
		values.put(Media.TITLE, filename);
		Uri uri = null;
		uri = activity.getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, values);

		return uri;
	}

}
