package com.demo.lottery.ui.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.demo.lottery.R;
import com.demo.lottery.constant.Constant;
import com.demo.lottery.global.SharedPreferencesConfig;
import com.demo.lottery.ui.adapter.TabManager;
import com.demo.lottery.ui.fragment.main.LotteryHallFragment;
import com.demo.lottery.ui.fragment.main.MyLotteryFragment;
import com.demo.lottery.ui.fragment.main.SettingFragment;
import com.demo.lottery.ui.listener.OnClickAvoidForceListener;
import com.demo.lottery.utils.ToastManager;

public class MainActivity extends FragmentActivity
{
	private TabHost tabHost;

	private TabManager tabManager;

	private Bundle bundle;

	private LinearLayout backBtn;

	private boolean isExit;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		ToastManager.getInstance()
				.showMessage(SharedPreferencesConfig.config(MainActivity.this).get(Constant.USER_NAME), MainActivity.this);

		findViews();
		initView();
		setViewClick();
	}

	/**
	 * 获得控件
	 */
	private void findViews()
	{
		backBtn = (LinearLayout) findViewById(R.id.returnBtn);
		backBtn.setVisibility(View.VISIBLE);
		((Button) findViewById(R.id.remarkBtn)).setVisibility(View.GONE);
		tabHost = (TabHost) findViewById(android.R.id.tabhost);
	}

	/**
	 * 初始化控件
	 */
	private void initView()
	{
		tabManager = new TabManager(this, tabHost, R.id.main_content);
		tabHost.setup();

		// 将tab项视图实例化，可以定义后面的布局
		View gcdt = (View) LayoutInflater.from(this).inflate(R.layout.common_detail_tab, null);
		TextView gcdtTV = (TextView) gcdt.findViewById(R.id.detail_tab_tv);
		gcdtTV.setText(getResources().getString(R.string.gcdt));
		gcdtTV.setVisibility(View.GONE);
		ImageView gcdtIV = (ImageView) gcdt.findViewById(R.id.img_icon);
		gcdtIV.setBackgroundResource(R.drawable.tab_main_gcdt_selector);

		View wdcp = (View) LayoutInflater.from(this).inflate(R.layout.common_detail_tab, null);
		TextView wdcpTV = (TextView) wdcp.findViewById(R.id.detail_tab_tv);
		wdcpTV.setText(getResources().getString(R.string.wdcp));
		wdcpTV.setVisibility(View.GONE);
		ImageView wdcpIV = (ImageView) wdcp.findViewById(R.id.img_icon);
		wdcpIV.setBackgroundResource(R.drawable.tab_main_wdcp_selector);

		View kjgg = (View) LayoutInflater.from(this).inflate(R.layout.common_detail_tab, null);
		TextView kjggTV = (TextView) kjgg.findViewById(R.id.detail_tab_tv);
		kjggTV.setText(getResources().getString(R.string.kjgg));
		ImageView kjggIV = (ImageView) kjgg.findViewById(R.id.img_icon);
		kjggIV.setBackgroundResource(R.drawable.tab_meeting_s_selector);

		View setting = (View) LayoutInflater.from(this).inflate(R.layout.common_detail_tab, null);
		TextView settingTV = (TextView) setting.findViewById(R.id.detail_tab_tv);
		settingTV.setText(getResources().getString(R.string.setting));
		ImageView settingIV = (ImageView) setting.findViewById(R.id.img_icon);
		settingIV.setBackgroundResource(R.drawable.tab_meeting_s_selector);

		tabManager.addTab(tabHost.newTabSpec("first").setIndicator(gcdt), new LotteryHallFragment().getClass(), bundle);
		tabManager.addTab(tabHost.newTabSpec("second").setIndicator(wdcp), new MyLotteryFragment().getClass(), bundle);
		tabManager.addTab(tabHost.newTabSpec("third").setIndicator(kjgg), new SettingFragment().getClass(), bundle);
		tabManager.addTab(tabHost.newTabSpec("forth").setIndicator(setting), new SettingFragment().getClass(), bundle);

		// 还原保存的数据,界面返回的是显示跳转之前的tab页
		bundle = getIntent().getExtras();
		if (bundle != null && bundle.getString("tab") != null && !bundle.getString("tab").equals(""))
		{
			// 还原最后一次被选中的标签
			tabHost.setCurrentTabByTag(bundle.getString("tab"));
		}
	}

	/**
	 * 监听
	 */
	private void setViewClick()
	{
		backBtn.setOnClickListener(onClickAvoidForceListener);
	}

	private OnClickAvoidForceListener onClickAvoidForceListener = new OnClickAvoidForceListener()
	{

		@Override
		public void onClickAvoidForce(View v)
		{
			switch (v.getId())
			{
			// case R.id.layout_dlt:
			// Intent intentDlt = new Intent();
			// intentDlt.setClass(MainActivity.this, DLTMainActivity.class);
			// startActivity(intentDlt);
			// break;
			// case R.id.layout_qxc:
			// Intent intentQxc = new Intent();
			// intentQxc.setClass(MainActivity.this, QXCMainActivity.class);
			// startActivity(intentQxc);
			// break;
				default:
					break;
			}

		}
	};

	@Override
	protected void onDestroy()
	{
		if (tabHost != null)
		{
			tabHost.clearAllTabs();
			tabHost = null;
		}
		if (tabManager != null)
		{
			tabManager.clear();
			tabManager = null;
		}
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			back();
			return false;
		}
		else
		{
			return super.onKeyDown(keyCode, event);
		}
	}

	public void back()
	{
		if (!isExit)
		{
			isExit = true;
			ToastManager.getInstance().showMessage("再按一次退出程序", MainActivity.this);
			mHandler.sendEmptyMessageDelayed(1, 2000);
		}
		else
		{
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			startActivity(intent);
			System.exit(0);
		}
	}

	private Handler mHandler = new Handler()
	{

		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
				case 1:
					isExit = false;
					break;
				default:
					break;
			}
		}
	};
}
