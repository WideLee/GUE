package org.xstuido.gue.fragment;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.xstuido.gue.R;
import org.xstuido.gue.activity.AllWeatherActivity;
import org.xstuido.gue.activity.BaseApplication;
import org.xstuido.gue.activity.MoreActivity;
import org.xstuido.gue.activity.SignInActivity;
import org.xstuido.gue.db.GetUpEarlyDB;
import org.xstuido.gue.util.Constant;
import org.xstuido.gue.util.Event;
import org.xstuido.gue.util.HiThread;
import org.xstuido.gue.util.Tool;
import org.xstuido.gue.util.weather.Weather;
import org.xstuido.gue.util.weather.WeatherUtil;
import org.xstuido.gue.view.cards.NothingToDoCard;
import org.xstuido.gue.view.cards.SignInCard;
import org.xstuido.gue.view.cards.ToDoCard;
import org.xstuido.gue.view.cards.WeatherCard;
import org.xstuido.gue.view.cards.objects.CardStack;
import org.xstuido.gue.view.cards.views.CardUI;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * 今日日程以及天气等界面
 * 
 * @author 11331075 高蓝光 <glglzb@qq.com>
 * 
 */
public class TodayToDoFragment extends Fragment {

	private CardUI mCardView;
	private CardStack mWeatherStack;
	private CardStack mTodoStack;
	private CardStack mSignInStack;

	private ImageView mMoreImageView;
	private ImageView mSettingImageView;

	private GetUpEarlyDB mDB;
	private boolean isInit = false;

	private OnClickListener mSignInClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			v.setBackgroundResource(R.drawable.bg_circle_pressed);
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					Intent intent = new Intent(getActivity(), SignInActivity.class);
					intent.putExtra("Action", Constant.REQUEST_CODE_SIGNIN);
					startActivityForResult(intent, Constant.REQUEST_CODE_SIGNIN);
					getActivity().overridePendingTransition(R.anim.in_from_right,
							R.anim.out_to_left);
				}
			}, 500);
		}
	};

	private OnClickListener mOnWeatherClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(getActivity(), AllWeatherActivity.class);
			startActivityForResult(intent, Constant.REQUEST_CODE_WEATHER_LIST);
			getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
		}
	};

	private HiThread mGetWeather = new HiThread() {
		@Override
		public void run() {
			WeatherUtil weatherUtil = WeatherUtil.getInstance();

			ArrayList<Weather> cityList = weatherUtil.getWeatherList();
			for (int i = 0; i < cityList.size(); i++) {
				String city = cityList.get(i).getCityName();
				Weather weather = weatherUtil.requestWeather(city);
				if (weather != null && weather.isInit()) {
					weatherUtil.updateWeather(i, weather);

					Message msg = new Message();
					msg.what = Constant.MESSAGE_GET_WEATHER_DONE;
					msg.obj = i;
					mHandler.sendMessage(msg);
				} else if (weather == null) {
					Message msg = new Message();
					msg.what = Constant.MESSAGE_GET_WEATHER_FAIL;
					msg.obj = Tool.getString(R.string.weather_fail);
					mHandler.sendMessage(msg);
				} else {
					Message msg = new Message();
					msg.what = Constant.MESSAGE_GET_WEATHER_FAIL;
					msg.obj = weather.getErrorContent();
					mHandler.sendMessage(msg);
				}

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	};

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constant.MESSAGE_GET_WEATHER_DONE:
				mCardView.refresh();
				break;
			case Constant.MESSAGE_GET_WEATHER_FAIL:
				Tool.showToast(msg.obj.toString());
				break;
			case Constant.MESSAGE_SWIPE_TODO_CARD:
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
			case Constant.MESSAGE_SWIPE_WEATHER_CARD:

				int count = mWeatherStack.getCount();
				if (count == 0) {
					mWeatherStack.setTitle("");
				} else {
					mWeatherStack.setTitle("实时天气");
				}
				mCardView.refresh();
				break;
			case Constant.MESSAGE_SWIPE_SIGNIN_CARD:
				mSignInStack.setTitle("");
				mCardView.refresh();
				break;
			default:
				break;
			}
		}
	};

	public TodayToDoFragment() {
		mDB = new GetUpEarlyDB(BaseApplication.getContext());
		isInit = false;
		mWeatherStack = new CardStack();
		mWeatherStack.setTitle("实时天气");
		mTodoStack = new CardStack();
		mTodoStack.setTitle("要做的事");
		mSignInStack = new CardStack();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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

		mMoreImageView = (ImageView) main.findViewById(R.id.banner_more);
		mMoreImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), MoreActivity.class);
				startActivity(intent);
				getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
			}
		});

		mSettingImageView = (ImageView) main.findViewById(R.id.iv_setting);
		mSettingImageView.setOnClickListener(mOnWeatherClickListener);

		if (!isInit) {
			initView();
		}
		isInit = true;

		try {
			ArrayList<Event> signEvents = mDB.getEventByDate(new Date(), 1);
			if (signEvents.size() == 0) {
				SignInCard card = new SignInCard(false, mHandler);
				card.setSignClickListener(mSignInClickListener);
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
				ToDoCard card = new ToDoCard(event, mHandler, false);
				mCardView.addCard(card);
			}
			mCardView.refresh();

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return main;
	}

	/**
	 * 初始化界面
	 */
	private void initView() {
		WeatherUtil weatherUtil = WeatherUtil.getInstance();
		ArrayList<String> cityList = mDB.getAllLocation();
		if (cityList.size() == 0) {
			mWeatherStack.setTitle("");
		}
		weatherUtil.removeAllWeather();
		for (int i = 0; i < cityList.size(); i++) {
			String city = cityList.get(i);
			weatherUtil.addWeather(new Weather(city));
			WeatherCard card = new WeatherCard(i, mHandler, true);
			card.setOverflowClickListener(mOnWeatherClickListener);
			mWeatherStack.add(card);
		}
		mCardView.refresh();
		mGetWeather.start();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	/**
	 * 获取当天所有未完成的日程记录
	 * 
	 * @return 日程事件的列表
	 * @throws ParseException
	 *             日期格式错误的时候抛出异常
	 */
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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == Constant.REQUEST_CODE_SIGNIN) {
			SignInCard card;
			mSignInStack.removeAllCards();
			if (resultCode == Activity.RESULT_OK) {
				card = new SignInCard(true, mHandler);
			} else {
				card = new SignInCard(false, mHandler);
			}
			card.setSignClickListener(mSignInClickListener);
			mSignInStack.add(card);
		} else if (requestCode == Constant.REQUEST_CODE_WEATHER_LIST) {
			mWeatherStack.removeAllCards();
			ArrayList<String> cityList = mDB.getAllLocation();
			if (cityList.size() == 0) {
				mWeatherStack.setTitle("");
			} else {
				mWeatherStack.setTitle("实时天气");
			}
			WeatherUtil weatherUtil = WeatherUtil.getInstance();
			for (int i = 0; i < weatherUtil.getWeatherList().size(); i++) {
				WeatherCard card = new WeatherCard(i, mHandler, true);
				card.setOverflowClickListener(mOnWeatherClickListener);
				mWeatherStack.add(card);
			}
		}
		mCardView.refresh();
		super.onActivityResult(requestCode, resultCode, data);
	}
}
