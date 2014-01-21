package com.demo.lottery.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

/**
 * 简单的异步处理类
 * 
 * @author liqing
 * @data 2013-5-3
 */
public abstract class AsyncTaskUtil {

	/**
	 * 后台数据处理方法，耗时间的操作实现该方法去处理
	 * 
	 * @param params
	 * @return
	 */
	protected abstract Object backDataProcess(Object... params);

	/**
	 * 数据返回后的处理，可以操作UI和初始化全局数据
	 * 
	 * @param result
	 * @return
	 */
	protected abstract Object backResult(Object result);

	private Context context;
	private String proString;
	private boolean isShowdialog;

    private boolean isCanCancel;

	private Object resultObject;

	private ProgressDialog myProgressDialog;

	/**
	 * 
	 * @param context
	 *            对话框所依附的上下文
	 * @param proString
	 *            进度条显示文字
	 * @param isShowDialog
	 *            是否显示进度条
	 * @param para
	 *            数据返回的回调处理
	 */
	public AsyncTaskUtil(Context context, String proString,
			boolean isShowDialog, boolean isCanCancel,Object para) {

		this.context = context;
		this.proString = proString;
		this.isShowdialog = isShowDialog;
        this.isCanCancel = isCanCancel;
		CurrentAsyncTask task = new CurrentAsyncTask();
		task.execute(para);
	}

    public AsyncTaskUtil(Context context, String proString,
                         boolean isShowDialog,Object para) {

        this.context = context;
        this.proString = proString;
        this.isShowdialog = isShowDialog;
        this.isCanCancel = true;
        CurrentAsyncTask task = new CurrentAsyncTask();
        task.execute(para);
    }

	protected class CurrentAsyncTask extends AsyncTask<Object, Object, Object> {

		@Override
		protected void onPreExecute() {
			showProgress();
		}

		@Override
		protected Object doInBackground(Object... params) {
            Object obj =null;

            try{
               obj =  backDataProcess(params);
            }catch (Exception e){
                e.printStackTrace();
            }
		    return obj;
		}

		@Override
		protected void onPostExecute(Object result) {
			closeProgress();
			resultObject = result;
			backResult(result);
			super.onPostExecute(result);
		}

	}

	/**
	 * 暂时不做对话框的可取消性，不需要阻碍UI则设置isShowdialog为false，否则视为阻碍UI线程 必须等后台操作执行完之后才释放UI
	 */
	public void showProgress() {
		if (myProgressDialog == null) {
			myProgressDialog = new ProgressDialog(context);
			myProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			myProgressDialog.setMessage(proString);
			myProgressDialog.setCancelable(isCanCancel);
			myProgressDialog.setCanceledOnTouchOutside(false);
		}

		if (!myProgressDialog.isShowing() && isShowdialog) {
			myProgressDialog.show();
		}

	}

	/**
	 * dialog的消息由于异步线程执行返回后，UI可能由于其他异常导致已经销毁，此时dialog的dismss会出现
	 * attach的归属错误，对其进行异常捕捉。
	 */
	public void closeProgress() {
		try {
			if (myProgressDialog != null && myProgressDialog.isShowing()) {
				myProgressDialog.dismiss();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 获取后台数据的处理结果
	 * 
	 * @return
	 */
	public Object getProcessResult() {
		return resultObject;
	}

}