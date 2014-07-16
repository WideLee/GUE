package org.xstuido.gue.adapter;

import java.lang.ref.SoftReference;

import org.xstuido.gue.R;
import org.xstuido.gue.util.Tool;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public abstract class ViewPager4SameItem extends RelativeLayout {
    private static final float DEFAULT_SWITCH_BTN_SIZE = 25;

    private ViewPager mViewPager;
    private View mBtnLeft, mBtnRight;

    private OnPageSelectedListener mOnPageSelectedListener;

    public ViewPager4SameItem(Context context) {
	super(context);
	init();
    }

    public ViewPager4SameItem(Context context, AttributeSet attrs) {
	super(context, attrs);
	init();
    }

    public ViewPager4SameItem(Context context, AttributeSet attrs, int defStyle) {
	super(context, attrs, defStyle);
	init();
    }

    private void init() {
	mViewPager = new ViewPager(getContext());
	mViewPager.setAdapter(new HiAdapter());
	mViewPager.setOffscreenPageLimit(1);
	addView(mViewPager, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
	    @Override
	    public void onPageSelected(int arg0) {
		if (isSwitchBtnEnabled()) {
		    if (arg0 == mViewPager.getAdapter().getCount() - 1) {
			mBtnRight.setVisibility(View.GONE);
		    } else {
			mBtnRight.setVisibility(View.VISIBLE);
		    }
		    if (arg0 == 0) {
			mBtnLeft.setVisibility(View.GONE);
		    } else {
			mBtnLeft.setVisibility(View.VISIBLE);
		    }
		}
		if (mOnPageSelectedListener != null) {
		    mOnPageSelectedListener.onPageSelected(arg0);
		}
	    }

	    @Override
	    public void onPageScrolled(int arg0, float arg1, int arg2) {
	    }

	    @Override
	    public void onPageScrollStateChanged(int arg0) {
	    }
	});

	if (isSwitchBtnEnabled()) {
	    mBtnLeft = new View(getContext());
	    LayoutParams mParamsLeft = new LayoutParams(Tool.dip2px(getSwitchBtnSize()),
		    Tool.dip2px(getSwitchBtnSize()));
	    mParamsLeft.setMargins(Tool.dip2px(20), 0, 0, 0);
	    mParamsLeft.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
	    mParamsLeft.addRule(RelativeLayout.CENTER_VERTICAL);
	    mBtnLeft.setBackgroundResource(R.drawable.pager_prev);
	    addView(mBtnLeft, mParamsLeft);

	    mBtnRight = new View(getContext());
	    LayoutParams mParamsRight = new LayoutParams(Tool.dip2px(getSwitchBtnSize()),
		    Tool.dip2px(getSwitchBtnSize()));
	    mParamsRight.setMargins(0, 0, Tool.dip2px(20), 0);
	    mParamsRight.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
	    mParamsRight.addRule(RelativeLayout.CENTER_VERTICAL);
	    mBtnRight.setBackgroundResource(R.drawable.pager_next);
	    addView(mBtnRight, mParamsRight);

	    mBtnLeft.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
		    mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1, true);
		}
	    });
	    mBtnRight.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
		    mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);
		}
	    });

	}
    }

    private final class HiAdapter extends PagerAdapter {
	private SoftReference<View> mViewCacheRef;
	private int mChildCount = 0;

	public HiAdapter() {
	}

	@Override
	public void notifyDataSetChanged() {
	    mChildCount = getCount();
	    super.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
	    return getItemCount();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
	    return arg0 == arg1;
	}

	@Override
	public Object instantiateItem(View container, int position) {
	    if (mViewCacheRef != null) {
		View v = mViewCacheRef.get();
		if (v != null) {
		    configView(v, position);
		    ((ViewGroup) container).addView(v);
		    mViewCacheRef.clear();
		    return v;
		}
	    }
	    View v = createView();
	    configView(v, position);
	    ((ViewPager) container).addView(v);
	    return v;
	}

	@Override
	public void destroyItem(View container, int position, Object object) {
	    mViewCacheRef = new SoftReference<View>((View) object);
	    ((ViewPager) container).removeView((View) object);
	}

	@Override
	public int getItemPosition(Object object) {
	    if (mChildCount > 0) {
		mChildCount--;
		return POSITION_NONE;
	    }
	    return super.getItemPosition(object);
	}
    }

    public void setOnPageSelectedListener(OnPageSelectedListener listener) {
	mOnPageSelectedListener = listener;
    }

    public ViewPager getViewPage() {
	return mViewPager;
    }

    public int getCurrentItem() {
	return mViewPager.getCurrentItem();
    }

    public boolean isSwitchBtnEnabled() {
	return true;
    }

    public void notifyDataSetChanged() {
	mViewPager.getAdapter().notifyDataSetChanged();
    }

    public void setCurrentItem(int position) {
	setCurrentItem(position, true);
    }

    public void setCurrentItem(int position, boolean smoothScroll) {
	mViewPager.setCurrentItem(position, smoothScroll);
	if (isSwitchBtnEnabled()) {
	    if (position == 0) {
		mBtnLeft.setVisibility(View.GONE);
	    }
	    if (position == mViewPager.getAdapter().getCount() - 1) {
		mBtnRight.setVisibility(View.GONE);
	    }
	}
    }

    public float getSwitchBtnSize() {
	return DEFAULT_SWITCH_BTN_SIZE;
    }

    public abstract View createView();

    public abstract int getItemCount();

    public abstract void configView(View view, int position);

    public static interface OnPageSelectedListener {
	public void onPageSelected(int position);
    }
}
