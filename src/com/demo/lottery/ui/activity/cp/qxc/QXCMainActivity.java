package com.demo.lottery.ui.activity.cp.qxc;

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
import com.demo.lottery.ui.adapter.QxcListAdapter;
import com.demo.lottery.ui.listener.OnClickAvoidForceListener;
import com.demo.lottery.ui.views.MyListView;
import com.demo.lottery.utils.RandomNumberUtil;
import com.demo.lottery.utils.StringUtil;
import com.demo.lottery.utils.ToastManager;

public class QXCMainActivity extends Activity
{
	private MyListView listView;

	private QxcListAdapter adapter;

	private List<Map<String, Object>> mapList;

	private Button jxBtn;

	private Button tzBtn;

	private Button delBtn;

	private List<Integer> r;

	private NumberAdapter adapterNum;

	private String flag;

	private int zs = 1;

	private String qxcChooseNum = "";

	private Map<String, String> cpMapFromCpPay;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.layout_qxc_main);

		((ScrollView) findViewById(R.id.scrollView1)).smoothScrollTo(0, 0);

		listView = (MyListView) findViewById(R.id.list_qxc);
		jxBtn = (Button) findViewById(R.id.btn_jx);
		tzBtn = (Button) findViewById(R.id.btn_confirm);
		delBtn = (Button) findViewById(R.id.btn_cancel);

		adapter = new QxcListAdapter(QXCMainActivity.this);
		mapList = new ArrayList<Map<String, Object>>();
		adapterNum = new NumberAdapter(QXCMainActivity.this);

		initData();
		initView();
		setViewClick();
	}

	@SuppressWarnings("unchecked")
	private void initData()
	{
		flag = getIntent().getStringExtra("flag");
		cpMapFromCpPay = (Map<String, String>) getIntent().getSerializableExtra("cpMapFromCpPay");

		for (int i = 1; i < 8; i++)
		{
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("NO", "第" + (i) + "位");
			List<Map<String, Object>> gridMapList = new ArrayList<Map<String, Object>>();
			for (int j = 0; j < 10; j++)
			{
				Map<String, Object> m = new HashMap<String, Object>();
				m.put("no", j);
				m.put("num", j);
				m.put("isChoose", false);
				m.put("type", "red");
				gridMapList.add(m);
			}
			map.put("num_listmap", gridMapList);
			mapList.add(map);
		}
	}

	private void initView()
	{
		adapter.setDataList(mapList);
		listView.setAdapter(adapter);

		if (cpMapFromCpPay != null && cpMapFromCpPay.size() > 0)
		{
			String red = cpMapFromCpPay.get("red");
			initBeforeNum(StringUtil.StringToMap(red, ","));
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
					if (StringUtil.isBlank(SharedPreferencesConfig.config(QXCMainActivity.this).get(Constant.USER_ID)))
						ToastManager.getInstance().showMessage("请先登录!", QXCMainActivity.this);
					else if (isSuccess())
						submitLottery();
					else
						ToastManager.getInstance().showMessage(getResources().getString(R.string.gc_qxc_empty_toast), QXCMainActivity.this);
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
		// 每行10个红球 共7行
		r = RandomNumberUtil.getRandom(6, 10);
		adapterNum.getIntLists().clear();
		for (int i = 0; i < adapter.getList().size(); i++)
		{
			for (int j = 0; j < adapter.getMapList().size(); j++)
			{
				((List<Map<String, Object>>) adapter.getList().get(i).get("num_listmap")).get(j).put("isChoose", false); // 第x个数字设置为选中
			}
			int no = (Integer) ((List<Map<String, Object>>) adapter.getList().get(i).get("num_listmap")).get(r.get(i)).get("no");
			adapterNum.getIntLists().add(no);
			((List<Map<String, Object>>) adapter.getList().get(i).get("num_listmap")).get(r.get(i)).put("isChoose", true); // 第x个数字设置为选中
			adapterNum.notifyDataSetChanged();
			adapter.notifyDataSetChanged();
		}
	}

	@SuppressWarnings("unchecked")
	private void initBeforeNum(List<Map<String, Object>> listMap)
	{

		for (int i = 0; i < adapter.getList().size(); i++)
		{
			List<Integer> red = (List<Integer>) listMap.get(i).get("num");
			adapter.setMapList((List<Map<String, Object>>) adapter.getList().get(i).get("num_listmap"));
			for (int j = 0; j < adapter.getMapList().size(); j++)
			{
				((List<Map<String, Object>>) adapter.getList().get(i).get("num_listmap")).get(j).put("isChoose", false); // 第x个数字设置为选中
			}

			for (int j = 0; j < red.size(); j++)
			{
				int no = (Integer) ((List<Map<String, Object>>) adapter.getList().get(i).get("num_listmap")).get(red.get(j)).get("no");
				adapterNum.getIntLists().add(no);
				((List<Map<String, Object>>) adapter.getList().get(i).get("num_listmap")).get(red.get(j)).put("isChoose", true); // 第x个数字设置为选中
			}
			adapterNum.notifyDataSetChanged();
			adapter.notifyDataSetChanged();
		}
	}

	private boolean isSuccess()
	{
		Map<String, Map<String, Object>> resultMap = new HashMap<String, Map<String, Object>>();
		adapterNum.getIntLists().clear();// 清空已选好的号
		for (int i = 0; i < adapter.getList().size(); i++)
		{
			for (int j = 0; j < adapter.getMapList().size(); j++)
			{
				boolean isChoose = (Boolean) ((List<Map<String, Object>>) adapter.getList().get(i).get("num_listmap")).get(j).get(
						"isChoose");

				int no = (Integer) ((List<Map<String, Object>>) adapter.getList().get(i).get("num_listmap")).get(j).get("no");
				if (isChoose)
				{// 如果选中,记录选中的位置和选中的号码
					Map<String, Object> m = new HashMap<String, Object>();
					if (resultMap.containsKey(i + ""))
					{// 如果位置存在,将号码相加
						String xuan = StringUtil.Object2String(((Map<String, Object>) resultMap.get(i + "")).get("xuan"));
						m.put("wei", i);
						m.put("xuan", xuan + "、" + no);
					}
					else
					{
						m.put("wei", i);
						m.put("xuan", no + "");
					}
					resultMap.put(i + "", m);

					adapterNum.getIntLists().add(no);
				}
			}
		}
		String toast = "";
		qxcChooseNum = "";
		zs = 1;
		for (int y = 0; y < 7; y++)
		{
			if (resultMap.containsKey(y + ""))
			{
				Map<String, Object> result = (Map<String, Object>) resultMap.get(y + "");
				String[] sArray = result.get("xuan").toString().split("、");
				toast += "第" + ((Integer) result.get("wei") + 1) + "位:" + result.get("xuan") + "  个数:" + sArray.length + "\n";
				qxcChooseNum += result.get("xuan") + ",";
				zs *= sArray.length;
			}
		}
		boolean isSuccess = true;
		if (resultMap.size() < 7)
		{
			zs = 0;
			isSuccess = false;
		}
		ToastManager.getInstance().showMessage(toast + "注数:" + zs, QXCMainActivity.this);
		return isSuccess;
	}

	/**
	 * 提交选号
	 */
	@SuppressWarnings("unchecked")
	private void submitLottery()
	{
		Map<String, String> cpMap = new HashMap<String, String>();
		cpMap.put("red", qxcChooseNum);
		cpMap.put("zhu", zs + "");

		Intent intent = new Intent();
		intent.setClass(QXCMainActivity.this, CpConfirmPayActivity.class);
		intent.putExtra("cpType", getResources().getString(R.string.gc_qxc));
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
		QXCMainActivity.this.finish();
	}

	@SuppressWarnings("unchecked")
	private void cleanSelectNumber()
	{
		for (int i = 0; i < adapter.getList().size(); i++)
		{
			for (int j = 0; j < adapter.getMapList().size(); j++)
			{
				((List<Map<String, Object>>) adapter.getList().get(i).get("num_listmap")).get(j).put("isChoose", false); // 第x个数字设置为选中
			}
			adapterNum.notifyDataSetChanged();
			adapter.notifyDataSetChanged();
		}
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
