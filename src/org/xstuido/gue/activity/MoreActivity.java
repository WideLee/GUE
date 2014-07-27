package org.xstuido.gue.activity;

import org.xstuido.gue.R;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
				Intent intent = new Intent();
				// ComponentName comp = new ComponentName("limk.example.game",
				// "limk.example.game.Example");
				ComponentName comp = new ComponentName("limk.ble", "limk.ble.MainActivity");
				intent.setComponent(comp);
				startActivity(intent);
				finish();
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
