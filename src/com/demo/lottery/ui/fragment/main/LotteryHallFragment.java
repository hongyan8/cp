package com.demo.lottery.ui.fragment.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;

import com.demo.lottery.R;
import com.demo.lottery.constant.Constant;
import com.demo.lottery.global.SharedPreferencesConfig;
import com.demo.lottery.ui.activity.cp.dlt.DLTMainActivity;
import com.demo.lottery.ui.activity.cp.qxc.QXCMainActivity;
import com.demo.lottery.ui.activity.login.LoginActivity;
import com.demo.lottery.ui.adapter.MainGridAdapter;
import com.demo.lottery.ui.fragment.BaseFragment;
import com.demo.lottery.ui.listener.OnClickAvoidForceListener;
import com.demo.lottery.ui.listener.OnItemClickAvoidForceListener;
import com.demo.lottery.ui.views.MyGridView;
import com.demo.lottery.utils.StringUtil;

public class LotteryHallFragment extends BaseFragment
{
	private MyGridView gridView;

	private List<Map<String, Object>> mapList;

	private MainGridAdapter adapter;

	private Button loginBtn;

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
		((ViewGroup) getView()).removeAllViews();
		((ViewGroup) getView()).addView(inflater.inflate(R.layout.fragment_main_lotteryhall, null));
		((LinearLayout) getActivity().findViewById(R.id.windowtitle)).setVisibility(View.GONE);

		loginBtn = (Button) getActivity().findViewById(R.id.btn_login);
		gridView = (MyGridView) getActivity().findViewById(R.id.gv_main);

		mapList = new ArrayList<Map<String, Object>>();
		adapter = new MainGridAdapter(getActivity());

		initData();
		initView();
		setViewClick();
		return null;
	}

	private void initData()
	{

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ID", "1");
		map.put("TITLE", "大乐透");
		map.put("TOTAL", "13亿5千6万");
		map.put("DATE", "第xxx期");
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("ID", "2");
		map1.put("TITLE", "七星彩");
		map1.put("TOTAL", "3亿8千4万");
		map1.put("DATE", "第xxx期");
		mapList.add(map);
		mapList.add(map1);
	}

	private void initView()
	{
		gridView.setNumColumns(2);
		adapter.setDataList(mapList);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(onItemClickAvoidForceListener);
		adapter.notifyDataSetChanged();
	}

	private void setViewClick()
	{
		loginBtn.setOnClickListener(onClickAvoidForceListener);
	}

	private OnClickAvoidForceListener onClickAvoidForceListener = new OnClickAvoidForceListener()
	{

		@Override
		public void onClickAvoidForce(View v)
		{
			switch (v.getId())
			{
				case R.id.btn_login:
					if (StringUtil.isBlank(SharedPreferencesConfig.config(getActivity()).get(Constant.USER_ID)))
					{
						Intent intent = new Intent();
						intent.setClass(getActivity(), LoginActivity.class);
						getActivity().startActivity(intent);
					}
					else
					{
						new AlertDialog.Builder(getActivity()).setMessage(R.string.confirm_logout)
								.setNegativeButton(R.string.cancel, new OnClickListener()
								{

									@Override
									public void onClick(DialogInterface dialog, int which)
									{

										dialog.cancel();
									}
								}).setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener()
								{
									public void onClick(DialogInterface dialog, int which)
									{
										SharedPreferencesConfig.saveConfig(getActivity(), Constant.USER_ID, "");
										dialog.cancel();
									}
								}).show();
					}
					break;

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
			Intent intent = new Intent();
			String id = mapList.get(arg2).get("ID").toString();

			if ("1".equals(id))
				intent.setClass(getActivity(), DLTMainActivity.class);
			else if ("2".equals(id))
			{
				intent.setClass(getActivity(), QXCMainActivity.class);
			}
			getActivity().startActivity(intent);

		}
	};

	@Override
	public void onHiddenChanged(boolean hidden)
	{
		if (!hidden)
		{
			((LinearLayout) getActivity().findViewById(R.id.windowtitle)).setVisibility(View.GONE);
		}
		super.onHiddenChanged(hidden);
	}
}
