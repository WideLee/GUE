package org.xstuido.gue.fragment;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.xstuido.gue.R;
import org.xstuido.gue.activity.BaseApplication;
import org.xstuido.gue.cards.NothingToDoCard;
import org.xstuido.gue.cards.SignInCard;
import org.xstuido.gue.cards.ToDoCard;
import org.xstuido.gue.cards.WeatherCard;
import org.xstuido.gue.cards.objects.CardStack;
import org.xstuido.gue.cards.views.CardUI;
import org.xstuido.gue.db.GetUpEarlyDB;
import org.xstuido.gue.util.Event;
import org.xstuido.gue.util.HiThread;
import org.xstuido.gue.util.weather.LocationDAO;
import org.xstuido.gue.util.weather.Weather;
import org.xstuido.gue.util.weather.WeatherUtil;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class TodayToDoFragment extends Fragment {

	public static final int MESSAGE_GET_WEATHER_DONE = 0;
	public static final int MESSAGE_GET_WEATHER_FAIL = 1;
	public static final int MESSAGE_SWIPE_TODO_CARD_DONE = 2;
	public static final int MESSAGE_SWIPE_WEATHER_CARD_DONE = 3;

	private CardUI mCardView;
	private CardStack mWeatherStack;
	private CardStack mTodoStack;
	private CardStack mSignInStack;

	private GetUpEarlyDB mDB;
	private WeatherUtil mWeatherUtil;
	private boolean isInit = false;

	private HiThread mGetWeather = new HiThread() {
		@Override
		public void run() {
			if (mWeatherUtil != null) {
				mWeatherUtil.initWeatherList();
				Message msg = new Message();
				msg.what = MESSAGE_GET_WEATHER_DONE;
				mHandler.sendMessage(msg);
			}
		}
	};

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_GET_WEATHER_DONE:
				// ArrayList<Weather> result = mWeatherUtil.getWeatherList();
				mWeatherStack.removeAllCards();
				// for (Weather weather : result) {
				// mWeatherStack.add(new WeatherCard(weather, mHandler, true));
				// }
				mCardView.refresh();
				break;
			case MESSAGE_SWIPE_TODO_CARD_DONE:
				long id = (Long) msg.obj;
				try {
					Event event = mDB.getEventById(id);
					event.setIsDone(1);
					mDB.updateEventById(event);

					ArrayList<Event> toDoEvents = getToDoEvent();
					if (toDoEvents.size() == 0) {
						mCardView.addCard(new NothingToDoCard());
						mCardView.refresh();
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			case MESSAGE_SWIPE_WEATHER_CARD_DONE:
				int count = mWeatherStack.getCount();
				System.out.println(count);
				if (count == 0) {
					mWeatherStack.setTitle("");
					mCardView.refresh();
				}
			default:
				break;
			}
		};
	};

	public TodayToDoFragment() {
		mWeatherUtil = new WeatherUtil(mHandler);
		mDB = new GetUpEarlyDB(BaseApplication.getContext());
		isInit = false;
		mWeatherStack = new CardStack();
		mWeatherStack.setTitle("今天天气");
		mTodoStack = new CardStack();
		mTodoStack.setTitle("要做的事");
		mSignInStack = new CardStack();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		System.out.println("On Insert Event");
		mDB.insert(new Event(1, 0, System.currentTimeMillis(), new Date().toString()));
		mDB.insert(new Event(0, 0, System.currentTimeMillis(), new Date().toString()));
	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View main = inflater.inflate(R.layout.fragment_today_todo, null);
		mCardView = (CardUI) main.findViewById(R.id.lv_cards);
		mCardView.setSwipeable(true);
		mCardView.addStack(mSignInStack);
		mCardView.addStack(mWeatherStack);
		mCardView.addStack(mTodoStack);

		if (!isInit) {
			initView();
		}
		isInit = true;

		try {
			ArrayList<Event> signEvents = mDB.getEventByDate(new Date(), 1);
			if (signEvents.size() == 0) {
				SignInCard card = new SignInCard(false);
				card.setSignClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						System.out.println("Click");
					}
				});
				mSignInStack.setTitle("今日签到");
				mSignInStack.removeAllCards();
				mSignInStack.add(card);
				mCardView.refresh();
			}

			ArrayList<Event> toDoEvents = getToDoEvent();
			if (toDoEvents.size() == 0) {
				mCardView.addCard(new NothingToDoCard());
			}
			for (Event event : toDoEvents) {
				ToDoCard card = new ToDoCard(event, mHandler, true);
				mCardView.addCard(card);
			}
			mCardView.refresh();

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return main;
	}

	private void initView() {
		mGetWeather.start();

		for (String city : LocationDAO.getCityList()) {
			WeatherCard card = new WeatherCard(new Weather(city), mHandler, true);
			mWeatherStack.add(card);
		}
		mCardView.refresh();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	private ArrayList<Event> getToDoEvent() throws ParseException {
		ArrayList<Event> result = new ArrayList<Event>();
		List<Event> events = mDB.getEventByDate(new Date(), 0);
		for (Event event : events) {
			if (event.isDone() == 0) {
				result.add(event);
			}
		}
		return result;
	}
}
