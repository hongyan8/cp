package com.demo.lottery.ui.activity.login;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
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
 * @Description Register
 * @author wanru.chen
 * @date 2014-1-14
 */
public class RegisterActivity extends Activity
{

	private Button closeButton;

	private Button registerButton;

	private EditText nameEditText;

	private EditText pwdEditText;

	private EditText nickNameEditText;

	private EditText phoneEditText;

	private EditText emailEditText;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.layout_register);

		findViews();
		initData();
		initView();
		setViewClick();
	}

	private void findViews()
	{
		((ImageView) findViewById(R.id.img_return)).setVisibility(View.GONE);
		((TextView) findViewById(R.id.formTilte)).setText(getResources().getString(R.string.register));

		closeButton = (Button) findViewById(R.id.btn_close);
		registerButton = (Button) findViewById(R.id.btn_register);

		nameEditText = (EditText) findViewById(R.id.edit_name);
		pwdEditText = (EditText) findViewById(R.id.edit_pwd);
		nickNameEditText = (EditText) findViewById(R.id.edit_nick_name);
		phoneEditText = (EditText) findViewById(R.id.edit_phone);
		emailEditText = (EditText) findViewById(R.id.edit_email);

		// nameEditText.setText("chan");
		// pwdEditText.setText("123");
		// nickNameEditText.setText("cxm");
		// phoneEditText.setText("1399999999");
	}

	private void initData()
	{

	}

	private void initView()
	{

	}

	private void setViewClick()
	{
		closeButton.setOnClickListener(onClickAvoidForceListener);
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
					RegisterActivity.this.finish();
					break;
				case R.id.btn_register:
					if (isPassCheck())
					{
						ProgressUtil.getInstance(RegisterActivity.this).showProgress(2 * 60 * 1000);
						register();
					}
					break;
				default:
					break;
			}

		}
	};

	private void register()
	{
		Map<String, String> configMap = SharedPreferencesConfig.config(this);

		Map<String, Object> paras = new HashMap<String, Object>();
		paras.put("command_id", CommandConstants.REGISTER);
		paras.put("user_name", nameEditText.getText().toString().trim());
		paras.put("password", pwdEditText.getText().toString().trim());
		paras.put("user_tel", phoneEditText.getText().toString().trim());
		paras.put("email", emailEditText.getText().toString().trim());
		paras.put("nick_name", nickNameEditText.getText().toString().trim());
		paras.put("remark", "");
		paras.put("user_type", "");

		Map<String, Object> parasTemp = new HashMap<String, Object>();
		String json = JsonUtil.encodeCmd(paras);
		parasTemp.put("string", json);
		
		new HttpPostAsync(RegisterActivity.this)
		{
			@Override
			public Object backResult(Object result)
			{// 请求回调
				Log.i("json", result.toString());

				if (result == null || "".equals(result.toString()))
				{
					ProgressUtil.getInstance(RegisterActivity.this).dismissProgress();
					ToastManager.getInstance().showMessage("服务器异常，请联系管理员!", RegisterActivity.this);
				}
				else if (Constant.HTTP_REQUEST_FAIL.equals(result.toString().trim()))
				{
					ProgressUtil.getInstance(RegisterActivity.this).dismissProgress();
					ToastManager.getInstance().showMessage("连接不上服务器", RegisterActivity.this);
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
						ProgressUtil.getInstance(RegisterActivity.this).dismissProgress();
						ToastManager.getInstance().showMessage(desc, RegisterActivity.this);
					}
					else
					{
						ProgressUtil.getInstance(RegisterActivity.this).dismissProgress();
						ToastManager.getInstance().showMessage("注册成功!", RegisterActivity.this);
						RegisterActivity.this.finish();
					}
				}
				return "";
			}
		}.execute(Constant.POST_KEYVALUE_DATA, configMap.get(Constant.MOBILE_POST_URL), parasTemp);
	}

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
		else if (StringUtil.isBlank(nickNameEditText.getText().toString()))
		{
			nickNameEditText.setError(getResources().getString(R.string.user_nick_name_hint));
			nickNameEditText.requestFocus();
			return false;
		}
		return true;
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
