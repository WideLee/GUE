package org.xstuido.gue.util;

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

public class WeatherUtil {

    public static final String HOST_URL = "http://www.webxml.com.cn/WebServices/WeatherWebService.asmx/getWeatherbyCityName";

    private HttpResponse mHttpResponse;
    private HttpClient mHttpClient;
    private ArrayList<Weather> mWeatherList;

    public WeatherUtil() {
	mHttpClient = new DefaultHttpClient();
	mWeatherList = new ArrayList<Weather>();
    }

    public void initWeatherList() {
	mWeatherList.clear();
	ArrayList<String> cityList = LocationDAO.getCityList();
	for (String city : cityList) {
	    Weather weather = requestWeather(city);
	    try {
		Thread.sleep(1000);
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }
	    mWeatherList.add(weather);
	}
    }

    public Weather getWeather(int index) {
	return mWeatherList.get(index);
    }

    public ArrayList<Weather> getWeatherList() {
	return mWeatherList;
    }

    private Weather requestWeather(String city) {
	Weather weather = new Weather();

	HttpGet httpGet = new HttpGet(HOST_URL + "?theCityName=" + city);
	try {
	    mHttpResponse = mHttpClient.execute(httpGet);
	    int reponseCode = mHttpResponse.getStatusLine().getStatusCode();
	    if (reponseCode == HttpStatus.SC_OK) {
		String resultData = EntityUtils.toString(mHttpResponse
			.getEntity());
		ArrayList<String> data = new ArrayList<String>();
		data = analyzeXML(resultData);
		if (data.size() >= 1) {
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

    private ArrayList<String> analyzeXML(String resultData) {
	ArrayList<String> result = new ArrayList<String>();
	String item = new String();
	try {
	    XmlPullParserFactory parserFactory = XmlPullParserFactory
		    .newInstance();
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
