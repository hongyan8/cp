package com.demo.lottery.ui.fragment.main;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demo.lottery.R;
import com.demo.lottery.ui.fragment.BaseFragment;
import com.demo.lottery.ui.listener.OnClickAvoidForceListener;

public class SettingFragment extends BaseFragment
{

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
		((ViewGroup) getView()).addView(inflater.inflate(R.layout.fragment_main_setting, null));
		((TextView) getActivity().findViewById(R.id.formTilte)).setText("设置");
		((LinearLayout) getActivity().findViewById(R.id.windowtitle)).setVisibility(View.VISIBLE);
		// TODO
		// .setOnClickListener(onClickAvoidForceListener);
		return null;
	}

	private OnClickAvoidForceListener onClickAvoidForceListener = new OnClickAvoidForceListener()
	{

		@Override
		public void onClickAvoidForce(View v)
		{
		}
	};

	@Override
	public void onHiddenChanged(boolean hidden)
	{
		if (!hidden)
		{
			((LinearLayout) getActivity().findViewById(R.id.windowtitle)).setVisibility(View.VISIBLE);
			((TextView) getActivity().findViewById(R.id.formTilte)).setText("设置");
		}
		super.onHiddenChanged(hidden);
	}
}
