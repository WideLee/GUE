package org.xstuido.gue.util.weather;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xstuido.gue.util.Constant;

public class WeatherUtil {

	private ArrayList<Weather> mWeatherList;

	private static WeatherUtil instance;

	private WeatherUtil() {
		mWeatherList = new ArrayList<Weather>();
	}

	public static synchronized WeatherUtil getInstance() {
		if (instance == null) {
			instance = new WeatherUtil();
		}
		return instance;
	}

	public ArrayList<Weather> getWeatherList() {
		return mWeatherList;
	}

	public void setWeatherList(ArrayList<Weather> mWeatherList) {
		this.mWeatherList = mWeatherList;
	}

	public Weather removeWeather(int index) {
		return mWeatherList.remove(index);
	}

	public void addWeather(Weather weather) {
		this.mWeatherList.add(weather);
	}

	public Weather updateWeather(int index, Weather weather) {
		return mWeatherList.set(index, weather);
	}

	public void removeAllWeather() {
		mWeatherList.clear();
	}

	public Weather requestWeather(String city) {
		HttpResponse mHttpResponse;
		HttpClient mHttpClient;
		mHttpClient = new DefaultHttpClient();
		Weather weather = null;

		HttpGet httpGet = new HttpGet(Constant.HOST_URL + "?theCityName=" + city);
		try {
			mHttpResponse = mHttpClient.execute(httpGet);
			int reponseCode = mHttpResponse.getStatusLine().getStatusCode();
			if (reponseCode == HttpStatus.SC_OK) {
				String resultData = EntityUtils.toString(mHttpResponse.getEntity());
				ArrayList<String> data = new ArrayList<String>();
				data = analyzeXML(resultData);
				if (data.size() >= 1) {
					weather = new Weather();
					// System.out.println(data);
					weather.initWeather(data);
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return weather;
	}

	public ArrayList<String> analyzeXML(String resultData) {
		ArrayList<String> result = new ArrayList<String>();
		String item = new String();
		try {
			XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
			XmlPullParser parser = parserFactory.newPullParser();
			parser.setInput(new StringReader(resultData));

			int event = parser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {
				switch (event) {
				case XmlPullParser.START_DOCUMENT:
					item = new String();
					break;
				case XmlPullParser.START_TAG:
					if (parser.getName().equals("string")) {
						item = parser.nextText();
						result.add(item);
					}
					break;
				case XmlPullParser.END_TAG:
					if (parser.getName().equals("string")) {
						item = null;
					}
					break;
				}
				event = parser.next();
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
}
