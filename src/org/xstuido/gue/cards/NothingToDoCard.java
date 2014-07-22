package org.xstuido.gue.cards;

import org.xstuido.gue.R;
import org.xstuido.gue.cards.objects.RecyclableCard;

import android.view.View;

public class NothingToDoCard extends RecyclableCard {

	public NothingToDoCard() {
		this.hasOverflow = false;
		this.isClickable = false;
	}

	@Override
	protected void applyTo(View convertView) {

	}

	@Override
	protected int getCardLayoutId() {
		return R.layout.card_nothing_todo;
	}

}
