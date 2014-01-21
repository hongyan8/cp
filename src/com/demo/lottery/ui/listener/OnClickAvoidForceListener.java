package com.demo.lottery.ui.listener;

import com.demo.lottery.utils.ClickEffectUtil;

import android.view.View;
import android.view.View.OnClickListener;

 

public abstract class OnClickAvoidForceListener implements OnClickListener
{

	public void onClick(View v)
	{
		ClickEffectUtil util = ClickEffectUtil.getInstance(); 
		if (util.isSmoothClick())
		{
		 
			util.playAnimation(v);
			onClickAvoidForce(v);
		}
		else
		{
			ClickEffectUtil.getInstance().slowDownDialog(v.getContext());
		}
	}

	public abstract void onClickAvoidForce(View v);
}
