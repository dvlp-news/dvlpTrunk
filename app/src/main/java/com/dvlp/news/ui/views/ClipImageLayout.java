package com.dvlp.news.ui.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.RelativeLayout;

import org.xutils.common.Callback;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.io.File;

public class ClipImageLayout extends RelativeLayout {

    private Context mContext;

	private ClipZoomImageView mZoomImageView;
	private ClipImageBorderView mClipImageView;

	/**
	 * 这里测试，直接写死了大小，真正使用过程中，可以提取为自定义属性
	 */
	private int mHorizontalPadding = 20;

	public ClipImageLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
        mContext = context;
		mZoomImageView = new ClipZoomImageView(context);
		mClipImageView = new ClipImageBorderView(context);

        // 计算padding的px
        mHorizontalPadding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, mHorizontalPadding, getResources()
                        .getDisplayMetrics());
        mZoomImageView.setHorizontalPadding(mHorizontalPadding);
        mClipImageView.setHorizontalPadding(mHorizontalPadding);

		android.view.ViewGroup.LayoutParams lp = new LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);

		this.addView(mZoomImageView, lp);
		this.addView(mClipImageView, lp);
	}

	/**
	 * 对外公布设置边距的方法,单位为dp
	 * 
	 * @param mHorizontalPadding
	 */
	public void setHorizontalPadding(int mHorizontalPadding) {
		this.mHorizontalPadding = mHorizontalPadding;
	}

	/**
	 * 裁切图片
	 * 
	 * @return
	 */
	public Bitmap clip() {
		return mZoomImageView.clip();
	}

    public void setLocalImagePath(String path) {
		Uri uri = Uri.fromFile(new File(path));

		ImageOptions.Builder builder = new ImageOptions.Builder();
		builder.setAutoRotate(true);
		builder.setLoadingDrawable(null);
        builder.setFailureDrawable(null);

		x.image().loadDrawable(uri.toString(), builder.build(), new Callback.CacheCallback<Drawable>() {
			@Override
			public boolean onCache(Drawable result) {
				return false;
			}

			@Override
			public void onSuccess(Drawable result) {
				mZoomImageView.setImageDrawable(result);
			}

			@Override
			public void onError(Throwable ex, boolean isOnCallback) {

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
