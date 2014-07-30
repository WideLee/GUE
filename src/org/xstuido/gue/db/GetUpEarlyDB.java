package org.xstuido.gue.db;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.xstuido.gue.util.Event;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * GetUpEarly的SQLite数据库接口，包括事件以及城市两个表的增删改查信息
 * 
 * @author 11331173 李明宽 <sysu_limingkuan@163.com>
 * 
 */
public class GetUpEarlyDB extends SQLiteOpenHelper {

	private static final String DB_NAME = "GetUpEarly.db";
	private static final int DB_VRESION = 1;
	private static final String EVENT_TABLE = "event";
	private static final String LOCATION_TABLE = "location";

	private static final String COLUMN_KEY_EID = "eid";
	private static final String COLUMN_IS_SIGN_IN = "is_sign_in";
	private static final String COLUMN_IS_DONE = "is_done";
	private static final String COLUMN_EVENT_TIME = "event_time";
	private static final String COLUMN_EVENT_CONTENT = "event_content";

	private static final String COLUMN_KEY_LID = "lid";
	private static final String COLUMN_CITY_NAME = "city_name";

	@SuppressLint("SimpleDateFormat")
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private static final String EVENT_SQL_CREATE = "create table " + EVENT_TABLE + " ( "
			+ COLUMN_KEY_EID + " integer primary key autoincrement, " + COLUMN_IS_SIGN_IN
			+ " integer, " + COLUMN_IS_DONE + " integer, " + COLUMN_EVENT_TIME + " text, "
			+ COLUMN_EVENT_CONTENT + " text);";
	private static final String LOCATION_SQL_CREATE = "create table " + LOCATION_TABLE + " ( "
			+ COLUMN_KEY_LID + " integer primary key autoincrement, " + COLUMN_CITY_NAME
			+ " text);";

	public GetUpEarlyDB(Context context) {
		super(context, DB_NAME, null, DB_VRESION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(EVENT_SQL_CREATE);
		db.execSQL(LOCATION_SQL_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS event");
		db.execSQL("DROP TABLE IF EXISTS location");
		onCreate(db);
	}

	/**
	 * 插入一个日程事件
	 * 
	 * @param entity
	 *            日程事件
	 * @return 这个事件在数据库中对应的id
	 */
	public long insert(Event entity) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(COLUMN_IS_SIGN_IN, entity.isSignIn());
		values.put(COLUMN_IS_DONE, entity.isDone());
		values.put(COLUMN_EVENT_TIME, dateFormat.format(new Date(entity.getTime())));
		values.put(COLUMN_EVENT_CONTENT, entity.getContent());

		long rid = db.insert(EVENT_TABLE, null, values);
		entity.setId(rid);
		db.close();
		return rid;
	}

	/**
	 * 根据事件的ID删除一个事件
	 * 
	 * @param id
	 *            要删除的事件的ID
	 * @return 返回总共删除的记录条数
	 */
	public int deleteEventById(Long id) {
		SQLiteDatabase db = getWritableDatabase();
		String whereClause = COLUMN_KEY_EID + " = ?";
		String[] whereArgs = { id.toString() };
		int row = db.delete(EVENT_TABLE, whereClause, whereArgs);
		db.close();
		return row;
	}

	/**
	 * 根据ID更新一条事件记录
	 * 
	 * @param entity
	 *            新的日程事件
	 * @return 更新的记录条数
	 */
	public int updateEventById(Event entity) {
		SQLiteDatabase db = getWritableDatabase();
		String whereClause = COLUMN_KEY_EID + " = ?";
		String[] whereArgs = { Integer.toString((int) entity.getId()) };
		ContentValues values = new ContentValues();

		values.put(COLUMN_IS_SIGN_IN, entity.isSignIn());
		values.put(COLUMN_IS_DONE, entity.isDone());
		values.put(COLUMN_EVENT_TIME, dateFormat.format(new Date(entity.getTime())));
		values.put(COLUMN_EVENT_CONTENT, entity.getContent());

		int rows = db.update(EVENT_TABLE, values, whereClause, whereArgs);
		db.close();
		return rows;
	}

	/**
	 * 根据日程事件的ID来查询相应的事件
	 * 
	 * @param id
	 *            事件的ID
	 * @return 查询到的事件，若为空表示没查询到该ID的事件
	 * @throws ParseException
	 *             当日期不符合规范的时候抛出异常
	 */
	public Event getEventById(Long id) throws ParseException {
		Event event = null;
		SQLiteDatabase db = getReadableDatabase();
		String selection = COLUMN_KEY_EID + " = ?";
		String[] selectionArgs = { id.toString() };
		Cursor c = db.query(EVENT_TABLE, null, selection, selectionArgs, null, null, null);
		if (c.moveToNext()) {
			event = new Event(c.getInt(c.getColumnIndex(COLUMN_IS_SIGN_IN)), c.getInt(c
					.getColumnIndex(COLUMN_IS_DONE)), dateFormat.parse(
					c.getString(c.getColumnIndex(COLUMN_EVENT_TIME))).getTime(), c.getString(c
					.getColumnIndex(COLUMN_EVENT_CONTENT)));
			event.setId(c.getLong(c.getColumnIndex(COLUMN_KEY_EID)));
		}
		c.close();
		db.close();
		return event;
	}

	/**
	 * 选出特定一天的日程事件记录
	 * 
	 * @param date
	 *            需要查找的日期
	 * @param choose
	 *            如果为1 那么选出的是当天所有签到记录，如果是0选出的是当天日程记录，其他返回当天所有的事件
	 * @return
	 * @throws ParseException
	 */
	@SuppressLint("SimpleDateFormat")
	public ArrayList<Event> getEventByDate(Date date, int choose) throws ParseException {
		ArrayList<Event> list = new ArrayList<Event>();
		SQLiteDatabase db = getReadableDatabase();
		SimpleDateFormat format_begin = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		SimpleDateFormat format_end = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
		String dateBegin = format_begin.format(date);
		String dateEnd = format_end.format(date);
		String SEARCH_EVENT = "select * from " + EVENT_TABLE + " where  ( datetime("
				+ COLUMN_EVENT_TIME + ") < \"" + dateEnd + "\" and " + "datetime("
				+ COLUMN_EVENT_TIME + ") > \"" + dateBegin + "\" );";

		Cursor c = db.rawQuery(SEARCH_EVENT, null);

		boolean is_ava = (choose == 0 || choose == 1);
		while (c.moveToNext()) {
			if (!is_ava || (is_ava && (c.getInt(c.getColumnIndex(COLUMN_IS_SIGN_IN)) == choose))) {
				Event event = new Event(c.getInt(c.getColumnIndex(COLUMN_IS_SIGN_IN)), c.getInt(c
						.getColumnIndex(COLUMN_IS_DONE)), dateFormat.parse(
						c.getString(c.getColumnIndex(COLUMN_EVENT_TIME))).getTime(), c.getString(c
						.getColumnIndex(COLUMN_EVENT_CONTENT)));
				event.setId(c.getLong(c.getColumnIndex(COLUMN_KEY_EID)));
				list.add(event);
			}
		}
		c.close();
		db.close();

		return list;
	}

	/**
	 * 选出所有的日程事件记录
	 * 
	 * @param choose
	 *            如果为1 那么选出的是签到记录，如果是0选出的是日程记录，其他返回所有的Event
	 * @return
	 * @throws ParseException
	 */
	public ArrayList<Event> getALLEvent(int choose) throws ParseException {
		ArrayList<Event> list = new ArrayList<Event>();
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.query(EVENT_TABLE, null, null, null, null, null, COLUMN_EVENT_TIME);

		boolean is_ava = (choose == 0 || choose == 1);
		while (c.moveToNext()) {
			if (!is_ava || (is_ava && (c.getInt(c.getColumnIndex(COLUMN_IS_SIGN_IN)) == choose))) {
				Event event = new Event(c.getInt(c.getColumnIndex(COLUMN_IS_SIGN_IN)), c.getInt(c
						.getColumnIndex(COLUMN_IS_DONE)), dateFormat.parse(
						c.getString(c.getColumnIndex(COLUMN_EVENT_TIME))).getTime(), c.getString(c
						.getColumnIndex(COLUMN_EVENT_CONTENT)));
				event.setId(c.getLong(c.getColumnIndex(COLUMN_KEY_EID)));
				list.add(event);
			}
		}

		c.close();
		db.close();

		return list;
	}

	/**
	 * 向天气管理中插入一个城市
	 * 
	 * @param city
	 *            城市名称
	 * @return 如果数据库中没有该城市返回true，否则不插入返回false
	 */
	public boolean insert(String city) {
		if (hasLocation(city)) {
			return false;
		}

		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(COLUMN_CITY_NAME, city);
		db.insert(LOCATION_TABLE, null, values);
		db.close();
		return true;
	}

	/**
	 * 删除天气城市
	 * 
	 * @param city
	 *            城市名称
	 * @return 如果本来已经没有这个城市返回false，如果正常删掉城市返回true
	 */
	public boolean delete(String city) {
		if (!hasLocation(city)) {
			return false;
		}

		SQLiteDatabase db = getWritableDatabase();
		String whereClause = COLUMN_CITY_NAME + " = ?";
		String[] whereArgs = { city };
		db.delete(LOCATION_TABLE, whereClause, whereArgs);
		db.close();

		return true;
	}

	/**
	 * 查询数据库是否已经存在给定的城市
	 * 
	 * @param city
	 *            城市的名字
	 * @return 如果已经存在返回true，如果不存在返回false
	 */
	public boolean hasLocation(String city) {
		boolean result = false;
		SQLiteDatabase db = getReadableDatabase();
		String selection = COLUMN_CITY_NAME + " = ?";
		String[] selectionArgs = { city };
		Cursor c = db.query(LOCATION_TABLE, null, selection, selectionArgs, null, null, null);
		if (c.moveToNext()) {
			result = true;
		}
		c.close();
		db.close();
		return result;
	}

	/**
	 * 获取所有的位置城市信息
	 * 
	 * @return 所有的城市列表
	 */
	public ArrayList<String> getAllLocation() {
		ArrayList<String> result = new ArrayList<String>();

		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.query(LOCATION_TABLE, null, null, null, null, null, COLUMN_CITY_NAME);
		while (c.moveToNext()) {
			result.add(c.getString(c.getColumnIndex(COLUMN_CITY_NAME)));
		}
		c.close();
		db.close();
		return result;
	}
}
