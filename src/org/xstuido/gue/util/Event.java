package org.xstuido.gue.util;

public class Event {
	public static final int NOT_IMPLEMENT = -1;

	private int mIsSignIn;
	private int mIsDone;
	private long mTime;
	private String mContent;
	private long mId;

	public Event(int isSignin, int isDone, long time, String content) {
		mIsSignIn = isSignin;
		mIsDone = isDone;
		mTime = time;
		mContent = content;
	}

	public int isSignIn() {
		return mIsSignIn;
	}

	public void setIsSignIn(int mIsSignIn) {
		this.mIsSignIn = mIsSignIn;
	}

	public int isDone() {
		return mIsDone;
	}

	public void setIsDone(int isDone) {
		this.mIsDone = isDone;
	}

	public long getTime() {
		return mTime;
	}

	public void setTime(long mTime) {
		this.mTime = mTime;
	}

	public String getContent() {
		return mContent;
	}

	public void setContent(String mContent) {
		this.mContent = mContent;
	}

	public long getId() {
		return mId;
	}

	public void setId(long mId) {
		this.mId = mId;
	}
}
