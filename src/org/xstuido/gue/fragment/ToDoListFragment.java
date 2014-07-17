package org.xstuido.gue.fragment;

import java.util.Calendar;

import org.xstuido.gue.R;
import org.xstuido.gue.activity.BaseApplication;
import org.xstuido.gue.adapter.CalendarViewPager;
import org.xstuido.gue.adapter.ViewPager4SameItem.OnPageSelectedListener;
import org.xstuido.gue.db.GetUpEarlyDB;
import org.xstuido.gue.util.Tool;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ToDoListFragment extends Fragment {

	private Button mPreButton;
	private Button mNextButton;
	private TextView mMonthNameTextView;
	private LinearLayout monthTitleLayout;
	private CalendarViewPager mCalendarViewPager;

	private OnPageSelectedListener mOnPageSelectedListener;
	private GetUpEarlyDB mDB;
	private ListView mTodoListView;

	private boolean isInit = false;

	public ToDoListFragment() {
		mDB = new GetUpEarlyDB(BaseApplication.getContext());
		isInit = false;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View main = inflater.inflate(R.layout.fragment_todo_list, null);
		mPreButton = (Button) main.findViewById(R.id.btn_pre_month);
		mNextButton = (Button) main.findViewById(R.id.btn_next_month);
		mMonthNameTextView = (TextView) main.findViewById(R.id.tv_month_name);
		monthTitleLayout = (LinearLayout) main.findViewById(R.id.ll_month_title);
		mCalendarViewPager = (CalendarViewPager) main.findViewById(R.id.calendar);
		if (!isInit) {
			initView();
		}

		isInit = true;
		return main;
	}

	private void initView() {
		mPreButton.setVisibility(View.VISIBLE);
		mNextButton.setVisibility(View.VISIBLE);
		mMonthNameTextView.setText(Tool.getYearMonth(mCalendarViewPager.getCurDate()));
		monthTitleLayout.removeAllViews();
		for (int j = 0; j < CalendarViewPager.DAY_NUM_OF_WEEK; j++) {
			TextView tv = new TextView(getActivity());
			tv.setTextSize(20);
			tv.setGravity(Gravity.CENTER);
			tv.setTextColor(getActivity().getResources().getColor(R.color.card_text));
			tv.setTextSize(16);
			tv.setText(Tool.getWeekDayName(j));
			LinearLayout.LayoutParams inner_params = new LinearLayout.LayoutParams(
					Tool.getScreenW() / 8, LinearLayout.LayoutParams.WRAP_CONTENT);
			monthTitleLayout.addView(tv, inner_params);
		}

		mPreButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mCalendarViewPager.setCurrentItem(mCalendarViewPager.getCurrentItem() - 1);
			}
		});

		mNextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mCalendarViewPager.setCurrentItem(mCalendarViewPager.getCurrentItem() + 1);
			}
		});

		mOnPageSelectedListener = new OnPageSelectedListener() {

			@Override
			public void onPageSelected(int position) {
				mMonthNameTextView.setText(Tool.getYearMonth(mCalendarViewPager.getCurDate()));
				if (mCalendarViewPager.isFirstMonth()) {
					mPreButton.setVisibility(View.INVISIBLE);
				} else {
					mPreButton.setVisibility(View.VISIBLE);
				}

				if (mCalendarViewPager.isLastMonth()) {
					mNextButton.setVisibility(View.INVISIBLE);
				} else {
					mNextButton.setVisibility(View.VISIBLE);
				}

			}
		};
		mCalendarViewPager.setOnPageSelectedListener(mOnPageSelectedListener);
		mCalendarViewPager.setOnDateClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Calendar cal = (Calendar) v.getTag();
				Toast.makeText(BaseApplication.getContext(), "Click", Toast.LENGTH_SHORT).show();
				updateToDoList(cal);
			}
		});

	}

	private void updateToDoList(Calendar cal) {

	}

	@Override
	public void onResume() {
		initView();
		super.onResume();
	}
}