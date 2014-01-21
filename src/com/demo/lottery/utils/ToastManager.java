package com.demo.lottery.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.demo.lottery.R;

/**
 * Title:ToastManager.class
 * Description: 弹出信息框
 * Copyright:Copyright (c) 2012
 * Company:湖南科创
 * 
 * @author wanru.chen
 * @version 1.0
 * @date 2012-7-17
 */
public class ToastManager
{
	/**
	 * 单实例
	 */
	private static ToastManager instance = null;

	/**
	 * 显示信息集合
	 */
	private List<String> messages = null;

	private Context context = null;

	private Toast toast = null;

	/**
	 * 刷新�除信息
	 */
	private Handler refreshHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			if (msg.what == 0)
			{
				messages.remove(msg.obj);

				showMessage();
			}
		}
	};

	private ToastManager()
	{
		messages = new ArrayList<String>();
	}

	public static ToastManager getInstance()
	{
		if (null == instance)
		{
			instance = new ToastManager();
		}

		return instance;
	}

	/**
	 * 显示信息(带参数)
	 * 
	 * @param message
	 * @param context
	 */
	public void showMessage(String message, Context context)
	{
		if (null == message || null == context || messages.contains(message))
		{
			return;
		}

		messages.add(message);

		this.context = context;

		showMessage();

		Message mes = new Message();
		mes.what = 0;
		mes.obj = message;

		refreshHandler.sendMessageDelayed(mes, 2000);

	}

	/**
	 * 显示信息
	 */
	private void showMessage()
	{
		if (messages.size() == 0)
		{
			toast.cancel();
			return;
		}

		LayoutInflater inflater = LayoutInflater.from(context);

		View view = inflater.inflate(R.layout.toast_list, null);

		ListView toastLstView = (ListView) view.findViewById(R.id.list_toast);

		ListAdapter adapter = new ArrayAdapter<String>(context, R.layout.toast_list_item, messages);

		toastLstView.setAdapter(adapter);

		toastLstView.setVisibility(View.VISIBLE);

		if (null == toast)
		{
			toast = new Toast(context);
			toast.setDuration(Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER, 0, 100);
		}

		toast.setView(view);

		toast.show();

	}
}
