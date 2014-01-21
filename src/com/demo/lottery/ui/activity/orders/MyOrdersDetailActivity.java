package com.demo.lottery.ui.activity.orders;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.demo.lottery.R;
import com.demo.lottery.ui.listener.OnClickAvoidForceListener;

public class MyOrdersDetailActivity extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.layout_order_detail);

		findViews();
		initData();
		initView();
		setViewClick();
	}

	private void findViews()
	{
		((ImageView) findViewById(R.id.img_return)).setVisibility(View.GONE);
		((TextView) findViewById(R.id.formTilte)).setText(getResources().getString(R.string.order_detail));

	}

	private void initData()
	{
		
	}

	private void initView()
	{

	}

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

	@Override
	protected void onDestroy()
	{
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
	}

}
