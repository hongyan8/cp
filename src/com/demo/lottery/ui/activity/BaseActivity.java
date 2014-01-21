package com.demo.lottery.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;

import com.demo.lottery.R;

public abstract class BaseActivity extends Activity
{
	/**
	 * BaseActivity
	 */
	private static BaseActivity lastActivity = null;

	private boolean isBaseOnCreate = true;

	/**
	 * 是否在OnCreate处理后关闭等待框
	 */
	private boolean isOnCreateDismiss = true;

	/**
	 * 加载等待圈
	 */
	private ProgressDialog myProgressDialog;

	/**
	 * 显示等待框
	 */
	private static final int SHOW_PROGRESS_DIALOG = 10;

	/**
	 * 关闭等待框
	 */
	private static final int DISMISS_PROGRESS = 11;

	private static final int MSG_INIT = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		BaseActivity.setLastActivity(this);
		// 去掉顶上的标题框
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		onCreateFindView(savedInstanceState);
		onCreateAddListener(savedInstanceState);
	}

	/**
	 * 初始化界面 （加载控件View对象）
	 * 
	 * @param savedInstanceState
	 */
	protected abstract void onCreateFindView(Bundle savedInstanceState);

	/**
	 * 加载操作事件（View控件的点击事件响应）
	 * 
	 * @param savedInstanceState
	 */
	protected abstract void onCreateAddListener(Bundle savedInstanceState);

	/**
	 * 线程加载数据（用于后台处理耗时操作，主要用于数据库读取或发送请求操作，该方法中不能有UI操作，有UI操作会有不可预料的异常）
	 */
	protected abstract void onCreateTaskLoadData();

	/**
	 * 线程加载View（后台数据加载完后，用于界面View控件刷新操作，不能有后台耗时处理，会堵塞主线程）
	 */
	protected abstract void onCreateTaskAddView();

	@Override
	protected void onResume()
	{
		super.onResume();
		// onResume更新当前活动的Activity对象
		BaseActivity.setLastActivity(this);

		if (isBaseOnCreate)
		{
			isBaseOnCreate = false;
			handler.sendEmptyMessageDelayed(MSG_INIT, 300);
		}
		else
		{
			dismissProgress();
			onBaseResume();
		}

	}

	/**
	 * onCreate进入时不调用 用于onResume()处理
	 */
	protected void onBaseResume()
	{

	}

	/**
	 * 用于设置静态变量，修改findbugs的错误
	 * 
	 * @param activity
	 *            当前的activity对象
	 */
	public static void setLastActivity(BaseActivity activity)
	{
		lastActivity = activity;
	}

	public static BaseActivity getLastActivity()
	{
		return lastActivity;
	}

	private Handler progressHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{

			// 切换横竖屏会异常
			try
			{
				if (msg != null)
				{
					int what = msg.what;

					if (what == SHOW_PROGRESS_DIALOG)
					{
						if (myProgressDialog == null)
						{
							myProgressDialog = new ProgressDialog(BaseActivity.this);
							myProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
							myProgressDialog.setMessage(getResources().getString(R.string.dealing));
							myProgressDialog.setCancelable(true);
							myProgressDialog.setCanceledOnTouchOutside(false);
						}

						if (!myProgressDialog.isShowing())
						{
							myProgressDialog.show();
						}
					}
					else if (what == DISMISS_PROGRESS && null != myProgressDialog && myProgressDialog.isShowing())
					{
						myProgressDialog.dismiss();
					}

				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

		}
	};

	/**
	 * 等待圈显示
	 */
	public void showProgress()
	{
		progressHandler.sendEmptyMessage(SHOW_PROGRESS_DIALOG);
		// 10秒后自动关闭等待框
		progressHandler.sendEmptyMessageDelayed(DISMISS_PROGRESS, 10 * 1000);
	}

	/**
	 * 等待圈显示
	 * 
	 * @param waittime
	 *            等待时间
	 */
	public void showProgress(long waittime)
	{
		progressHandler.sendEmptyMessage(SHOW_PROGRESS_DIALOG);
		// waittime后自动关闭等待框
		progressHandler.sendEmptyMessageDelayed(DISMISS_PROGRESS, waittime);
	}

	/**
	 * 等待圈消失
	 */
	public void dismissProgress()
	{
		progressHandler.sendEmptyMessage(DISMISS_PROGRESS);
	}

	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
				case MSG_INIT:
					PageTask task = new PageTask(BaseActivity.this);
					task.execute();
					break;
				default:
					super.handleMessage(msg);
					break;
			}
		}
	};

	protected class PageTask extends AsyncTask<String, Integer, String>
	{

		public PageTask(Context context)
		{
		}

		/**
		 * 该方法将在执行实际的后台操作前被UI thread调用。可以在该方法中做一些准备工作
		 */
		@Override
		protected void onPreExecute()
		{
			// 任务启动，可以在这里显示一个对话框，这里简单处理
		}

		/**
		 * 将在onPreExecute 方法执行后马上执行，该方法运行在后台线程中。这里将主要负责执行那些很耗时的后台计算工作。
		 * 
		 * @param params
		 * @return
		 */
		@Override
		protected String doInBackground(String... params)
		{
			onCreateTaskLoadData();
			return null;
		}

		/**
		 * 在doInBackground 执行完成后，onPostExecute 方法将被UI
		 * thread调用，后台的计算结果将通过该方法传递到UI thread
		 * 
		 * @param result
		 */
		@Override
		protected void onPostExecute(String result)
		{
			onCreateTaskAddView();

			if (isOnCreateDismiss)
			{
				dismissProgress();
			}

		}

	}
}
