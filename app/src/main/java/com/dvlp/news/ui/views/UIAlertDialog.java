package com.dvlp.news.ui.views;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dvlp.news.R;


public class UIAlertDialog extends Dialog {

	public UIAlertDialog(Context context) {
		super(context);
		setCancelable(false);
	}

	public UIAlertDialog(Context context, int theme) {
		super(context, theme);
		setCancelable(false);
	}

	public static class Builder {
		private Context context; //上下文对象
		private CharSequence title; //对话框标题
		private CharSequence message; //对话框内容
		private CharSequence confirm_btnText; //按钮名称“确定”
		private CharSequence cancel_btnText; //按钮名称“取消”
		private View contentView; //对话框中间加载的其他布局界面
		/*按钮坚挺事件*/
		private OnClickListener confirm_btnClickListener;
		private OnClickListener cancel_btnClickListener;

		public Builder(Context context) {
			this.context = context;
		}

		/*设置对话框信息*/
		public Builder setMessage(CharSequence message) {
			this.message = message;
			return this;
		}

		public Builder setMessage(int message) {
			this.message = (CharSequence) context.getText(message);
			return this;
		}

		public Builder setTitle(int title) {
			this.title = (CharSequence) context.getText(title);
			return this;
		}

		public Builder setTitle(CharSequence title) {
			this.title = title;
			return this;
		}

		/**
		 * 设置对话框界面
		 * @param v View
		 * @return
		 */
		public Builder setContentView(View v) {
			this.contentView = v;
			return this;
		}

		public Builder setPositiveButton(int confirm_btnText,
										 OnClickListener listener) {
			this.confirm_btnText = (CharSequence) context
					.getText(confirm_btnText);
			this.confirm_btnClickListener = listener;
			return this;
		}

		public Builder setPositiveButton(CharSequence confirm_btnText,
										 OnClickListener listener) {
			this.confirm_btnText = confirm_btnText;
			this.confirm_btnClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(int cancel_btnText,
										 OnClickListener listener) {
			this.cancel_btnText = (CharSequence) context
					.getText(cancel_btnText);
			this.cancel_btnClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(CharSequence cancel_btnText,
										 OnClickListener listener) {
			this.cancel_btnText = cancel_btnText;
			this.cancel_btnClickListener = listener;
			return this;
		}

		public UIAlertDialog create() {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// instantiate the dialog with the custom Theme
			final UIAlertDialog dialog = new UIAlertDialog(context, R.style.UIAlertDialog);
			View layout = inflater.inflate(R.layout.layout_uialert_dialog, null);
			dialog.addContentView(layout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			// set the dialog title
			TextView titleView = ((TextView) layout.findViewById(R.id.title));
			if (TextUtils.isEmpty(title)) {
				titleView.setVisibility(View.GONE);
			} else {
				titleView.setVisibility(View.VISIBLE);
				titleView.setText(title);
			}
			((TextView) layout.findViewById(R.id.title)).getPaint().setFakeBoldText(true);;
			// set the confirm button
			if (confirm_btnText != null) {
				((Button) layout.findViewById(R.id.confirm_btn))
						.setText(confirm_btnText);
				if (confirm_btnClickListener != null) {
					((Button) layout.findViewById(R.id.confirm_btn))
							.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									confirm_btnClickListener.onClick(dialog,
											DialogInterface.BUTTON_POSITIVE);
								}
							});
				}
			} else {
				// if no confirm button just set the visibility to GONE
				layout.findViewById(R.id.confirm_btn).setVisibility(View.GONE);
			}
			// set the cancel button
			if (cancel_btnText != null) {
				((Button) layout.findViewById(R.id.cancel_btn))
						.setText(cancel_btnText);
				if (cancel_btnClickListener != null) {
					((Button) layout.findViewById(R.id.cancel_btn))
							.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									cancel_btnClickListener.onClick(dialog,
											DialogInterface.BUTTON_NEGATIVE);
								}
							});
				}
			} else {
				// if no confirm button just set the visibility to GONE
				layout.findViewById(R.id.cancel_btn).setVisibility(
						View.GONE);
			}
			// set the content message
			if (message != null) {
				((TextView) layout.findViewById(R.id.message)).setText(message);
			} else if (contentView != null) {
				((LinearLayout) layout.findViewById(R.id.message).getParent()).removeAllViews();
				((LinearLayout) layout.findViewById(R.id.message).getParent()).addView(
						contentView, new LayoutParams(
								LayoutParams.WRAP_CONTENT,
								LayoutParams.WRAP_CONTENT));
			}
			dialog.setContentView(layout);


			Window dialogWindow = dialog.getWindow();
			WindowManager m = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
			Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
			WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
			p.width = (int) (d.getWidth() * 0.8); // 高度设置为屏幕的0.6
			dialogWindow.setAttributes(p);
			return dialog;
		}

	}
}
