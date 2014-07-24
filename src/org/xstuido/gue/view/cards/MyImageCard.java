package org.xstuido.gue.view.cards;

import org.xstuido.gue.R;
import org.xstuido.gue.view.cards.objects.RecyclableCard;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MyImageCard extends RecyclableCard {

	public MyImageCard(String title, int image) {
		super(title, image);
	}

	@Override
	protected int getCardLayoutId() {
		return R.layout.card_picture;
	}

	@Override
	protected void applyTo(View convertView) {
		((TextView) convertView.findViewById(R.id.title)).setText(title);
		((ImageView) convertView.findViewById(R.id.imageView1)).setImageResource(image);
	}

}
