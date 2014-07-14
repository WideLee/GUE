package org.xstuido.gue.fragment;

import java.util.ArrayList;

import org.xstuido.gue.R;
import org.xstuido.gue.cards.WeatherCard;
import org.xstuido.gue.cards.objects.CardStack;
import org.xstuido.gue.cards.views.CardUI;
import org.xstuido.gue.db.GetUpEarlyDB;
import org.xstuido.gue.util.HiThread;
import org.xstuido.gue.util.LocationUtil;
import org.xstuido.gue.util.Weather;
import org.xstuido.gue.util.WeatherUtil;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TodayToDoFragment extends Fragment {

	private static final int GET_WEATHER_DONE = 0;

	private CardUI mCardView;
	private CardStack mWeatherStack;

	private GetUpEarlyDB mDB;
	private WeatherUtil mWeatherUtil;
	private boolean isInit = false;

	private HiThread mGetWeather = new HiThread() {
		public void run() {
			if (mWeatherUtil != null) {
				mWeatherUtil.initWeatherList();
				Message msg = new Message();
				msg.what = GET_WEATHER_DONE;
				mHandler.sendMessage(msg);
			}
		}
	};

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GET_WEATHER_DONE:
				ArrayList<Weather> result = mWeatherUtil.getWeatherList();
				mWeatherStack.removeAllCards();
				for (Weather weather : result) {
					mWeatherStack.add(new WeatherCard(weather, true));
				}
				mCardView.refresh();
				break;
			default:
				break;
			}
		};
	};

	public TodayToDoFragment() {
		mWeatherUtil = new WeatherUtil();
		mDB = new GetUpEarlyDB(getActivity());
		isInit = false;
		mWeatherStack = new CardStack();
		mWeatherStack.setTitle("Weather");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View main = inflater.inflate(R.layout.fragment_today_todo, null);
		mCardView = (CardUI) main.findViewById(R.id.lv_cards);
		mCardView.setSwipeable(true);
		mCardView.addStack(mWeatherStack);
		mCardView.refresh();

		System.out.println(mWeatherStack.getCards());

		if (!isInit) {
			initView();
			mGetWeather.start();
		}
		isInit = true;
		return main;
	}

	private void initView() {

		for (String city : LocationUtil.getCityList()) {
			mWeatherStack.add(new WeatherCard(new Weather(city), true));
		}
		mCardView.refresh();
	}

	@Override
	public void onResume() {
		super.onResume();
	}
}