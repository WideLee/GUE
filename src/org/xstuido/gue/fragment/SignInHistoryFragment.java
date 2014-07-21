package org.xstuido.gue.fragment;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.xstuido.gue.R;
import org.xstuido.gue.activity.BaseApplication;
import org.xstuido.gue.adapter.SignHistoryAdapter;
import org.xstuido.gue.db.GetUpEarlyDB;
import org.xstuido.gue.util.Event;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class SignInHistoryFragment extends Fragment {

	private ListView mHistoryListView;
	private GetUpEarlyDB mDB;

	private boolean isInit;

	public SignInHistoryFragment() {
		mDB = new GetUpEarlyDB(BaseApplication.getContext());
		isInit = false;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View main = inflater.inflate(R.layout.fragment_sign_history, null);
		mHistoryListView = (ListView) main.findViewById(R.id.lv_history);

		if (!isInit) {
			initView();
		}

		List<Event> data = new ArrayList<Event>();
		try {
			data = mDB.getALLEvent(1);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		SignHistoryAdapter adapter = new SignHistoryAdapter(BaseApplication.getContext());
		adapter.setData(data);
		mHistoryListView.setAdapter(adapter);

		return main;
	}

	private void initView() {
	}

	@Override
	public void onResume() {
		super.onResume();
	}

}