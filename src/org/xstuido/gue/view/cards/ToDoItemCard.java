package org.xstuido.gue.view.cards;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.xstuido.gue.R;
import org.xstuido.gue.util.Event;
import org.xstuido.gue.view.cards.objects.RecyclableCard;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 日历视图中的日程卡片
 * 
 * @author 11331068 冯亚臣 <1967558085@qq.com>
 */
public class ToDoItemCard extends RecyclableCard {

	private Event mEvent;

	public ToDoItemCard(Event event, Boolean hasOverflow) {
		this.hasOverflow = hasOverflow;
		this.mEvent = event;
		this.isClickable = true;
	}

	@Override
	protected int getCardLayoutId() {
		return R.layout.card_todo_item;
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	protected void applyTo(View convertView) {
		TextView timeTextView = (TextView) convertView.findViewById(R.id.tv_time);
		TextView desTextView = (TextView) convertView.findViewById(R.id.description);

		Date date = new Date(mEvent.getTime());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		String time = dateFormat.format(date);
		timeTextView.setText(time);
		desTextView.setText(mEvent.getContent());

		ImageView overflowImageView = ((ImageView) convertView.findViewById(R.id.overflow));
		if (hasOverflow == true) {
			overflowImageView.setVisibility(View.VISIBLE);
		} else {
			overflowImageView.setVisibility(View.GONE);
		}
		convertView.setOnClickListener(getClickListener());
		convertView.setTag(mEvent);
	}

	@Override
	public void OnSwipeCard() {
		super.OnSwipeCard();
	}
}
