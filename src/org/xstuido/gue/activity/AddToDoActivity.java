package org.xstuido.gue.activity;

import java.text.ParseException;
import java.util.Calendar;

import org.xstuido.gue.R;
import org.xstuido.gue.db.GetUpEarlyDB;
import org.xstuido.gue.util.Constant;
import org.xstuido.gue.util.Event;
import org.xstuido.gue.util.Tool;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

public class AddToDoActivity extends Activity {

    private DatePickerDialog mDatePicker;
    private TimePickerDialog mTimePicker;
    private Button mDatePickButton;
    private Button mTimePickButton;
    private Calendar mCalendar;
    private EditText mEditText;
    private TextView mBackTextView;
    private TextView mOKTextView;

    private boolean mIsPickDate;
    private int requestCode;
    private long eventID;
    Intent intent;

    private GetUpEarlyDB mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_add_todo);

	mIsPickDate = false;
	mDB = new GetUpEarlyDB(BaseApplication.getContext());
	mCalendar = Calendar.getInstance();

	mDatePickButton = (Button) findViewById(R.id.btn_select_date);
	mTimePickButton = (Button) findViewById(R.id.btn_select_time);
	mEditText = (EditText) findViewById(R.id.et_todo);
	mBackTextView = (TextView) findViewById(R.id.tv_back);
	mOKTextView = (TextView) findViewById(R.id.banner_ok);
	intent = getIntent();
	requestCode = intent.getIntExtra("Action", 0);
	if (requestCode == Constant.REQUEST_CODE_UPDATE_EVENT) {
	    eventID = intent.getLongExtra("Value", -1);
	    mIsPickDate = true;
	    try {
		Event event = mDB.getEventById(eventID);
		mCalendar.setTimeInMillis(event.getTime());
		mDatePickButton.setText(DateFormat.format("yyyy-MM-dd",
			mCalendar));
		mTimePickButton.setText(DateFormat.format("HH:mm", mCalendar));
		mEditText.setText(event.getContent());
	    } catch (ParseException e) {
		e.printStackTrace();
	    }
	}
	mOKTextView.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		if (!mIsPickDate) {
		    Tool.showToast(Tool.getString(R.string.select_date_hint));
		} else if (mEditText.getText().toString().equals("")) {
		    Tool.showToast(Tool.getString(R.string.say_something));
		} else {
		    if (requestCode == Constant.REQUEST_CODE_ADD_EVENT) {
			mDB.insert(new Event(0, 0, mCalendar.getTimeInMillis(),
				mEditText.getText().toString()));
		    } else if (requestCode == Constant.REQUEST_CODE_UPDATE_EVENT) {
			Event event = new Event(0, 0, mCalendar
				.getTimeInMillis(), mEditText.getText()
				.toString());
			event.setId(eventID);
			mDB.updateEventById(event);
		    }
		    Tool.showToast(Tool.getString(R.string.add_ok));
		    setResult(RESULT_OK);
		    finish();
		    overridePendingTransition(R.anim.in_from_left,
			    R.anim.out_to_right);
		}
	    }
	});

	mBackTextView.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		setResult(RESULT_CANCELED);
		finish();
		overridePendingTransition(R.anim.in_from_left,
			R.anim.out_to_right);
	    }
	});

	mDatePicker = new DatePickerDialog(this, 0, new OnDateSetListener() {

	    @Override
	    public void onDateSet(DatePicker view, int year, int monthOfYear,
		    int dayOfMonth) {
		mCalendar.set(Calendar.YEAR, year);
		mCalendar.set(Calendar.MONTH, monthOfYear);
		mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		mDatePickButton.setText(DateFormat.format("yyyy-MM-dd",
			mCalendar));
		mIsPickDate = true;
	    }
	}, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
		mCalendar.get(Calendar.DAY_OF_MONTH));

	mTimePicker = new TimePickerDialog(this, 0, new OnTimeSetListener() {
	    @Override
	    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
		mCalendar.set(Calendar.MINUTE, minute);
		mTimePickButton.setText(DateFormat.format("HH:mm", mCalendar));
	    }
	}, mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE),
		true);

	mDatePickButton.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		mDatePicker.show();
	    }
	});

	mTimePickButton.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		mTimePicker.show();
	    }
	});
    }
}
