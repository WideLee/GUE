package org.xstuido.gue.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.xstuido.gue.R;
import org.xstuido.gue.activity.AddToDoActivity;
import org.xstuido.gue.activity.BaseApplication;
import org.xstuido.gue.db.GetUpEarlyDB;
import org.xstuido.gue.util.Constant;
import org.xstuido.gue.util.Event;
import org.xstuido.gue.util.Tool;
import org.xstuido.gue.view.adapter.CalendarCardAdapter;
import org.xstuido.gue.view.adapter.CalendarViewPager;
import org.xstuido.gue.view.adapter.ViewPager4SameItem.OnPageSelectedListener;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * 日历以及所有的日程事件列表
 *
 * @author 11331075 高蓝光 <glglzb@qq.com>
 */
public class ToDoListFragment extends Fragment {

    private Button mPreButton;
    private Button mNextButton;
    private TextView mMonthNameTextView;
    private LinearLayout monthTitleLayout;
    private CalendarViewPager mCalendarViewPager;

    private OnPageSelectedListener mOnPageSelectedListener;
    private GetUpEarlyDB mDB;
    // private CardUI mCardView;
    private ListView mCardList;
    private CalendarCardAdapter mAdapter;

    private ImageView mAddImageView;

    private boolean isInit = false;

    private AdapterView.OnItemClickListener mOnCardItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (view.getTag() instanceof CalendarCardAdapter.ViewHolder) {
                final Event event = ((CalendarCardAdapter.ViewHolder) view.getTag()).mEvent;
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),
                        R.style.DialogTheme);
                builder.setItems(
                        new String[]{Tool.getString(R.string.modify_event),
                                Tool.getString(R.string.delete_event)},
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        Intent intent = new Intent(getActivity(), AddToDoActivity.class);
                                        intent.putExtra("Action", Constant.REQUEST_CODE_UPDATE_EVENT);
                                        intent.putExtra("Value", event.getId());
                                        startActivityForResult(intent,
                                                Constant.REQUEST_CODE_UPDATE_EVENT);
                                        getActivity().overridePendingTransition(R.anim.in_from_right,
                                                R.anim.out_to_left);
                                        break;
                                    case 1:
                                        mDB.deleteEventById(event.getId());
                                        updateToDoList(mCalendarViewPager.getSelectedCalendar());
                                        Tool.showToast(getActivity(), Tool.getString(R.string.delete_ok));
                                        break;
                                    default:
                                        break;
                                }
                            }
                        });
                builder.show();
            }
        }
    };

    public ToDoListFragment() {
        mDB = new GetUpEarlyDB(BaseApplication.getContext());
        isInit = false;
    }

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View main = inflater.inflate(R.layout.fragment_todo_list, null);
        mPreButton = (Button) main.findViewById(R.id.btn_pre_month);
        mNextButton = (Button) main.findViewById(R.id.btn_next_month);
        mMonthNameTextView = (TextView) main.findViewById(R.id.tv_month_name);
        monthTitleLayout = (LinearLayout) main.findViewById(R.id.ll_month_title);
        mCalendarViewPager = (CalendarViewPager) main.findViewById(R.id.calendar);

        mCardList = (ListView) main.findViewById(R.id.lv_cards_list);
        mCardList.setOnItemClickListener(mOnCardItemClickListener);
        mAdapter = new CalendarCardAdapter(getActivity());
        mCardList.setAdapter(mAdapter);

        // mCardView = (CardUI) main.findViewById(R.id.lv_cards_list);
        // mCardView.setSwipeable(false);

        mAddImageView = (ImageView) main.findViewById(R.id.banner_add);
        mAddImageView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddToDoActivity.class);
                intent.putExtra("Action", Constant.REQUEST_CODE_ADD_EVENT);
                startActivityForResult(intent, Constant.REQUEST_CODE_ADD_EVENT);
                getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            }
        });
        mAddImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Tool.showBannerToast(getActivity(), getString(R.string.help_banner_add_todo));
                return true;
            }
        });

        updateToDoList(Calendar.getInstance());

        if (!isInit) {
            initView();
        }

        isInit = true;
        return main;
    }

    /**
     * 初始化界面
     */
    private void initView() {
        mPreButton.setVisibility(View.VISIBLE);
        mNextButton.setVisibility(View.VISIBLE);
        mMonthNameTextView.setText(Tool.getYearMonth(mCalendarViewPager.getCurDate()));
        monthTitleLayout.removeAllViews();
        for (int j = 0; j < CalendarViewPager.DAY_NUM_OF_WEEK; j++) {
            TextView tv = new TextView(getActivity());
            tv.setTextSize(20);
            tv.setGravity(Gravity.CENTER);
            tv.setTextColor(getActivity().getResources().getColor(R.color.card_text));
            tv.setTextSize(16);
            tv.setText(Tool.getWeekDayName(j));
            LinearLayout.LayoutParams inner_params = new LinearLayout.LayoutParams(
                    Tool.getScreenW() / 8, LayoutParams.WRAP_CONTENT);
            monthTitleLayout.addView(tv, inner_params);
        }

        mPreButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mCalendarViewPager.setCurrentItem(mCalendarViewPager.getCurrentItem() - 1);
            }
        });

        mNextButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mCalendarViewPager.setCurrentItem(mCalendarViewPager.getCurrentItem() + 1);
            }
        });

        mOnPageSelectedListener = new OnPageSelectedListener() {

            @Override
            public void onPageSelected(int position) {
                mMonthNameTextView.setText(Tool.getYearMonth(mCalendarViewPager.getCurDate()));
                if (mCalendarViewPager.isFirstMonth()) {
                    mPreButton.setVisibility(View.INVISIBLE);
                } else {
                    mPreButton.setVisibility(View.VISIBLE);
                }

                if (mCalendarViewPager.isLastMonth()) {
                    mNextButton.setVisibility(View.INVISIBLE);
                } else {
                    mNextButton.setVisibility(View.VISIBLE);
                }

            }
        };
        mCalendarViewPager.setOnPageSelectedListener(mOnPageSelectedListener);
        mCalendarViewPager.setOnDateClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar cal = (Calendar) v.getTag();
                mCalendarViewPager.setSelectedCalendar(cal);
                updateToDoList(cal);
            }
        });
    }

    /**
     * 根据选中的日期更新事件列表
     *
     * @param cal 选中的日期实例
     */
    private void updateToDoList(Calendar cal) {
        try {
            ArrayList<Event> events = mDB.getEventByDate(cal.getTime(), 0);
            /*if (events.size() == 0) {
                mCardView.addCard(new NothingToDoCard());
            }
            for (Event event : events) {
                ToDoItemCard card = new ToDoItemCard(event, true);

                mCardView.addCard(card);
            }
            mCardView.refresh(); */
            mAdapter.setData(events);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            updateToDoList(mCalendarViewPager.getSelectedCalendar());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        initView();
        super.onResume();
    }
}