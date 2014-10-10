package org.xstuido.gue.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.xstuido.gue.R;
import org.xstuido.gue.util.Event;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by limkuan on 2014/10/2.
 */
public class CalendarCardAdapter extends BaseAdapter{

    private List<Event> data;
    private Context mContext;

    public CalendarCardAdapter(Context context) {
        mContext = context;
        data = new ArrayList<Event>();
    }

    public void setData(List<Event> data) {
        this.data = data;
        notifyDataSetChanged();
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
        Event item = getItem(position);
        ViewHolder vh = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.lvi_calendar_list,
                    parent, false);
            vh = new ViewHolder();
            vh.mTimeTextView = (TextView) convertView.findViewById(R.id.tv_time);
            vh.mContentTextView = (TextView) convertView.findViewById(R.id.tv_content);
            vh.mEvent = item;
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }



        vh.mTimeTextView.setText(getHourMinute(item.getTime()));
        vh.mContentTextView.setText(item.getContent());

        return convertView;
    }


    public class ViewHolder {
        public TextView mTimeTextView;
        public TextView mContentTextView;
        public Event mEvent;
    }

    private String getHourMinute(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        String ho = hour < 10 ? "0" + Integer.toString(hour) : Integer.toString(hour);
        String min = minute < 10 ? "0" + Integer.toString(minute) : Integer.toString(minute);
        return ho + ":" + min;
    }
}
