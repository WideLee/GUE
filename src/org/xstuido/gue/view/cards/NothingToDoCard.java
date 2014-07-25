package org.xstuido.gue.view.cards;

import org.xstuido.gue.R;
import org.xstuido.gue.view.cards.objects.RecyclableCard;

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
