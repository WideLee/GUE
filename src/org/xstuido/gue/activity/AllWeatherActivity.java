package org.xstuido.gue.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import org.xstuido.gue.R;
import org.xstuido.gue.db.GetUpEarlyDB;
import org.xstuido.gue.util.Constant;
import org.xstuido.gue.util.HiThread;
import org.xstuido.gue.util.Tool;
import org.xstuido.gue.util.weather.Weather;
import org.xstuido.gue.util.weather.WeatherUtil;
import org.xstuido.gue.view.cards.WeatherCard;
import org.xstuido.gue.view.cards.views.CardUI;
import org.xstuido.gue.view.dialog.LoadingDialog;

import java.util.ArrayList;

/**
 * 管理所有位置天气的界面
 * 
 * @author 11331031 陈熙迪 <375900030@qq.com>
 * 
 */
public class AllWeatherActivity extends Activity {
	private TextView mBackTextView;
	private ImageView mRefreshImageView;
	private ImageView mAddImageView;

	private CardUI mCardView;
	private LoadingDialog mLoadingDialog;
	private GetUpEarlyDB mDB;

	private HiThread mGetWeatherThread = new HiThread() {
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

			Message msg = new Message();
			msg.what = Constant.MESSAGE_GET_WEATHER_ALL_DONE;
			mHandler.sendMessage(msg);
			mLoadingDialog.dismiss();
		}
	};

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constant.MESSAGE_GET_WEATHER_ALL_DONE:
				Tool.showToast(AllWeatherActivity.this, Tool.getString(R.string.update_success));
				break;
			case Constant.MESSAGE_GET_WEATHER_DONE:
				mCardView.refresh();
				break;
			case Constant.MESSAGE_GET_WEATHER_FAIL:
				Tool.showToast(AllWeatherActivity.this, msg.obj.toString());
				break;
			case Constant.MESSAGE_SWIPE_WEATHER_CARD:
				final int index = (Integer) msg.obj;
				DialogInterface.OnClickListener mPositiveClickListener = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						WeatherUtil util = WeatherUtil.getInstance();
						Weather name = util.getWeatherList().get(index);
						boolean res = mDB.delete(name.getCityName());
						if (res) {
							util.getWeatherList().remove(index);
							updateWeatherList();
						}
						Tool.showToast(AllWeatherActivity.this, Tool.getString(R.string.delete_ok));
					}
				};
				DialogInterface.OnClickListener mNegativeClickListener = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						mCardView.addCard(new WeatherCard(index, mHandler, false));
						mCardView.refresh();

					}
				};
				DialogInterface.OnCancelListener onCancelListener = new DialogInterface.OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
						mCardView.addCard(new WeatherCard(index, mHandler, false));
						mCardView.refresh();
					}
				};
				AlertDialog.Builder builder = new AlertDialog.Builder(AllWeatherActivity.this);
				builder.setMessage(Tool.getString(R.string.ensure_delete));
				builder.setNegativeButton(Tool.getString(R.string.cancel), mNegativeClickListener);
				builder.setPositiveButton(Tool.getString(R.string.ok), mPositiveClickListener);
				builder.setOnCancelListener(onCancelListener);
				builder.show();
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_weather);

		mBackTextView = (TextView) findViewById(R.id.tv_back);
		mRefreshImageView = (ImageView) findViewById(R.id.iv_refresh);
		mAddImageView = (ImageView) findViewById(R.id.iv_banner_add);
		mCardView = (CardUI) findViewById(R.id.lv_weathers);
		mLoadingDialog = new LoadingDialog(this);
		mDB = new GetUpEarlyDB(BaseApplication.getContext());

		initViews();
	}

	private void initViews() {
		mBackTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
			}
		});

		mRefreshImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mLoadingDialog.show();
				mGetWeatherThread.start();
			}
		});
        mRefreshImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Tool.showBannerToast(AllWeatherActivity.this, getString(R.string.help_banner_update_weather));
                return true;
            }
        });

		mAddImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), AddCityActivity.class);
				startActivityForResult(intent, Constant.REQUEST_CODE_ADD_CITY);
				overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
			}
		});
        mAddImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Tool.showBannerToast(AllWeatherActivity.this, getString(R.string.help_banner_add_weather));
                return true;
            }
        });

		mCardView.setSwipeable(true);
		updateWeatherList();
	}

	/**
	 * 更新天气列表
	 */
	private void updateWeatherList() {
		ArrayList<String> citys = new ArrayList<String>();
		citys = mDB.getAllLocation();
		mCardView.clearCards();
		for (int i = 0; i < citys.size(); i++) {
			mCardView.addCard(new WeatherCard(i, mHandler, false));
		}
		mCardView.refresh();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			finish();
			overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
			return true;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == Constant.REQUEST_CODE_ADD_CITY) {
			if (resultCode == RESULT_OK) {
				updateWeatherList();
			}
		}
	}
}
