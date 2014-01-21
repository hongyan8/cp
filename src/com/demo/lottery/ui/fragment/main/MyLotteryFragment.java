package com.demo.lottery.ui.fragment.main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demo.lottery.R;
import com.demo.lottery.constant.CommandConstants;
import com.demo.lottery.constant.Constant;
import com.demo.lottery.global.SharedPreferencesConfig;
import com.demo.lottery.services.HttpPostAsync;
import com.demo.lottery.ui.activity.orders.MyOrdersDetailActivity;
import com.demo.lottery.ui.adapter.MyLotteryListAdapter;
import com.demo.lottery.ui.fragment.BaseFragment;
import com.demo.lottery.ui.listener.OnClickAvoidForceListener;
import com.demo.lottery.ui.listener.OnItemClickAvoidForceListener;
import com.demo.lottery.ui.views.MyListView;
import com.demo.lottery.utils.JsonUtil;
import com.demo.lottery.utils.ProgressUtil;
import com.demo.lottery.utils.StringUtil;
import com.demo.lottery.utils.ToastManager;

public class MyLotteryFragment extends BaseFragment
{
	private MyListView listView;

	private List<Map<String, Object>> mapList;

	private MyLotteryListAdapter adapter;

	private Map<String, String> configMap;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		// TODO 可以重写该方法对数据加载时dialog的一些属性进行设置
		super.onCreate(savedInstanceState);
	}

	@Override
	protected Object onCreateTaskLoadData(Object... params)
	{
		// TODO 在这里做相关的数据耗时操作
		if (StringUtil.isBlank(configMap.get(Constant.USER_ID)))
		{
			ToastManager.getInstance().showMessage("请先登录!", getActivity());
		}
		else
		{
			initData();
		}
		return null;
	}

	@Override
	protected Object onEndTaskAddView(Object result)
	{
		// TODO 界面和数据的适配操作

		if (getView() == null)
		{
			return null;
		}
		// configMap = SharedPreferencesConfig.config(getActivity());
		// ((ViewGroup) getView()).removeAllViews();
		// ((ViewGroup) getView()).addView(inflater.inflate(R.layout.fragment_main_myorders, null));
		// ((TextView) getActivity().findViewById(R.id.formTilte)).setText("我的订单");
		// ((LinearLayout) getActivity().findViewById(R.id.windowtitle)).setVisibility(View.VISIBLE);
		//
		// listView = (MyListView) getActivity().findViewById(R.id.list_orders);
		// adapter = new MyLotteryListAdapter(getActivity());
		//
		// if (StringUtil.isBlank(configMap.get(Constant.USER_ID)))
		// {
		// ToastManager.getInstance().showMessage("请先登录!", getActivity());
		// }
		// else
		// {
		// ProgressUtil.getInstance(getActivity()).showProgress(2 * 60 * 1000);
		// initData();
		// setViewClick();
		// }
		
		configMap = SharedPreferencesConfig.config(getActivity());
		((ViewGroup) getView()).removeAllViews();
		((ViewGroup) getView()).addView(inflater.inflate(R.layout.fragment_main_myorders, null));
		((TextView) getActivity().findViewById(R.id.formTilte)).setText("我的订单");
		((LinearLayout) getActivity().findViewById(R.id.windowtitle)).setVisibility(View.VISIBLE);

		listView = (MyListView) getActivity().findViewById(R.id.list_orders);
		adapter = new MyLotteryListAdapter(getActivity());

		setViewClick();

		adapter.setDataList(mapList);
		mHandler.sendEmptyMessage(1);

		return null;
	}

	private void initData()
	{
		Map<String, Object> paras = new HashMap<String, Object>();
		paras.put("command_id", CommandConstants.GETCONTENTLIST);
		paras.put("contentType", CommandConstants.ORDER);
		paras.put("user_id", configMap.get(Constant.USER_ID));

		Map<String, Object> parasTemp = new HashMap<String, Object>();
		String json = JsonUtil.encodeCmd(paras);
		parasTemp.put("string", json);

		new HttpPostAsync(getActivity())
		{
			@SuppressWarnings("unchecked")
			@Override
			public Object backResult(Object result)
			{// 请求回调
				Log.i("json", result.toString());

				if (result == null || "".equals(result.toString()))
				{
					ProgressUtil.getInstance(getActivity()).dismissProgress();
					ToastManager.getInstance().showMessage("服务器异常，请联系管理员!", getActivity());
				}
				else if (Constant.HTTP_REQUEST_FAIL.equals(result.toString().trim()))
				{
					ProgressUtil.getInstance(getActivity()).dismissProgress();
					ToastManager.getInstance().showMessage("连接不上服务器", getActivity());
				}
				else
				{
					Map<String, Object> mapstr = JsonUtil.getMapString(result.toString());
					Map<String, Object> headMap = JsonUtil.getMapString(mapstr.get("head").toString());
					Map<String, Object> dataMap = JsonUtil.getMapString(mapstr.get("data").toString());
					boolean isSuccess = false;
					if ("true".equals(headMap.get("success")))
						isSuccess = true;
					String desc = (String) headMap.get("desc");
					if (!isSuccess && !StringUtil.isBlank(desc))
					{
						ProgressUtil.getInstance(getActivity()).dismissProgress();
						ToastManager.getInstance().showMessage(desc, getActivity());
					}
					else
					{
						ProgressUtil.getInstance(getActivity()).dismissProgress();
						// Map<String, Object> map = JsonUtil.getMapString(dataMap.get("dataList").toString());
						mapList = (List<Map<String, Object>>) JsonUtil.getList(dataMap.get("dataList").toString());
						// loadSuccessDeal(mapList);// 成功登录后处理
					}
				}
				return "";
			}
		}.execute(Constant.POST_KEYVALUE_DATA, configMap.get(Constant.MOBILE_POST_URL), parasTemp);

	}

	// @SuppressWarnings("unchecked")
	// private void loadSuccessDeal(List<Map<String, Object>> list)
	// {
	// new LoadDealTask().execute(list);
	// }
	//
	// class LoadDealTask extends AsyncTask<List<Map<String, Object>>, Integer, Void>
	// {
	//
	// @Override
	// protected void onPreExecute()
	// {
	// showProgress();
	// }
	//
	// @Override
	// protected void onProgressUpdate(Integer... progress)
	// {
	//
	// Log.d("", "probar =" + progress[0]);
	// myProgressDialog.setProgress(progress[0]);
	// }
	//
	// @Override
	// protected Void doInBackground(List<Map<String, Object>>... list)
	// {
	// try
	// {
	// adapter.setDataList(list[0]);
	// mHandler.sendEmptyMessage(1);
	// }
	// catch (Exception e)
	// {
	// e.printStackTrace();
	// }
	// return null;
	// }
	//
	// @Override
	// protected void onPostExecute(Void result)
	// {
	// if (myProgressDialog != null && myProgressDialog.isShowing())
	// try
	// {
	// myProgressDialog.dismiss();
	// }
	// catch (Exception e)
	// {
	// Log.e("", "dialog err ,but it's dosen't matter!");
	// e.printStackTrace();
	// }
	//
	// }
	//
	// }

	private void setViewClick()
	{
	}

	private OnClickAvoidForceListener onClickAvoidForceListener = new OnClickAvoidForceListener()
	{

		@Override
		public void onClickAvoidForce(View v)
		{
			switch (v.getId())
			{

				default:
					break;
			}
		}
	};

	private OnItemClickAvoidForceListener onItemClickAvoidForceListener = new OnItemClickAvoidForceListener()
	{

		@Override
		public void onItemClickAvoidForce(AdapterView<?> arg0, View arg1, int arg2, long arg3)
		{
			// Intent intent = new Intent();
			// String id = mapList.get(arg2).get("ID").toString();
			//
			// if ("1".equals(id))
			// intent.setClass(getActivity(), DLTMainActivity.class);
			// else if ("2".equals(id))
			// {
			// intent.setClass(getActivity(), QXCMainActivity.class);
			// }
			// getActivity().startActivity(intent);
			Intent intent = new Intent();
			intent.setClass(getActivity(), MyOrdersDetailActivity.class);
			startActivity(intent);
		}
	};

	private Handler mHandler = new Handler()
	{

		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
				case 1:
					listView.setAdapter(adapter);
					listView.setOnItemClickListener(onItemClickAvoidForceListener);
					adapter.notifyDataSetChanged();
					break;
				default:
					break;
			}
		}
	};

	@Override
	public void onHiddenChanged(boolean hidden)
	{
		if (!hidden)
		{
			((TextView) getActivity().findViewById(R.id.formTilte)).setText("我的订单");
			((LinearLayout) getActivity().findViewById(R.id.windowtitle)).setVisibility(View.VISIBLE);
			listView = (MyListView) getActivity().findViewById(R.id.list_orders);
		}
		super.onHiddenChanged(hidden);
	}
}
