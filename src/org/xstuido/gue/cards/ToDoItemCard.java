package org.xstuido.gue.cards;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.xstuido.gue.R;
import org.xstuido.gue.cards.objects.RecyclableCard;
import org.xstuido.gue.util.Event;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("SimpleDateFormat")
public class ToDoItemCard extends RecyclableCard {

	private Event mEvent;

	public ToDoItemCard(Event event, Boolean hasOverflow) {
		this.hasOverflow = hasOverflow;
		this.mEvent = event;
	}

	@Override
	protected int getCardLayoutId() {
		return R.layout.card_todo_item;
	}

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
	}

	@Override
	public void OnSwipeCard() {
		super.OnSwipeCard();
	}

}
