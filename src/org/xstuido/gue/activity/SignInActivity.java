package org.xstuido.gue.activity;

import org.xstuido.gue.R;
import org.xstuido.gue.db.GetUpEarlyDB;
import org.xstuido.gue.util.Event;
import org.xstuido.gue.util.Tool;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 填写签到信息的界面
 * 
 * @author 11331031 陈熙迪 <375900030@qq.com>
 * 
 */
public class SignInActivity extends Activity {

	private EditText mEditText;
	private TextView mBackTextView;
	private TextView mOKTextView;

	private GetUpEarlyDB mDB;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signin);

		mDB = new GetUpEarlyDB(BaseApplication.getContext());

		mEditText = (EditText) findViewById(R.id.et_todo);
		mBackTextView = (TextView) findViewById(R.id.tv_back);
		mOKTextView = (TextView) findViewById(R.id.banner_ok);

		mOKTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mEditText.getText().toString().equals("")) {
					Tool.showToast(Tool.getString(R.string.say_something));
				} else {
					mDB.insert(new Event(1, 1, System.currentTimeMillis(), mEditText.getText()
							.toString()));

					setResult(RESULT_OK);
					finish();
					overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
				}
			}
		});

		mBackTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
				overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			setResult(RESULT_CANCELED);
			finish();
			overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
			return true;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
}
