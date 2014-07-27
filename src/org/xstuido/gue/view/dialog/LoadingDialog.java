package org.xstuido.gue.view.dialog;

import org.xstuido.gue.R;
import org.xstuido.gue.util.Tool;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;

/**
 * ��װThread��Handler���û����Է���ĵ��ý��ȶԻ���<b>���첽ִ�д���</b>���ڴ���ִ����ɺ󣬽������߳�UI�ĸ��¡� ���������
 * {@link LoadingDialogExecute}������ʹ�á�
 * 
 * @see LoadingDialogExecute
 * @author �Ż����
 * @version 0.2
 */
public class LoadingDialog {

	private ProgressDialog mProgressDialog;
	private Context mContext;
	private boolean isShowDialog = true;
	private OnDismissListener mOnDismissListener;
	private OnCancelListener mOnCancelListener;

	/**
	 * ���ȶԻ����캯��
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
	 * �����Ƿ���ʾ���ȶԻ���Ϊfalseʱ�����ȶԻ�����ʾ�����첽������Ȼ����ִ��
	 * 
	 * @param isShowDialog
	 */
	public ProgressDialog setIsShowDialog(boolean isShowDialog) {
		this.isShowDialog = isShowDialog;
		return getProgressDialog();
	}

	/**
	 * ��ȡ���ȶԻ���
	 * 
	 * @return ���ȶԻ���
	 */
	public ProgressDialog getProgressDialog() {
		return mProgressDialog;
	}

	/**
	 * ��ȡContext
	 * 
	 * @return Context
	 */
	public Context getContext() {
		return mContext;
	}

	/**
	 * ����Context
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
