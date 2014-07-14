package org.xstuido.gue.adapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.xstuido.gue.R;
import org.xstuido.gue.util.Event;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SignHistoryAdapter extends BaseAdapter {

	private List<Event> data;
	private Context mContext;

	public SignHistoryAdapter(Context context) {
		mContext = context;
		data = new ArrayList<Event>();
	}

	public void setData(List<Event> data) {
		this.data = data;
		notifyDataSetChanged();
	}

	@Override
	public boolean isEnabled(int position) {
		return false;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Event getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return data.get(position).hashCode();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.lvi_signin_history, parent, false);
			vh = new ViewHolder();
			vh.mGetupTimeTextView = (TextView) convertView
					.findViewById(R.id.tv_get_up_time);
			vh.mSignTimeTextView = (TextView) convertView
					.findViewById(R.id.tv_sign_time);
			vh.mMindTextView = (TextView) convertView
					.findViewById(R.id.tv_mind);
			vh.mUpLineView = convertView.findViewById(R.id.line_upper);
			vh.mLowLine1View = convertView.findViewById(R.id.line_lower_1);
			vh.mLowLine2View = convertView.findViewById(R.id.line_lower_2);

			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		Event item = getItem(position);

		vh.mGetupTimeTextView.setText(getHourMinute(item.getTime()));
		vh.mSignTimeTextView.setText(getMonthDate(item.getTime()));
		vh.mMindTextView.setText(item.getContent());
		if (position == 0) {
			vh.mUpLineView.setVisibility(View.GONE);
		} else {
			vh.mUpLineView.setVisibility(View.VISIBLE);
		}

		if (position == getCount() - 1) {
			vh.mLowLine1View.setVisibility(View.GONE);
			vh.mLowLine2View.setVisibility(View.GONE);
		} else {
			vh.mLowLine1View.setVisibility(View.VISIBLE);
			vh.mLowLine2View.setVisibility(View.VISIBLE);
		}

		return convertView;
	}

	private String getMonthDate(long time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		int month = calendar.get(Calendar.MONTH) + 1;
		int date = calendar.get(Calendar.DAY_OF_MONTH);
		String mon = month < 10 ? "0" + Integer.toString(month) : Integer
				.toString(month);
		String day = date < 10 ? "0" + Integer.toString(date) : Integer
				.toString(date);
		return mon + "." + day;
	}

	private String getHourMinute(long time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		String ho = hour < 10 ? "0" + Integer.toString(hour) : Integer
				.toString(hour);
		String min = minute < 10 ? "0" + Integer.toString(minute) : Integer
				.toString(minute);
		return ho + ":" + min;
	}

	private class ViewHolder {
		TextView mSignTimeTextView;
		TextView mGetupTimeTextView;
		TextView mMindTextView;
		View mUpLineView;
		View mLowLine1View;
		View mLowLine2View;
	}

}
