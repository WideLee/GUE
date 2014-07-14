package org.xstuido.gue.cards;

import org.xstuido.gue.R;
import org.xstuido.gue.cards.objects.RecyclableCard;

import android.view.View;
import android.widget.TextView;

public class MyCard extends RecyclableCard {

	public MyCard(String title){
		super(title);
	}

	@Override
	protected int getCardLayoutId() {
		return R.layout.card_ex;
	}

	@Override
	protected void applyTo(View convertView) {
		((TextView) convertView.findViewById(R.id.title)).setText(title);
	}
}
