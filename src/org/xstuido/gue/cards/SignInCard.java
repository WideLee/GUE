package org.xstuido.gue.cards;

import org.xstuido.gue.R;
import org.xstuido.gue.cards.objects.RecyclableCard;
import org.xstuido.gue.util.Tool;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class SignInCard extends RecyclableCard {

	private boolean mIsSignIn;
	private OnClickListener mSignClickListener;

	public SignInCard(boolean isSignIn) {
		this.hasOverflow = false;
		this.isClickable = false;
		this.mIsSignIn = isSignIn;
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

}
