package com.demo.lottery.ui.activity.cp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.demo.lottery.R;
import com.demo.lottery.constant.CommandConstants;
import com.demo.lottery.constant.Constant;
import com.demo.lottery.global.SharedPreferencesConfig;
import com.demo.lottery.services.HttpPostAsync;
import com.demo.lottery.ui.activity.cp.dlt.DLTMainActivity;
import com.demo.lottery.ui.activity.cp.qxc.QXCMainActivity;
import com.demo.lottery.ui.activity.login.MainActivity;
import com.demo.lottery.ui.adapter.ConfirmPayListAdapter;
import com.demo.lottery.ui.adapter.NumberAdapter;
import com.demo.lottery.ui.adapter.QxcListAdapter;
import com.demo.lottery.ui.listener.OnClickAvoidForceListener;
import com.demo.lottery.ui.listener.OnItemClickAvoidForceListener;
import com.demo.lottery.utils.JsonUtil;
import com.demo.lottery.utils.NumberUtil;
import com.demo.lottery.utils.ProgressUtil;
import com.demo.lottery.utils.RandomNumberUtil;
import com.demo.lottery.utils.StringUtil;
import com.demo.lottery.utils.ToastManager;
import com.demo.lottery.utils.UUID;

public class CpConfirmPayActivity extends Activity
{

	private String cpType = "";

	private boolean isItemClick = false;

	private int cpTypeFlag = 0;// 0:大乐透 1:七星彩

	private Map<String, String> cpMap;

	private ConfirmPayListAdapter adapter;

	private List<Map<String, String>> list;

	private ListView listView;

	private Button addChooseBtn;

	private Button addChooseMachineBtn;

	private Button payBtn;

	private TextView payTotalText;

	private EditText qishuEdit;

	private EditText beishuEdit;

	/******* DLT **********/
	private List<Map<String, Object>> topMapList;

	private List<Map<String, Object>> bottomMapList;

	private NumberAdapter adapterTop;

	private NumberAdapter adapterBottom;

	private Map<String, Object> randomNumer;

	private List<Integer> r1 = new ArrayList<Integer>();

	private List<Integer> r2 = new ArrayList<Integer>();

	/******* QXC *********/
	private List<Integer> r;

	private NumberAdapter adapterNum;

	private QxcListAdapter qxcAdapter;

	private List<Map<String, Object>> qxcMapList;

	private int position = 0;

	private int totalPay = 0;

	/**
	 * 投注内容
	 */
	private String tz_content = "";

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.layout_cp_confirmpay);
		listView = (ListView) findViewById(R.id.list_confirmpay);
		addChooseBtn = (Button) findViewById(R.id.btn_add_choose);
		addChooseMachineBtn = (Button) findViewById(R.id.btn_add_choose_machine);
		payTotalText = (TextView) findViewById(R.id.text_pay_total);
		payBtn = (Button) findViewById(R.id.btn_pay);
		qishuEdit = (EditText) findViewById(R.id.edit_zhui);
		beishuEdit = (EditText) findViewById(R.id.edit_tou);

		adapter = new ConfirmPayListAdapter(CpConfirmPayActivity.this);
		list = new ArrayList<Map<String, String>>();

		cpType = getIntent().getStringExtra("cpType");
		cpMap = (Map<String, String>) getIntent().getSerializableExtra("cpInfo");

		topMapList = new ArrayList<Map<String, Object>>();
		bottomMapList = new ArrayList<Map<String, Object>>();
		adapterTop = new NumberAdapter(CpConfirmPayActivity.this);
		adapterBottom = new NumberAdapter(CpConfirmPayActivity.this);
		qxcAdapter = new QxcListAdapter(CpConfirmPayActivity.this);
		adapterNum = new NumberAdapter(CpConfirmPayActivity.this);
		qxcMapList = new ArrayList<Map<String, Object>>();

		initData();
		initView();
		setViewClick();
	}

	private void initData()
	{
		if (getResources().getString(R.string.gc_dlt).equals(cpType))
			cpTypeFlag = 0;
		else if (getResources().getString(R.string.gc_qxc).equals(cpType))
			cpTypeFlag = 1;

		isItemClick = getIntent().getBooleanExtra("isItemClick", false);

		if (cpMap != null && cpMap.size() > 0)
		{
			String zhuInfo = "";
			if ("1".equals(cpMap.get("zhu")))
				zhuInfo = getResources().getString(R.string.gc_dstz) + "  " + cpMap.get("zhu") + "注";
			else
				zhuInfo = getResources().getString(R.string.gc_fstz) + "  " + cpMap.get("zhu") + "注";
			Map<String, String> map = new HashMap<String, String>();
			map.put("red", StringUtil.Object2String(cpMap.get("red")));
			map.put("blue", StringUtil.Object2String(cpMap.get("blue")));
			map.put("zhuInfo", zhuInfo);
			map.put("zhu", cpMap.get("zhu"));
			list.add(map);
		}
		payTotalText();
	}

	private void initView()
	{
		((TextView) findViewById(R.id.formTilte)).setText(cpType);
		payTotalText.setText("共" + totalPay + "注" + totalPay * 2 + "元");

		adapter.setDataList(list);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(onItemClickAvoidForceListener);
	}

	private void setViewClick()
	{
		// 手选 startActivityForResult(前一页, requestCode)
		addChooseBtn.setOnClickListener(onClickAvoidForceListener);
		addChooseMachineBtn.setOnClickListener(onClickAvoidForceListener);
		payBtn.setOnClickListener(onClickAvoidForceListener);
	}

	private OnClickAvoidForceListener onClickAvoidForceListener = new OnClickAvoidForceListener()
	{

		@Override
		public void onClickAvoidForce(View v)
		{
			switch (v.getId())
			{
				case R.id.btn_add_choose:
					Intent intent = new Intent();
					if (cpTypeFlag == 0)
						intent.setClass(CpConfirmPayActivity.this, DLTMainActivity.class);
					else if (cpTypeFlag == 1)
						intent.setClass(CpConfirmPayActivity.this, QXCMainActivity.class);
					intent.putExtra("flag", cpType);
					startActivityForResult(intent, 1);
					break;
				case R.id.btn_add_choose_machine:
					if (cpTypeFlag == 0)
						machineDLTRandom();
					else if (cpTypeFlag == 1)
						machineQXCRandom();
					break;
				case R.id.btn_pay:
					ProgressUtil.getInstance(CpConfirmPayActivity.this).showProgress(2 * 60 * 1000);
					toPay();
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
			position = arg2;
			Intent intent = new Intent();
			String red = ((TextView) arg1.findViewById(R.id.text_red_num)).getText().toString();
			String blue = StringUtil.Object2String(((TextView) arg1.findViewById(R.id.text_blue_num)).getText());
			String zhu = ((TextView) arg1.findViewById(R.id.text_zhu_info)).getText().toString();

			Map<String, Object> cpMapFromCpPay = new HashMap<String, Object>();
			cpMapFromCpPay.put("red", red);
			cpMapFromCpPay.put("blue", blue);
			cpMapFromCpPay.put("zhu", zhu);

			Bundle mBundle = new Bundle();
			// 采用parcelable传输数据
			mBundle.putSerializable("cpMapFromCpPay", (Serializable) cpMapFromCpPay);
			intent.putExtras(mBundle);
			intent.putExtra("flag", cpType);
			if (cpTypeFlag == 0)
			{
				intent.setClass(CpConfirmPayActivity.this, DLTMainActivity.class);
			}
			else if (cpTypeFlag == 1)
			{
				intent.setClass(CpConfirmPayActivity.this, QXCMainActivity.class);
			}
			startActivityForResult(intent, 2);
		}
	};

	@SuppressWarnings("unchecked")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK)
		{

			switch (requestCode)
			{
				case 1:
					cpType = data.getStringExtra("cpType");
					cpMap = (Map<String, String>) data.getSerializableExtra("cpInfo");
					initData();
					// payTotalText.setText("共" + totalPay + "注" + totalPay * 2 + "元");
					adapter.notifyDataSetChanged();
					break;
				case 2:
					list.remove(position);
					cpType = data.getStringExtra("cpType");
					cpMap = (Map<String, String>) data.getSerializableExtra("cpInfo");
					initData();
					// payTotalText.setText("共" + totalPay + "注" + totalPay * 2 + "元");
					adapter.notifyDataSetChanged();
					position = 0;
					break;
				default:
					break;
			}

		}
		else
		{
		}
	}

	private void finishActivity()
	{
		// TODO 弹出是否关闭该页面
		if (isItemClick)
		{// 存在多个当前页
			Intent intent = new Intent();
			intent.setClass(CpConfirmPayActivity.this, MainActivity.class);
			startActivity(intent);
		}
		else
			CpConfirmPayActivity.this.finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			finishActivity();
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	private void machineDLTRandom()
	{
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
			// adapterTop.notifyDataSetChanged();
		}
		for (int iterable_element : r2)
		{
			adapterBottom.getIntBlueLists().add((Integer) bottomMapList.get(iterable_element - 1).get("no"));
			bottomMapList.get(iterable_element - 1).put("isChoose", true); // 第x个数字设置为选中
			// adapterBottom.notifyDataSetChanged();
		}

		Map<String, String> map = new HashMap<String, String>();
		map.put("red", StringUtil.listToStringArray(adapterTop.getIntLists()));
		map.put("blue", StringUtil.listToStringArray(adapterBottom.getIntBlueLists()));
		map.put("zhuInfo", getResources().getString(R.string.gc_dstz) + "  1注");
		map.put("zhu", 1 + "");
		list.add(map);

		payTotalText();
		adapter.notifyDataSetChanged();
	}

	@SuppressWarnings("unchecked")
	private void machineQXCRandom()
	{
		qxcMapList.clear();

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
			qxcMapList.add(map);
		}
		qxcAdapter.setDataList(qxcMapList);
		// qxcAdapter.notifyDataSetChanged();

		// 每行10个红球 共7行
		r = RandomNumberUtil.getRandom(6, 10);
		adapterNum.getIntLists().clear();
		for (int i = 0; i < qxcAdapter.getList().size(); i++)
		{
			qxcAdapter.setMapList((List<Map<String, Object>>) qxcAdapter.getList().get(i).get("num_listmap"));
			for (int j = 0; j < qxcAdapter.getMapList().size(); j++)
			{
				((List<Map<String, Object>>) qxcAdapter.getList().get(i).get("num_listmap")).get(j).put("isChoose", false); // 第x个数字设置为选中
			}
			int no = (Integer) ((List<Map<String, Object>>) qxcAdapter.getList().get(i).get("num_listmap")).get(r.get(i)).get("no");
			adapterNum.getIntLists().add(no);
			((List<Map<String, Object>>) qxcAdapter.getList().get(i).get("num_listmap")).get(r.get(i)).put("isChoose", true); // 第x个数字设置为选中
		}

		Map<String, String> map = new HashMap<String, String>();
		map.put("red", adapterNum.getIntLists().toString().replace("[", "").replace("]", ""));
		map.put("zhuInfo", getResources().getString(R.string.gc_dstz) + "  1注");
		map.put("zhu", 1 + "");
		list.add(map);
		payTotalText();
		adapter.notifyDataSetChanged();
	}

	private void toPay()
	{
		Map<String, String> configMap = SharedPreferencesConfig.config(this);

		// 提交订单：dealOrder
		//
		// 参数：contentFlag：add
		// user_id,lottery_type_code,order_nper,order_code,betting_amount,order_status_code,
		// betting_content,multiple_num,betting_num,betting_type

		Map<String, Object> paras = new HashMap<String, Object>();
		paras.put("command_id", CommandConstants.DEALORDER);
		paras.put("contentFlag", "add");
		paras.put("user_id", configMap.get(Constant.USER_ID));
		paras.put("lottery_type_code", "004");// 彩票类型code
		paras.put("order_nper", qishuEdit.getText().toString().trim());// 期数
		paras.put("order_code", UUID.timeUUID());// 订单号
		paras.put("betting_amount", totalPay * 2);// 投注金额
		paras.put("order_status_code", "1001");
		paras.put("betting_content", tz_content);// 投注内容
		paras.put("multiple_num", beishuEdit.getText().toString().trim());// 倍数
		paras.put("betting_num", totalPay);
		paras.put("betting_type", "");

		Map<String, Object> parasTemp = new HashMap<String, Object>();
		String json = JsonUtil.encodeCmd(paras);
		parasTemp.put("string", json);

		new HttpPostAsync(CpConfirmPayActivity.this)
		{
			@Override
			public Object backResult(Object result)
			{// 请求回调
				Log.i("json", result.toString());

				if (result == null || "".equals(result.toString()))
				{
					ProgressUtil.getInstance(CpConfirmPayActivity.this).dismissProgress();
					ToastManager.getInstance().showMessage("服务器异常，请联系管理员!", CpConfirmPayActivity.this);
				}
				else if (Constant.HTTP_REQUEST_FAIL.equals(result.toString().trim()))
				{
					ProgressUtil.getInstance(CpConfirmPayActivity.this).dismissProgress();
					ToastManager.getInstance().showMessage("连接不上服务器", CpConfirmPayActivity.this);
				}
				else
				{
					Map<String, Object> mapstr = JsonUtil.getMapString(result.toString());
					Map<String, Object> headMap = JsonUtil.getMapString(mapstr.get("head").toString());
					boolean isSuccess = false;
					if ("true".equals(headMap.get("success")))
						isSuccess = true;
					String desc = (String) headMap.get("desc");
					if (!isSuccess && !StringUtil.isBlank(desc))
					{
						ProgressUtil.getInstance(CpConfirmPayActivity.this).dismissProgress();
						ToastManager.getInstance().showMessage(desc, CpConfirmPayActivity.this);
					}
					else
					{
						ProgressUtil.getInstance(CpConfirmPayActivity.this).dismissProgress();
						ToastManager.getInstance().showMessage("提交订单成功!\n付款待实现....", CpConfirmPayActivity.this);
						CpConfirmPayActivity.this.finish();
					}
				}
				return "";
			}
		}.execute(Constant.POST_KEYVALUE_DATA, configMap.get(Constant.MOBILE_POST_URL), parasTemp);

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

	/**
	 * 计算总价
	 */
	private void payTotalText()
	{
		totalPay = 0;
		for (int i = 0; i < list.size(); i++)
		{
			totalPay += Integer.parseInt(list.get(i).get("zhu"));
			tz_content += list.get(i).get("zhuInfo") + "\n";
		}
		payTotalText.setText("共" + totalPay + "注" + totalPay * 2 + "元");
	}
}
