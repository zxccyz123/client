package com.dd.ui;

import com.dd.bootstrap.BootstrapButton;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * 引导界面
 * 
 * @author daidong
 * 
 */
public class GuideActivity extends Activity implements OnClickListener {

	/** 登录注册按钮 **/
	private BootstrapButton login_btn, register_btn;

	private ImageView show_iv;

	/** 循环显示的图片 **/
	private Drawable[] pictures;

	/** 动画 **/
	private Animation[] animations;

	/** 当前是哪一张图片 **/
	private int currentItem = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);

		initView();

		initData();
	}

	private void initView() {
		show_iv = (ImageView) findViewById(R.id.iv_guide_picture);
		login_btn = (BootstrapButton) findViewById(R.id.guide_login);
		register_btn = (BootstrapButton) findViewById(R.id.guide_register);

		pictures = new Drawable[] {
				getResources().getDrawable(R.drawable.guide_pic1),
				getResources().getDrawable(R.drawable.guide_pic2),
				getResources().getDrawable(R.drawable.guide_pic3) };

		animations = new Animation[] {
				AnimationUtils.loadAnimation(this, R.anim.guide_fade_in),
				AnimationUtils.loadAnimation(this, R.anim.guide_fade_in_scale),
				AnimationUtils.loadAnimation(this, R.anim.guide_fade_out) };
	}

	private void initData() {
		login_btn.setOnClickListener(this);
		register_btn.setOnClickListener(this);

		animations[0].setDuration(1500);
		animations[1].setDuration(3000);
		animations[2].setDuration(1500);

		animations[0].setAnimationListener(new GuideAnimationListener(0));
		animations[1].setAnimationListener(new GuideAnimationListener(1));
		animations[2].setAnimationListener(new GuideAnimationListener(2));

		show_iv.setImageDrawable(pictures[currentItem]);
		show_iv.startAnimation(animations[0]);
	}

	class GuideAnimationListener implements AnimationListener {
		private int index;

		public GuideAnimationListener(int index) {
			this.index = index;
		}

		@Override
		public void onAnimationStart(Animation animation) {
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			if (index < (animations.length - 1)) {
				show_iv.startAnimation(animations[index + 1]);
			} else {
				currentItem++;
				if (currentItem > (pictures.length - 1)) {
					currentItem = 0;
				}
				show_iv.setImageDrawable(pictures[currentItem]);
				show_iv.startAnimation(animations[0]);
			}
		}

		@Override
		public void onAnimationRepeat(Animation animation) {

		}

	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.guide_login:
			intent.setClass(GuideActivity.this, LoginActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.guide_register:
			intent.setClass(GuideActivity.this, RegActivity.class);
			startActivity(intent);
			finish();
			break;
		default:
			break;
		}

	}

}
