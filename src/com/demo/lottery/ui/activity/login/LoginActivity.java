package com.demo.lottery.ui.activity.login;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.demo.lottery.R;
import com.demo.lottery.constant.CommandConstants;
import com.demo.lottery.constant.Constant;
import com.demo.lottery.global.SharedPreferencesConfig;
import com.demo.lottery.services.HttpPostAsync;
import com.demo.lottery.ui.listener.OnClickAvoidForceListener;
import com.demo.lottery.utils.JsonUtil;
import com.demo.lottery.utils.ProgressUtil;
import com.demo.lottery.utils.StringUtil;
import com.demo.lottery.utils.ToastManager;

/**
 * @Description Login
 * @author wanru.chen
 * @date 2014-1-14
 */
public class LoginActivity extends Activity
{
	private ProgressDialog myProgressDialog = null;

	private Button closeButton;

	private Button loginButton;

	private Button registerButton;

	private EditText nameEditText;

	private EditText pwdEditText;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.layout_login);

		findViews();
		initData();
		initView();
		setViewClick();
	}

	private void findViews()
	{
		((ImageView) findViewById(R.id.img_return)).setVisibility(View.GONE);
		((TextView) findViewById(R.id.formTilte)).setText(getResources().getString(R.string.login));

		closeButton = (Button) findViewById(R.id.btn_close);
		loginButton = (Button) findViewById(R.id.btn_login);
		registerButton = (Button) findViewById(R.id.btn_register);

		nameEditText = (EditText) findViewById(R.id.edit_name);
		pwdEditText = (EditText) findViewById(R.id.edit_pwd);
	}

	private void initData()
	{

	}

	private void initView()
	{
		nameEditText.setText("cwr");
		pwdEditText.setText("123456");
	}

	private void setViewClick()
	{
		closeButton.setOnClickListener(onClickAvoidForceListener);
		loginButton.setOnClickListener(onClickAvoidForceListener);
		registerButton.setOnClickListener(onClickAvoidForceListener);
	}

	private OnClickAvoidForceListener onClickAvoidForceListener = new OnClickAvoidForceListener()
	{

		@Override
		public void onClickAvoidForce(View v)
		{
			switch (v.getId())
			{
				case R.id.btn_close:
					LoginActivity.this.finish();
					break;
				case R.id.btn_login:
					if (isPassCheck())
					{
						ProgressUtil.getInstance(LoginActivity.this).showProgress(2 * 60 * 1000);
						login();
					}
					// ToastManager.getInstance().showMessage("login~~~", LoginActivity.this);
					break;
				case R.id.btn_register:
					Intent intent = new Intent();
					intent.setClass(LoginActivity.this, RegisterActivity.class);
					startActivity(intent);
					break;
				default:
					break;
			}

		}
	};

	private boolean isPassCheck()
	{
		if (StringUtil.isBlank(nameEditText.getText().toString()))
		{
			nameEditText.setError(getResources().getString(R.string.user_name_hint));
			nameEditText.requestFocus();
			return false;
		}
		else if (StringUtil.isBlank(pwdEditText.getText().toString()))
		{
			pwdEditText.setError(getResources().getString(R.string.user_pwd_hint));
			pwdEditText.requestFocus();
			return false;
		}
		return true;
	}

	private void login()
	{
		Map<String, String> configMap = SharedPreferencesConfig.config(this);
		Map<String, Object> paras = new HashMap<String, Object>();
		paras.put("command_id", CommandConstants.LOGIN);
		paras.put("user_name", nameEditText.getText().toString().trim());
		paras.put("password", pwdEditText.getText().toString().trim());

		Map<String, Object> parasTemp = new HashMap<String, Object>();
		String json = JsonUtil.encodeCmd(paras);
		parasTemp.put("string", json);

		new HttpPostAsync(LoginActivity.this)
		{
			@Override
			public Object backResult(Object result)
			{// 请求回调

				if (result == null || "".equals(result.toString()))
				{
					ProgressUtil.getInstance(LoginActivity.this).dismissProgress();
					ToastManager.getInstance().showMessage("服务器异常，请联系管理员!", LoginActivity.this);
				}
				else if (Constant.HTTP_REQUEST_FAIL.equals(result.toString().trim()))
				{
					ProgressUtil.getInstance(LoginActivity.this).dismissProgress();
					ToastManager.getInstance().showMessage("连接不上服务器", LoginActivity.this);
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
						ProgressUtil.getInstance(LoginActivity.this).dismissProgress();
						ToastManager.getInstance().showMessage(desc, LoginActivity.this);
					}
					else
					{
						loginSuccessDeal(dataMap);// 成功登录后处理
					}
				}
				return "";
			}
		}.execute(Constant.POST_KEYVALUE_DATA, configMap.get(Constant.MOBILE_POST_URL), parasTemp);

	}

	private void loginSuccessDeal(Map<String, Object> map)
	{
		new LoginDealTask().execute(map);
	}

	class LoginDealTask extends AsyncTask<Map<String, Object>, Integer, Void>
	{

		@Override
		protected void onPreExecute()
		{
			showProgressBar();
		}

		@Override
		protected void onProgressUpdate(Integer... progress)
		{

			Log.d("", "probar =" + progress[0]);
			myProgressDialog.setProgress(progress[0]);
		}

		@Override
		protected Void doInBackground(Map<String, Object>... map)
		{
			try
			{
				publishProgress(10);
				// 2，保存登录用户的基本信息到手机文件存储库中
				if (map != null)
				{
					@SuppressWarnings("unchecked") Map<String, Object> m = (Map<String, Object>) JsonUtil.decodeCmd(map[0].get("userinfo")
							.toString());
					SharedPreferencesConfig.saveConfig(LoginActivity.this, Constant.USER_NAME, StringUtil.Object2String(m.get("userName")));
					SharedPreferencesConfig.saveConfig(LoginActivity.this, Constant.USER_PASSWORD,
							StringUtil.Object2String(m.get("password")));
					SharedPreferencesConfig.saveConfig(LoginActivity.this, Constant.USER_NICKNAME,
							StringUtil.Object2String(m.get("nickName")));
					SharedPreferencesConfig.saveConfig(LoginActivity.this, Constant.USER_ID, StringUtil.Object2String(m.get("id")));
					SharedPreferencesConfig
							.saveConfig(LoginActivity.this, Constant.MOBILEPHONE, StringUtil.Object2String(m.get("userTel")));
				}
				else
					ToastManager.getInstance().showMessage("保存用户信息失败!", LoginActivity.this);

				publishProgress(70);

				ProgressUtil.getInstance(LoginActivity.this).dismissProgress();
				Intent intent = new Intent();
				intent.setClass(LoginActivity.this, MainActivity.class);
				startActivity(intent);
				LoginActivity.this.finish();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result)
		{
			if (myProgressDialog != null && myProgressDialog.isShowing())
				try
				{
					myProgressDialog.dismiss();
				}
				catch (Exception e)
				{
					Log.e("", "dialog err ,but it's dosen't matter!");
					e.printStackTrace();
				}

		}

	}

	public void showProgressBar()
	{
		if (myProgressDialog == null)
		{
			myProgressDialog = new ProgressDialog(LoginActivity.this);
			myProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			myProgressDialog.setMax(100);
			myProgressDialog.setMessage("登录成功，正在加载数据到本地...");
			myProgressDialog.setCancelable(true);
			myProgressDialog.setCanceledOnTouchOutside(false);
		}

		if (!myProgressDialog.isShowing())
		{
			myProgressDialog.show();
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
