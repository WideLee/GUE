package org.xstuido.gue.view.dialog;

import org.xstuido.gue.R;
import org.xstuido.gue.util.Tool;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;

/**
 * 封装Thread和Handler，用户可以方便的调用进度对话框，<b>并异步执行代码</b>，在代码执行完成后，进行主线程UI的更新。 该类需配合
 * {@link LoadingDialogExecute}代理类使用。
 * 
 * @see LoadingDialogExecute
 * @author 优化设计
 * @version 0.2
 */
public class LoadingDialog {

	private ProgressDialog mProgressDialog;
	private Context mContext;
	private boolean isShowDialog = true;
	private OnDismissListener mOnDismissListener;
	private OnCancelListener mOnCancelListener;

	/**
	 * 进度对话框构造函数
	 * 
	 * @param context
	 *            Context
	 */
	public LoadingDialog(Context context) {
		this.mContext = context;
		initProgressDialog();
	}

	private void initProgressDialog() {
		mProgressDialog = new ProgressDialog(mContext, ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setMessage(Tool.getString(R.string.loading));
		mProgressDialog.setCanceledOnTouchOutside(false);

		mProgressDialog.setOnDismissListener(mOnDismissListener);
		mProgressDialog.setOnCancelListener(mOnCancelListener);
	}

	public void setOnDismissListener(OnDismissListener mOnDismissListener) {
		this.mOnDismissListener = mOnDismissListener;
	}

	public void setOnCancelListener(OnCancelListener mOnCancelListener) {
		this.mOnCancelListener = mOnCancelListener;
	}

	public boolean getIsShowDialog() {
		return isShowDialog;
	}

	/**
	 * 设置是否显示进度对话框，为false时，进度对话框不显示，但异步代码依然正常执行
	 * 
	 * @param isShowDialog
	 */
	public ProgressDialog setIsShowDialog(boolean isShowDialog) {
		this.isShowDialog = isShowDialog;
		return getProgressDialog();
	}

	/**
	 * 获取进度对话框
	 * 
	 * @return 进度对话框
	 */
	public ProgressDialog getProgressDialog() {
		return mProgressDialog;
	}

	/**
	 * 获取Context
	 * 
	 * @return Context
	 */
	public Context getContext() {
		return mContext;
	}

	/**
	 * 设置Context
	 * 
	 * @param _c
	 */
	public void setContext(Context context) {
		this.mContext = context;
	}

	public void show() {
		if (mProgressDialog == null) {
			initProgressDialog();
		}
		mProgressDialog.show();
	}

	public void dismiss() {
		mProgressDialog.dismiss();
	}

}
