package org.xstuido.gue.util;

/**
 * 在项目代码中用到的全局静态常量，用于区分消息传递
 * 
 * @author 11331068 冯亚臣 <1967558085@qq.com>
 */
public class Constant {
	public static final int MESSAGE_GET_WEATHER_DONE = 0;
	public static final int MESSAGE_GET_WEATHER_FAIL = 1;

	public static final int MESSAGE_SWIPE_TODO_CARD = 2;
	public static final int MESSAGE_SWIPE_WEATHER_CARD = 3;
	public static final int MESSAGE_SWIPE_SIGNIN_CARD = 4;

	public static final int MESSAGE_SCREEN_SHOT_DONE = 5;
	public static final int MESSAGE_SCREEN_SHOT_FAIL = 6;

	public static final int MESSAGE_LOADING_DONE = 7;
	public static final int MESSAGE_LOADING_FAIL = 8;

    public static final int MESSAGE_AD_SHOW_DONE = 15;

	public static final int REQUEST_CODE_ADD_EVENT = 9;
	public static final int REQUEST_CODE_UPDATE_EVENT = 10;
	public static final int REQUEST_CODE_SIGNIN = 11;
	public static final int REQUEST_CODE_WEATHER_LIST = 12;
	public static final int REQUEST_CODE_ADD_CITY = 13;
	public static final int MESSAGE_GET_WEATHER_ALL_DONE = 14;

	/**
	 * 天气服务WebService的URL地址
	 */
	public static final String HOST_URL = "http://www.webxml.com.cn/WebServices/WeatherWebService.asmx/getWeatherbyCityName";
	public static final String APP_KEY = "10bac302962111e38d4300163e0029e5";

}
