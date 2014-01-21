package com.demo.lottery.ui.activity.cp.dlt;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ScrollView;

import com.demo.lottery.R;
import com.demo.lottery.constant.Constant;
import com.demo.lottery.global.SharedPreferencesConfig;
import com.demo.lottery.ui.activity.cp.CpConfirmPayActivity;
import com.demo.lottery.ui.adapter.NumberAdapter;
import com.demo.lottery.ui.listener.OnClickAvoidForceListener;
import com.demo.lottery.ui.views.MyGridView;
import com.demo.lottery.utils.NumberUtil;
import com.demo.lottery.utils.RandomNumberUtil;
import com.demo.lottery.utils.StringUtil;
import com.demo.lottery.utils.ToastManager;

public class DLTMainActivity extends Activity
{
	private MyGridView gridViewTop;

	private MyGridView gridViewBottom;

	private List<Map<String, Object>> topMapList;

	private List<Map<String, Object>> bottomMapList;

	private NumberAdapter adapterTop;

	private NumberAdapter adapterBottom;

	private Map<String, Object> randomNumer;

	private Button jxBtn;

	private Button tzBtn;

	private Button delBtn;

	private List<Integer> r1 = new ArrayList<Integer>();

	private List<Integer> r2 = new ArrayList<Integer>();

	private String flag;

	private Map<String, String> cpMapFromCpPay;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.layout_dlt_main);
		((ScrollView) findViewById(R.id.scrollView1)).smoothScrollTo(0, 0);

		gridViewTop = (MyGridView) findViewById(R.id.gv1);
		gridViewBottom = (MyGridView) findViewById(R.id.gv2);

		jxBtn = (Button) findViewById(R.id.btn_jx);
		tzBtn = (Button) findViewById(R.id.btn_confirm);
		delBtn = (Button) findViewById(R.id.btn_cancel);

		topMapList = new ArrayList<Map<String, Object>>();
		bottomMapList = new ArrayList<Map<String, Object>>();

		adapterTop = new NumberAdapter(DLTMainActivity.this);
		adapterBottom = new NumberAdapter(DLTMainActivity.this);

		initData();
		initView();
		setViewClick();
	}

	@SuppressWarnings("unchecked")
	private void initData()
	{
		flag = getIntent().getStringExtra("flag");
		cpMapFromCpPay = (Map<String, String>) getIntent().getSerializableExtra("cpMapFromCpPay");

		for (int i = 1; i < 36; i++)
		{
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("no", i);
			map.put("num", NumberUtil.plusZero(i));
			map.put("isChoose", false);
			map.put("type", "red");
			topMapList.add(map);
		}

		for (int i = 1; i < 13; i++)
		{
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("no", i);
			map.put("num", NumberUtil.plusZero(i));
			map.put("isChoose", false);
			map.put("type", "blue");
			bottomMapList.add(map);
		}
	}

	private void initView()
	{
		gridViewTop.setNumColumns(6);
		adapterTop.setDataList(topMapList);
		gridViewTop.setAdapter(adapterTop);

		gridViewBottom.setNumColumns(6);
		adapterBottom.setDataList(bottomMapList);
		gridViewBottom.setAdapter(adapterBottom);

		if (cpMapFromCpPay != null && cpMapFromCpPay.size() > 0)
		{
			String red = cpMapFromCpPay.get("red");
			String blue = cpMapFromCpPay.get("blue");

			initBeforeNum(StringUtil.StringToList(red, ","), StringUtil.StringToList(blue, ","));
		}
	}

	private void setViewClick()
	{
		jxBtn.setOnClickListener(onClickAvoidForceListener);
		tzBtn.setOnClickListener(onClickAvoidForceListener);
		delBtn.setOnClickListener(onClickAvoidForceListener);
	}

	private OnClickAvoidForceListener onClickAvoidForceListener = new OnClickAvoidForceListener()
	{

		@Override
		public void onClickAvoidForce(View v)
		{
			switch (v.getId())
			{
				case R.id.btn_jx:
					machineRandom();
					break;
				case R.id.btn_confirm:
					RandomNumberUtil.removeDuplicateWithOrder(adapterTop.getIntLists());
					RandomNumberUtil.removeDuplicateWithOrder(adapterBottom.getIntBlueLists());
					if (StringUtil.isBlank(SharedPreferencesConfig.config(DLTMainActivity.this).get(Constant.USER_ID)))
						ToastManager.getInstance().showMessage("请先登录!", DLTMainActivity.this);
					else if (adapterTop.getIntLists().size() >= 5 && adapterBottom.getIntBlueLists().size() >= 2)
						submitLottery();
					else
						ToastManager.getInstance().showMessage(getResources().getString(R.string.gc_dlt_empty_toast), DLTMainActivity.this);
					break;
				case R.id.btn_cancel:
					cleanSelectNumber();
					break;
				default:
					break;
			}

		}
	};

	@SuppressWarnings("unchecked")
	private void machineRandom()
	{
		// 5个红球 2个蓝球 红球共35 蓝球共12
		randomNumer = RandomNumberUtil.getRandom(5, 2, 36, 13);
		r1 = (List<Integer>) randomNumer.get("no1");
		r2 = (List<Integer>) randomNumer.get("no2");

		for (int i = 0; i < topMapList.size(); i++)
		{
			adapterTop.getIntLists().clear();
			topMapList.get(i).put("isChoose", false); // 第x个数字设置为选中
		}
		for (int i = 0; i < bottomMapList.size(); i++)
		{
			adapterBottom.getIntBlueLists().clear();
			bottomMapList.get(i).put("isChoose", false); // 第x个数字设置为选中
		}

		for (int iterable_element : r1)
		{
			adapterTop.getIntLists().add((Integer) topMapList.get(iterable_element - 1).get("no"));
			topMapList.get(iterable_element - 1).put("isChoose", true); // 第x个数字设置为选中
			adapterTop.notifyDataSetChanged();
		}
		for (int iterable_element : r2)
		{
			adapterBottom.getIntBlueLists().add((Integer) bottomMapList.get(iterable_element - 1).get("no"));
			bottomMapList.get(iterable_element - 1).put("isChoose", true); // 第x个数字设置为选中
			adapterBottom.notifyDataSetChanged();
		}
	}

	private void initBeforeNum(List<Integer> red, List<Integer> blue)
	{
		for (int i = 0; i < topMapList.size(); i++)
		{
			topMapList.get(i).put("isChoose", false); // 第x个数字设置为选中
		}
		for (int i = 0; i < bottomMapList.size(); i++)
		{
			bottomMapList.get(i).put("isChoose", false); // 第x个数字设置为选中
		}

		for (int iterable_element : red)
		{
			adapterTop.getIntLists().add((Integer) topMapList.get(iterable_element - 1).get("no"));
			topMapList.get(iterable_element - 1).put("isChoose", true); // 第x个数字设置为选中
			adapterTop.notifyDataSetChanged();
		}
		for (int iterable_element : blue)
		{
			adapterBottom.getIntBlueLists().add((Integer) bottomMapList.get(iterable_element - 1).get("no"));
			bottomMapList.get(iterable_element - 1).put("isChoose", true); // 第x个数字设置为选中
			adapterBottom.notifyDataSetChanged();
		}

	}

	/**
	 * 提交选号
	 */
	private void submitLottery()
	{
		ToastManager.getInstance().showMessage(
				"红的:" + adapterTop.getIntLists().toString() + adapterTop.getIntLists().size() + "个\n" + "蓝的:" + adapterBottom
						.getIntBlueLists().toString() + adapterBottom.getIntBlueLists().size() + "个\n" + "共" + NumberUtil.getZhuNumber(
						adapterTop.getIntLists().size(), 5, adapterBottom.getIntBlueLists().size(), 2) + "注", DLTMainActivity.this);

		Map<String, String> cpMap = new HashMap<String, String>();
		// cpMap.put("red", adapterTop.getIntLists().toString());
		// cpMap.put("blue", adapterBottom.getIntBlueLists().toString());
		cpMap.put("red", StringUtil.listToStringArray(adapterTop.getIntLists()));
		cpMap.put("blue", StringUtil.listToStringArray(adapterBottom.getIntBlueLists()));
		cpMap.put("zhu", NumberUtil.getZhuNumber(adapterTop.getIntLists().size(), 5, adapterBottom.getIntBlueLists().size(), 2) + "");

		Intent intent = new Intent();
		intent.setClass(DLTMainActivity.this, CpConfirmPayActivity.class);
		intent.putExtra("cpType", getResources().getString(R.string.gc_dlt));

		Bundle mBundle = new Bundle();
		// 采用parcelable传输数据
		mBundle.putSerializable("cpInfo", (Serializable) cpMap);
		intent.putExtras(mBundle);

		if (cpMapFromCpPay != null)
			intent.putExtra("isItemClick", true);

		if (StringUtil.isBlank(flag))
			startActivity(intent);
		else
			setResult(RESULT_OK, intent);
		DLTMainActivity.this.finish();
	}

	private void cleanSelectNumber()
	{
		for (int i = 0; i < topMapList.size(); i++)
		{
			adapterTop.getIntLists().clear();
			topMapList.get(i).put("isChoose", false); // 第x个数字设置为选中
		}
		for (int i = 0; i < bottomMapList.size(); i++)
		{
			adapterBottom.getIntBlueLists().clear();
			bottomMapList.get(i).put("isChoose", false); // 第x个数字设置为选中
		}
		adapterTop.notifyDataSetChanged();
		adapterBottom.notifyDataSetChanged();
	}

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
