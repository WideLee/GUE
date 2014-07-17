package org.xstuido.gue.cards;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.xstuido.gue.R;
import org.xstuido.gue.cards.objects.RecyclableCard;
import org.xstuido.gue.fragment.TodayToDoFragment;
import org.xstuido.gue.util.Event;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("SimpleDateFormat")
public class ToDoCard extends RecyclableCard {

	private Event mEvent;
	private Handler mHandler;

	public ToDoCard(Event event, Handler handler, Boolean hasOverflow) {
		this.hasOverflow = hasOverflow;
		this.mHandler = handler;
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
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");

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
		Message msg = new Message();
		msg.what = TodayToDoFragment.MESSAGE_SWIPE_TODO_CARD_DONE;
		msg.obj = mEvent.getId();
		mHandler.sendMessage(msg);

		super.OnSwipeCard();
	}

}
