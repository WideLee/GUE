package org.xstuido.gue.fragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.xstuido.gue.R;
import org.xstuido.gue.activity.BaseApplication;
import org.xstuido.gue.db.GetUpEarlyDB;
import org.xstuido.gue.util.Constant;
import org.xstuido.gue.util.Event;
import org.xstuido.gue.util.HiThread;
import org.xstuido.gue.util.Tool;
import org.xstuido.gue.view.adapter.SignHistoryAdapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

public class SignInHistoryFragment extends Fragment {

	private ListView mHistoryListView;
	private GetUpEarlyDB mDB;
	private ImageView mShareButton;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constant.MESSAGE_SCREEN_SHOT_DONE:
				Intent shareIntent = new Intent(Intent.ACTION_SEND);
				shareIntent.setType("image/*");
				Uri uri = (Uri) msg.obj;
				shareIntent.putExtra(Intent.EXTRA_TEXT, Tool.getString(R.string.share_text));
				shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
				startActivity(Intent.createChooser(shareIntent,
						Tool.getString(R.string.share_title)));
				break;
			case Constant.MESSAGE_SCREEN_SHOT_FAIL:
				String errorMessage = msg.obj.toString();
				Tool.showToast(errorMessage);
				break;
			default:
				break;
			}
		}
	};

	private HiThread mScreenShot = new HiThread() {

		@Override
		public void run() {
			List<Object> param = getParams();
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

				Bitmap bitmap = (Bitmap) param.get(0);

				int stautsHeight = (Integer) param.get(1);
				// Log.d("mClip", "状态栏的高度为:" + stautsHeight);

				Point point = new Point();
				getActivity().getWindowManager().getDefaultDisplay().getSize(point);
				// create the bitmap what is needed
				bitmap = Bitmap.createBitmap(bitmap, 0, stautsHeight, point.x, point.y
						- stautsHeight);

				// ouput the bitmap to sdcard
				String path = Environment.getExternalStorageDirectory().getAbsolutePath()
						+ "/gue/images/";
				String name = Long.toString(System.currentTimeMillis()) + ".bmp";
				Uri imageUri = Uri.EMPTY;

				try {
					File pathDir = new File(path);
					if (!pathDir.exists()) {
						pathDir.mkdirs();
					}
					File imageFile = new File(path + name);
					if (!imageFile.exists()) {
						imageFile.createNewFile();
					}
					FileOutputStream fos;

					fos = new FileOutputStream(imageFile);
					bitmap.compress(CompressFormat.JPEG, 100, fos);
					fos.flush();
					fos.close();

					imageUri = Uri.fromFile(imageFile);

					Message message = new Message();
					message.what = Constant.MESSAGE_SCREEN_SHOT_DONE;
					message.obj = imageUri;
					mHandler.sendMessage(message);

				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				Message message = new Message();
				message.what = Constant.MESSAGE_SCREEN_SHOT_FAIL;
				message.obj = Tool.getString(R.string.sdcard_unmount);
				mHandler.sendMessage(message);
			}
		}
	};

	private boolean isInit;

	public SignInHistoryFragment() {
		mDB = new GetUpEarlyDB(BaseApplication.getContext());
		isInit = false;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View main = inflater.inflate(R.layout.fragment_sign_history, null);
		mHistoryListView = (ListView) main.findViewById(R.id.lv_history);
		mShareButton = (ImageView) main.findViewById(R.id.btn_share);

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

		mShareButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				View view = getActivity().getWindow().getDecorView();
				view.setDrawingCacheEnabled(true);
				view.buildDrawingCache();
				Bitmap bitmap = view.getDrawingCache();

				// get Height of Status bar
				Rect frame = new Rect();
				// get size of screen
				view.getWindowVisibleDisplayFrame(frame);
				int stautsHeight = frame.top;

				List<Object> param = new ArrayList<Object>();
				param.add(bitmap);
				param.add(stautsHeight);
				mScreenShot.start(param);
			}
		});
		return main;
	}

	private void initView() {
	}

	@Override
	public void onResume() {
		super.onResume();
	}

}