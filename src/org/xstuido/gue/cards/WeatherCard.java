package org.xstuido.gue.cards;

import org.xstuido.gue.R;
import org.xstuido.gue.cards.objects.RecyclableCard;
import org.xstuido.gue.util.Weather;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class WeatherCard extends RecyclableCard {

	private Weather mWeather;

	public WeatherCard(Weather weather, Boolean hasOverflow) {
		this.hasOverflow = hasOverflow;
		this.mWeather = weather;
	}

	@Override
	protected int getCardLayoutId() {
		return R.layout.card_weather;
	}

	@Override
	protected void applyTo(View convertView) {
		TextView cityNameTextView = ((TextView) convertView.findViewById(R.id.tv_city_name));
		cityNameTextView.setText(mWeather.getCityName());
		TextView temperatureTextView = ((TextView) convertView.findViewById(R.id.tv_temperature));
		temperatureTextView.setText(mWeather.getTemperature());

		TextView dateTextView = ((TextView) convertView.findViewById(R.id.tv_date));
		dateTextView.setText(mWeather.getUpdateTime());

		TextView overviewTextView = ((TextView) convertView.findViewById(R.id.tv_overview));
		overviewTextView.setText(mWeather.getOverView());

		TextView uvaTextView = ((TextView) convertView.findViewById(R.id.tv_uva));
		uvaTextView.setText(mWeather.getUva());

		String drawable = mWeather.getWeatherDrawable();
		int index = Integer.parseInt(drawable.split("\\.")[0]);
		ImageView weatherIconImageView = ((ImageView) convertView
				.findViewById(R.id.tv_weather_icon));
		weatherIconImageView.setBackgroundResource(R.drawable.weather_00 + index);

		TextView loadingTextView = (TextView) convertView.findViewById(R.id.tv_loading);
		RelativeLayout weatherLayout = (RelativeLayout) convertView.findViewById(R.id.rl_weather);
		if (mWeather.isInit()) {
			weatherLayout.setVisibility(View.VISIBLE);
			loadingTextView.setVisibility(View.GONE);
		} else {
			weatherLayout.setVisibility(View.GONE);
			loadingTextView.setVisibility(View.VISIBLE);
		}

		ImageView overflowImageView = ((ImageView) convertView.findViewById(R.id.overflow));
		if (hasOverflow == true) {
			overflowImageView.setVisibility(View.VISIBLE);
		} else {
			overflowImageView.setVisibility(View.GONE);
		}
	}
}
