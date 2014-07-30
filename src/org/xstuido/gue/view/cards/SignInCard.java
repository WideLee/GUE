package org.xstuido.gue.view.cards;

import org.xstuido.gue.R;
import org.xstuido.gue.util.Constant;
import org.xstuido.gue.util.Tool;
import org.xstuido.gue.view.cards.objects.RecyclableCard;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class SignInCard extends RecyclableCard {

	private boolean mIsSignIn;
	private OnClickListener mSignClickListener;
	private Handler mHandler;

	public SignInCard(boolean isSignIn, Handler handler) {
		this.hasOverflow = false;
		this.isClickable = false;
		this.mIsSignIn = isSignIn;
		this.mHandler = handler;
	}

	public void setSignClickListener(OnClickListener mSignClickListener) {
		this.mSignClickListener = mSignClickListener;
	}

	@Override
	protected void applyTo(View convertView) {
		TextView titleTextView = (TextView) convertView.findViewById(R.id.title);
		TextView contentTextView = (TextView) convertView.findViewById(R.id.content);

		if (mIsSignIn) {
			titleTextView.setText(Tool.getString(R.string.today_already_signin));
			contentTextView.setText(Tool.getString(R.string.signin_done));
			contentTextView.setClickable(false);
		} else {
			titleTextView.setText(Tool.getString(R.string.today_not_signin));
			contentTextView.setText(Tool.getString(R.string.signin));
			contentTextView.setClickable(true);
			contentTextView.setOnClickListener(mSignClickListener);
		}
	}

	@Override
	protected int getCardLayoutId() {
		return R.layout.card_signin;
	}

	@Override
	public void OnSwipeCard() {
		Message msg = new Message();
		msg.what = Constant.MESSAGE_SWIPE_SIGNIN_CARD;
		mHandler.sendMessage(msg);

		super.OnSwipeCard();
	}

}
