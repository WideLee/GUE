package org.xstuido.gue.activity;

import org.xstuido.gue.R;
import org.xstuido.gue.util.Tool;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 跳转到更多应用的扩展界面
 * 
 * @author 11331209 刘柯汕 <1946222543@qq.com>
 * 
 */
public class MoreActivity extends Activity {

	private RelativeLayout mRelativeLayout;
	private TextView mBackTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_more);

		mRelativeLayout = (RelativeLayout) findViewById(R.id.rl_game);
		mBackTextView = (TextView) findViewById(R.id.tv_back);

		mRelativeLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				PackageInfo packageInfo;
				try {
					packageInfo = getPackageManager().getPackageInfo("com.chemy.PDgame", 0);
				} catch (NameNotFoundException e) {
					packageInfo = null;
					e.printStackTrace();
				}
				if (packageInfo == null) {
					Tool.showToast("没有安装该应用");
				} else {
					Intent intent = new Intent();
					ComponentName comp = new ComponentName("com.chemy.PDgame",
							"org.cocos2dx.cpp.AppActivity");
					intent.setComponent(comp);
					startActivity(intent);
					finish();
				}
			}
		});

		mBackTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
				overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			finish();
			overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
			return true;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

}
