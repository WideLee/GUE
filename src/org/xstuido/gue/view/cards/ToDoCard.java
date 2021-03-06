package org.xstuido.gue.view.cards;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.xstuido.gue.R;
import org.xstuido.gue.util.Constant;
import org.xstuido.gue.util.Event;
import org.xstuido.gue.view.cards.objects.RecyclableCard;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 当天需要做的事件中日程卡片
 * 
 * @author 11331068 冯亚臣 <1967558085@qq.com>
 */
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

	@SuppressLint("SimpleDateFormat")
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
		msg.what = Constant.MESSAGE_SWIPE_TODO_CARD;
		msg.obj = mEvent.getId();
		mHandler.sendMessage(msg);

		super.OnSwipeCard();
	}

}
