package org.xstuido.gue.fragment;

import android.annotation.SuppressLint;
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
import android.widget.TextView;

import org.xstuido.gue.R;
import org.xstuido.gue.activity.BaseApplication;
import org.xstuido.gue.db.GetUpEarlyDB;
import org.xstuido.gue.util.Constant;
import org.xstuido.gue.util.Event;
import org.xstuido.gue.util.HiThread;
import org.xstuido.gue.util.Tool;
import org.xstuido.gue.view.adapter.SignHistoryAdapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * 签到记录界面，包含分享功能
 *
 * @author 11331075 高蓝光 <glglzb@qq.com>
 */
public class SignInHistoryFragment extends Fragment {

    private ListView mHistoryListView;
    private GetUpEarlyDB mDB;
    private ImageView mShareButton;
    private ImageView mNothingImageView;
    private TextView mNothingTextView;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.MESSAGE_SCREEN_SHOT_DONE:
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("image/*");
                    Uri uri = (Uri) msg.obj;
                    shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, Tool.getString(R.string.share_text));

                    startActivity(Intent.createChooser(shareIntent,
                            Tool.getString(R.string.share_title)));
                    break;
                case Constant.MESSAGE_SCREEN_SHOT_FAIL:
                    String errorMessage = msg.obj.toString();
                    Tool.showToast(getActivity(), errorMessage);
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
                Point point = new Point();
                getActivity().getWindowManager().getDefaultDisplay().getSize(point);

                bitmap = Bitmap.createBitmap(bitmap, 0, stautsHeight, point.x, point.y
                        - stautsHeight);

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

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View main = inflater.inflate(R.layout.fragment_sign_history, null);
        mHistoryListView = (ListView) main.findViewById(R.id.lv_history);
        mShareButton = (ImageView) main.findViewById(R.id.btn_share);
        mNothingImageView = (ImageView) main.findViewById(R.id.iv_time_line_nothing);
        mNothingTextView = (TextView) main.findViewById(R.id.tv_time_line_nothing);

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

        if (data.size() == 0) {
            mNothingTextView.setVisibility(View.VISIBLE);
            mNothingImageView.setVisibility(View.VISIBLE);
        } else {
            mNothingTextView.setVisibility(View.GONE);
            mNothingImageView.setVisibility(View.GONE);
        }
        mShareButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                View view = getActivity().getWindow().getDecorView();
                view.setDrawingCacheEnabled(true);
                view.buildDrawingCache();
                Bitmap bitmap = view.getDrawingCache();

                Rect frame = new Rect();
                view.getWindowVisibleDisplayFrame(frame);
                int stautsHeight = frame.top;

                List<Object> param = new ArrayList<Object>();
                param.add(bitmap);
                param.add(stautsHeight);
                mScreenShot.start(param);
            }
        });
        mShareButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Tool.showBannerToast(getActivity(), getString(R.string.help_banner_share));
                return true;
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