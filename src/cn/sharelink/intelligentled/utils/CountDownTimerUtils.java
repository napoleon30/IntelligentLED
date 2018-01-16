package cn.sharelink.intelligentled.utils;

import cn.sharelink.intelligentled.R;
import cn.sharelink.intelligentled.application.MainApplication;

import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

public class CountDownTimerUtils extends CountDownTimer {
	private TextView mTextView;

	/**
	 * @param textView
	 *            The TextView
	 * 
	 * 
	 * @param millisInFuture
	 *            The number of millis in the future from the call to
	 *            {@link #start()} until the countdown is done and
	 *            {@link #onFinish()} is called.
	 * @param countDownInterval
	 *            The interval along the way to receiver {@link #onTick(long)}
	 *            callbacks.
	 */
	public CountDownTimerUtils(TextView textView, long millisInFuture,
			long countDownInterval) {
		super(millisInFuture, countDownInterval);
		this.mTextView = textView;
	}
	

	@Override
	public void onTick(long millisUntilFinished) {
		mTextView.setClickable(false); // 设置不可点击
		mTextView.setText(millisUntilFinished / 1000 + " s"); // 设置倒计时时�?
		mTextView.setBackgroundResource(R.drawable.selectors_btn_button); // 设置按钮为灰色，这时是不能点击的

		/**
		 * 超链�? URLSpan 文字背景颜色 BackgroundColorSpan 文字颜色 ForegroundColorSpan 字体大小
		 * AbsoluteSizeSpan 粗体、斜�? StyleSpan 删除�? StrikethroughSpan 下划�?
		 * UnderlineSpan 图片 ImageSpan
		 * http://blog.csdn.net/ah200614435/article/details/7914459
		 */
		SpannableString spannableString = new SpannableString(mTextView
				.getText().toString()); // 获取按钮上的文字
		ForegroundColorSpan span = new ForegroundColorSpan(Color.RED);
		/**
		 * public void setSpan(Object what, int start, int end, int flags) {
		 * 主要是start跟end，start是起始位�?,无论中英文，都算�?个�??
		 * �?0�?始计算起。end是结束位置，�?以处理的文字，包含开始位置，但不包含结束位置�?
		 */
		spannableString.setSpan(span, 0, 2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);// 将�?�计时的时间设置为红�?
		mTextView.setText(spannableString);
	}

	@Override
	public void onFinish() {
		mTextView.setText(R.string.register_aty_fetch_vercode);
		mTextView.setClickable(true);// 重新获得点击
		mTextView.setBackgroundResource(R.drawable.selectors_btn_button); // 还原背景�?
	}
}
