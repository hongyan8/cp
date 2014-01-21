package com.demo.lottery.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.demo.lottery.R;
import com.demo.lottery.ui.activity.login.LoginActivity;

public class ProgressUtil
{

	/**
	 * 单实例
	 */
	private static ProgressUtil instance = null;

	private Context context = null;

	/**
	 * 显示等待框
	 */
	private static final int SHOW_PROGRESS_DIALOG = 10;

	/**
	 * 关闭等待框
	 */
	private static final int DISMISS_PROGRESS = 11;

	/**
	 * 加载等待圈
	 */
	private ProgressDialog myProgressDialog;

	public ProgressUtil(Context context)
	{
		this.context = context;
	}

	public static ProgressUtil getInstance(Context context)
	{
		if (null == instance)
		{
			instance = new ProgressUtil(context);
		}

		return instance;
	}

	public Handler progressHandler = new Handler()
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
							myProgressDialog = new ProgressDialog(context);
							myProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
							myProgressDialog.setMessage(context.getResources().getString(R.string.dealing));
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

	public void showProgressBar(String str)
	{
		if (myProgressDialog == null)
		{
			myProgressDialog = new ProgressDialog(context);
			myProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			myProgressDialog.setMax(100);
			myProgressDialog.setMessage(str);
			myProgressDialog.setCancelable(true);
			myProgressDialog.setCanceledOnTouchOutside(false);
		}

		if (!myProgressDialog.isShowing())
		{
			myProgressDialog.show();
		}

	}
}
