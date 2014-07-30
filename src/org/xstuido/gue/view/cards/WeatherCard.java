package org.xstuido.gue.view.cards;

import org.xstuido.gue.R;
import org.xstuido.gue.util.Constant;
import org.xstuido.gue.util.weather.Weather;
import org.xstuido.gue.util.weather.WeatherUtil;
import org.xstuido.gue.view.cards.objects.RecyclableCard;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class WeatherCard extends RecyclableCard {

	private int mIndex;
	private Handler mHandler;
	private OnClickListener mOverflowClickListener;

	public WeatherCard(int index, Handler handler, Boolean hasOverflow) {
		this.hasOverflow = hasOverflow;
		this.mHandler = handler;
		this.mIndex = index;
	}

	@Override
	protected int getCardLayoutId() {
		return R.layout.card_weather;
	}

	@Override
	protected void applyTo(View convertView) {
		Weather mWeather = WeatherUtil.getInstance().getWeatherList().get(mIndex);

		TextView cityNameTextView = ((TextView) convertView.findViewById(R.id.tv_city_name));
		TextView temperatureTextView = ((TextView) convertView.findViewById(R.id.tv_temperature));
		TextView dateTextView = ((TextView) convertView.findViewById(R.id.tv_date));
		TextView overviewTextView = ((TextView) convertView.findViewById(R.id.tv_overview));
		TextView uvaTextView = ((TextView) convertView.findViewById(R.id.tv_uva));
		ImageView weatherIconImageView = ((ImageView) convertView
				.findViewById(R.id.tv_weather_icon));
		TextView loadingTextView = (TextView) convertView.findViewById(R.id.tv_loading);
		RelativeLayout weatherLayout = (RelativeLayout) convertView.findViewById(R.id.rl_weather);
		ImageView overflowImageView = ((ImageView) convertView.findViewById(R.id.overflow));

		cityNameTextView.setText(mWeather.getCityName());
		temperatureTextView.setText(mWeather.getTemperature());
		dateTextView.setText(mWeather.getUpdateTime());
		overviewTextView.setText(mWeather.getOverView());
		uvaTextView.setText(mWeather.getUva());

		String drawable = mWeather.getWeatherDrawable();
		String str = drawable.split("\\.")[0];
		int index = 0;
		if (str.matches("[0-9]")) {
			index = Integer.parseInt(str);
		} else {
			index = 32;
		}

		weatherIconImageView.setBackgroundResource(R.drawable.weather_00 + index);
		if (mWeather.isInit()) {
			weatherLayout.setVisibility(View.VISIBLE);
			loadingTextView.setVisibility(View.GONE);
		} else {
			weatherLayout.setVisibility(View.GONE);
			loadingTextView.setVisibility(View.VISIBLE);
		}
		if (hasOverflow == true) {
			overflowImageView.setVisibility(View.VISIBLE);
		} else {
			overflowImageView.setVisibility(View.GONE);
		}
		overflowImageView.setOnClickListener(mOverflowClickListener);
	}

	public void setOverflowClickListener(OnClickListener mOverflowClickListener) {
		this.mOverflowClickListener = mOverflowClickListener;
	}

	@Override
	public void OnSwipeCard() {
		Message msg = new Message();
		msg.what = Constant.MESSAGE_SWIPE_WEATHER_CARD;
		msg.obj = mIndex;
		mHandler.sendMessage(msg);

		super.OnSwipeCard();
	}
}
