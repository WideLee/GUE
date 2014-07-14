package org.xstuido.gue.adapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.xstuido.gue.R;
import org.xstuido.gue.util.Tool;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CalendarViewPager extends ViewPager4SameItem {

	public static final int DAY_NUM_OF_WEEK = 7;
	public static final int WEEK_NUM_OF_MONTH = 6;

	private int mMonthCount = 0;
	private OnClickListener mClickListener;

	public CalendarViewPager(Context context) {
		super(context);
		init();
	}

	public CalendarViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CalendarViewPager(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	@Override
	public boolean isSwitchBtnEnabled() {
		return false;
	}

	private void init() {

		mMonthCount = 7;
		mClickListener = null;
		notifyDataSetChanged();

		setCurrentItem(mMonthCount / 2);
	}

	@Override
	public View createView() {

		LinearLayout layout = (LinearLayout) inflateView(R.layout.lvi_hi_month,
				null);

		for (int i = 0; i < WEEK_NUM_OF_MONTH; i++) {
			LinearLayout inner = new LinearLayout(getContext());
			inner.setOrientation(LinearLayout.HORIZONTAL);
			inner.setGravity(Gravity.CENTER);
			for (int j = 0; j < DAY_NUM_OF_WEEK; j++) {
				LinearLayout item = new LinearLayout(getContext());
				item.setGravity(Gravity.CENTER_HORIZONTAL);
				item.setOrientation(LinearLayout.VERTICAL);

				final TextView tv = new TextView(getContext());
				tv.setTextSize(18);
				tv.setGravity(Gravity.CENTER);
				tv.setOnClickListener(mClickListener);
				tv.setClickable(true);

				LinearLayout.LayoutParams tv_params = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT, Tool.dip2px(25));
				tv_params.setMargins(0, Tool.dip2px(2), 0, Tool.dip2px(2));
				item.addView(tv, tv_params);

				View dot = new View(getContext());
				dot.setBackgroundResource(R.drawable.black_dot);
				dot.setVisibility(View.INVISIBLE);
				LinearLayout.LayoutParams img_params = new LinearLayout.LayoutParams(
						Tool.dip2px(4), Tool.dip2px(4));
				img_params.setMargins(0, Tool.dip2px(1), 0, 0);
				item.addView(dot, img_params);

				LinearLayout.LayoutParams inner_params = new LinearLayout.LayoutParams(
						Tool.getScreenW() / 8,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				inner.addView(item, inner_params);
			}
			LinearLayout.LayoutParams layout_params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			layout.addView(inner, layout_params);

			View view = new View(getContext());
			view.setBackgroundColor(getResources().getColor(R.color.card_text));
			LinearLayout.LayoutParams line_params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT, 1);
			line_params.setMargins(0, Tool.dip2px(2), 0, Tool.dip2px(2));
			layout.addView(view, line_params);
		}
		return layout;
	}

	@Override
	public int getItemCount() {
		return mMonthCount;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void configView(View view, int position) {
		List<List<LinearLayout>> monthDays = (List<List<LinearLayout>>) view
				.getTag();

		LinearLayout outer = (LinearLayout) view;
		if (monthDays == null) {
			monthDays = new ArrayList<List<LinearLayout>>();
			for (int i = 0; i < WEEK_NUM_OF_MONTH; i++) {
				List<LinearLayout> weekDays = new ArrayList<LinearLayout>();
				LinearLayout inner = (LinearLayout) outer.getChildAt(i * 2);
				for (int j = 0; j < DAY_NUM_OF_WEEK; j++) {
					LinearLayout item = (LinearLayout) inner.getChildAt(j);
					weekDays.add(item);
				}
				monthDays.add(weekDays);
			}
			view.setTag(monthDays);
		}

		Calendar cal = getCalendar(position, true);

		for (int i = 0; i < monthDays.size(); i++) {
			List<LinearLayout> weekDays = monthDays.get(i);
			int invisiableCount = 0;

			for (int j = 0; j < weekDays.size(); j++) {
				LinearLayout item = weekDays.get(j);
				TextView tv = ((TextView) item.getChildAt(0));

				tv.setTag(cal.clone());
				if (cal.get(Calendar.MONTH) == getCalendar(position, false)
						.get(Calendar.MONTH)) {
					tv.setTextColor(getResources().getColor(R.color.black));
				} else {
					tv.setTextColor(getResources().getColor(
							R.color.card_light_text));
				}
				tv.setText(Integer.toString(cal.get(Calendar.DAY_OF_MONTH)));

				item.getChildAt(1).setVisibility(View.GONE);

				LinearLayout.LayoutParams tv_params = (LinearLayout.LayoutParams) tv
						.getLayoutParams();
				tv.setLayoutParams(tv_params);

				cal.add(Calendar.DAY_OF_MONTH, 1);
			}
			View line = outer.getChildAt(outer.getChildCount() - 1);
			if (invisiableCount >= 7) {
				line.setVisibility(View.INVISIBLE);
			} else {
				line.setVisibility(View.VISIBLE);
			}
		}

	}

	private View inflateView(int resource, ViewGroup root) {
		return LayoutInflater.from(getContext()).inflate(resource, root);
	}

	private Calendar getCalendar(int position, boolean isMove) {
		Calendar curDate = Calendar.getInstance();
		curDate.setTimeInMillis(Tool.getCurCalendar().getTimeInMillis());

		curDate.add(Calendar.MONTH, position - mMonthCount / 2);
		return isMove ? Tool.getFirstDayOfMonth(curDate) : curDate;
	}

	public Calendar getCurDate() {
		return getCalendar(getViewPage().getCurrentItem(), false);
	}

	public int getCurrentItem() {
		return getViewPage().getCurrentItem();
	}

	public void setOnDateClickListener(OnClickListener clickListener) {
		mClickListener = clickListener;
	}

	public boolean isFirstMonth() {
		return Tool.getCurCalendar().get(Calendar.MONTH)
				- getCurDate().get(Calendar.MONTH) == mMonthCount / 2;
	}

	public boolean isLastMonth() {
		return getCurDate().get(Calendar.MONTH)
				- Tool.getCurCalendar().get(Calendar.MONTH) == mMonthCount / 2;
	}
}
