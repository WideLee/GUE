package org.xstuido.gue.fragment;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.xstuido.gue.R;
import org.xstuido.gue.adapter.SignHistoryAdapter;
import org.xstuido.gue.db.GetUpEarlyDB;
import org.xstuido.gue.util.Event;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class SignInHistoryFragment extends Fragment {

	private TextView mSignTextView;
	private ListView mHistoryListView;
	private GetUpEarlyDB mDB;

	public SignInHistoryFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mDB = new GetUpEarlyDB(getActivity());
		View main = inflater.inflate(R.layout.fragment_sign_history, null);

		mSignTextView = (TextView) main.findViewById(R.id.tv_significant);
		mHistoryListView = (ListView) main.findViewById(R.id.lv_history);
		initView();
		return main;
	}

	private void initView() {
		List<Event> data = new ArrayList<Event>();
		try {
			data = mDB.getALLEvent(1);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		SignHistoryAdapter adapter = new SignHistoryAdapter(getActivity());
		adapter.setData(data);
		mHistoryListView.setAdapter(adapter);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

}