package com.demo.lottery.ui.listener;

import com.demo.lottery.utils.ClickEffectUtil;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

 
public abstract class OnItemClickAvoidForceListener implements OnItemClickListener
{
	public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3)
	{
		ClickEffectUtil util = ClickEffectUtil.getInstance();
	 
		if (util.isSmoothClick())
		{
		 
			util.playAnimation(view);
			onItemClickAvoidForce(arg0, view, arg2, arg3);
		}
		else
		{
			ClickEffectUtil.getInstance().slowDownDialog(view.getContext());
		}
	}

	public abstract void onItemClickAvoidForce(AdapterView<?> arg0, View arg1, int arg2, long arg3);

}
