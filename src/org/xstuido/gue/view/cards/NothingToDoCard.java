package org.xstuido.gue.view.cards;

import org.xstuido.gue.R;
import org.xstuido.gue.view.cards.objects.RecyclableCard;

import android.view.View;

/**
 * 今天没有安排任务的卡片
 * 
 * @author 11331068 冯亚臣 <1967558085@qq.com>
 */
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
