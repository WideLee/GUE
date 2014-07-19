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

@SuppressLint("SimpleDateFormat")
public class GetUpEarlyDB extends SQLiteOpenHelper {

	private static final String DB_NAME = "GetUpEarly.db";
	private static final int DB_VRESION = 1;
	private static final String EVENT_TABLE = "event";

	private static final String COLUMN_KEY_EID = "eid";
	private static final String COLUMN_IS_SIGN_IN = "is_sign_in";
	private static final String COLUMN_IS_DONE = "is_done";
	private static final String COLUMN_EVENT_TIME = "event_time";
	private static final String COLUMN_EVENT_CONTENT = "event_content";

	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private static final String EVENT_SQL_CREATE = "create table " + EVENT_TABLE + " ( "
			+ COLUMN_KEY_EID + " integer primary key autoincrement, " + COLUMN_IS_SIGN_IN
			+ " integer, " + COLUMN_IS_DONE + " integer, " + COLUMN_EVENT_TIME + " text, "
			+ COLUMN_EVENT_CONTENT + " text);";

	public GetUpEarlyDB(Context context) {
		super(context, DB_NAME, null, DB_VRESION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		System.out.println("create database");
		db.execSQL(EVENT_SQL_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// 升级数据库
		db.execSQL("DROP TABLE IF EXISTS event");
		onCreate(db);
	}

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

	// 删除数据操作
	public int deleteEventById(Long id) {
		SQLiteDatabase db = getWritableDatabase();
		String whereClause = COLUMN_KEY_EID + " = ?";
		String[] whereArgs = { id.toString() };
		int row = db.delete(EVENT_TABLE, whereClause, whereArgs);
		db.close();
		return row;
	}

	// 更新数据操作
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

	// 查询数据操作
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
	 * 
	 * @param date
	 *            需要查找的日期
	 * @param choose
	 *            如果为1 那么选出的是当天所有签到记录，如果是0选出的是当天日程记录，其他返回当天所有的事件
	 * @return
	 * @throws ParseException
	 */
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
}
