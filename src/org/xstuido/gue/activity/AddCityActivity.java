package org.xstuido.gue.activity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xstuido.gue.R;
import org.xstuido.gue.db.GetUpEarlyDB;
import org.xstuido.gue.util.Constant;
import org.xstuido.gue.util.HiThread;
import org.xstuido.gue.util.Tool;
import org.xstuido.gue.util.weather.Weather;
import org.xstuido.gue.util.weather.WeatherUtil;
import org.xstuido.gue.view.dialog.LoadingDialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class AddCityActivity extends Activity {

	private TextView mBackTextView;
	private EditText mCityEditText;
	private ListView mHintListView;

	private ArrayAdapter<String> mAdapter;
	private ArrayList<String> mData;
	private ArrayList<String> mHintList;
	private LoadingDialog mLoadingDialog;
	private GetUpEarlyDB mDB;

	private TextWatcher mTextWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			boolean result = false;
			mData.clear();
			for (int i = 0; i < mHintList.size(); i++) {
				if (mHintList.get(i).equals(s.toString())) {
					result = true;
					mData.add(mHintList.get(i));
				} else if (mHintList.get(i).contains(s)) {
					mData.add(mHintList.get(i));
				}
			}
			if (!result) {
				mData.add(s.toString());
			}
			mAdapter.notifyDataSetChanged();
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {
		}
	};

	private HiThread mGetWeather = new HiThread() {

		@Override
		public void run() {
			WeatherUtil weatherUtil = WeatherUtil.getInstance();

			// System.out.println("***********AddCityActivity*********\n"
			// + weatherUtil.getWeatherList());

			String city = getParams().get(0).toString();
			Weather weather = weatherUtil.requestWeather(city);

			// System.out.println("***********AddCityActivity*********\n" +
			// weather);

			// 预防WebXML的自动补全功能
			if (weather != null && weather.isInit() && city.equals(weather.getCityName())) {
				weatherUtil.addWeather(weather);

				Message msg = new Message();
				msg.what = Constant.MESSAGE_GET_WEATHER_DONE;
				msg.obj = city;
				mHandler.sendMessage(msg);
			} else if (weather == null) {
				Message msg = new Message();
				msg.what = Constant.MESSAGE_GET_WEATHER_FAIL;
				msg.obj = Tool.getString(R.string.weather_fail);
				mHandler.sendMessage(msg);
			} else {
				Message msg = new Message();
				msg.what = Constant.MESSAGE_GET_WEATHER_FAIL;
				msg.obj = weather.getErrorContent().equals("") ? "查询结果为空！" : weather
						.getErrorContent();
				mHandler.sendMessage(msg);
			}

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	};

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			mLoadingDialog.dismiss();
			switch (msg.what) {
			case Constant.MESSAGE_GET_WEATHER_DONE:
				mDB.insert(msg.obj.toString());
				setResult(RESULT_OK);
				finish();
				overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
				Tool.showToast(Tool.getString(R.string.add_ok));
				break;
			case Constant.MESSAGE_GET_WEATHER_FAIL:
				Tool.showToast(msg.obj.toString());
				mCityEditText.setText("");
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_loction);

		mData = new ArrayList<String>();
		mLoadingDialog = new LoadingDialog(this);
		mDB = new GetUpEarlyDB(BaseApplication.getContext());

		mBackTextView = (TextView) findViewById(R.id.tv_back);
		mCityEditText = (EditText) findViewById(R.id.et_input_city);
		mHintListView = (ListView) findViewById(R.id.lv_hint);

		mCityEditText.addTextChangedListener(mTextWatcher);

		mBackTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
				overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
			}
		});
		getHintList();

		mData.addAll(mHintList);
		mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mData);
		mHintListView.setAdapter(mAdapter);
		mHintListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String city = mData.get(position);
				List<Object> params = new ArrayList<Object>();
				params.add(city);
				mGetWeather.start(params);
				mLoadingDialog.show();
			}
		});
	}

	private void getHintList() {
		mHintList = new ArrayList<String>();
		String item = new String();
		try {
			InputStream inStream = getApplicationContext().getAssets().open("support_list.xml");
			XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
			XmlPullParser parser = parserFactory.newPullParser();
			parser.setInput(inStream, "UTF-8");

			int event = parser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {
				switch (event) {
				case XmlPullParser.START_DOCUMENT:
					item = new String();
					break;
				case XmlPullParser.START_TAG:
					if (parser.getName().equals("string")) {
						item = parser.nextText();
						mHintList.add(item.replaceAll("[ a-zA-Z\\(\\)0-9]", ""));
					}
					break;
				case XmlPullParser.END_TAG:
					if (parser.getName().equals("string")) {
						item = null;
					}
					break;
				default:
					break;
				}
				event = parser.next();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			setResult(RESULT_CANCELED);
			finish();
			overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
			return true;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
}
