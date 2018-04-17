package com.dvlp.news.ui.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 圆角图片控件
 */
public class RoundImageView extends ImageView {
	public RoundImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public RoundImageView(Context context) {
		super(context);
		init();
	}

	private final RectF roundRect = new RectF();
	private float rect_adius = 0;
	private final Paint maskPaint = new Paint();
	private final Paint zonePaint = new Paint();

	private void init() {
		maskPaint.setAntiAlias(true);
		maskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		//
		zonePaint.setAntiAlias(true);
		zonePaint.setColor(Color.WHITE);
		//
//		float density = getResources().getDisplayMetrics().density;
//		rect_adius = rect_adius * density;
	}
//
//	public void setRectAdius(float adius) {
//		rect_adius = adius;
//		invalidate();
//	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		int w = getWidth() - getPaddingLeft() - getPaddingRight();
		int h = getHeight() - getPaddingTop() - getPaddingBottom();
		rect_adius = w / 2;
		roundRect.set(getPaddingLeft(), getPaddingTop(), w + getPaddingLeft(), h + getPaddingTop());
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		if (getDrawable() == null) {
			super.onDraw(canvas);
		} else {
			canvas.saveLayer(roundRect, zonePaint, Canvas.ALL_SAVE_FLAG);
			canvas.drawRoundRect(roundRect, rect_adius, rect_adius, zonePaint);// 绘制 第一层 圆形

			canvas.saveLayer(roundRect, maskPaint, Canvas.ALL_SAVE_FLAG);
			super.onDraw(canvas);
			canvas.restore();
		}
	}
}
