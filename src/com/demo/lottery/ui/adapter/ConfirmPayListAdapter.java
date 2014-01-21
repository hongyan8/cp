package com.demo.lottery.ui.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.demo.lottery.R;
import com.demo.lottery.utils.StringUtil;

public class ConfirmPayListAdapter extends BaseAdapter
{

	/**
	 * Layout填充器
	 */
	private LayoutInflater inflater = null;

	/**
	 * 存储列表实体类的list
	 */
	private List<Map<String, String>> list = null;

	/**
	 * 列表类
	 */
	@SuppressWarnings("unused")
	private Context context;

	/**
	 * 构造方法
	 * 
	 * @param context
	 *            列表类
	 */
	public ConfirmPayListAdapter(Context context)
	{
		this.context = context;
		this.inflater = LayoutInflater.from(context);
	}

	public void setDataList(List<Map<String, String>> list)
	{
		this.list = list;
	}

	public List<Map<String, String>> getList()
	{
		return list;
	}

	@Override
	public int getCount()
	{
		return list == null ? 0 : list.size();
	}

	@Override
	public Object getItem(int position)
	{
		return position;
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		Map<String, String> map = list.get(position);
		// 先确保view存在
		if (convertView == null)
		{
			convertView = inflater.inflate(R.layout.layout_cp_confirmpay_list_item, null);
		}

		// 获得holder
		ConfirmPayListHolder holder = null;
		Object tag = convertView.getTag();
		if (tag == null)
		{
			holder = new ConfirmPayListHolder(convertView);
		}
		else
		{
			holder = (ConfirmPayListHolder) tag;
		}
		holder.setData(map);
		// 设置tag
		convertView.setTag(holder);

		return convertView;
	}

	private class ConfirmPayListHolder
	{

		TextView textRed;

		TextView textBlue;

		TextView textZhuInfo;

		private ConfirmPayListHolder(View convertView)
		{
			textRed = (TextView) convertView.findViewById(R.id.text_red_num);
			textBlue = (TextView) convertView.findViewById(R.id.text_blue_num);
			textZhuInfo = (TextView) convertView.findViewById(R.id.text_zhu_info);
		}

		private void setData(Map<String, String> map)
		{
			textRed.setText(StringUtil.Object2String(map.get("red")));
			textBlue.setText(StringUtil.Object2String(map.get("blue")));
			textZhuInfo.setText(StringUtil.Object2String(map.get("zhuInfo")));
		}
	}

}
