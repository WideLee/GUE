package org.xstuido.gue.fragment;

import java.util.Calendar;

import org.xstuido.gue.R;
import org.xstuido.gue.db.GetUpEarlyDB;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ToDoListFragment extends Fragment {
	private Button mPreButton;
	private Button mNextButton;
	private TextView mMonthNameTextView;
	private LinearLayout monthTitleLayout;
	private GetUpEarlyDB mDB;
	private ListView mTodoListView;

	public ToDoListFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mDB = new GetUpEarlyDB(getActivity());
		View main = inflater.inflate(R.layout.fragment_todo_list, null);
		initView();
		return main;
	}

	private void initView() {

	}

	private void updateToDoList(Calendar cal) {

	}

	@Override
	public void onResume() {
		initView();
		super.onResume();
	}
}