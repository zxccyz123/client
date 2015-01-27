package com.dd.widget;

import com.dd.ui.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;

/**
 *带删除图标的EditText
 * 
 * @author Administrator
 * 
 */
public class ClearEditText extends EditText implements OnFocusChangeListener,
		TextWatcher {

	/** 删除图标 **/
	private Drawable mClearDrawable;

	/** 是否获得焦点 **/
	private boolean hasFocus;

	public ClearEditText(Context context) {
		super(context, null);
	}

	public ClearEditText(Context context, AttributeSet attrs) {
		this(context, attrs, android.R.attr.editTextStyle);
	}

	public ClearEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		mClearDrawable = getCompoundDrawables()[2];
		if (mClearDrawable == null) {
			mClearDrawable = getResources().getDrawable(R.drawable.close);
		}
		mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(),
				mClearDrawable.getIntrinsicHeight());
		setClearIconVisible(false);
		setOnFocusChangeListener(this);
		addTextChangedListener(this);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			if (getCompoundDrawables()[2] != null) {
				boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight())
						&& (event.getX() < ((getWidth() + getTotalPaddingRight())));
				if (touchable)
					this.setText("");
			}
		}

		return super.onTouchEvent(event);
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		this.hasFocus = hasFocus;
		if (hasFocus)
			setClearIconVisible(getText().length() > 0);
		else
			setClearIconVisible(false);
	}

	protected void setClearIconVisible(boolean visible) {
		Drawable right = visible ? mClearDrawable : null;
		setCompoundDrawables(getCompoundDrawables()[0],
				getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int count, int after) {
		if (hasFocus)
			setClearIconVisible(s.length() > 0);
	}

	@Override
	public void afterTextChanged(Editable arg0) {

	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {

	}

	public void setShakeAnimation() {
		this.startAnimation(shakeAnimation(5));
	}

	public static Animation shakeAnimation(int counts) {
		Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
		translateAnimation.setInterpolator(new CycleInterpolator(counts));
		translateAnimation.setDuration(1000);
		return translateAnimation;
	}

}
