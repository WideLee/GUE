package org.xstuido.gue.activity;

import org.xstuido.gue.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class RouterActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_route);

		Handler mHandler = new Handler();
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				Intent intent = new Intent(RouterActivity.this, MainActivity.class);
				startActivity(intent);
				overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
				finish();

			}
		}, 1200);
	}
}
