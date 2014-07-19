package org.xstuido.gue.activity;

import org.xstuido.gue.R;
import org.xstuido.gue.fragment.SignInHistoryFragment;
import org.xstuido.gue.fragment.ToDoListFragment;
import org.xstuido.gue.fragment.TodayToDoFragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class MainActivity extends FragmentActivity {

	private RadioGroup mNavigatorGroup;

	private TodayToDoFragment mTodayToDoFragment;
	private ToDoListFragment mToDoListFragment;
	private SignInHistoryFragment mSignInHistoryFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mTodayToDoFragment = new TodayToDoFragment();
		mToDoListFragment = new ToDoListFragment();
		mSignInHistoryFragment = new SignInHistoryFragment();

		mNavigatorGroup = (RadioGroup) findViewById(R.id.rg_navigate);

		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.add(R.id.content, mTodayToDoFragment);
		mNavigatorGroup.check(R.id.rb_today_todo);
		ft.commit();

		OnCheckedChangeListener checkedChangeListener = new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
				// ft.setCustomAnimations(android.R.anim.fade_in,
				// android.R.anim.fade_out);
				switch (checkedId) {
				case R.id.rb_today_todo:
					ft.replace(R.id.content, mTodayToDoFragment);
					break;
				case R.id.rb_todo_manage:
					ft.replace(R.id.content, mToDoListFragment);
					break;
				case R.id.rb_signin_history:
					ft.replace(R.id.content, mSignInHistoryFragment);
					break;
				default:
					break;
				}
				ft.commit();
			}
		};

		mNavigatorGroup.setOnCheckedChangeListener(checkedChangeListener);
	}
}
